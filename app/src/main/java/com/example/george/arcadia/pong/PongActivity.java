package com.example.george.arcadia.pong;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.example.george.arcadia.MenuActivity;
import com.example.george.arcadia.R;

import java.text.DecimalFormat;

public class PongActivity extends AppCompatActivity {
    private PongView view;
    private GameState state;
    private GameLogic logic;
    private Handler handler;
    private SurfaceHolder holder;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        view = (PongView)findViewById(R.id.pong_view);
        view.setParent(this);

        logic = new GameLogic(this);
        view.setLogic(logic);

        handler = new Handler();
        paint = new Paint();


    }

    public void runGame(){

    }

    public void setGameState(GameState state){
        this.state = state;
    }

    public GameState getState(){
        return state;
    }

    public void setHolder(SurfaceHolder holder){
        this.holder = holder;
    }




    public void displayWinDialogFragment(String winner) {
        Bundle args = new Bundle();
        args.putString("winner", winner);
        WinDialogFragment dialog = new WinDialogFragment();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"newGame");
    }

    @Override
    public void onBackPressed()
    {
        boolean stopped = view.stopThread();
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

        if(stopped){
            Intent intent = new Intent(PongActivity.this, MenuActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(PongActivity.this, "Error: cannot go back till the thread has finished",
                    Toast.LENGTH_SHORT).show();
        }

    }
}