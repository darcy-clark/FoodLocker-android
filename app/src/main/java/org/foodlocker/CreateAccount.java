package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.foodlocker.structs.User;
import org.foodlocker.utils.DeviceDataUtil;
import org.foodlocker.utils.FirebaseUtil;

public class CreateAccount extends Activity {

    private TextView errorMsgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);

        errorMsgTv = findViewById(R.id.create_act_err_msg);

        Button userBtn = findViewById(R.id.user_btn);
        Button volunteerBtn = findViewById(R.id.volunteer_btn);
        userBtn.setOnClickListener(new UserTypeButtonListener(volunteerBtn));
        volunteerBtn.setOnClickListener(new UserTypeButtonListener(userBtn));

        Button createActBtn = findViewById(R.id.create_act_btn);
        createActBtn.setOnClickListener(new CreateActButtonListener(userBtn, volunteerBtn));
    }

    public void onAccountCreation(String username, String actType) {
        DeviceDataUtil.registerAccount(getApplicationContext(), username, actType);
        startActivity(new Intent(this, WelcomePage.class));
    }

    public void onDuplicateUsername() {
        errorMsgTv.setText(getString(R.string.create_act_duplicate_err));
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
        private String actType;

        CreateActButtonListener(Button userBtn, Button volunteerBtn) {
            this.userBtn = userBtn;
            this.volunteerBtn = volunteerBtn;
        }

        @Override
        public void onClick(View view) {
            String username = usernameIn.getText().toString();
            String password = passwordIn.getText().toString();
            if (userBtn.isSelected()) {
                actType = "user";
            } else if (volunteerBtn.isSelected()) {
                actType = "volunteer";
            }
            if(!checkUserInfo(username, password)) {
                return;
            }

            sendNewUser(new User(username, password, actType));
            Log.d("AccountCreate", "Gucci Gang");
        }

        private boolean checkUserInfo(String username, String password) {
            if (actType == null) {
                errorMsgTv.setText(getText(R.string.create_act_no_role));
                return false;
            }

            if (username.isEmpty()) {
                errorMsgTv.setText(getText(R.string.create_act_no_user));
                return false;
            }

            if (password.isEmpty()) {
                errorMsgTv.setText(getText(R.string.create_act_no_pass));
                return false;
            }

            if (!password.equals(confirmPassIn.getText().toString())) {
                errorMsgTv.setText(getText(R.string.create_act_no_match));
                return false;
            }
            return true;
        }

        private void sendNewUser(User newUser) {
            FirebaseUtil firebaseUtil = new FirebaseUtil();
            firebaseUtil.createUser(newUser, CreateAccount.this, actType);
        }
    }
}
