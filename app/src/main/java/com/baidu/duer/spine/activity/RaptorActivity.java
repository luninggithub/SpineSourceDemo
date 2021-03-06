package com.baidu.duer.spine.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.baidu.duer.spine.R;
import com.baidu.duer.spine.model.Raptor;
import com.baidu.duer.spine.model.Raptor3794;


public class RaptorActivity extends AndroidApplication {

//    Raptor raptor;
    Raptor3794 raptor;
    View raptorView;

    Button btnWalk, btnRoar, btnJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raptor);
        btnWalk = (Button) findViewById(R.id.btn_walk);
        btnWalk.setOnClickListener(vOnClickListener);

        btnRoar = (Button) findViewById(R.id.btn_roar);
        btnRoar.setOnClickListener(vOnClickListener);

        btnJump = (Button) findViewById(R.id.btn_jump);
        btnJump.setOnClickListener(vOnClickListener);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.r = cfg.g = cfg.b = cfg.a = 8;
        raptor = new Raptor3794();
        raptorView = initializeForView(raptor, cfg);
        if (raptorView instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) raptorView;
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderOnTop(true);
        }
        addRaptor();
    }

    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnWalk) {
//                raptor.setAnimate("walk");
            } else if (view == btnRoar) {
//                raptor.setAnimate("roar");
            } else if (view == btnJump) {
//                raptor.setAnimate("jump");
            }
        }
    };

    public void addRaptor() {
        final WindowManager windowManager = getWindowManager();
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        raptorView.setOnTouchListener(new View.OnTouchListener() {

            float lastX, lastY;

            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getRawX();
                float y = event.getRawY();
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    layoutParams.x += (int) (x - lastX);
                    layoutParams.y += (int) (y - lastY);
                    windowManager.updateViewLayout(raptorView, layoutParams);
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//                    raptor.setAnimate("flying");
                }
                return true;
            }
        });
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.flags = 40;
        layoutParams.width = dp2Px(900);
        layoutParams.height = dp2Px(500);
        layoutParams.format = -3;
        windowManager.addView(raptorView, layoutParams);
    }

    public int dp2Px(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        getWindowManager().removeViewImmediate(raptorView);
        super.onDestroy();
    }
}
