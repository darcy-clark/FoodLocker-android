package org.foodlocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.foodlocker.structs.Box;
import org.foodlocker.utils.FirebaseUtil;

import java.util.List;

public class OrderFirstPage extends Activity {

    private ListView boxList;
    private List<Box> boxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_first_page);

        boxList = findViewById(R.id.order_box_list);
        FirebaseUtil firebaseUtil = new FirebaseUtil();
        firebaseUtil.retrieveBoxes(this);
        boxList.setClickable(true);
        boxList.setOnItemClickListener(new BoxItemClickListener());
    }

    public void populateBoxList(List<Box> boxes) {
        this.boxes = boxes;
        BoxListAdapter boxListAdapter = new BoxListAdapter(this, boxes);
        boxList.setAdapter(boxListAdapter);
    }

    class BoxItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("box", boxes.get(position));
            Intent intent = new Intent(OrderFirstPage.this, OrderSecondPage.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
