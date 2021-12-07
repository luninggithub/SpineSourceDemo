package com.baidu.duer.spine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.duer.spine.activity.LimiShowActivity;
import com.baidu.duer.spine.activity.RaptorActivity;

/**
 * http://zh.esotericsoftware.com/spine-json-format#JSON-export-format
 */
public class MainActivity extends Activity {

    private Button  btnDragon, btnLimiShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDragon = (Button) findViewById(R.id.btn_dragon);
        btnDragon.setOnClickListener(vOnClickListener);
        btnLimiShow = (Button) findViewById(R.id.btn_limi);
        btnLimiShow.setOnClickListener(vOnClickListener);
    }

    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnDragon) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RaptorActivity.class);
                startActivity(intent);
            } else if (view == btnLimiShow) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LimiShowActivity.class);
                startActivity(intent);
            }
        }
    };
}
