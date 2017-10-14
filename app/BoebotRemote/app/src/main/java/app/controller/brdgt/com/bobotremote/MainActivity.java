package app.controller.brdgt.com.bobotremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView transmit = (TextView) findViewById(R.id.input_output);

        Button start = (Button) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // tells user that the machine can accept commands now
                transmit.setText("Intaking");
            }
        });

        Button end = (Button) findViewById(R.id.end_button);
        end.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                transmit.setText("Outputting");
            }
        });

        final TextView command_print = (TextView) findViewById(R.id.command_printer);

        Button up = (Button) findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Forward");
            }
        });

        Button left = (Button) findViewById(R.id.left_arrow);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Left");
            }
        });

        Button right = (Button) findViewById(R.id.right_arrow);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Right");
            }
        });

        Button down = (Button) findViewById(R.id.down_arrow);
        down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Back");
            }
        });

        Button delay = (Button) findViewById(R.id.delay);
        delay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Delay");
            }
        });

        Button leftTurn = (Button) findViewById(R.id.turn_left);
        leftTurn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Turn Left");
            }
        });

        Button rightTurn = (Button) findViewById(R.id.turn_right);
        rightTurn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                command_print.setText("Turn Right");
            }
        });

        final TextView connect = (TextView) findViewById(R.id.power);

        Button connection = (Button) findViewById(R.id.connector);
        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // establishes connection with bluetooth
                // makes sure the boebot is on
                connect.setText("On");
            }
        });
    }
}
