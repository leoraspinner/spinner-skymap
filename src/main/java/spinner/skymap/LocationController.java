package spinner.skymap;

import com.andrewoid.apikeys.ApiKey;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import spinner.skymap.json.GeocodingResponse;

import javax.swing.*;

public class LocationController {
    private final GeocodingService geocodingService;
    private final AstronomyController astronomyController;
    private final String openWeatherMapKey;

    public LocationController(AstronomyController astronomyController) {
        this.astronomyController = astronomyController;
        this.geocodingService = new GeocodingServiceFactory().getService();

        ApiKey apiKey = new ApiKey("openweathermap");
        this.openWeatherMapKey = apiKey.get();
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
            GeocodingResponse location = responses[0];
            astronomyController.updateLocation(location.lat, location.lon);
        }
    }
}
