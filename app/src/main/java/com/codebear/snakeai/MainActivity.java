package com.codebear.snakeai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codebear.snakeai.control.BaseControl;
import com.codebear.snakeai.control.GameControl3;
import com.codebear.snakeai.view.MapView;

public class MainActivity extends AppCompatActivity {

    MapView mapView;
    BaseControl gameControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mv_snake);
        gameControl = new GameControl3(mapView);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameControl.start();
            }
        });
    }
}
