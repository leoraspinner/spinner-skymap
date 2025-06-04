package spinner.skymap;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import spinner.skymap.json.GeocodingResponse;

public interface GeocodingService {
    @GET("geo/1.0/direct")
    Single<GeocodingResponse[]> getLocationCoordinates(
            @Query("q") String locationName,
            @Query("limit") int limit,
            @Query("appid") String apiKey
    );
}
