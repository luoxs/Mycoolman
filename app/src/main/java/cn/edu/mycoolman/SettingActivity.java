package cn.edu.mycoolman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class SettingActivity extends AppCompatActivity implements BleNotifyResponse, BleWriteResponse {
    public DataRead dataRead;
    private Button btUnit;
    private MybluetoothClient mClient;
    private String MAC;
    private UUID service;
    private UUID character;
    private ProgressDialog progressDialog;
    private String passstr;
    private byte bytepass1;
    private byte bytepass2;
    private byte bytepass3;
    private String a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBluetooth();
        getPassworld();
        initdataread();

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
                mClient.write(MAC, service, character, write, SettingActivity.this);
            }
        });

    }

    private void initBluetooth() {
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        //  dataRead = new DataRead();
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("mac");
            service = (UUID) intent.getSerializableExtra("service");
            character = (UUID) intent.getSerializableExtra("character");
        }
        mClient.notify(MAC, service, character, this);
    }

    private void getPassworld() {
        SharedPreferences sharePref = getSharedPreferences("datafile", Context.MODE_PRIVATE);
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

    private void initdataread() {
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
            Log.v("set", "data----");
        }
    }
}