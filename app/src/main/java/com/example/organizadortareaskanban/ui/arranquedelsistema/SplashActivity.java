package com.example.organizadortareaskanban.ui.arranquedelsistema;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.organizadortareaskanban.BuildConfig;
import com.example.organizadortareaskanban.MainActivity;
import com.example.organizadortareaskanban.R;

public class SplashActivity extends AppCompatActivity {
    Integer inicio;
    ImageView logo;
    private ObjectAnimator animatorAlpha;
    private final long ANIMATION_DURATION = 5000;
    private AnimatorSet animatorSet;
    private final int DURACION_SPLASH = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        logo=(ImageView)findViewById(R.id.logo);
        //animacion del logo
        animatorAlpha=ObjectAnimator.ofFloat(logo, View.ALPHA, 0.0f,1f);
        animatorAlpha.setDuration(ANIMATION_DURATION);
        AnimatorSet animatorSetAlpha = new AnimatorSet();
        animatorSetAlpha.play(animatorAlpha);
        animatorSetAlpha.start();
        /*animatorSetAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animation.start();
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch(getFirstTimeRun(SplashActivity.this)) {
                    case 0:
                        // Se inicia por primera ves
                        Intent intento= new Intent(SplashActivity.this, RegistrarActivity.class);
                        startActivity(intento);
                        break;
                    case 1:
                        //Ya se inicio una ves
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        //Primer inicio luego de una actualizacion
                        break;
                }
            };
            },
        DURACION_SPLASH);
    }

    //Retorna: 0 primera vez / 1 no es primera vez / 2 nueva versión
    public static int getFirstTimeRun(Context contexto) {
        SharedPreferences sp = contexto.getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }
}
