package searchit.webonise.com.searchit.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.adapter.PlacesAdapter;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.network.RetroCallImplementor;
import searchit.webonise.com.searchit.network.RetroCallIneractor;

public class SearchActivity extends Activity implements View.OnClickListener {
    public static final String PREFS_NAME = "PingBusPrefs";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";
    private SharedPreferences settings;
    private Set<String> history;
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
        settings = getSharedPreferences(PREFS_NAME, 0);
        history = settings.getStringSet(PREFS_SEARCH_HISTORY, new HashSet<String>());
        setAutoCompleteSource();
        // Set the "Enter" event on the search input
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        textView.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(progress!=null){
                        progress.show();
                    }
                    retroCallImplementor.getAllPlaces(actv.getText().toString(), handler);
                    addSearchInput(textView.getText().toString());
                    return true;
                }
                return false;
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
    private void addSearchInput(String input)
    {
        if (!history.contains(input))
        {
            history.add(input);
            setAutoCompleteSource();
        }
    }
    private void setAutoCompleteSource()
    {
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setTextColor(Color.BLUE);
       final  ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                progress.show();
                //savedSearch.
                retroCallImplementor.getAllPlaces(adapter.getItem(position).toString(), handler);

            }
        });
        actv.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    addSearchInput(actv.getText().toString());
                    if(progress!=null){
                        progress.show();
                    }
                    retroCallImplementor.getAllPlaces(actv.getText().toString(), handler);
                    return true;
                }
                return false;
            }
        });
        actv.setAdapter(adapter);
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
    private void savePrefs()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREFS_SEARCH_HISTORY, history);

        editor.commit();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        savePrefs();
    }

    @Override
    public void onBackPressed() {
        savePrefs();
        super.onBackPressed();
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
