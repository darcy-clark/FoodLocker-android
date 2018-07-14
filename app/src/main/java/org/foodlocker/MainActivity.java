package org.foodlocker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ImageView logoImg = findViewById(R.id.logo);
        TextView welcomeMsg = findViewById(R.id.welcome_msg);
        Animation logoFadeOut = createLogoAnimation(logoImg, welcomeMsg);
        logoImg.setAnimation(logoFadeOut);
    }

    private Animation createLogoAnimation(final ImageView logoImg, final TextView welcomeMsg) {
        Animation.AnimationListener fadeOutListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoImg.setVisibility(View.INVISIBLE);
                welcomeMsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        Animation logoFadeOut = new AlphaAnimation(1, 0);
        logoFadeOut.setDuration(500);
        logoFadeOut.setInterpolator(new AccelerateInterpolator());
        logoFadeOut.setStartOffset(2000);
        logoFadeOut.setAnimationListener(fadeOutListener);

        return logoFadeOut;
    }
}
