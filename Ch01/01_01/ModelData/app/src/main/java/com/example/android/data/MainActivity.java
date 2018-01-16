package com.example.android.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.data.model.DataItem;

public class MainActivity extends AppCompatActivity {

        TextView tvOut;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            DataItem item =
                    new DataItem(null, "My menu item", "a category",
                            "a description", 1, 9.95, "apple_pie.jpg");

            tvOut = (TextView) findViewById(R.id.out);
            tvOut.setText(item.toString());

        }
}
