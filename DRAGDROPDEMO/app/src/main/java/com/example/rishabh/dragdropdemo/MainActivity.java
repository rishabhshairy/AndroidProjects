package com.example.rishabh.dragdropdemo;

import android.content.ClipData;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button drag;
    TextView totals,success;
    LinearLayout drop;
    int total,failure=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drag=(Button)findViewById(R.id.drag);
        totals=(TextView)findViewById(R.id.total);
        success=(TextView)findViewById(R.id.success);
        drop=(LinearLayout)findViewById(R.id.bottomLinear);

        drop.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                final int action=dragEvent.getAction();
                switch (action)
                {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        failure++;
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DROP:
                        total++;

                        int suc=total-failure;
                        success.setText("succesfull drops::"+suc);
                        totals.setText("total drops::"+total);
                        return true;
                }
                return true;
            }
        });

        drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data=ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(drag);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data,shadowBuilder,null,0);
                }
                return false;
            }
        });
    }
}
