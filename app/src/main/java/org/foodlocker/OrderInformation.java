package org.foodlocker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.foodlocker.structs.Order;

public class OrderInformation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_information);

        Order order = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order = (Order) extras.getSerializable("order");
        }
        if (order == null) {
            throw new RuntimeException();
        }

        TextView boxNameTv = findViewById(R.id.order_info_box);
        boxNameTv.setText(getString(R.string.order_info_box, order.getBox()));
        TextView lockerNumTv = findViewById(R.id.order_info_locker_number);
        lockerNumTv.setText(getString(R.string.order_info_locker_num, order.getLockerNumber()));
        TextView lockComboTv = findViewById(R.id.order_info_combo);
        lockComboTv.setText(getString(R.string.order_info_combo, order.getLockerCombo()));
        String dietRestrictions = "";
        int counter = 0;
        for(String rest : order.getDietRestrictions()) {
            if (counter != order.getDietRestrictions().size() - 1) {
                dietRestrictions += rest + ", ";
            } else {
                dietRestrictions += rest;
            }
            counter++;
        }
        TextView dietTv = findViewById(R.id.order_info_diet);
        dietTv.setText(getString(R.string.order_info_diet, dietRestrictions));
    }
}
