package searchit.webonise.com.searchit.view;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.adapter.PlacesImagesAdapter;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.network.RetroCallImplementor;
import searchit.webonise.com.searchit.network.RetroCallIneractor;
import searchit.webonise.com.searchit.utils.Constant;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String placeID = "";
    private Handleupdate handler = null;
    private RetroCallImplementor retroCallImplementor;
    private ProgressDialog progress;
    private RecyclerView recyclerView;
    private PlacesImagesAdapter mAdapter;
    private Result places = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (this.getIntent().hasExtra(Constant.KEY_INTENT_PLACEID))
            placeID = this.getIntent().getStringExtra(Constant.KEY_INTENT_PLACEID);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage(this.getResources().getString(R.string.progress_title));
        handler = new Handleupdate();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //mAdapter = new PlacesImagesAdapter(places, MapsActivity.this);
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(mAdapter);

        retroCallImplementor = new RetroCallImplementor();
        if (progress != null) {
            progress.show();
        }
        retroCallImplementor.getPlaceDetails(placeID, handler);
    }

    class Handleupdate implements RetroCallIneractor {

        @Override
        public void updatePlaces(List<Result> places) {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }

        @Override
        public void updatePlaceDetails(Result place) {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            MapsActivity.this.places = places;
            mAdapter = new PlacesImagesAdapter(place, MapsActivity.this);
            recyclerView.setAdapter(mAdapter);
        }


        @Override
        public void onFailure() {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
