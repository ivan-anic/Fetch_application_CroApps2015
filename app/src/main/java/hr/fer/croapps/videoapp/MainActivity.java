package hr.fer.croapps.videoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.api.services.youtube.YouTube;

import java.util.ArrayList;

import com.google.api.services.youtube.YouTube;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    static String Target_Translate = "Translate";
    String target_op = Target_Translate; //dummy default
    static String KEY_ANIM = "TARGET_ANIM";
    Button button;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        searchText = (EditText) findViewById(R.id.searchText);
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
                searchText.setTextColor(Color.BLACK);
                searchText.setTypeface(null, Typeface.NORMAL);
                searchText.invalidate();
            }
        });
        animTranslate.setAnimationListener(animationListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                if (searchText.getCurrentTextColor()== Color.BLACK && searchText.getText().toString().length()>0) {
                    target_op = Target_Translate;
                    v.startAnimation(animTranslate);
                }
            }
        });

    }
    Animation.AnimationListener animationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }


        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
            intent.putExtra(KEY_ANIM, target_op);
            intent.putExtra("string", searchText.getText().toString());
            startActivity(intent);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}