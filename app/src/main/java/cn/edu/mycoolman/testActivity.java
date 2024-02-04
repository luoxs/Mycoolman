package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class testActivity extends AppCompatActivity implements BleNotifyResponse, BleWriteResponse {
    public DataRead dataRead;
    private Button btUnit;
    private MybluetoothClient mClient;
    private String MAC;
    //  private UUID service;
    //  private UUID character;
    private final UUID service = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    private final UUID character = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    private ProgressDialog progressDialog;
    private String passstr;
    private String a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        dataRead = new DataRead();
        initBluetooth();
        getPassworld();
        initdata();


        btUnit = findViewById(R.id.btunit);
        btUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] write = new byte[8];
                write[0] = (byte) 0xAA;
                write[1] = 0x08;
                write[2] = dataRead.getUnit();
                write[3] = (byte) Integer.parseInt(a, 16);
                write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
                byte[] bytin = {write[1], write[2], write[3], write[4]};
                int x = utilCRC.alex_crc16(bytin, 4);
                write[6] = (byte) (0xFF & x);
                write[5] = (byte) (0xFF & (x >> 8));
                write[7] = 0x55;
                mClient.write(MAC, service, character, write, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        Log.d("code ", String.valueOf(code));
                    }
                });
            }
        });
        //返回按键
        ImageButton btback = findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.unnotify(MAC, service, character, new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {
                        Log.v("unnoty", "successfully!");
                    }
                });
                Intent intent = new Intent();
                intent.setClass(testActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //返回到首页
        Button btreturn = findViewById(R.id.btconnect);
        btreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.disconnect(MAC);
                Intent intent = new Intent();
                intent.setClass(testActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBluetooth() {
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        //  dataRead = new DataRead();
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("device");
        }
        mClient.notify(MAC, service, character, this);
    }

    private void getPassworld() {
        SharedPreferences sharePref = getSharedPreferences("datafile", Context.MODE_PRIVATE);
        MAC = sharePref.getString("device", "");
        passstr = sharePref.getString(MAC, "");
        try {
            a = passstr.substring(0, 1);
            b = passstr.substring(1, 2);
            c = passstr.substring(2, 3);
        } catch (Exception e) {
            Log.v("password", "get failed");
        }
    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        updateStatus(value);
    }

    @Override
    public void onResponse(int code) {

    }

    private void initdata() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x01;
        write[2] = 0x00;
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);
    }

    private void updateStatus(byte[] data) {
        Log.v("write", "successfully");
        if (data.length == 22) {
            dataRead.setData(data);
            if (dataRead.getUnit() == 0x01) {
                btUnit.setText("°C >");
            } else {
                btUnit.setText("°F >");
            }

        }
    }
}