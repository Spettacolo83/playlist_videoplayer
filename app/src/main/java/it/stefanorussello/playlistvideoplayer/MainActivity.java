package it.stefanorussello.playlistvideoplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private JSONObject jsonPlaylist;
    private ProgressDialog pDialog;
    private ListView listVideo;
    private PlaylistAdapter playlistAdapter;

    public interface PlaylistLoadingHandler {
        // Handler called when loading of json finished
        void loadingFinished(boolean completed, String message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listVideo = (ListView)findViewById(R.id.listVideo);

        try {
            // Retrieving playlist json from webservice
            downloadPlaylist(new PlaylistLoadingHandler() {
                @Override
                public void loadingFinished(boolean completed, String message) {
                    stopLoading(message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            stopLoading(e.getLocalizedMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void downloadPlaylist(final PlaylistLoadingHandler handler) throws IOException {

        // Loading dialog during download
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.LOAD_PLAYLIST));
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.show();

        String urlInfo = "http://www.followmemobile.com/rest/api/videoplayer?transform=1";

        Request request = new Request.Builder()
                .url(urlInfo)
                .get()
                .header("Authorization", Credentials.basic("followmemobile", "elibomemwollof"))
                .build();

        // Calling webservice through OkHttpClient
        OkHttpClient clientStatus = new OkHttpClient();

        clientStatus.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Failed retrieving
                handler.loadingFinished(false, getResources().getString(R.string.ERROR_CONN));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Processing webservice response
                if (response.isSuccessful()) {
                    String strInfo = null;
                    try {
                        // Creating Json object for playlist
                        strInfo = response.body().string();
                        jsonPlaylist = new JSONObject(strInfo);

                        handler.loadingFinished(true, null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: ERROR to retrieve playlist details");
                        handler.loadingFinished(false, getResources().getString(R.string.ERROR_JSON));
                    }
                } else {
                    handler.loadingFinished(false, getResources().getString(R.string.ERROR_CONN));
                }
            }
        });
    }

    private void stopLoading(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Stop loading dialog
                    pDialog.dismiss();
                    if (message != null) {
                        // If error, it appears as a Toast
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG  ).show();
                    }
                    if (playlistAdapter == null) {
                        // Setting Playlist adapter to ListView
                        playlistAdapter = new PlaylistAdapter(MainActivity.this, jsonPlaylist);
                        listVideo.setAdapter(playlistAdapter);
                    } else {
                        // Refresh Playlist adapter if not null
                        playlistAdapter.notifyDataSetChanged();
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "run: Error: " + ex.getLocalizedMessage());
                }
            }
        });
    }


    // Playlist Adapter

    public class PlaylistAdapter extends BaseAdapter {

        Context mContext;
        JSONObject jPlaylist;
        JSONArray aPlaylist;

        public PlaylistAdapter(Context context, JSONObject jsonPlaylist)
        {
            // Constructor

            mContext = context;
            jPlaylist = jsonPlaylist;
            try {
                aPlaylist = jPlaylist.getJSONArray("videoplayer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            if (aPlaylist != null) {
                return aPlaylist.length();
            }

            return 0;
        }

        @Override
        public Object getItem(int i) {
            if (aPlaylist != null) {
                try {
                    return aPlaylist.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        public long getItemId(int i) {
            if (aPlaylist != null) {
                try {
                    return aPlaylist.getJSONObject(i).getLong("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            // Creating view and its components
            ViewHolder vh;
            Bitmap bmpThumb = null;

            view = getLayoutInflater().inflate(R.layout.list_item_video, null);
            vh = new ViewHolder();

            vh.cellClickable = (LinearLayout)view.findViewById(R.id.cellClickable);
            vh.cellClickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openVideo(i);
                }
            });
            vh.imgThumb = (ImageView)view.findViewById(R.id.imgThumb);
            vh.lblTitle = (TextView)view.findViewById(R.id.lblTitle);
            vh.lblTime = (TextView)view.findViewById(R.id.lblTime);

            view.setTag(vh);

            try {
                String urlThumb = aPlaylist.getJSONObject(i).getString("thumbnail");

                // Using Picasso class to load the thumbnails
                Picasso.with(MainActivity.this).invalidate(urlThumb);
                Picasso.with(MainActivity.this)
                        .load(urlThumb)
                        .into(vh.imgThumb);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                // Setting time, title and thumbnail image on view
                vh.lblTitle.setText(aPlaylist.getJSONObject(i).getString("title"));
                long lTime = aPlaylist.getJSONObject(i).getLong("time");
                String strTime = String.format(Locale.getDefault(),"%02d min %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(lTime),
                        TimeUnit.MILLISECONDS.toSeconds(lTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lTime))
                );
                vh.lblTime.setText(strTime);
                vh.imgThumb.setImageBitmap(bmpThumb);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return view;
        }

        private void openVideo(int nPosition) {

            // Called when a video has been selected from list
            Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);

            intent.putExtra("playlist", jPlaylist.toString());
            intent.putExtra("currentvideo", nPosition);

            // Opening VideoPlayer activity
            startActivity(intent);
        }
    }

    // Holder of list_item_video layout
    static class ViewHolder
    {
        LinearLayout cellClickable;
        ImageView imgThumb;
        TextView lblTitle;
        TextView lblTime;
    }


}