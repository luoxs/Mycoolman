package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.search.SearchRequest;

import java.util.UUID;

public class PassActivity extends AppCompatActivity implements BleNotifyResponse, BleWriteResponse, View.OnTouchListener, View.OnClickListener, TextWatcher {
    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;
    private ProgressDialog progressDialog;
    private String passstr;
    private DataRead dataRead;
    private EditText pass1, pass2, pass3;
    private Button btcancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        pass1 = findViewById(R.id.password1);
        pass2 = findViewById(R.id.password2);
        pass3 = findViewById(R.id.password3);
        //   btcancel = findViewById(R.id.btcancel);

        pass1.setOnTouchListener(this);
        pass1.addTextChangedListener(this);
        pass2.setOnTouchListener(this);
        pass2.addTextChangedListener(this);
        pass3.setOnTouchListener(this);
        pass3.addTextChangedListener(this);
        //    btcancel.setOnClickListener(this);

        mClient = MybluetoothClient.getInstance(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("mac");
            service = (UUID) intent.getSerializableExtra("service");
            character = (UUID) intent.getSerializableExtra("character");
        }
        mClient.notify(MAC, service, character, this);
        dataRead = new DataRead();
        getPassWord();
    }

    //显示密钥
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
            mClient.writeNoRsp(MAC, service, character, write, this);
        }
    }

    //收到通知
    public void updateStatus(byte[] data) {
        Log.v("update----", "now---");
        if (data.length == 22) {
            dataRead.setData(data);
            if (dataRead.getGc() == 0) {
                Log.v("password", "wrong");
            } else {
                Intent intent = new Intent(PassActivity.this, MainActivity.class);
                // intent.putExtra("devicename",arrayList.get(i));
                intent.putExtra("mac", MAC);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        updateStatus(value);
    }

    @Override
    public void onResponse(int code) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.password1:
                pass1.setText(null);
                break;
            case R.id.password2:
                pass2.setText(null);
                break;
            case R.id.password3:
                pass3.setText(null);
                break;
        }
        return false; //取消软键盘
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (pass1.getText().length() + pass2.getText().length() + pass3.getText().length() == 3) {
            String a = pass1.getText().toString();
            String b = pass2.getText().toString();
            String c = pass3.getText().toString();
            handlepass(a, b, c);
        }
    }

    private void handlepass(String a, String b, String c) {
        passstr = a + b + c;
        try {
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
        } catch (Exception e) {
            Log.v("password---", "out of range");
        }
    }
}