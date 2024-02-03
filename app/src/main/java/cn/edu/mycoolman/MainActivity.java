package cn.edu.mycoolman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BleNotifyResponse, BleWriteResponse {
    private final UUID service4UUID = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    private final UUID charAUUID = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    private MybluetoothClient mClient;
    private String MAC;
    private UUID service;
    private UUID character;
    private ProgressDialog progressDialog;
    private String passstr;
    private DataRead dataRead;

    private TextView lbmode;
    private TextView label1;
    private TextView label2;
    private TextView label3;
    private ImageView img;

    private TextView lbselect;
    private TextView lbcurrent;
    private TextView lbsetting;
    private ImageButton btadd;
    private ImageButton btdrop;

    private ImageButton bthigh;
    private ImageButton btmedium;
    private ImageButton btlow;
    private ImageButton bton;
    private ImageButton btoff;
    private ImageButton btpower;
    private ImageButton bttemp;
    private ImageButton btbattery;
    private ImageButton btturbo;

    private byte bytepass1;
    private byte bytepass2;
    private byte bytepass3;
    private String a, b, c;


    private ImageButton btsetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        dataRead = new DataRead();
        //  getSupportActionBar().hide(); //隐藏状态栏
        initController();

        initBluetooth();
        getPassworld();
    }

    //初始化蓝牙
    private void initBluetooth() {
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("mac");
            //  service = (UUID) intent.getSerializableExtra("service");
            // character = (UUID) intent.getSerializableExtra("character");
            service = service4UUID;
            character = charAUUID;
        }
        mClient.notify(MAC, service, character, this);

    }

    //初始化控件
    private void initController() {
        lbmode = findViewById(R.id.lbmode);
        img = findViewById(R.id.img);
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);
        lbselect = findViewById(R.id.lbselected);
        lbcurrent = findViewById(R.id.lbcurrent);
        lbsetting = findViewById(R.id.lbsetting);
        btadd = findViewById(R.id.btadd);
        btadd.setOnClickListener(this);
        btdrop = findViewById(R.id.btdrop);
        btdrop.setOnClickListener(this);
        bthigh = findViewById(R.id.bthigh);
        bthigh.setOnClickListener(this);
        btmedium = findViewById(R.id.btmedium);
        btmedium.setOnClickListener(this);
        btlow = findViewById(R.id.btlow);
        btlow.setOnClickListener(this);
        bton = findViewById(R.id.bton);
        bton.setOnClickListener(this);
        btoff = findViewById(R.id.btoff);
        btoff.setOnClickListener(this);
        btpower = findViewById(R.id.btpower);
        btpower.setOnClickListener(this);
        bttemp = findViewById(R.id.bttemp);
        bttemp.setOnClickListener(this);
        btbattery = findViewById(R.id.btbattery);
        btbattery.setOnClickListener(this);
        btturbo = findViewById(R.id.btturbo);
        btturbo.setOnClickListener(this);
        btsetting = findViewById(R.id.btsetting);
        btsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // intent.setClass(MainActivity.this, SettingActivity.class);
                intent.setClass(MainActivity.this, testActivity.class);
                startActivity(intent);
            }
        });

        //j进入主页面
        lbmode.setVisibility(View.INVISIBLE);
        label1.setVisibility(View.VISIBLE);
        label2.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
        lbselect.setVisibility(View.INVISIBLE);
        lbcurrent.setVisibility(View.INVISIBLE);
        lbsetting.setVisibility(View.INVISIBLE);
        btadd.setVisibility(View.INVISIBLE);
        btdrop.setVisibility(View.INVISIBLE);
        bthigh.setVisibility(View.INVISIBLE);
        btmedium.setVisibility(View.INVISIBLE);
        btlow.setVisibility(View.INVISIBLE);
        bton.setVisibility(View.INVISIBLE);
        btoff.setVisibility(View.INVISIBLE);
    }

    //获取密码
    private void getPassworld() {
        SharedPreferences sharePref = MainActivity.this.getSharedPreferences("datafile", Context.MODE_PRIVATE);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btpower:
                setPower();
                break;
            case R.id.bttemp:
                setTemp();
                break;
            case R.id.btbattery:
                setBattery();
                break;
            case R.id.btturbo:
                setTurbo();
                break;
            case R.id.btadd:
                setAdd();
                break;
            case R.id.btdrop:
                setDrop();
                break;
            case R.id.bthigh:
                setHigh();
                break;
            case R.id.btmedium:
                setMedium();
                break;
            case R.id.btlow:
                setLow();
                break;
            case R.id.bton:
                setOn();
                break;
            case R.id.btoff:
                setOff();
                break;
        }
    }

    private void setPower() {
        lbmode.setVisibility(View.INVISIBLE);
        label1.setVisibility(View.VISIBLE);
        label2.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
        lbselect.setVisibility(View.INVISIBLE);
        lbcurrent.setVisibility(View.INVISIBLE);
        lbsetting.setVisibility(View.INVISIBLE);
        btadd.setVisibility(View.INVISIBLE);
        btdrop.setVisibility(View.INVISIBLE);
        bthigh.setVisibility(View.INVISIBLE);
        btmedium.setVisibility(View.INVISIBLE);
        btlow.setVisibility(View.INVISIBLE);
        bton.setVisibility(View.INVISIBLE);
        btoff.setVisibility(View.INVISIBLE);

        btpower.setBackground(getDrawable(R.drawable.poweron));
        bttemp.setBackground(getDrawable(R.drawable.tempoff));
        btbattery.setBackground(getDrawable(R.drawable.batteryoff));
        btturbo.setBackground(getDrawable(R.drawable.turbooff));

        byte powerstatus = dataRead.getPower();
        if (powerstatus == 0x00) {
            powerstatus = 0x01;
        } else {
            powerstatus = 0x00;
        }
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x02;
        write[2] = powerstatus;
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);

    }

    private void setTemp() {
        lbmode.setVisibility(View.VISIBLE);
        label1.setVisibility(View.INVISIBLE);
        label2.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        lbselect.setVisibility(View.VISIBLE);
        lbcurrent.setVisibility(View.VISIBLE);
        lbsetting.setVisibility(View.VISIBLE);
        btadd.setVisibility(View.VISIBLE);
        btdrop.setVisibility(View.VISIBLE);
        bthigh.setVisibility(View.INVISIBLE);
        btmedium.setVisibility(View.INVISIBLE);
        btlow.setVisibility(View.INVISIBLE);
        bton.setVisibility(View.INVISIBLE);
        btoff.setVisibility(View.INVISIBLE);

        btpower.setBackground(getDrawable(R.drawable.poweroff));
        bttemp.setBackground(getDrawable(R.drawable.tempon));
        btbattery.setBackground(getDrawable(R.drawable.batteryoff));
        btturbo.setBackground(getDrawable(R.drawable.turbooff));
        lbmode.setText("Current Temperature");
    }

    private void setBattery() {
        lbmode.setVisibility(View.VISIBLE);
        label1.setVisibility(View.INVISIBLE);
        label2.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        lbselect.setVisibility(View.INVISIBLE);
        lbcurrent.setVisibility(View.INVISIBLE);
        lbsetting.setVisibility(View.INVISIBLE);
        btadd.setVisibility(View.INVISIBLE);
        btdrop.setVisibility(View.INVISIBLE);
        bthigh.setVisibility(View.VISIBLE);
        btmedium.setVisibility(View.VISIBLE);
        btlow.setVisibility(View.VISIBLE);
        bton.setVisibility(View.INVISIBLE);
        btoff.setVisibility(View.INVISIBLE);

        btpower.setBackground(getDrawable(R.drawable.poweroff));
        bttemp.setBackground(getDrawable(R.drawable.tempoff));
        btbattery.setBackground(getDrawable(R.drawable.batteryon));
        btturbo.setBackground(getDrawable(R.drawable.turbooff));
        lbmode.setText("Battery Protection");
    }

    private void setTurbo() {
        lbmode.setVisibility(View.VISIBLE);
        label1.setVisibility(View.INVISIBLE);
        label2.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        lbselect.setVisibility(View.INVISIBLE);
        lbcurrent.setVisibility(View.INVISIBLE);
        lbsetting.setVisibility(View.INVISIBLE);
        btadd.setVisibility(View.INVISIBLE);
        btdrop.setVisibility(View.INVISIBLE);
        bthigh.setVisibility(View.INVISIBLE);
        btmedium.setVisibility(View.INVISIBLE);
        btlow.setVisibility(View.INVISIBLE);
        bton.setVisibility(View.VISIBLE);
        btoff.setVisibility(View.VISIBLE);

        btpower.setBackground(getDrawable(R.drawable.poweroff));
        bttemp.setBackground(getDrawable(R.drawable.tempoff));
        btbattery.setBackground(getDrawable(R.drawable.batteryoff));
        btturbo.setBackground(getDrawable(R.drawable.turboon));
        lbmode.setText("Turbo Mode");
    }

    private void setAdd() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x03;
        write[2] = (byte) (dataRead.getTempcool() + 1);
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);
    }

    private void setDrop() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x03;
        write[2] = (byte) (dataRead.getTempcool() - 1);
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);
    }

    private void setHigh() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x07;
        write[2] = 0x02;
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);
    }

    private void setMedium() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x07;
        write[2] = 0x01;
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);
    }

    private void setLow() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x07;
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

    private void setOn() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x05;
        write[2] = 0x01;
        write[3] = (byte) Integer.parseInt(a, 16);
        write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
        byte[] bytin = {write[1], write[2], write[3], write[4]};
        int x = utilCRC.alex_crc16(bytin, 4);
        write[6] = (byte) (0xFF & x);
        write[5] = (byte) (0xFF & (x >> 8));
        write[7] = 0x55;
        mClient.write(MAC, service, character, write, this);

    }

    private void setOff() {
        byte[] write = new byte[8];
        write[0] = (byte) 0xAA;
        write[1] = 0x05;
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

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        updateStatus(value);
    }

    @Override
    public void onResponse(int code) {
        /*
        Log.v("失败", "断开连接");
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  //
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();
        mClient.connect(MAC, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code != -1) {
                    Log.v("重连", "重新连接成功！");
                }
            }
        });*/
    }


    private void updateStatus(byte[] data) {
        Log.v("write", "successfully");
        if (data.length == 22) {
            dataRead.setData(data);
            Log.v("set", "data----");
            if (dataRead.getBattery() == 0x02) {
                bthigh.setBackground(getDrawable(R.drawable.highon));
                btmedium.setBackground(getDrawable(R.drawable.mediumoff));
                btlow.setBackground(getDrawable(R.drawable.lowoff));
            } else if (dataRead.getBattery() == 0x01) {
                bthigh.setBackground(getDrawable(R.drawable.highoff));
                btmedium.setBackground(getDrawable(R.drawable.meidiumon));
                btlow.setBackground(getDrawable(R.drawable.lowoff));
            } else {
                bthigh.setBackground(getDrawable(R.drawable.highoff));
                btmedium.setBackground(getDrawable(R.drawable.mediumoff));
                btlow.setBackground(getDrawable(R.drawable.lowon));
            }

            if (dataRead.getTurbo() == 0x01) {
                bton.setBackground(getDrawable(R.drawable.onon));
                btoff.setBackground(getDrawable(R.drawable.offoff));
            } else {
                bton.setBackground(getDrawable(R.drawable.onoff));
                btoff.setBackground(getDrawable(R.drawable.offon));
            }

            if (dataRead.getUnit() == 0x01) {
                if (dataRead.getTempReal() > 128) {
                    lbcurrent.setText((dataRead.getTempReal() - 256) + "°C");
                } else {
                    lbcurrent.setText(dataRead.getTempReal() + "°C");
                }
            } else {
                if (dataRead.getTempReal() > 128) {
                    lbcurrent.setText(Math.round((dataRead.getTempReal() - 256) * 1.8 + 32) + "°F");
                } else {
                    lbcurrent.setText(Math.round(dataRead.getTempReal() * 1.8 + 32) + "°F");
                }
            }

            if (dataRead.getUnit() == 0x01) {
                if (dataRead.getTempcool() > 128) {
                    lbsetting.setText((dataRead.getTempcool() - 256) + "°C");
                } else {
                    lbsetting.setText(dataRead.getTempcool() + "°C");
                }
            } else {
                if (dataRead.getTempReal() > 128) {
                    lbsetting.setText(Math.round((dataRead.getTempcool() - 256) * 1.8 + 32) + "°F");
                } else {
                    lbsetting.setText(Math.round(dataRead.getTempcool() * 1.8 + 32) + "°F");
                }
            }
        } else {
            return;
        }
    }

}