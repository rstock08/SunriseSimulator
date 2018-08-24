package goldsquadron.sunrise;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private Button alButton;
    private Button btButton;
    private Button myWinButtion;
    private Button offButton;
    private Button onButton;

    TextView t1;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    String address = null, name = null;
    String command;
    public  ArrayList<Window> windows = new ArrayList<Window>();
    //BluetoothAdapter myBluetooth = null;
    //BluetoothSocket btSocket = null;
    //Set<BluetoothDevice> pairedDevices;
    //static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alButton = (Button) findViewById(R.id.alButton);
        alButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlarmPage();
            }
        });
        btButton = (Button) findViewById(R.id.roomButton);
        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBluetoothPage();
            }
        });
        t1 = (TextView) findViewById(R.id.btAddressText);
        try {
            findBT();
            openBT();
        } catch (Exception e) {
        }

        myWinButtion = (Button) findViewById(R.id.winButton);
        myWinButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("on");
            }
        });

        offButton = (Button) findViewById(R.id.offButton);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("off");
            }
        });

        onButton = (Button) findViewById(R.id.onButton);
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("on");
            }
        });


    }

    public void openAlarmPage(){
        Intent intent = new Intent(this, alarmActivity.class);
        startActivity(intent);
    }

    public void openBluetoothPage(){
        Intent intent = new Intent(this, btTestActivity.class);
        startActivity(intent);
    }


    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            t1.setText("No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        t1.setText("Bluetooth Device Found");
    }

    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        t1.setText("dsadasdadas");
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        t1.setText("Bluetooth Opened");
    }


    private void sendData(String i)
    {
        try
        {
            mmOutputStream.write(i.getBytes());
            Toast.makeText(getApplicationContext(), "data sent", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            command = data;
                                            t1.setText(command);
                                            /*if(!command.equals("complete")) {
                                                String[] split = command.split(",");
                                                addWindow(split);
                                            }*/
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void addWindow(String[] cmd){
        String name = cmd[0];
        int winAddress = Integer.parseInt(cmd[1]);
        int status = Integer.parseInt(cmd[2]);
        Window win = new Window(name,winAddress,status);
        windows.add(win);
    }
}
