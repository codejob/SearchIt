package searchit.webonise.com.searchit.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.adapter.PlacesAdapter;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.network.RetroCallImplementor;
import searchit.webonise.com.searchit.network.RetroCallIneractor;

public class SearchActivity extends Activity implements View.OnClickListener {
    String[] language = {"Indore", "ujjain", "Pune", "US", "iPhone", "Android", "ASP.NET", "PHP"};
    private RecyclerView recyclerView;
    private ImageButton mBtn_select_place;
    AutoCompleteTextView actv;
    private PlacesAdapter mAdapter;
    private List<Result> places = new ArrayList<>();
    private Handleupdate handler = null;
    private RetroCallImplementor retroCallImplementor;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handler = new Handleupdate();
        retroCallImplementor = new RetroCallImplementor();
        initView();

    }

    private void initView() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage(this.getResources().getString(R.string.progress_title));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, language);
        //Getting the instance of AutoCompleteTextView
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLUE);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               progress.show();
                retroCallImplementor.getAllPlaces(adapter.getItem(position).toString(), handler);

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mBtn_select_place = (ImageButton) findViewById(R.id.button_select_place);
        mBtn_select_place.setOnClickListener(this);
        mAdapter = new PlacesAdapter(places,SearchActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    class Handleupdate implements RetroCallIneractor {

        @Override
        public void updatePlaces(List<Result> places) {
            if(progress!=null && progress.isShowing()){
                progress.dismiss();
            }
            SearchActivity.this.places = places;
            mAdapter.notifyDataSetChanged();
            mAdapter = new PlacesAdapter(places,SearchActivity.this);
            recyclerView.setAdapter(mAdapter);
        }

        @Override
        public void updatePlaceDetails(Result place) {

        }


        @Override
        public void onFailure() {
            if(progress!=null && progress.isShowing()){
                progress.dismiss();
            }
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select_place:
               if(progress!=null){
                   progress.show();
               }
                retroCallImplementor.getAllPlaces(actv.getText().toString(), handler);

        }
    }
}
