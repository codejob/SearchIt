package searchit.webonise.com.searchit.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import searchit.webonise.com.searchit.model.Places;
import searchit.webonise.com.searchit.model.placesdetails.PhotoDetails;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.utils.Constant;

/**
 * Created by Prateek on 11/19/2016.
 */

public class RetroCallImplementor {
    List<Result> places = null;

    public List<Result> getAllPlaces(String query , final RetroCallIneractor retroCallIneractor){
        places = null;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Places> call = apiService.getPlaces(query,Constant.API_KEY_PLACES);
        call.enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                int statusCode = response.code();

                 places = response.body().getResults();
                retroCallIneractor.updatePlaces(places);
                // recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {
                // Log error here since request failed
                Log.e("ERROR", t.toString());
                retroCallIneractor.onFailure();
            }
        });
        return  places;
    }
    public List<Result> getPlaceDetails(String placeId, final RetroCallIneractor retroCallIneractor){
        places = null;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //Call<Places> call = apiService.getPlacesNearBy(location,radius+"",Constant.API_KEY_PLACES);
        Call<PhotoDetails> call = apiService.getPlaceDeatil(placeId,Constant.API_KEY_PLACES);
        call.enqueue(new Callback<PhotoDetails>() {
            @Override
            public void onResponse(Call<PhotoDetails> call, Response<PhotoDetails> response) {
                int statusCode = response.code();

                Result placesDetails  = response.body().getResult();
                retroCallIneractor.updatePlaceDetails(placesDetails);
            }

            @Override
            public void onFailure(Call<PhotoDetails> call, Throwable t) {
                // Log error here since request failed
                Log.e("ERROR", t.toString());
                retroCallIneractor.onFailure();
            }
        });
        return  places;
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyA8oSQtjL2WTCD6fAoO76uzQc8AB2ZNujk");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
        }
    }
}
