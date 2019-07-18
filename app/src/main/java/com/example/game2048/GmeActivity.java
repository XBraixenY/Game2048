package com.example.game2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.game2048.beans.Grid;
import com.example.game2048.beans.OnSwipeTouchListener;
import com.example.game2048.beans.Square;

public class GmeActivity extends AppCompatActivity  implements GestureDetector.OnGestureListener {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int VELOCITY_THRESHOLD = 100;
    public static final String PREFS_NAME = "Preferences";

    int [][] imagesId;
    Square [] [] gridSquares;
    Grid grid;
    Button restartBtn;
    GridLayout myLayout;
    GestureDetector gestureDetector;
    Button scoreBtn, bestBtn;
    SharedPreferences settings;
    TextView gameOver;

    boolean isOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gme);
        imagesId = new int[4][4];
        gridSquares = new Square[4][4];
        final View[] viewsId = new View[16];

        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int best = settings.getInt("best", 0);
        grid = new Grid(4, this, best);

        viewsId[0]=findViewById(R.id.game_0_0);
        viewsId[1]=findViewById(R.id.game_0_1);
        viewsId[2]=findViewById(R.id.game_0_2);
        viewsId[3]=findViewById(R.id.game_0_3);
        viewsId[4]=findViewById(R.id.game_1_0);
        viewsId[5]=findViewById(R.id.game_1_1);
        viewsId[6]=findViewById(R.id.game_1_2);
        viewsId[7]=findViewById(R.id.game_1_3);
        viewsId[8]=findViewById(R.id.game_2_0);
        viewsId[9]=findViewById(R.id.game_2_1);
        viewsId[10]=findViewById(R.id.game_2_2);
        viewsId[11]=findViewById(R.id.game_2_3);
        viewsId[12]=findViewById(R.id.game_3_0);
        viewsId[13]=findViewById(R.id.game_3_1);
        viewsId[14]=findViewById(R.id.game_3_2);
        viewsId[15]=findViewById(R.id.game_3_3);

        restartBtn = findViewById(R.id.BtnRestart);
        myLayout = findViewById(R.id.Grid1);
        scoreBtn = findViewById(R.id.score);
        bestBtn = findViewById(R.id.best);
        gameOver = findViewById(R.id.gameOver);

        bestBtn.setText("BEST\n"+best);


        gestureDetector = new GestureDetector(this);
        //this may not work
        for (View v : viewsId){
            v.setOnTouchListener(new OnSwipeTouchListener(GmeActivity.this){
                public void onSwipeTop() {
                    grid.swipeOnGrid(Grid.UP);
                    setScoreAndBest();
                    isOver = grid.getIsOver();
                }
                public void onSwipeRight() {
                    grid.swipeOnGrid(Grid.RIGHT);
                    setScoreAndBest();
                    isOver = grid.getIsOver();
                }
                public void onSwipeLeft() {
                    grid.swipeOnGrid(Grid.LEFT);
                    setScoreAndBest();
                    isOver = grid.getIsOver();
                }
                public void onSwipeBottom() {
                    grid.swipeOnGrid(Grid.DOWN);
                    setScoreAndBest();
                    isOver = grid.getIsOver();
                }
            });
        }

        //Setting the grid
        grid.setGridSquares(viewsId);
        grid.restartGrid();
        grid.addRandomNumber();

        //setting the actions for buttons
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid.restartGrid();
                grid.addRandomNumber();
                setScoreAndBest();
                gameOver.setVisibility(View.INVISIBLE);
                myLayout.setVisibility(View.VISIBLE);
            }
        });


    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float vx, float vy) {
        boolean result = false;
        //movement across X and Y
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        if(Math.abs(diffX)>Math.abs(diffY)){
            //right or left swipe
            if(Math.abs(diffX)> SWIPE_THRESHOLD && Math.abs(vx)> VELOCITY_THRESHOLD){
                if(diffX>0){
                    grid.swipeOnGrid(Grid.RIGHT);
                    isOver = grid.getIsOver();

                }else {
                    grid.swipeOnGrid(Grid.LEFT);
                    isOver = grid.getIsOver();
                }
                result = true;
                setScoreAndBest();
            }
        }else{
            //up or down swipe
            if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(vy)>VELOCITY_THRESHOLD){
                if(diffY>0){
                    grid.swipeOnGrid(Grid.DOWN);
                    isOver = grid.getIsOver();
                }else{
                    grid.swipeOnGrid(Grid.UP);
                    isOver = grid.getIsOver();
                }
                result = true;
                setScoreAndBest();

            }
        }
        if(isOver) {
            System.out.println("IS OVER");
            gameOver.setVisibility(View.VISIBLE);
            //mylayout.setVisibility(View.INVISIBLE);
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void setScoreAndBest(){
        int score = grid.getScore();
        int best = grid.getBest();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("best",best);
        editor.apply();
        scoreBtn.setText("SCORE\n"+score);
        bestBtn.setText("BEST\n"+best);
    }
}




