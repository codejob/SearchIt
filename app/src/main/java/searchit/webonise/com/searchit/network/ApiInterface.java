package searchit.webonise.com.searchit.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import searchit.webonise.com.searchit.model.Places;
import searchit.webonise.com.searchit.model.placesdetails.PhotoDetails;


public interface ApiInterface {
    @GET("textsearch/json")
    Call<Places> getPlaces(@Query("query") String query,@Query("key") String apiKey);

    @GET("nearbysearch/json")
    Call<Places> getPlacesNearBy(@Query("location") String location,@Query("radius") String radius,@Query("key") String apiKey);

    @GET("details/json")
    Call<PhotoDetails> getPlaceDeatil(@Query("placeid") String placeid, @Query("key") String apiKey);
//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
