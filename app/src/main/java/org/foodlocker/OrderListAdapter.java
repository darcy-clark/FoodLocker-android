package org.foodlocker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import org.foodlocker.structs.Order;
import org.foodlocker.structs.OrderStatus;
import org.foodlocker.utils.DeviceDataUtil;
import org.foodlocker.utils.FirebaseUtil;

import java.util.Date;
import java.util.List;

public class OrderListAdapter extends ArraySwipeAdapter {

    private Context context;
    private List<Order> orders;

    public OrderListAdapter(Context context, List<Order> orders) {
        super(context, R.layout.order_list_item, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.order_list_item, parent, false);

        SwipeLayout swipeLayout = row.findViewById(R.id.order_list_swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        TextView boxNameTv = row.findViewById(R.id.box_name);
        TextView userTv = row.findViewById(R.id.user);
        TextView orderTimeTv = row.findViewById(R.id.order_time);

        Order order = orders.get(position);

        boxNameTv.setText(order.getBox());
        userTv.setText(order.getUser());
        long currentTime = new Date().getTime();
        int timeInMinutes = (int) ((currentTime - order.getTimestamp()) / 60000);
        orderTimeTv.setText(context.getString(R.string.order_list_time, timeInMinutes));

        RelativeLayout aboveLayout = row.findViewById(R.id.info_swipe_above);
        if (order.getStatus() == OrderStatus.ACCEPTED) {
            aboveLayout.setBackgroundColor(row.getResources().getColor(R.color.accepted_green));
            swipeLayout.setSwipeEnabled(false);
        } else {
            RelativeLayout belowLayout = row.findViewById(R.id.accept_swipe_below);
            belowLayout.setOnClickListener(new OnAcceptButtonListener(order));
        }

        return row;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.order_list_swipe;
    }

    class OnAcceptButtonListener implements View.OnClickListener {

        private Order order;

        OnAcceptButtonListener(Order order) {
            this.order = order;
        }

        @Override
        public void onClick(View view) {
            String currentVolunteer = DeviceDataUtil.retrieveCurrentVolunteer(view.getContext());
            order.setVolunteer(currentVolunteer);
            order.setStatus(OrderStatus.ACCEPTED);
            FirebaseUtil firebaseUtil = new FirebaseUtil();
            firebaseUtil.acceptOrder(order, (OrdersListPage) view.getContext());
        }
    }
}
