package spinner.skymap;

import com.andrewoid.apikeys.ApiKey;
import spinner.skymap.json.AstronomyResponse;
import org.junit.jupiter.api.Test;
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
                -73.7292, 40.6247, 0,
                "2025-05-28", "2025-05-28", "15:49:00", "moon"
        ).blockingGet();


        //Then
        assertNotNull(response);
        assertNotNull(response.data);
        assertNotNull(response.data.table);
        assertNotNull(response.data.table.rows);
        assertTrue(response.data.table.rows.length > 0);
        assertNotNull(response.data.table.rows[0].cells);
        assertTrue(response.data.table.rows[0].cells.length > 0);

        AstronomyResponse.Cell cell = response.data.table.rows[0].cells[0];
        assertNotNull(cell.position);
        assertNotNull(cell.position.horizontal);

        double altitude = cell.position.horizontal.altitude.degrees;
        double azimuth = cell.position.horizontal.azimuth.degrees;

        assertTrue(altitude >= -90 && altitude <= 90, "Altitude out of range: " + altitude);
        assertTrue(azimuth >= 0 && azimuth <= 360, "Azimuth out of range: " + azimuth);

        System.out.println("Moon - Altitude: " + altitude + "°, Azimuth: " + azimuth + "°");

    }
}