package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.UUID;

public class testActivity extends AppCompatActivity implements BleNotifyResponse {
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
        setContentView(R.layout.activity_test);
        getPassworld();
        initBluetooth();
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

    }

    @Override
    public void onResponse(int code) {

    }
}