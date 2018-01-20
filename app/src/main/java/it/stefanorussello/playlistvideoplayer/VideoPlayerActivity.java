package it.stefanorussello.playlistvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stefano Russello on 19/01/18.
 */

public class VideoPlayerActivity extends AppCompatActivity implements VideoRendererEventListener {

    private static final String TAG = "VideoPlayerActivity";
    private JSONObject jsonPlaylist;
    private int currentVideo = 0;
    private JSONArray aPlaylist = null;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve information from extras
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String strPlaylist = bundle.getString("playlist"); // Json playlist
        currentVideo = bundle.getInt("currentvideo"); // Selected video

        if (bundle != null) {
            try {
                jsonPlaylist = new JSONObject(strPlaylist);
                aPlaylist = jsonPlaylist.getJSONArray("videoplayer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.viewPlayer);

        // Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "playlistvideoplayer"), bandwidthMeterA);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // Loading playlist from json
        final DynamicConcatenatingMediaSource mediaSource = new DynamicConcatenatingMediaSource();
        for (int i=0; i<aPlaylist.length(); i++) {
            try {
                String currentUrl = aPlaylist.getJSONObject(i).getString("url");

                mediaSource.addMediaSource(new HlsMediaSource(Uri.parse(currentUrl), dataSourceFactory, 1, null, null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Prepare player and seek to selected track
        player.prepare(mediaSource);
        player.seekToDefaultPosition(currentVideo);
        changeTitle(currentVideo);


        player.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onPositionDiscontinuity() {
                changeTitle(player.getCurrentWindowIndex());
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                player.stop();
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }
        });

        player.setPlayWhenReady(true); // Run file/link when ready to play.
        player.setVideoDebugListener(this); // For listening to resolution change and outputing the resolution
    }

    private void changeTitle(int posVideo) {

        try {
            String currentTitle = aPlaylist.getJSONObject(posVideo).getString("title");
            if (currentTitle != null) {
                getSupportActionBar().setTitle(currentTitle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // VideoRendererEventListener methods

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged ["  + " width: " + width + " height: " + height + "]");
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        player.release();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // When pressed back the player activity will be closed
        finish();
        return true;
    }
}
