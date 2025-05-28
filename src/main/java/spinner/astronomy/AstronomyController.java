package spinner.astronomy;

import com.andrewoid.apikeys.ApiKey;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import spinner.astronomy.json.AstronomyResponse;

import javax.swing.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AstronomyController {
    private final JLabel altitudeLabel;
    private final JLabel azimuthLabel;
    private final AstronomyService service;
    private final String authHeader;

    public AstronomyController(JLabel altitudeLabel, JLabel azimuthLabel, AstronomyService service) {
        this.altitudeLabel = altitudeLabel;
        this.azimuthLabel = azimuthLabel;
        this.service = service;

        ApiKey appId = new ApiKey("atronomy_app_id");
        ApiKey secret = new ApiKey("atronomy_secret");
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString((appId + ":" + secret).getBytes()
                );
    }

    public void display() {
        String date = LocalDate.now().toString(); // "2025-05-28"
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

    private void handleResponse(AstronomyResponse response) {
        altitudeLabel.setText("Altitude: " + response.data.position.horizontal.altitude + "°");
        azimuthLabel.setText("Azimuth: " + response.data.position.horizontal.azimuth + "°");
    }
}
