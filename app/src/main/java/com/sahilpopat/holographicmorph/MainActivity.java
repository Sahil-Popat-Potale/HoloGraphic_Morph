package com.sahilpopat.holographicmorph;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // TODO: add a image and video handling and management
    // TODO: add a feature to view images and videos in a holographic way
    // TODO: provide a two catalog views, one for images and one for videos
    // TODO: provide a two catalog types as personal and public
    // TODO: provide a catalog of images and videos as samples in app
    // TODO: add a feature to add images and videos to the catalog
    // TODO: add a feature to delete images and videos from the catalog

    private MediaPlayer mediaPlayer;
    public GLSurfaceView glSurfaceView;
    public HoloRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up GLSurfaceView for OpenGL rendering
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new HoloRenderer();
        glSurfaceView.setRenderer(renderer);

        // Set up MediaPlayer for video playback
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("path/to/your/video/file.mp4");
            SurfaceTexture surfaceTexture = new SurfaceTexture(10);
            Surface surface = new Surface(surfaceTexture);
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepare();
            mediaPlayer.start();
            //surfaceTexture.release();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}