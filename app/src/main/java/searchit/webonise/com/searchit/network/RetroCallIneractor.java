package searchit.webonise.com.searchit.network;

import java.util.List;

import searchit.webonise.com.searchit.model.placesdetails.Result;

/**
 * Created by Prateek on 11/19/2016.
 */

public interface RetroCallIneractor {
    public void updatePlaces(List<Result> places);
    public void updatePlaceDetails(Result place);
    public void onFailure();
}
