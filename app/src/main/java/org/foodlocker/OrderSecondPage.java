package org.foodlocker;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.foodlocker.structs.Box;

public class OrderSecondPage extends Activity {

    private Box orderBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_second_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderBox = (Box) extras.getSerializable("box");
        }
        if (orderBox == null) {
            throw new RuntimeException();
        }

        TextView boxTitleTv = findViewById(R.id.second_box_title);
        boxTitleTv.setText(orderBox.getName());
        TextView boxDescTv = findViewById(R.id.second_box_desc);
        boxDescTv.setText(orderBox.getDescription());
        TextView boxItemsTv = findViewById(R.id.second_box_items);
        StringBuilder boxItems = new StringBuilder("Contains:");
        for (String item : orderBox.getItems()) {
            boxItems.append("\n" + item);
        }
        boxItemsTv.setText(boxItems.toString());
        TextView boxPickupTv = findViewById(R.id.second_box_pickup);
        String pickupString = "Pickup " + orderBox.getPickup();
        boxPickupTv.setText(pickupString);

        Button orderNow = findViewById(R.id.second_order_now_btn);
        if (orderBox.getInventory() == 0) {
            orderNow.setEnabled(false);
            TextView noStockMsg = findViewById(R.id.no_stock_msg);
            noStockMsg.setText(R.string.no_stock_msg);
        } else {
            orderNow.setOnClickListener(new OrderNowClickListener());
        }
    }

    class OrderNowClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d("Confirm click", "We got a confirm click");
        }
    }
}
