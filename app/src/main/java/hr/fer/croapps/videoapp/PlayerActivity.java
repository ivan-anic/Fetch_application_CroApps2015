package hr.fer.croapps.videoapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.security.Provider;
import java.util.List;


public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView playerView;
    EditText timeGet;
    private Handler handler;
    private ListView videosFound;
    private String newString;
    private List<VideoItem> searchResults;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_player);

        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, (YouTubePlayer.OnInitializedListener) this);
        Button home = (Button) findViewById(R.id.button4);
        Bundle extras = getIntent().getExtras();

        videosFound = (ListView) findViewById(R.id.listView2);
        if (extras == null){
            newString = null;
        } else {
            newString = extras.getString("name");
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(home_intent);
            }
        });
        handler = new android.os.Handler();
        addClickListener();
        searchOnYoutube(newString);
    }

    private void addClickListener(){
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                intent.putExtra("name", newString);
                startActivity(intent);
            }

        });
    }

    private void searchOnYoutube(final String keywords){
        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(PlayerActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound(){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.list_view, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_view, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.icon);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                TextView description = (TextView)convertView.findViewById(R.id.label);

                VideoItem searchResult = searchResults.get(position);
                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player,
                                        boolean restored) {
        if(!restored){
            player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            Button timeSet = (Button) findViewById(R.id.button3);
            timeGet = (EditText) findViewById(R.id.timeEdit);
            Button pause = (Button) findViewById(R.id.buttonpause);
            Button play = (Button) findViewById(R.id.buttonplay);

            timeSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (timeGet.getText().toString().length() > 0) {
                        player.seekToMillis(Integer.parseInt(timeGet.getText().toString())*1000);
                    }

                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.pause();
                }
            });


            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.play();
                }
            });
        }
    }
}