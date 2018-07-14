package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LoginPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        TextView noActMsgView = findViewById(R.id.no_account_msg);
        noActMsgView.setHighlightColor(Color.TRANSPARENT);
        noActMsgView.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString noActMsg = new SpannableString(noActMsgView.getText());
        noActMsg.setSpan(new NoAccountClickableSpan(this), 31, 38, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        noActMsgView.setText(noActMsg);
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
}
