package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        List<View> makeVisViews = new ArrayList<>();
        ImageView logoImg = findViewById(R.id.logo);
        TextView welcomeMsg = findViewById(R.id.welcome_msg);
        Spinner hsChoices = findViewById(R.id.hs_choices);
        Button toSigninBtn = findViewById(R.id.to_signin_btn);
        toSigninBtn.setOnClickListener(new NextButtonListener(this));

        makeVisViews.add(welcomeMsg);
        makeVisViews.add(hsChoices);
        makeVisViews.add(toSigninBtn);

        Animation logoFadeOut = createLogoAnimation(logoImg, makeVisViews);
        logoImg.setAnimation(logoFadeOut);
    }

    private Animation createLogoAnimation(ImageView logoImg, List<View> views) {
        Animation.AnimationListener fadeOutListener = new LogoAnimationListener(logoImg, views);

        // TODO: use actual fade out time values
        Animation logoFadeOut = new AlphaAnimation(1, 0);
        logoFadeOut.setDuration(1);
        logoFadeOut.setInterpolator(new AccelerateInterpolator());
        logoFadeOut.setStartOffset(0);
        logoFadeOut.setAnimationListener(fadeOutListener);

        return logoFadeOut;
    }

    class LogoAnimationListener implements Animation.AnimationListener {

        private ImageView logoImg;
        private List<View> views;

        LogoAnimationListener(ImageView logoImg, List<View> views) {
            this.logoImg = logoImg;
            this.views = views;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            logoImg.setVisibility(View.INVISIBLE);
            for(View view : views) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class NextButtonListener implements View.OnClickListener {

        private Activity mainActivity;

        NextButtonListener(Activity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onClick(View view) {
            startActivity(new Intent(mainActivity, LoginPage.class));
        }
    }
}
