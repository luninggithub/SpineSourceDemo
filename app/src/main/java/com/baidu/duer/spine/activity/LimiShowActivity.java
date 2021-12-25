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
import com.baidu.duer.spine.model.LimiShow2;
import com.baidu.duer.spine.util.LogUtil;


public class LimiShowActivity extends AndroidApplication {
    private static final String TAG = "LimiShowActivity";

    LimiShow2 limiShow;
    View limiShowView;

    Button btnShow, btnWoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limi_show);

        btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(vOnClickListener);

        btnWoman = (Button) findViewById(R.id.btn_woman);
        btnWoman.setOnClickListener(vOnClickListener);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.r = cfg.g = cfg.b = cfg.a = 8;
        limiShow = new LimiShow2();
        limiShowView = initializeForView(limiShow, cfg);
        if (limiShowView instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) limiShowView;
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderOnTop(true);
        }
        addLimiShow();
    }

    public void addLimiShow() {
        final WindowManager windowManager = getWindowManager();
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        limiShowView.setOnTouchListener(new View.OnTouchListener() {

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
                    windowManager.updateViewLayout(limiShowView, layoutParams);
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {

                }
                return true;
            }
        });
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.flags = 40;
        layoutParams.width = dp2Px(300);
        layoutParams.height = dp2Px(500);
        layoutParams.format = -3;
        windowManager.addView(limiShowView, layoutParams);
    }

    public int dp2Px(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_show) {
                LogUtil.i(TAG, "Show button is clicked!");
                limiShow.changeAction();
            }
        }
    };

    @Override
    protected void onDestroy() {
        getWindowManager().removeViewImmediate(limiShowView);
        super.onDestroy();
    }
}
