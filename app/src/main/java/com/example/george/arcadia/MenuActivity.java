package com.example.george.arcadia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.george.arcadia.pong.ImageAdapter;
import com.example.george.arcadia.pong.PongActivity;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private ArrayList<String> gamesClasses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initIntents();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                switch (gamesClasses.get(position)){
                    case("pong"):
                        intent = new Intent(MenuActivity.this, PongActivity.class);
                        break;
                    case("snake"):
                        intent = new Intent(MenuActivity.this, PongActivity.class);
                        break;
                    case("tetris"):
                        intent = new Intent(MenuActivity.this, PongActivity.class);
                        break;
                }

                startActivity(intent);
                Toast.makeText(MenuActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        /*Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PongActivity.class);
                startActivity(intent);

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initIntents(){
        gamesClasses.add("pong");
        gamesClasses.add("snake");
        gamesClasses.add("tetris");
    }

}
