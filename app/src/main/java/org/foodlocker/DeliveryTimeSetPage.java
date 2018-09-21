package org.foodlocker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.foodlocker.structs.Order;
import org.foodlocker.utils.FirebaseUtil;

import java.util.Locale;

public class DeliveryTimeSetPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery_time_set_page);

        Order order = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order = (Order) extras.getSerializable("order");
        }
        if (order == null) {
            throw new RuntimeException();
        }

        Button plusHour = findViewById(R.id.plus_button_1);
        Button plusMinute = findViewById(R.id.plus_button_2);
        Button minusHour = findViewById(R.id.minus_button_1);
        final Button minusMinute = findViewById(R.id.minus_button_2);
        Button confirmTime = findViewById(R.id.set_delivery_time_btn);
        final TextView hourTv = findViewById(R.id.hour);
        final TextView minuteTv = findViewById(R.id.minute);

        plusHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer hour = Integer.parseInt(hourTv.getText().toString());
                if (hour == 3) {
                    hourTv.setText(String.format(Locale.getDefault(), "%d", 7));
                } else if(hour == 12) {
                    hourTv.setText(String.format(Locale.getDefault(), "%d", 1));
                } else {
                    hourTv.setText(String.format(Locale.getDefault(), "%d", ++hour));
                }
            }
        });

        plusMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer minute = Integer.parseInt(minuteTv.getText().toString());
                if (minute < 45) {
                    minuteTv.setText(String.format(Locale.getDefault(), "%d", minute + 15));
                } else {
                    minuteTv.setText("00");
                }
            }
        });

        minusHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer hour = Integer.parseInt(hourTv.getText().toString());
                if (hour == 7) {
                    hourTv.setText(String.format(Locale.getDefault(), "%d", 3));
                } else if(hour == 1) {
                    hourTv.setText(String.format(Locale.getDefault(), "%d", 12));
                } else {
                    hourTv.setText(String.format(Locale.getDefault(), "%d", --hour));
                }
            }
        });

        minusMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer minute = Integer.parseInt(minuteTv.getText().toString());
                if (minute > 15) {
                    minuteTv.setText(String.format(Locale.getDefault(), "%d", minute - 15));
                } else if (minute == 15) {
                    minuteTv.setText("00");
                } else {
                    minuteTv.setText(String.format(Locale.getDefault(), "%d", 45));
                }
            }
        });

        confirmTime.setOnClickListener(new OnConfirmTimeClick(order, hourTv, minuteTv));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    class OnConfirmTimeClick implements View.OnClickListener {

        private Order order;
        private TextView hourTv;
        private TextView minuteTv;

        OnConfirmTimeClick(Order order, TextView hourTv, TextView minuteTv) {
            this.order = order;
            this.hourTv = hourTv;
            this.minuteTv = minuteTv;
        }

        @Override
        public void onClick(View view) {
            String deliveryTime = hourTv.getText() + ":" + minuteTv.getText();
            order.setDeliveryTime(deliveryTime);
            FirebaseUtil firebaseUtil = new FirebaseUtil();
            firebaseUtil.setDeliveryTime(order);
            DeliveryTimeSetPage.this.finish();
        }
    }
}
