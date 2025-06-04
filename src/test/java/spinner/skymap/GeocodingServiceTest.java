package spinner.skymap;

import com.andrewoid.apikeys.ApiKey;
import org.junit.jupiter.api.Test;
import spinner.skymap.json.GeocodingResponse;

import static org.junit.jupiter.api.Assertions.*;

class GeocodingServiceTest {
    @Test
    void getLocationCoordinates() {
        // Given
        GeocodingService service = new GeocodingServiceFactory().getService();
        ApiKey apiKey = new ApiKey("openweathermap");
        String key = apiKey.get();

        // When
        GeocodingResponse[] responses = service.getLocationCoordinates("Manhattan", 1, key).blockingGet();

        // Then
        assertNotNull(responses, "Response should not be null");
        assertTrue(responses.length > 0, "Should return at least one result");
        assertNotNull(responses[0].name, "First result should have a name");
        assertNotNull(responses[0].lat, "First result should have a latitude");
        assertNotNull(responses[0].lon, "First result should have a longitude");
        System.out.println("Found: " + responses[0].name + " (" + responses[0].lat + ", " + responses[0].lon + ")");
    }
}
