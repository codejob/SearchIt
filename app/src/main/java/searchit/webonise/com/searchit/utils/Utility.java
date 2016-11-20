package searchit.webonise.com.searchit.utils;

import java.util.ArrayList;
import java.util.List;

import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.network.ApiClient;

/**
 * Created by Prateek on 11/20/2016.
 */

public class Utility {
   public String getPhotosUrl(String reference, String maxSize){
       // https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CoQBdwAAAGUi0qj1njgbj-KTLMSFewPyPxYLS0YJ7wy9K6XwGO8l6sLhdAbm1UljMuC51BaaI3KLqsty_H2Hw4hS0FxrM6aP_nVXq-RykiUSDYO4M-YfwoyjBNeD89qBUX6x1nqtuyIO2UlMtq1H_FHZrSH60eT0yfBDdHnhtgnEoDDqzJnIEhDvzZztoaJ3WcQ-4JD9kvxYGhRVVo8-gGmVuaY4QbqOd_s5WnKAPA
        // &key=AIzaSyA8oSQtjL2WTCD6fAoO76uzQc8AB2ZNujk

        String url = ApiClient.BASE_URL+"photo?maxwidth="+maxSize+"&photoreference="+reference+"&key="+Constant.API_KEY_PLACES;
        return url;
    }


}
