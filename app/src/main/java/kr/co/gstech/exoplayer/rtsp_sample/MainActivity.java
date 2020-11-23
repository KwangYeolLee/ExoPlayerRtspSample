package kr.co.gstech.exoplayer.rtsp_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private CustomExoPlayerView customExoPlayerView;
    private CustomExoPlayerView customExoPlayerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMediaPlayer();
    }

    @SuppressLint("AuthLeak")
    private void createMediaPlayer() {
        customExoPlayerView = findViewById(R.id.customPlayerView);
        customExoPlayerView.initializePlayer(
                "rtsp://admin:a123456789@172.30.1.19:554/0");
        customExoPlayerView2 = findViewById(R.id.customPlayerView2);
        customExoPlayerView2.initializePlayer(
                "rtsp://admin:a123456789@172.30.1.19:554/0");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (customExoPlayerView != null) {
            customExoPlayerView.releasePlayer();
        }
        if (customExoPlayerView2 != null) {
            customExoPlayerView2.releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (customExoPlayerView != null) {
            customExoPlayerView.releasePlayer();
        }
        if (customExoPlayerView2 != null) {
            customExoPlayerView2.releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customExoPlayerView != null) {
            customExoPlayerView.releasePlayer();
        }
        if (customExoPlayerView2 != null) {
            customExoPlayerView2.releasePlayer();
        }
    }
}