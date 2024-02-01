package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class PassActivity extends AppCompatActivity implements BleNotifyResponse, BleWriteResponse {
    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;
    private ProgressDialog progressDialog;
    private String passstr;
    private DataRead dataRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        mClient = MybluetoothClient.getInstance(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("mac");
            service = (UUID) intent.getSerializableExtra("service");
            character = (UUID) intent.getSerializableExtra("character");
        }
        mClient.notify(MAC, service, character, this);
        getPassWord();

    }


    //获取密码
    public void getPassWord() {
        if (character != null) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[1] = 0x09;
            write[2] = 0x01;
            write[3] = 0x00;
            write[4] = 0x00;
            byte[] bytin = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytin, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            // mClient.write(MAC, service, character, write, PassActivity.this);
            mClient.write(MAC, service, character, write, this);
        }
    }

    //收到通知
    public void updateStatus(byte[] data) {
        Log.v("update----", "now---");
//        if(data.length == 22){
//            dataRead.setData(data);
//
//        }
    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        updateStatus(value);
    }

    @Override
    public void onResponse(int code) {

    }
}