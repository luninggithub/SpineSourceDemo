package com.baidu.duer.spine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.duer.spine.activity.RaptorActivity;


public class MainActivity extends Activity {

    private Button  btnDragon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDragon = (Button) findViewById(R.id.btn_dragon);
        btnDragon.setOnClickListener(vOnClickListener);
    }

    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnDragon) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RaptorActivity.class);
                startActivity(intent);
            }
        }
    };
}
