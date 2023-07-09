package com.example.vanni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {

        super.onDestroy();
        mediaPlayer.start();
        mediaPlayer.release();
        updateSeek.interrupt();
    }
    TextView textView;
    TextView textView2,endt,currt;
    ImageView play,previous,next,imageView2;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    int position;
    String textContent;
    SeekBar seekBar;
    Thread updateSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        play = findViewById(R.id.play);
        imageView2 = findViewById(R.id.imageView2);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        endt = findViewById(R.id.endt);
        currt = findViewById(R.id.currt);

        seekBar = findViewById(R.id.seekBar);

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        imageView2.startAnimation(rotate);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textView.setSelected(true);

        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);

        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        String endTime= createTime(mediaPlayer.getDuration());
        endt.setText(endTime);

        final Handler handler=new Handler();
        final int delay=1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime= createTime(mediaPlayer.getCurrentPosition());
                currt.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);

        updateSeek = new Thread() {
            @Override
            public void run() {

                int currentPosition = 0;

                while (currentPosition < mediaPlayer.getDuration()) {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeek.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_500), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_500), PorterDuff.Mode.SRC_IN);




        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    textView.setSelected(false);
                    imageView2.clearAnimation();
                } else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    textView.setSelected(true);
                    imageView2.startAnimation(rotate);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next.performClick();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != 0) {
                    position = position - 1;
                } else {
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                textView.setSelected(true);
                imageView2.startAnimation(rotate);
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                textView.setSelected(true);
                imageView2.startAnimation(rotate);
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
    }
        public String createTime(int duration)
        {
            String time="";
            int min= duration/1000/60;
            int sec= duration/1000%60;

            time+=min+":";

            if(sec<10)
            {
                time+="0";

            }
           time+=sec;
            return time;


        }



}