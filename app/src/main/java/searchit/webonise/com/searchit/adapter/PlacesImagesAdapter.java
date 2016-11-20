package searchit.webonise.com.searchit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.model.placesdetails.Location;
import searchit.webonise.com.searchit.model.placesdetails.Photo;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.utils.Constant;
import searchit.webonise.com.searchit.utils.Utility;
import searchit.webonise.com.searchit.view.MapsActivity;

/**
 * Created by Prateek on 11/20/2016.
 */

public class PlacesImagesAdapter extends RecyclerView.Adapter<PlacesImagesAdapter.MyViewHolder> {
    Result places = null;
    Context mContext = null;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    Utility utility;
    List<Photo> photos;

    @Override
    public PlacesImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_images_list_item_layout, parent, false);
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.cast_album_art_placeholder)
                .showImageOnFail(R.drawable.cast_album_art_placeholder)
                .showImageOnLoading(R.drawable.cast_album_art_placeholder).build();
        return new PlacesImagesAdapter.MyViewHolder(itemView);
    }

    public PlacesImagesAdapter(Result places, Context context) {
        mContext = context;
        this.places = places;
        utility = new Utility();
        photos = this.places.getPhotos();
    }

    @Override
    public void onBindViewHolder(PlacesImagesAdapter.MyViewHolder holder, int position) {
        //download and display image from url
        String ref = photos.get(position).getPhotoReference();

        imageLoader.displayImage(utility.getPhotosUrl(ref,"400"), holder.place, options);
        holder.place.setTag(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView place;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            place = (ImageView) view.findViewById(R.id.img_place);

        }


        @Override
        public void onClick(View view) {

            Toast.makeText(mContext, "clicked=" + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
