package cn.edu.mycoolman;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class SettingActivity extends AppCompatActivity {

    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;
    private ProgressDialog progressDialog;
    private DataRead dataRead;
    private ImageButton changelight;
    private Button reset;
    private ImageButton btReturn;
    private ImageButton btCheckFan;
    private Switch switchUnit;
    private ImageButton btSetTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        MAC = getIntent().getStringExtra("mac");
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        service = (UUID) getIntent().getSerializableExtra("service");
        character = (UUID) getIntent().getSerializableExtra("character");
        dataRead = new DataRead();

        btReturn = findViewById(R.id.btReturn);
        btCheckFan = findViewById(R.id.btCheckFan);
        btSetTimer = findViewById(R.id.btSetTimer);
        changelight = findViewById(R.id.changelight);
        switchUnit = findViewById(R.id.switchUnit);

        reset = findViewById(R.id.reset);

        mClient.notify(MAC, service, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                updateStatus(value);
            }

            @Override
            public void onResponse(int code) {
            }
        });

        getStatus();

        btCheckFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, FanActivity.class);
                intent.putExtra("mac", MAC);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                int x = dataRead.getWind();
                intent.putExtra("scale", dataRead.getWind());
                startActivity(intent);
            }
        });

        btSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, TimerActivity.class);
                intent.putExtra("mac", MAC);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                int x = dataRead.getBrightness();
                intent.putExtra("scale", x);
                startActivity(intent);
            }
        });

        changelight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, SetColor.class);
                intent.putExtra("mac", MAC);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                startActivity(intent);
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("mac",MAC);
                intent.putExtra("service",service);
                intent.putExtra("character",character);
                startActivity(intent);
            }
        });
        //更换温度单位
        switchUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte unit;
                if(b){
                    unit = 0x01;  //摄氏
                }else{
                    unit = 0x00;  //华氏
                }
                if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x17;
                    bytes[2] = unit;
                    byte[]  bytein = {bytes[1],bytes[2]};
                    int x =  utilCRC.alex_crc16(bytein,2);
                    bytes[4] = (byte) (0xFF & x);
                    bytes[3] = (byte) (0xFF&(x>>8));
                    bytes[5] = 0x55;

                    mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                                Log.v("write result", "######Write datas successfully!######");
                            } else {
                                Log.v("write result", "######Write datas faily!######");
                            }
                        }
                    });
                }
            }
        });
        //接受广播
        mClient.notify(MAC, service, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                updateStatus(value);
            }
            @Override
            public void onResponse(int code) {
            }
        });

        //重置
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x1E;
                    bytes[2] = 0x01;
                    byte[]  bytein = {bytes[1],bytes[2]};
                    int x =  utilCRC.alex_crc16(bytein,2);
                    bytes[4] = (byte) (0xFF & x);
                    bytes[3] = (byte) (0xFF&(x>>8));
                    bytes[5] = 0x55;

                    mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                                Log.v("write result", "######Write datas successfully!######");
                            } else {
                                Log.v("write result", "######Write datas faily!######");
                            }
                        }
                    });
                }
            }
        });

    }

    //更新初始状态
    private  void getStatus() {
        if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x01;
            bytes[2] = 0x00;
//            bytes[3] = (byte) 0xEE;
//            bytes[4] = 0x4D;
            byte[]  bytein = {bytes[1],bytes[2]};
            int x =  utilCRC.alex_crc16(bytein,2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF&(x>>8));
            bytes[5] = 0x55;

            mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code == REQUEST_SUCCESS) {
                        //Log.v("write result", "######Write datas successfully!######");
                    } else {
                        // Log.v("write result", "######Write datas faily!######");
                    }
                }
            });
        }
    }

    //更新设备信息
    private void updateStatus(byte[] data){
        dataRead.setData(data);
        Log.v("haha----","hhhhhhhhhhhhhh");
        if(dataRead.getUnit() == 0x00){
            switchUnit.setChecked(true);
        }else{
            switchUnit.setChecked(false);
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mClient.unnotify(MAC, service, character, new BleUnnotifyResponse() {
//            @Override
//            public void onResponse(int code) {
//                if (code == REQUEST_SUCCESS) {
//                    Log.v("stopped","notify stopped!");
//                }
//            }
//        });
//    }

    @Override
    protected  void onStart(){
        super.onStart();
        mClient.notify(MAC, service, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                updateStatus(value);
            }
            @Override
            public void onResponse(int code) {
            }
        });
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mClient.unnotify(MAC, service, character, new BleUnnotifyResponse() {
//            @Override
//            public void onResponse(int code) {
//                Log.v("--unnotify","unnotifyed bluetooth");
//            }
//        });
//    }
}
