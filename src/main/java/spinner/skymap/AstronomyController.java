package spinner.skymap;

import com.andrewoid.apikeys.ApiKey;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import spinner.skymap.json.AstronomyResponse;
import spinner.skymap.json.GeocodingResponse;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AstronomyController {
    private final SkyPanel skyPanel;
    private final AstronomyService astronomyService;
    private final GeocodingService geocodingService;
    private final String astronomyAuthHeader;
    private final String openWeatherMapKey;
    private double latitude = 40.6247;
    private double longitude = -73.7292;

    public AstronomyController(SkyPanel skyPanel) {
        this.skyPanel = skyPanel;

        this.astronomyService = new AstronomyServiceFactory().getService();

        ApiKey appId = new ApiKey("astronomy_app_id");
        ApiKey secret = new ApiKey("astronomy_secret");
        this.astronomyAuthHeader = "Basic " + Base64.getEncoder().encodeToString(
                (appId.get() + ":" + secret.get()).getBytes()
                );

        this.geocodingService = new GeocodingServiceFactory().getService();
        ApiKey openWeatherMapKey = new ApiKey("openweathermap");
        this.openWeatherMapKey = openWeatherMapKey.get();
    }

    public void searchLocation(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return;
        }

        Disposable disposable = geocodingService.getLocationCoordinates(locationName.trim(), 1, openWeatherMapKey)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.from(SwingUtilities::invokeLater))
        .subscribe(
                this::handleLocationResponse,
                Throwable::printStackTrace
        );
    }

    private void handleLocationResponse(GeocodingResponse[] responses) {
        if (responses != null && responses.length > 0) {
            updateLocation(responses[0].lat, responses[0].lon);
        }
    }

    public void updateLocation(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
        refreshSkyData();
    }

    public void refreshSkyData() {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Disposable disposable = astronomyService.getPosition(
                        astronomyAuthHeader,
                        longitude, latitude, 0,
                        date, date, time,
                        "sun,moon,mercury,venus,mars,jupiter,saturn,uranus,neptune,pluto"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        this::handleResponse,
                        Throwable::printStackTrace
                );
    }

    private void handleResponse(AstronomyResponse response)
    {
        List<CelestialBody> celestialBodies = new ArrayList<>();

        if (response.data != null
            && response.data.table != null
            && response.data.table.rows != null) {
            for (AstronomyResponse.Row row : response.data.table.rows) {
                if (row.cells != null) {
                    for (AstronomyResponse.Cell cell : row.cells) {
                        if (cell.position != null
                            && cell.position.horizontal != null) {
                            String name = formatName(cell.name);
                            double altitude = cell.position.horizontal.altitude.degrees;
                            double azimuth = cell.position.horizontal.azimuth.degrees;

                            celestialBodies.add(new CelestialBody(name, azimuth, altitude));
                        }
                    }
                }
            }
        }

        skyPanel.setBodies(celestialBodies);
    }

    private String formatName(String name) {
        if (name == null) {
            return "Unknown";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}

