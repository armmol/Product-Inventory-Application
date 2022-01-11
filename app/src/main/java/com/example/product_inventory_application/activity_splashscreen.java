package com.example.product_inventory_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activity_splashscreen extends AppCompatActivity {

    Animation top,bottom,bounce;
    ImageView img;
    TextView txt;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        getSupportActionBar ().hide ();
        getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView (R.layout.activity_splashscreen);

        top = AnimationUtils.loadAnimation (this,R.anim.fadeout);
        bottom = AnimationUtils.loadAnimation (this,R.anim.fadein);
        bounce = AnimationUtils.loadAnimation (this,R.anim.bounce);
        img = findViewById (R.id.img_spashscreenlogo);
        txt = findViewById (R.id.txt_spashscreen);

        img.setAnimation (top);
        img.setAnimation (bottom);

        txt.setAnimation (bounce);



        new Handler().postDelayed (new Runnable () {
            @Override
            public void run () {
                startActivity (new Intent (activity_splashscreen.this,activity_login.class));
                finish ();
            }
        },3500);

    }
}
