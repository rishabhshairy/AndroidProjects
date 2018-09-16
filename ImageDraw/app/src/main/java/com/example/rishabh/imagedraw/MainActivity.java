package com.example.rishabh.imagedraw;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CanvasView CanvasView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CanvasView = (CanvasView) findViewById(R.id.canvas);
        button = (Button) findViewById(R.id.clear);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CanvasView.clearCanvas();
            }
        });
    }

    public void end(View view) {
        final Toast toast=Toast.makeText(getApplicationContext(),"End of drawing Area",Toast.LENGTH_SHORT);
        toast.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        },500);

    }
}
