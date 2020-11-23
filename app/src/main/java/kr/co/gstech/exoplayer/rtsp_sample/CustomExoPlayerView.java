package kr.co.gstech.exoplayer.rtsp_sample;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.rtsp.RtspDefaultClient;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.source.rtsp.core.Client;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public class CustomExoPlayerView extends PlayerView implements PlaybackPreparer {
    private static final String TAG = "CustomExoPlayerView";
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;

    public CustomExoPlayerView(Context context) {
        super(context);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initializePlayer(String url) {
        Context context = getContext();
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(context, trackSelectionFactory);

        initMediaSourceRtsp(context, url);

        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        requestFocus();
    }

    private void initMediaSourceRtsp(Context context, String url) {
        DefaultTrackSelector.ParametersBuilder builder =
                new DefaultTrackSelector.ParametersBuilder(/* context= */ context);
        trackSelector.setParameters(builder.build());

        MediaSource mediaSource = new RtspMediaSource.Factory(RtspDefaultClient.factory()
                .setFlags(Client.FLAG_ENABLE_RTCP_SUPPORT)
                .setNatMethod(Client.RTSP_NAT_DUMMY))
                .createMediaSource(Uri.parse(url));

        RenderersFactory renderersFactory = buildRenderersFactory(false, context);

        player = new SimpleExoPlayer.Builder(context, renderersFactory)
                .setTrackSelector(trackSelector)
                .build();
        player.addListener(new PlayerEventListener());
        player.setPlayWhenReady(true);
        setPlayer(player);
        setPlaybackPreparer(this);
        player.prepare(mediaSource, true, false);
    }

    public RenderersFactory buildRenderersFactory(boolean preferExtensionRenderer, Context context) {
        @DefaultRenderersFactory.ExtensionRendererMode
        int extensionRendererMode =
                useExtensionRenderers()
                        ? (preferExtensionRenderer
                        ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(/* context= */ context)
                .setExtensionRendererMode(extensionRendererMode);
    }

    public boolean useExtensionRenderers() {
        return "withExtensions".equals("noExtensions");
    }

    private static class PlayerEventListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, @Player.State int playbackState) {
            Log.e(TAG, "onPlayerStateChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            Log.e(TAG, "onPlayerError");
            e.printStackTrace();
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.e(TAG, "onTracksChanged");
        }
    }

    public int getCurrentPosition() {
        return (int) player.getCurrentPosition();
    }

    public boolean isPlaying() {
        return player.getPlayWhenReady();
    }

    public void pause() {
        player.setPlayWhenReady(false);
    }

    public void start() {
        player.setPlayWhenReady(true);
    }

    public void seekTo(int position) {
        player.seekTo(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        player.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        player.setPlayWhenReady(false);
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void preparePlayback() {
        player.retry();
    }
}

