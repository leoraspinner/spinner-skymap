package spinner.astronomy;

import com.andrewoid.apikeys.ApiKey;
import spinner.astronomy.json.AstronomyResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

class AstronomyServiceTest {
    @Test
    void getPosition() {
        //Given
        AstronomyService service = new AstronomyServiceFactory().getService();
        ApiKey appId = new ApiKey("astronomy_app_id");
        ApiKey secret = new ApiKey("astronomy_secret");
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((appId.get() + ":" + secret.get()).getBytes()
        );

        //When
        AstronomyResponse response = service.getPosition(
                authHeader,
                -73.7292,
                40.6247,
                0,
                Instant.now().toString(),
                "moon"
        ).blockingGet();

        //Then
        assertNotNull(response.data.position.horizontal);
        assertTrue(response.data.position.horizontal.altitude >= -90);
        assertTrue(response.data.position.horizontal.azimuth >= 0);
    }

}