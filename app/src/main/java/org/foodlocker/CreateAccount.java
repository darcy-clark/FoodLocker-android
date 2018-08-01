package org.foodlocker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.foodlocker.structs.User;
import org.foodlocker.utils.FirebaseUtil;

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

        Button createActBtn = findViewById(R.id.create_act_btn);
        createActBtn.setOnClickListener(new CreateActButtonListener(userBtn, volunteerBtn));
    }

    public void onAccountCreation() {
        Log.d("Create Account", "We've reached the callback!");
    }

    public void onDuplicateUsername() {
        Log.d("Create Account", "Duplicate account detected!");
    }

    class UserTypeButtonListener implements View.OnClickListener {

        private Button otherButton;

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

    class CreateActButtonListener implements View.OnClickListener {

        private Button userBtn;
        private Button volunteerBtn;
        private EditText usernameIn = findViewById(R.id.new_username_in);
        private EditText passwordIn = findViewById(R.id.new_password_in);
        private EditText confirmPassIn = findViewById(R.id.confirm_pass_in);

        CreateActButtonListener(Button userBtn, Button volunteerBtn) {
            this.userBtn = userBtn;
            this.volunteerBtn = volunteerBtn;
        }

        @Override
        public void onClick(View view) {
            String username = usernameIn.getText().toString();
            String password = passwordIn.getText().toString();
            if(!checkUserInfo(username, password)) {
                return;
            }

            sendNewUser(new User(username, password));
            Log.d("AccountCreate", "Gucci Gang");
        }

        private boolean checkUserInfo(String username, String password) {
            if (!userBtn.isSelected() && !volunteerBtn.isSelected()) {
                Log.d("AccountCreate", "Specify role");
                return false;
            }

            if (username.isEmpty()) {
                Log.d("AccountCreate", "Specify username");
                return false;
            }

            if (password.isEmpty()) {
                Log.d("AccountCreate", "Specify password");
                return false;
            }

            if (!password.equals(confirmPassIn.getText().toString())) {
                Log.d("AccountCreate", "Confirmed password does not match");
                return false;
            }
            return true;
        }

        private void sendNewUser(User newUser) {
            FirebaseUtil.createUser(newUser, CreateAccount.this);
        }
    }
}
