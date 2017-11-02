package app.controller.brdgt.com.bobotremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;
import static app.controller.brdgt.com.bobotremote.R.string.connect;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter myBluetooth = null;
    private TextView mBluetoothStatus;
    private BluetoothSocket mBTSocket = null;
    private Handler mHandler;
    private Thread mConnectedThread;
    private Intent data;
    private BluetoothAdapter mBTAdapter;

    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mHandler = new Handler() {
            @Override
            public void publish(LogRecord record) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }

            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //  mReadBuffer.setText(readMessage);
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1)
                           mBluetoothStatus.setText("Connected to Device: " + (String)(msg.obj));
                    else
                           mBluetoothStatus.setText("Connection Failed");
                }
            }

        };

        myBluetooth = getDefaultAdapter();
        mBTAdapter = getDefaultAdapter();


        if (myBluetooth == null) {
            //device is not available
            finish();
        } else {
            if (!myBluetooth.isEnabled()) {
            } else {
                Intent connection = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(connection, 1);
            }

        }
        final TextView transmit = (TextView) findViewById(R.id.input_output);

        Button start = (Button) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // tells user that the machine can accept commands now
                transmit.setText("Intaking");
            }
        });

        Button end = (Button) findViewById(R.id.end_button);
        end.setOnClickListener(view -> transmit.setText("Outputting"));

        final TextView command_print = (TextView) findViewById(R.id.command_printer);

        Button up = (Button) findViewById(R.id.up);
        up.setOnClickListener(view -> command_print.setText("Forward"));

        Button left = (Button) findViewById(R.id.left_arrow);
        left.setOnClickListener(view -> command_print.setText("Left"));

        Button right = (Button) findViewById(R.id.right_arrow);
        right.setOnClickListener(view -> command_print.setText("Right"));

        Button down = (Button) findViewById(R.id.down_arrow);
        down.setOnClickListener(view -> command_print.setText("Back"));

        Button delay = (Button) findViewById(R.id.delay);
        delay.setOnClickListener(view -> command_print.setText("Delay"));

        Button leftTurn = (Button) findViewById(R.id.turn_left);
        leftTurn.setOnClickListener(view -> command_print.setText("Turn Left"));

        Button rightTurn = (Button) findViewById(R.id.turn_right);
        rightTurn.setOnClickListener(view -> command_print.setText("Turn Right"));

        final TextView connect = (TextView) findViewById(R.id.power);

        Button connection = (Button) findViewById(R.id.connector);
        connection.setOnClickListener(view -> command_print.setText("On"));

// Enter here after user selects "yes" or "no" to enabling radio
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // connect.setText("On");
            } else {
                //connect.setText("Off");
            }
        }
    }

    private void discover(View view) {
        //Check if device is already discovering
        if (mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
        } else if (mBTAdapter.isEnabled()) {
            mBTAdapter.startDiscovery();
            Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
            // registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }
            //connect.setText("Connecting...");
            //Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            //spawn new thread to avoid blocking the GUI one
            new Thread() {
                public void run() {
                    boolean fail = false;
                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendTarget();
                        } catch(IOException e2) {
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    private class ConnectedThread extends Thread {
        private BluetoothSocket mmSocket = null;
        private InputStream mmInStream = null;
        private OutputStream mmOutStream = null;

        public ConnectedThread(BluetoothSocket socket) {
            {
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                //get input and output streams, using team objects because member streams are final
                try {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) {
                }
                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(String input) {
            byte[] bytes = input.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}




