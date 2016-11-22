package searchit.webonise.com.searchit.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import searchit.webonise.com.searchit.R;
import searchit.webonise.com.searchit.utils.Utility;

import static android.content.ContentValues.TAG;

/**
 * Created by Prateek on 11/21/2016.
 * This class will take the inputstream of the image from server and download and save in sd card.
 */

public class DownloadFileAsyncTask extends AsyncTask<InputStream, Void, String> {

    final String appDirectoryName = "SearchIt";
    final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);
    Context mContext = null;
    ProgressDialog progress = null;
    InputStream inputStream = null;
    String filename = null;

    public DownloadFileAsyncTask(Context mContext, String filename) {
        this.mContext = mContext;
        this.filename = filename+".jpg";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(mContext);
        progress.setCancelable(false);
        progress.setMessage(mContext.getResources().getString(R.string.download_progress_title));
        progress.show();
    }

    @Override
    protected String doInBackground(InputStream... params) {


        OutputStream output = null;
        try {
            inputStream = params[0];


            if (!imageRoot.exists())
                imageRoot.mkdirs();
            File file = new File(imageRoot, filename);
            if (file.exists()) {
                return Utility.getString(mContext, R.string.file_exist);
            }
            output = new FileOutputStream(file);

            byte[] buffer = new byte[1024]; // or other buffer size
            int read;

            Log.d(TAG, "Attempting to write to: " + imageRoot + "/" + filename);
            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                Log.v(TAG, "Writing to buffer to output stream.");
            }
            Log.d(TAG, "Flushing output stream.");
            output.flush();
            Log.d(TAG, "Output flushed.");
        } catch (IOException e) {
            Log.e(TAG, "IO Exception: " + e.getMessage());
            e.printStackTrace();
            return Utility.getString(mContext, R.string.download_fail);
        } finally {
            try {
                if (output != null) {
                    output.close();
                    Log.d(TAG, "Output stream closed sucessfully.");
                } else {
                    Log.d(TAG, "Output stream is null");
                }
            } catch (IOException e) {
                Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                e.printStackTrace();
                return Utility.getString(mContext, R.string.download_fail);
            }
        }
        return Utility.getString(mContext, R.string.download_complete);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
        Log.d(TAG, "Download success: " + result);
        Utility.showToast(mContext, result);
        // TODO: show a snackbar or a toast
    }
}
