package br.feevale.bluetoothclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

public class BluetoothClientMainActivity extends AppCompatActivity {
    public static final String BT = "BTAPP";
    BluetoothDevice device = null;
    EditText editInput;
    PrintWriter out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        editInput = (EditText) findViewById(R.id.editInput);
        bluetooth();
    }

    public void bluetooth(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            Log.d(BT, "isNull!");
            return;
        }
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        device = null;
        for(BluetoothDevice d:devices){
            Log.d(BT,d.getName());
            if(d.getName().equals("GT-I9100")){
                device = d;
            }
        }

        Log.d(BT, device.getName());
        new Thread(){
            @Override
            public void run() {
                try {
                    UUID id = UUID.fromString("2a43cfa0-368b-11e6-bdf4-0800200c9a66");
                    BluetoothSocket s = device.createRfcommSocketToServiceRecord(id);
                    Log.d(BT,"START");
                    s.connect();
                    out = new PrintWriter(s.getOutputStream(),true);
                    out.println("Connected!");
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public void send(View v){
        out.println(editInput.getText().toString());
        editInput.setText("");
        if(editInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        out.flush();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_client_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
