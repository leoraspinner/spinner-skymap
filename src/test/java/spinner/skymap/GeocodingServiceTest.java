package spinner.skymap;

import com.andrewoid.apikeys.ApiKey;
import org.junit.jupiter.api.Test;
import spinner.skymap.json.GeocodingResponse;

import static org.junit.jupiter.api.Assertions.*;

class GeocodingServiceTest {
    @Test
    void getLocationCoordinates() {
        //Given
        GeocodingService service = new GeocodingServiceFactory().getService();
        ApiKey apiKey = new ApiKey("OPENWEATHERMAP");
        String key = apiKey.get();


        //When
        GeocodingResponse[] responses = service.getLocationCoordinates("Manhattan", 1, key).blockingGet();

        //Then
        assertNotNull(responses, "Response should not be null");
        assertTrue(responses.length > 0, "Response should have at least one location");
        assertEquals("Manhattan", responses[0].name, "First location name should be Manhattan");

    }
}