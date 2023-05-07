package cn.edu.mycoolman;

import static android.widget.Toast.makeText;
import static com.inuker.bluetooth.library.Code.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;

import java.sql.Array;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;
    private ProgressDialog progressDialog;
    private DataRead dataRead;

    private ImageButton btReturn;
    private ImageView imageTitle;
    private ImageButton btSetting;

    private ImageView imageA;
    private ImageView imageWindMachine;
    private ImageView imageCool;
    private ImageView imageWarm;
    private ImageView imageHimidity;

    private ImageView imageLight;
    private ImageView imageSleep;
    private ImageView imageTemperatureHigh;
    private ImageView imageTemperatureLow;

    private ImageView imageUnit;
    private ImageView imageChange;

    private ImageView imageQuiet;
    private ImageView imageNormal;
    private ImageView imageTurbo;
    private ImageView imageWind;
    private ImageView imageTimerHigh;
    private ImageView imageTimerLow;
    private View viewDot;
    private ImageView imageTimerDecimal;
    private ImageView imageTimer;

    private ImageButton btPower;
    private ImageButton btWind;
    private ImageButton btAdd;
    private ImageButton btLight;
    private ImageButton btUnit;
    private ImageButton btMode;
    private ImageButton btTimer;
    private ImageButton btMinus;
    private ImageButton btQuiet;
    private ImageButton btTurbo;

//    private  boolean fanSelected;
//    private  boolean lightSelected;
//    private  boolean uintSelected;
//    private  boolean modeSelected;
//    private  boolean timerSelected;
//    private  boolean quitSelected;
//    private  boolean turboSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataRead = new DataRead();

        btReturn = findViewById(R.id.btReturn);
        imageTitle = findViewById(R.id.imageTitle);
        btSetting = findViewById(R.id.btSetting);

        imageA = findViewById(R.id.imageA);
        imageWindMachine = findViewById(R.id.imageWindmachine);
        imageCool = findViewById(R.id.imageCool);
        imageWarm = findViewById(R.id.imageWarm);
        imageHimidity = findViewById(R.id.imageHimidity);

        imageLight = findViewById(R.id.imageLight);
        imageSleep = findViewById(R.id.imageSleep);
        imageTemperatureHigh = findViewById(R.id.imageTemperatureHigh);
        imageTemperatureLow = findViewById(R.id.imageTemperatureLow);

        imageUnit = findViewById(R.id.imageUnit);
        imageChange = findViewById(R.id.imageChange);

        imageQuiet = findViewById(R.id.imageQuiet);
        imageNormal = findViewById(R.id.imageNormal);
        imageTurbo = findViewById(R.id.imageTurbo);
        imageWind = findViewById(R.id.imageWind);
        imageTimerHigh = findViewById(R.id.imageTimerHigh);
        imageTimerLow = findViewById(R.id.imageTimerLow);
        viewDot = findViewById(R.id.viewDot);
        imageTimerDecimal = findViewById(R.id.imageTimerDecimal);
        imageTimer = findViewById(R.id.imageTimer);

        btPower = findViewById(R.id.btPower);
        btWind = findViewById(R.id.btWind);
        btAdd = findViewById(R.id.btAdd);
        btLight = findViewById(R.id.btLight);
        btUnit = findViewById(R.id.btUnit);
        btMode = findViewById(R.id.btMode);
        btTimer = findViewById(R.id.btTimer);
        btMinus = findViewById(R.id.btMinus);
        btQuiet = findViewById(R.id.btQuiet);
        btTurbo = findViewById(R.id.btTurbo);
        //  progressDialog = ProgressDialog.show(this,"getCharacter","Get service and character...");


        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.filekey), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.MACkey), "");
                editor.putString(getString(R.string.serviceKey), service.toString());
                    editor.putString(getString(R.string.characterKey), character.toString());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this,ListDevice.class);
                    startActivity(intent);
                }
            });

            btSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                    //intent.putExtra("devicename",);
                    intent.putExtra("mac",MAC);
                    intent.putExtra("service",service);
                    intent.putExtra("character",character);
                    startActivity(intent);
                }
            });

            btPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    power();
                }
            });
            btWind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setWind();
                }
            });
            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setAdd();
                }
            });
            btMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setMinus();
                }
            });
            btLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setlight();
                }
            });
            btUnit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setUnit();
                }
            });
            btMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setMode();
                }
            });
            btTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setTimer();
                }
            });

            btQuiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setQuiet();
                }
            });
            btTurbo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setTurbo();
                }
            });

            MAC = getIntent().getStringExtra("mac");
            mClient = MybluetoothClient.getInstance(getApplicationContext());
            service = (UUID) getIntent().getSerializableExtra("service");
            character = (UUID) getIntent().getSerializableExtra("character");
            getStatus();

//        mClient.connect(MAC, new BleConnectResponse() {
//            @Override
//            public void onResponse(int code, BleGattProfile profile) {
//                if (code == REQUEST_SUCCESS) {
//                    //Log.d("connect", "---Connected successfully!---");
//                    List<BleGattService> listServices = profile.getServices();
//                    if(listServices.size()>0){
//                        service = listServices.get(2).getUUID();
//                        List<BleGattCharacter> listCharacters = listServices.get(2).getCharacters();
//                        if(listCharacters.size()>0){
//                            character = listCharacters.get(0).getUuid();
//                            progressDialog.dismiss();
//                            getStatus();
//                            Log.v("---value for MAC:",MAC);
//                        }
//                    }
//                }else if(code == REQUEST_FAILED){
//                }
//            }
//        });

            //蓝牙的状态监听
            mClient.registerConnectStatusListener(MAC, new BleConnectStatusListener() {
                @Override
                public void onConnectStatusChanged(String macAddress, int status) {
                    if (status == STATUS_CONNECTED) {
                        //蓝牙设备处于连接状态
                    } else if (status == STATUS_DISCONNECTED) {
                        //蓝牙设备断开
                    }
                }
            });

    }

    @Override
    protected void onStart(){
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
        getStatus();
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

    //更新初始状态
    private  void getStatus() {
//        mClient.notify(MAC, service, character, new BleNotifyResponse() {
//            @Override
//            public void onNotify(UUID service, UUID character, byte[] value) {
//                updateStatus(value);
//            }
//            @Override
//            public void onResponse(int code) {
//            }
//        });

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
    //开关操作
    private  void power() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//
//        fanSelected = false;
//        lightSelected = false;
//        uintSelected = false;
//        modeSelected = false;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;

        if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x10;
            if (dataRead.getPower() == 0x00) {
                bytes[2] = 0x01;
            } else {
                bytes[2] = 0x00;
            }
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
    //设置风量
    private  void setWind(){
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        fanSelected = true;
//        lightSelected = false;
//        uintSelected = false;
//        modeSelected = false;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;
        if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x12;
            bytes[2] = (byte) ((dataRead.getWind() + 1) % 5);
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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
    //按钮加
    private void  setAdd() {
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
        //所有按钮都不打开，或者设置温度按钮打开，设置温度
        byte[] bytes = new byte[6];
//        if((!(fanSelected||lightSelected||modeSelected||timerSelected||quitSelected||turboSelected)||uintSelected)){
        // byte[] bytes = new byte[6];
        bytes[0] = (byte) 0xAA;
        bytes[1] = 0x11;
        bytes[2] = (byte) (dataRead.getTempSetting() + 1);
        byte[] bytein = {bytes[1], bytes[2]};
        int x = utilCRC.alex_crc16(bytein, 2);
        bytes[4] = (byte) (0xFF & x);
        bytes[3] = (byte) (0xFF & (x >> 8));
        bytes[5] = 0x55;
//        }
        //模式选择按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&modeSelected){
//           // byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x13;
//            bytes[2] = (byte)((dataRead.getMode() +3)%5);
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
        //风量按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&fanSelected){
//           // byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x12;
//            bytes[2] = (byte)((dataRead.getWind() +1)%5);
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
        //定时按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&timerSelected){
//           // byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x16;
//            bytes[2] = (byte)(dataRead.getCountdown() +5);
//            if(bytes[2]<0) bytes[2]+=128;
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
        //logo灯按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&lightSelected){
//            //byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x1D;
//            bytes[2] =(byte)(dataRead.getBrightness()+1);
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
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
    //按钮减
    private void setMinus() {
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
        //所有按钮都不打开，或者设置温度按钮打开，设置温度
        byte[] bytes = new byte[6];
//        if(!(fanSelected||lightSelected||modeSelected||timerSelected||quitSelected||turboSelected)||uintSelected){
        // byte[] bytes = new byte[6];
        bytes[0] = (byte) 0xAA;
        bytes[1] = 0x11;
        bytes[2] = (byte) (dataRead.getTempSetting() - 1);
        byte[] bytein = {bytes[1], bytes[2]};
        int x = utilCRC.alex_crc16(bytein, 2);
        bytes[4] = (byte) (0xFF & x);
        bytes[3] = (byte) (0xFF & (x >> 8));
        bytes[5] = 0x55;
//        }
        //模式选择按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&modeSelected){
//            // byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x13;
//            bytes[2] = (byte)((dataRead.getMode() +2)%5);
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
//        //风量按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&fanSelected){
//            // byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x12;
//            bytes[2] = (byte)((dataRead.getWind() +4)%5);
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
//        //定时按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&timerSelected){
//            // byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x16;
//            bytes[2] = (byte)(dataRead.getCountdown() -5);
//            if(bytes[2]<0) bytes[2]+= 128;
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
//        //logo灯按钮打开
//        if((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(dataRead.getPower()==0x01)&&lightSelected){
//            //byte[] bytes = new byte[6];
//            bytes[0] = (byte) 0xAA;
//            bytes[1] = 0x1D;
//            if(dataRead.getBrightness()>0) {
//                bytes[2] = (byte) (dataRead.getBrightness() - 1);
//            }
//            byte[]  bytein = {bytes[1],bytes[2]};
//            int x =  utilCRC.alex_crc16(bytein,2);
//            bytes[4] = (byte) (0xFF & x);
//            bytes[3] = (byte) (0xFF&(x>>8));
//            bytes[5] = 0x55;
//        }
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
    //设置亮度
    private void setlight() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        if(dataRead.getPower() == 0x01) {
//            btAdd.setBackgroundColor(ContextCompat.getColor(this, R.color.highligt));
//        }else{
//            btAdd.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        }
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        if (dataRead.getPower() == 0x01) {
//            btMinus.setBackgroundColor(ContextCompat.getColor(this, R.color.highligt));
//        } else {
//            btMinus.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        }
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this, R.color.lowlight));
//        fanSelected = false;
//        lightSelected = true;
//        uintSelected = false;
//        modeSelected = false;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;
        if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x18;
            bytes[2] = (byte) ((dataRead.getLogo() + 1) % 2);
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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
    //设置单位
    private  void setUnit() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        fanSelected = false;
//        lightSelected = false;
//        uintSelected = true;
//        modeSelected = false;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;
        if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x17;
            bytes[2] = (byte) (dataRead.getUnit());
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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
    //设置模式
    private void setMode() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        fanSelected = false;
//        lightSelected = false;
//        uintSelected = false;
//        modeSelected = true;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;
        if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) && (dataRead.getPower() == 0x01)) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x13;
            bytes[2] = (byte) ((dataRead.getMode() + 3) % 5);
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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
    //设置安静
    private  void setQuiet() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        fanSelected = false;
//        lightSelected = false;
//        uintSelected = false;
//        modeSelected = false;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;
        if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) || (dataRead.getPower() == 0x01)) {

            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x15;
            bytes[2] = (byte) ((dataRead.getSleep() + 1) % 2);
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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
    //设置加强
    private  void setTurbo() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        fanSelected = false;
//        lightSelected = false;
//        uintSelected = false;
//        modeSelected = false;
//        timerSelected = false;
//        quitSelected = false;
//        turboSelected = false;
        if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) || (dataRead.getPower() == 0x01)) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x14;
            bytes[2] = (byte) ((dataRead.getTurbo() + 1) % 2);
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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
    //设置定时
    private  void  setTimer() {
//        btPower.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btWind.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btAdd.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btLight.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btUnit.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btMode.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTimer.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btMinus.setBackgroundColor(ContextCompat.getColor(this,R.color.highligt));
//        btQuiet.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        btTurbo.setBackgroundColor(ContextCompat.getColor(this,R.color.lowlight));
//        fanSelected = false;
//        lightSelected = false;
//        uintSelected = false;
//        modeSelected = false;
//        timerSelected = true;
//        quitSelected = false;
//        turboSelected = false;
        if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) || (dataRead.getPower() == 0x01)) {
            byte[] bytes = new byte[6];
            bytes[0] = (byte) 0xAA;
            bytes[1] = 0x16;
            bytes[2] = (byte) (dataRead.getCountdown() + 5);
            if (bytes[2] < 0) bytes[2] += 128;
            byte[] bytein = {bytes[1], bytes[2]};
            int x = utilCRC.alex_crc16(bytein, 2);
            bytes[4] = (byte) (0xFF & x);
            bytes[3] = (byte) (0xFF & (x >> 8));
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


    //处理返回数据
    private void updateStatus(byte[] data){
        //Log.v("--dosomething","dosomenthing about data");
       // Log.v("datalength",String.valueOf(data.length));
        dataRead.setData(data);
        imageA.setVisibility(View.INVISIBLE);
        imageWindMachine.setVisibility(View.INVISIBLE);
        imageChange.setVisibility(View.INVISIBLE);
        imageCool.setVisibility(View.INVISIBLE);
        imageWarm.setVisibility(View.INVISIBLE);
        imageHimidity.setVisibility(View.INVISIBLE);
        //imageLight.setVisibility(View.INVISIBLE);
        imageSleep.setVisibility(View.INVISIBLE);
        imageUnit.setVisibility(View.VISIBLE);
        imageChange.setVisibility(View.INVISIBLE);

        imageTemperatureHigh.setVisibility(View.VISIBLE);
        imageTemperatureLow.setVisibility(View.VISIBLE);
        imageQuiet.setVisibility(View.INVISIBLE);
        imageNormal.setVisibility(View.INVISIBLE);
        imageTurbo.setVisibility(View.INVISIBLE);
        imageWind.setVisibility(View.INVISIBLE);
        imageTimerHigh.setVisibility(View.INVISIBLE);
        imageTimerLow.setVisibility(View.INVISIBLE);
        imageTimerDecimal.setVisibility(View.INVISIBLE);
        viewDot.setVisibility(View.INVISIBLE);
        imageTimer.setVisibility(View.INVISIBLE);

        btQuiet.setEnabled(true);
        btTurbo.setEnabled(true);
        btWind.setEnabled(true);

        int temperatureHigh,temperatureLow;
        if(dataRead.getLogo()==0x00){
            imageLight.setVisibility(View.INVISIBLE);
        }else{
            imageLight.setVisibility(View.VISIBLE);
        }
        if(dataRead.getUnit() == 0x01){
            imageUnit.setImageResource(R.drawable.celsius);
        }else{
            imageUnit.setImageResource(R.drawable.fahrenheit);
        }
        temperatureHigh =  dataRead.getTempSetting()/10;
        temperatureLow = dataRead.getTempSetting()%10;
        String iconNameHight = String.format("big%d",temperatureHigh);
        @SuppressLint("DefaultLocale") String iconNameLow = String.format("big%d",temperatureLow);

        int idHigh = getResources().getIdentifier("cn.edu.mycoolman:drawable/" + iconNameHight, null, null);
        int idLow = getResources().getIdentifier("cn.edu.mycoolman:drawable/" + iconNameLow, null, null);
        imageTemperatureHigh.setImageResource(idHigh);
        imageTemperatureHigh.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageTemperatureLow.setImageResource(idLow);
        imageTemperatureLow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        if(dataRead.getPower() == 0x00){
            btWind.setEnabled(false);
            btAdd.setEnabled(false);
            btUnit.setEnabled(false);
            btMode.setEnabled(false);
            btTimer.setEnabled(false);
            btMinus.setEnabled(false);
            btQuiet.setEnabled(false);
            btTurbo.setEnabled(false);
        }else {
            btWind.setEnabled(true);
            btAdd.setEnabled(true);
            btUnit.setEnabled(true);
            btMode.setEnabled(true);
            btTimer.setEnabled(true);
            btMinus.setEnabled(true);
            btQuiet.setEnabled(true);
            btTurbo.setEnabled(true);

            //风量
            switch (dataRead.getMode()) {
                case 0x04:
                    imageA.setVisibility(View.VISIBLE);
                    btQuiet.setEnabled(false);
                    btTurbo.setEnabled(false);
                    btWind.setEnabled(false);
                    break;
                case 0x02:
                    imageWindMachine.setVisibility(View.VISIBLE);
                    break;
                case 0x00:
                    imageCool.setVisibility(View.VISIBLE);
                    break;
                case 0x03:
                    imageWarm.setVisibility(View.VISIBLE);
                    break;
                case 0x01:
                    imageHimidity.setVisibility(View.VISIBLE);
                    btQuiet.setEnabled(false);
                    btTurbo.setEnabled(false);
                    btWind.setEnabled(false);
                    break;
                default:
                    break;
            }
            imageWind.setVisibility(View.VISIBLE);
            String iconWindName = String.format("wind%d", dataRead.getWind());
            if (iconWindName.equals("wind0") ) {
                iconWindName = "autowind";
            }
            int idWind = getResources().getIdentifier("cn.edu.mycoolman:drawable/" + iconWindName, null, null);
            imageWind.setImageResource(idWind);
            //安静或强力模式
            if (dataRead.getSleep() == 0x01) {
                imageQuiet.setVisibility(View.VISIBLE);
                imageSleep.setVisibility(View.VISIBLE);
            } else if (dataRead.getTurbo() == 0x01) {
                imageTurbo.setVisibility(View.VISIBLE);
                imageChange.setVisibility(View.VISIBLE);
            } else {
                imageNormal.setVisibility(View.VISIBLE);
                imageSleep.setVisibility(View.INVISIBLE);
                imageChange.setVisibility(View.INVISIBLE);
            }

            //设置logo灯
            if (dataRead.getLogo() == 0x01) {
                imageLight.setVisibility(View.VISIBLE);
            } else {
                imageLight.setVisibility(View.INVISIBLE);
            }

            //显示定时
            if (dataRead.getCountdown() == 0x00) {
                imageTimerHigh.setVisibility(View.INVISIBLE);
                imageTimerLow.setVisibility(View.INVISIBLE);
                viewDot.setVisibility(View.INVISIBLE);
                imageTimerDecimal.setVisibility(View.INVISIBLE);
                imageTimer.setVisibility(View.INVISIBLE);
            } else {
                imageTimerLow.setVisibility(View.VISIBLE);
                viewDot.setVisibility(View.VISIBLE);
                imageTimerDecimal.setVisibility(View.VISIBLE);
                imageTimer.setVisibility(View.VISIBLE);

                int timerHigh, timerLow, timerDecimal;
                int c = dataRead.getCountdown();
                timerHigh = dataRead.getCountdown() /10/10;
                timerLow = dataRead.getCountdown() / 10 % 10;
                timerDecimal = dataRead.getCountdown() / 5 % 2 * 5;
                String iconHigh = String.format("small%d", timerHigh);
                String iconLow = String.format("small%d", timerLow);
                String iconDecimal = String.format("small%d", timerDecimal);
                if (timerHigh > 0) {
                    imageTimerHigh.setVisibility(View.VISIBLE);
                    int high = getResources().getIdentifier("cn.edu.mycoolman:drawable/" + iconHigh, null, null);
                    imageTimerHigh.setImageResource(high);
                }
                int low = getResources().getIdentifier("cn.edu.mycoolman:drawable/" + iconLow, null, null);
                imageTimerLow.setImageResource(low);
                int decimal = getResources().getIdentifier("cn.edu.mycoolman:drawable/" + iconDecimal, null, null);
                imageTimerDecimal.setImageResource(decimal);
            }
        }
    }

}
