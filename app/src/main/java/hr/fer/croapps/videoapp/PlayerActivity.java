package hr.fer.croapps.videoapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.security.Provider;


public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView playerView;
    Boolean clicked;
    EditText timeGet;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_player);

        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, (YouTubePlayer.OnInitializedListener) this);
        Button timeSet = (Button) findViewById(R.id.button3);
        timeGet = (EditText) findViewById(R.id.timeEdit);
        clicked = false;

        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeGet.getText().toString().length()>0){
                    playerView.initialize(YoutubeConnector.KEY, (YouTubePlayer.OnInitializedListener) PlayerActivity.this);
                    clicked = true;
                }

            }
        });
        /*timeGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked){
                    clicked=false;
                    player.loadVideo(getIntent().getStringExtra("VIDEO_ID"),Integer.parseInt(timeGet.getText().toString()));

                }else
                    player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            }
            });
        */
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
        //player.loadVideo(getIntent().getStringExtra("VIDEO_ID"), Integer.parseInt(timeGet.getText().toString()));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean restored) {
        if(!restored){
            if (clicked){
                clicked=false;
                player.seekToMillis(Integer.parseInt(timeGet.getText().toString()));
            }else
                player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
        }
    }
}
