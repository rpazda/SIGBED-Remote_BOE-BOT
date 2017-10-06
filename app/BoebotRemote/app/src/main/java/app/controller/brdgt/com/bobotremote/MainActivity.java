package app.controller.brdgt.com.bobotremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start = (Button) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Begin Instructions");
            }
        });

        final Button end = (Button) findViewById(R.id.end_button);
        end.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Stop Instructions");
            }
        });

        final Button up = (Button) findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Forward");
            }
        });

        final Button left = (Button) findViewById(R.id.left_arrow);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Left");
            }
        });

        final Button right = (Button) findViewById(R.id.right_arrow);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Right");
            }
        });

        final Button down = (Button) findViewById(R.id.down_arrow);
        down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Back");
            }
        });

        final Button delay = (Button) findViewById(R.id.delay);
        delay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Delay");
            }
        });

        final Button leftTurn = (Button) findViewById(R.id.turn_left);
        leftTurn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Turn Left");
            }
        });

        final Button rightTurn = (Button) findViewById(R.id.turn_right);
        rightTurn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Turn Right");
            }
        });

        final Button connect = (Button) findViewById(R.id.connector);
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Connected");
            }
        });
    }
}
