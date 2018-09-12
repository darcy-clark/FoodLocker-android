package org.foodlocker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ScrollView;

import com.daimajia.swipe.SwipeLayout;

import org.foodlocker.structs.Order;
import org.foodlocker.utils.FirebaseUtil;

import java.util.List;

public class OrdersListPage extends Activity {

    private ListView orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_orders_list_page);

        orderList = findViewById(R.id.order_list);
        FirebaseUtil firebaseUtil = new FirebaseUtil();
        firebaseUtil.retrieveOrders(this);
    }

    public void populateOrderList(List<Order> orders) {
        OrderListAdapter orderListAdapter = new OrderListAdapter(this, orders);
        orderList.setAdapter(orderListAdapter);
    }
}
