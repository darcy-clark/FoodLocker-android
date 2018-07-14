package org.foodlocker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CreateAccount extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);

        Button userBtn = findViewById(R.id.user_btn);
        Button volunteerBtn = findViewById(R.id.volunteer_btn);
        userBtn.setOnClickListener(new UserTypeButtonListener(volunteerBtn));
        volunteerBtn.setOnClickListener(new UserTypeButtonListener(userBtn));
    }

    class UserTypeButtonListener implements View.OnClickListener {

        Button otherButton;

        UserTypeButtonListener(Button otherButton) {
            this.otherButton = otherButton;
        }

        @Override
        public void onClick(View view) {
            view.setSelected(true);
            otherButton.setSelected(false);
            // TODO: Add functionality (saving the button text to a variable could work, no control flow)
        }
    }

//    class VolunteerButtonListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//            view.setSelected(true);
//            // TODO: Add volunteer button-specific functionality
//        }
//    }
}
