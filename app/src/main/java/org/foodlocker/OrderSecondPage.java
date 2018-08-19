package org.foodlocker;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.foodlocker.structs.Box;

public class OrderSecondPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_second_page);

        Log.d("w00t", "onCreate: ");
        Log.d("w00t", ( (Box) getIntent().getExtras().getSerializable("box")).getName());
    }
}
