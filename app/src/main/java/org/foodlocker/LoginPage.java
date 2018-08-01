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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("lockers/b128/combo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DB", "onDataChange: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
