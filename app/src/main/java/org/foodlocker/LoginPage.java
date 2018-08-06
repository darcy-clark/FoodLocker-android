package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.foodlocker.structs.User;
import org.foodlocker.utils.FirebaseUtil;

public class LoginPage extends Activity {

    private TextView loginErrMsgTv;
    private EditText userIn;
    private EditText passIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        loginErrMsgTv = findViewById(R.id.login_err_msg);
        userIn = findViewById(R.id.username_in);
        passIn = findViewById(R.id.password_in);

        TextView noActMsgView = findViewById(R.id.no_account_msg);
        noActMsgView.setHighlightColor(Color.TRANSPARENT);
        noActMsgView.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString noActMsg = new SpannableString(noActMsgView.getText());
        noActMsg.setSpan(new NoAccountClickableSpan(this), 31, 38, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        noActMsgView.setText(noActMsg);

        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new LoginButtonListener());
    }

    public void onLogin() {
        startActivity(new Intent(this, WelcomePage.class));
    }

    public void onBadLogin() {
        loginErrMsgTv.setText(getText(R.string.login_no_match));
    }

    class NoAccountClickableSpan extends ClickableSpan {

        Activity loginPage;

        NoAccountClickableSpan(Activity loginPage) {
            this.loginPage = loginPage;
        }

        @Override
        public void onClick(View view) {
            startActivity(new Intent(loginPage, CreateAccount.class));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#ffffbb33"));
            ds.setUnderlineText(false);
        }
    }

    class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String username = userIn.getText().toString();
            String password = passIn.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                loginErrMsgTv.setText(getText(R.string.login_no_entry));
                return;
            }

            User user = new User(username, password);
            FirebaseUtil firebaseUtil = new FirebaseUtil();
            firebaseUtil.login(user, LoginPage.this);
        }
    }
}
