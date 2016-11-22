package searchit.webonise.com.searchit.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import searchit.webonise.com.searchit.model.Places;
import searchit.webonise.com.searchit.model.placesdetails.PhotoDetails;


public interface ApiInterface {
    @GET("textsearch/json")
    Call<Places> getPlaces(@Query("query") String query, @Query("key") String apiKey);

    @GET("nearbysearch/json")
    Call<Places> getPlacesNearBy(@Query("location") String location, @Query("radius") String radius, @Query("key") String apiKey);

    @GET("details/json")
    Call<PhotoDetails> getPlaceDeatil(@Query("placeid") String placeid, @Query("key") String apiKey);
    @GET
    @Streaming
    Call<ResponseBody> downloadImage(@Url String url);
}
