package searchit.webonise.com.searchit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.model.placesdetails.Photo;
import searchit.webonise.com.searchit.model.placesdetails.Result;
import searchit.webonise.com.searchit.network.ApiClient;
import searchit.webonise.com.searchit.network.ApiInterface;
import searchit.webonise.com.searchit.network.DownloadFileAsyncTask;
import searchit.webonise.com.searchit.utils.Utility;

import static android.content.ContentValues.TAG;

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
    ApiInterface service;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_images_list_item_layout, parent, false);
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.common_google_signin_btn_icon_light)
                .showImageOnFail(R.drawable.common_google_signin_btn_icon_light)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        return new MyViewHolder(itemView);
    }

    public PlacesImagesAdapter(Result places, Context context) {
        mContext = context;
        this.places = places;
        utility = new Utility();
        photos = this.places.getPhotos();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .build();
        service = retrofit.create(ApiInterface.class);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //download and display image from url
        String ref = photos.get(position).getPhotoReference();


        ImageLoader.getInstance().displayImage(utility.getPhotosUrl(ref, "400"), holder.place, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
        holder.place.setTag(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView place;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            place = (ImageView) view.findViewById(R.id.img_place);
            progressBar = (ProgressBar) view.findViewById(R.id.loading);

        }


        @Override
        public void onClick(View view) {
            if (Utility.hasSDCard() ) {
                if(Utility.isOnline(mContext)){
                final String ref = photos.get(getPosition()).getPhotoReference();
                service.downloadImage(utility.getPhotosUrl(ref, "800")).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, response.message());
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "Something's gone wrong");
                            // TODO: show error message
                            return;
                        }
                        DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask(mContext, ref);
                        downloadFileAsyncTask.execute(response.body().byteStream());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        // TODO: show error message
                    }
                });
                }else{
                    Utility.showToast(mContext, Utility.getString(mContext, R.string.no_network));
                }
            } else {
                Utility.showToast(mContext, Utility.getString(mContext, R.string.sdcard_not_present));
            }
        }
    }
}
