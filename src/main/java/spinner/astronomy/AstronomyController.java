package spinner.astronomy;

import com.andrewoid.apikeys.ApiKey;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import spinner.astronomy.json.AstronomyResponse;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AstronomyController {
    private final SkyPanel skyPanel;
    private final AstronomyService service;
    private final String authHeader;

    public AstronomyController(SkyPanel skyPanel, AstronomyService service) {
        this.skyPanel = skyPanel;
        this.service = service;

        ApiKey appId = new ApiKey("astronomy_app_id");
        ApiKey secret = new ApiKey("astronomy_secret");
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(
                (appId.get() + ":" + secret.get()).getBytes()
                );
    }

    public void display() {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Disposable disposable = service.getPosition(
                        authHeader,
                        -73.7292, 40.6247, 0,
                        date, date, time, "moon"
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

