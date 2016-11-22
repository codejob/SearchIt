package searchit.webonise.com.searchit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.model.placesdetails.Location;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.utils.Constant;
import searchit.webonise.com.searchit.view.MapsActivity;

/**
 * Created by Prateek on 11/19/2016.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    List<Result> places = null;
    Context mContext = null;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_list_items_layouts, parent, false);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // int itemPosition = recyclerView.indexOfChild(v);
//                Intent intent = new Intent(mContext, MapsActivity.class);
//                mContext.startActivity(intent);
//                //Toast.makeText(mContext,"clicked="+ getPosition(),Toast.LENGTH_SHORT).show();
//
//            }
//        });
        return new MyViewHolder(itemView);
    }

    public PlacesAdapter(List<Result> places, Context context) {
        mContext = context;
        this.places = places;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String place = places.get(position).getName();
        holder.place.setText(place);
        holder.place.setTag(position);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView place;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            place = (TextView) view.findViewById(R.id.textView_place_name);

        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, MapsActivity.class);
            Location location = places.get(getPosition()).getGeometry().getLocation();
            StringBuilder string_location = new StringBuilder();

            string_location.append(location.getLat() + "," + location.getLng());
            intent.putExtra(Constant.KEY_INTENT_PLACEID, places.get(getPosition()).getPlaceId());
            mContext.startActivity(intent);
        }
    }
}
