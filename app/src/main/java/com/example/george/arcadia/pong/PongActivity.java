package com.example.george.arcadia.pong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.example.george.arcadia.MainActivity;
import com.example.george.arcadia.R;

public class PongActivity extends AppCompatActivity {
    private PongView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        view = (PongView)findViewById(R.id.pong_view);
        view.setParent(this);
    }

    public void displayWinDialogFragment(String winner) {
        /*Bundle args = new Bundle();
        args.putString("winner", winner);
        WinDialogFragment dialog = new WinDialogFragment();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"newGame");*/
        runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(PongActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();*/
    }
}