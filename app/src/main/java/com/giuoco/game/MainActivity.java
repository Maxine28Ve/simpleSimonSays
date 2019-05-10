package com.giuoco.game;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.R.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private int order;
    private int actualMove;
    public CountDownTimer gameTimer;
    private boolean noSwipeWasPerformed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                actualMove = 0;
                noSwipeWasPerformed = false;
                rightMove();

            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                actualMove = 3;
                noSwipeWasPerformed = false;
                rightMove();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                actualMove = 2;
                noSwipeWasPerformed = false;
                rightMove();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                noSwipeWasPerformed = false;
                actualMove = 1;
                rightMove();
            }

        });
        gameTimer = new CountDownTimer(1500, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                final TextView timer = (TextView) findViewById(R.id.timer);
                timer.setText("remaining : " + millisUntilFinished + " secs");
            }

            @Override
            public void onFinish() {
                int point = 0;
                final TextView points = (TextView) findViewById(R.id.points);
                String testingPoints = new String(points.getText().toString());
                if(testingPoints != null && testingPoints.isEmpty() == false){
                    point = Integer.parseInt(testingPoints);
                }
                else{
                    point = 0;
                }
                if(point == 0){
                    final TextView gameover = (TextView) findViewById(R.id.gameover);
                    gameover.setText("You ran out of points! Game Over");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.gameTimer.cancel();
                            MainActivity.this.finish();
                        }
                    }, 1*1000);

                }
                if(noSwipeWasPerformed == true){
                    MainActivity.this.actualMove = -1;
                    MainActivity.this.rightMove();
                }
                SimonSays();
            }

            public void customOnStart(){
                onStart();
            }
        };
        SimonSays();
    }

    public void SimonSays(){
        final String[] lmao = new String[4];
        lmao[0] = "Swipe UP!";
        lmao[1] = "Swipe DOWN!";
        lmao[2] = "Swipe LEFT!";
        lmao[3] = "Swipe RIGHT!";
        final TextView textViewToChange = (TextView) findViewById(R.id.command);
        textViewToChange.setText("");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                int index = r.nextInt(4);
                MainActivity.this.order = index;
                textViewToChange.setText(lmao[index]);
                MainActivity.this.gameTimer.start();
                noSwipeWasPerformed = true;
            }
        }, 1*1000);

    }

    public void rightMove(){
        int point = 0;
        TextView points = (TextView) findViewById(R.id.points);
        String testingPoints = new String(points.getText().toString());
        if(testingPoints != null && testingPoints.isEmpty() == false){
            point = Integer.parseInt(testingPoints);
        }
        else{
            point = 0;
        }
        points.setText(String.valueOf(this.order == this.actualMove ? point + 1 : ((point > 0) ? point - 1 : 0)));
        Log.d("Points", "point: " + point + ", points: " + points.getText());
    }

}

class GameTimer extends TimerTask {
    MainActivity act;
    public GameTimer(MainActivity act){
        this.act = act;
    }

    public void run() {
        this.act.SimonSays();
    }

    public void onFinish(){

    }
}