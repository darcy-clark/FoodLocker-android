package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_page);

        TextView faqMsgView = findViewById(R.id.welcome_faq);
        faqMsgView.setHighlightColor(Color.TRANSPARENT);
        faqMsgView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString noActMsg = new SpannableString(faqMsgView.getText());
        noActMsg.setSpan(new FaqClickableSpan(), 20, 37, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        faqMsgView.setText(noActMsg);

        Button orderNowBtn = findViewById(R.id.order_now_btn);
        orderNowBtn.setOnClickListener(new OrderNowButtonClick());
    }

    class FaqClickableSpan extends ClickableSpan {

        @Override
        public void onClick(View view) {
            startActivity(new Intent(WelcomePage.this, FaqPage.class));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#ffffbb33"));
            ds.setUnderlineText(false);
        }
    }

    class OrderNowButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            startActivity(new Intent(WelcomePage.this, OrderFirstPage.class));
        }
    }
}
