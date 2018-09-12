package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.foodlocker.structs.Box;
import org.foodlocker.structs.Order;
import org.foodlocker.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderSecondPage extends Activity {

    private Box orderBox;
    private CheckBox vegeCheck;
    private CheckBox veganCheck;
    private CheckBox dairyCheck;
    private CheckBox glutenCheck;

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

        vegeCheck = findViewById(R.id.vege_check);
        veganCheck = findViewById(R.id.vegan_check);
        dairyCheck = findViewById(R.id.dairy_check);
        glutenCheck = findViewById(R.id.gluten_check);

        Button orderNow = findViewById(R.id.second_order_now_btn);
        if (orderBox.getInventory() == 0) {
            orderNow.setEnabled(false);
            TextView noStockMsg = findViewById(R.id.no_stock_msg);
            noStockMsg.setText(R.string.no_stock_msg);
        } else {
            orderNow.setOnClickListener(new OrderNowClickListener(orderBox.getName()));
        }
    }

    public void onOrderComplete(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        Intent intent = new Intent(this, OrderInformation.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class OrderNowClickListener implements View.OnClickListener {

        private String box;

        OrderNowClickListener(String box) {
            this.box = box;
        }

        @Override
        public void onClick(View view) {

            // This can be done better. Refactor later if time permits
            List<String> dietRestrictions = new ArrayList<>();
            if (vegeCheck.isChecked()) {
                dietRestrictions.add("Vegetarian");
            }
            if (veganCheck.isChecked()) {
                dietRestrictions.add("Vegan");
            }
            if (dairyCheck.isChecked()) {
                dietRestrictions.add("Dairy Free");
            }
            if (glutenCheck.isChecked()) {
                dietRestrictions.add("Gluten Free");
            }
            FirebaseUtil firebaseUtil = new FirebaseUtil();
            firebaseUtil.createOrder(box, dietRestrictions, OrderSecondPage.this);
        }
    }
}
