package com.mradzinski.casterexample;

import static com.mradzinski.casterexample.NyMimeTypes.getFileNameWithoutExtFromPath;
import static com.mradzinski.casterexample.NyMimeTypes.getMimeTypeFromPath;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.mediarouter.app.MediaRouteButton;

import com.mradzinski.caster.Caster;
import com.mradzinski.caster.ExpandedControlsStyle;
import com.mradzinski.caster.MediaData;

public class CasterDemoActivity extends AppCompatActivity {
    private static final String VIMEO_URL = "http://d3rlna7iyyu8wu.cloudfront.net/skip_armstrong/skip_armstrong_stereo_subs.m3u8";

    private Button playButton;
    private Button resumeButton;
    private Caster caster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caster_demo);

        caster = Caster.create(this);
        caster.addMiniController(R.layout.custom_mini_controller);

        ExpandedControlsStyle style = new ExpandedControlsStyle.Builder()
                .setSeekbarLineColor(getResources().getColor(R.color.green))
                .setSeekbarThumbColor(getResources().getColor(R.color.white))
                .setStatusTextColor(getResources().getColor(R.color.green))
                .build();

        caster.setExpandedPlayerStyle(style);

        setUpPlayButton();
        setUpMediaRouteButton();

    }

    private static String videoUrl = "http://d3rlna7iyyu8wu.cloudfront.net/skip_armstrong/skip_armstrong_stereo_subs.m3u8";

    private EditText dialogUrlEdittext;

    @Override
    public void onResume() {
        // paste whatever there is in the clipboard (hopefully it is a video url)
        super.onResume();
        CharSequence charSequence = getClipboardItem(this);
        if (charSequence != null) {
            dialogUrlEdittext.setText(charSequence.toString());
        }
    }

    private void setUpPlayButton() {
        dialogUrlEdittext = findViewById(R.id.dialog_url_edittext);
        playButton = findViewById(R.id.button_play);
        resumeButton = findViewById(R.id.button_resume);
        findViewById(R.id.dialog_url_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUrlEdittext.setText("");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoUrl = dialogUrlEdittext.getText().toString();
                if (!videoUrl.isEmpty()) {
                  //  caster.getPlayer().loadMediaAndPlay(createSampleMediaData());
                    //start with minicontroler
                    caster.getPlayer().loadMediaAndPlayInBackground(createSampleMediaData());
                }
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caster.getPlayer().isPaused()) {
                    caster.getPlayer().togglePlayPause();
                }
            }
        });

        caster.setOnConnectChangeListener(new Caster.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                playButton.setEnabled(true);
            }

            @Override
            public void onDisconnected() {
                playButton.setEnabled(false);
                resumeButton.setEnabled(false);
            }
        });

        caster.setOnCastSessionStateChanged(new Caster.OnCastSessionStateChanged() {
            @Override
            public void onCastSessionBegan() {
              //  playButton.setEnabled(false);
                resumeButton.setEnabled(false);
                Log.e("Caster", "Began playing video");
            }

            @Override
            public void onCastSessionFinished() {
                playButton.setEnabled(true);
                resumeButton.setEnabled(false);
                Log.e("Caster", "Finished playing video");
            }

            @Override
            public void onCastSessionPlaying() {
                String playingURL = caster.getPlayer().getCurrentPlayingMediaUrl();

                if (playingURL != null && playingURL.equals(videoUrl)) {
                  //  playButton.setEnabled(false);
                } else {
                    playButton.setEnabled(true);
                }

                resumeButton.setEnabled(false);
                Log.e("Caster", "Playing video");
            }

            @Override
            public void onCastSessionPaused() {
              //  playButton.setEnabled(false);
                resumeButton.setEnabled(true);
                Log.e("Caster", "Paused video");
            }
        });
    }


    private static MediaData createSampleMediaData() {
        String mimeType = getMimeTypeFromPath(videoUrl);
        return new MediaData.Builder(videoUrl)
                .setStreamType(MediaData.STREAM_TYPE_LIVE)
                //  .setContentType("application/x-mpeygURL")
                .setContentType(mimeType)
                .setMediaType(mimeType.contains("image") ? MediaData.MEDIA_TYPE_PHOTO : MediaData.MEDIA_TYPE_MOVIE)
                .setTitle(getFileNameWithoutExtFromPath(videoUrl))
               // .setDescription("Isaac searches for Rebekah to retrieve Arachnid's stolen XP.")
               // .setThumbnailUrl("https://dg8ynglluh5ez.cloudfront.net/151/1517168873360394134/square_thumbnail.jpg")
                .setPlaybackRate(MediaData.PLAYBACK_RATE_NORMAL)
                .setAutoPlay(true)
                .build();
    }



    private void setUpMediaRouteButton() {
        MediaRouteButton mediaRouteButton = findViewById(R.id.media_route_button);
        caster.setupMediaRouteButton(mediaRouteButton, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        caster.addMediaRouteMenuItem(menu, true);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static CharSequence getClipboardItem(Context context) {
        CharSequence clipboardText = null;
        ClipboardManager clipboardManager = ContextCompat.getSystemService(context, ClipboardManager.class);

        // if the clipboard contain data ...
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);

            // gets the clipboard as text.
            clipboardText = item.coerceToText(context);
        }

        return clipboardText;
    }
}
