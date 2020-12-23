package com.example.tiptop.LogInAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiptop.R;

public class LogoActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;

    private TextView logo_text;
    private ImageView logo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);

        logo_text = (TextView) findViewById(R.id.logo_text);
        logo_image = (ImageView) findViewById(R.id.logo_image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent go_to_login = new Intent(LogoActivity.this,LoginActivity.class);
                startActivity(go_to_login);
                finish();
            }
        },SPLASH_SCREEN);
    }
}