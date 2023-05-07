package cn.edu.mycoolman;
import static com.inuker.bluetooth.library.Code.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import java.util.List;
import java.util.UUID;
public class SetColor extends AppCompatActivity {
    private ImageButton btReturn;
    private SeekBar skFlashTime;
    private SeekBar skBrightness;
    private RadioButton radioColor;
    private RadioButton radioFlash;
    //private int valueSeekbar;
    private int valueFlash;
    private int valueBright;
    private TextView alterTime;
    private TextView tvBright;

    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;
    //private ProgressDialog progressDialog;
    private DataRead dataRead;
    private MyImageView imageCircle;

    private  int green;
    private  int red;
    private  int blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_color);

        mClient = MybluetoothClient.getInstance(getApplicationContext());
        MAC = getIntent().getStringExtra("mac");
        service = (UUID) getIntent().getSerializableExtra("service");
        character = (UUID) getIntent().getSerializableExtra("character");

        imageCircle = findViewById(R.id.circle);
        radioColor = findViewById(R.id.radiocolor);
        radioFlash = findViewById(R.id.radioflash);
        skFlashTime = findViewById(R.id.skFlashTime);
        skBrightness = findViewById(R.id.skBrightness);
        btReturn = findViewById(R.id.btReturn);
        alterTime = findViewById(R.id.altertime);
        tvBright = findViewById(R.id.tvBrightness);

        dataRead = new DataRead();
        //MAC = "07:34:37:39:12:3A";
        //MAC = "01:34:37:39:62:5E";

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetColor.this, SettingActivity.class);
                intent.putExtra("mac", MAC);
                intent.putExtra("service",service);
                intent.putExtra("character",character);
                startActivity(intent);
            }
        });
        //点击上面圆纽
        radioColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("--event--","radio1 clicked!");
                radioFlash.setChecked(false);
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)||(dataRead.getPower()==0x01)){
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x19;
                    bytes[2] = 0x00;
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
        //点击下面圆纽
        radioFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("--event--","radio2 clicked!");
                radioColor.setChecked(false);
                //设置彩灯闪烁5秒1次
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)||(dataRead.getPower()==0x01)){
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x19;
                    bytes[2] = 0x05;
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
        //拖动定时滚动条
        skFlashTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                valueFlash = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) || (dataRead.getPower() == 0x01)) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x19;
                    bytes[2] = (byte) valueFlash;
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

        //拖动亮度滚动条
        skBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                valueBright = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) || (dataRead.getPower() == 0x01)) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x1D;
                    bytes[2] = (byte) valueBright;
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
        });


        imageCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Rect localRect = new Rect();
                imageCircle.getLocalVisibleRect(localRect);

                int right = localRect.right;
                int bottom = localRect.bottom;

                int x = (int)motionEvent.getX();   //点击的坐标x
                int y = (int)motionEvent.getY();   // //点击的坐标y

//        int x = (int) event.getRawX();
//        int y = (int) event.getRawY();


                Log.v("--top--",String.valueOf(localRect.top));
                Log.v("--bottom--",String.valueOf(localRect.bottom));
                Log.v("--left--",String.valueOf(localRect.left));
                Log.v("--right--",String.valueOf(localRect.right));

                Log.v("--x--",String.valueOf(x));
                Log.v("--y--",String.valueOf(y));

                //imageCircle.setX(localRect.bottom/2);
                // imageCircle.setY(localRect.right/2);
                //imageCircle.setRadius(localRect.bottom/8);

                float pi = 3.14159f;
//        float coordx = right/2;
//        float coordy = bottom/2;

                float coordx = right/2 + imageCircle.getLeft();
                float coordy = bottom/2 + imageCircle.getTop();


                double value = (float) (x-coordx)/Math.sqrt((x-coordx) * (x-coordx)+(y-coordy)*(y-coordy));
                float arc = (float) Math.acos(value);
                //绿色通道
                if(arc<2.0/3.0 *pi){
                    green = (int)(255 * (pi*2.0/3.0 - arc)/(pi*2.0/3.0));
                }else{
                    green = 0;
                }
                //红色通道
                if(y>coordy && arc <2.0*3.0 *pi){
                    red = 0;
                }else if(y<coordy){
                    red = (int)((1.0 - Math.abs((arc -pi*2.0/3.0)/(pi*2.0/3.0)))*255);
                }else{
                    red = (int) Math.abs((arc-pi*2.0/3.0)/(pi*2.0/3.0) *255);
                }

                //蓝色通道
                if(y<coordy && arc< 2.0/3.0 *pi){
                    blue = 0;
                }else if(y<coordy){
                    blue = (int) Math.abs((arc -pi*2.0/3.0)/(pi*2.0/3.0)*255);
                }else{
                    blue = (int) (255 * (1 - Math.abs((arc - pi*2.0/3.0)/(pi *2.0/3.0))));
                }

                Log.v("red=",String.valueOf(red));
                Log.v("green=",String.valueOf(green));
                Log.v("blue=",String.valueOf(blue));


                imageCircle.setR(red);
                imageCircle.setB(blue);
                imageCircle.setG(green);
                imageCircle.postInvalidate();

                if (mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x19;
                    bytes[2] = 0x00;

                    byte[]  bytein = {bytes[1],bytes[2]};
                    int xx =  utilCRC.alex_crc16(bytein,2);
                    bytes[4] = (byte) (0xFF & xx);
                    bytes[3] = (byte) (0xFF&(xx>>8));
                    bytes[5] = 0x55;

                    mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                            } else {
                            }
                        }
                    });
                }


                //设置红色
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(radioColor).isChecked()) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x1A;
                    bytes[2] = (byte) red;
                    byte[]  bytein = {bytes[1],bytes[2]};
                    int xx =  utilCRC.alex_crc16(bytein,2);
                    bytes[4] = (byte) (0xFF & xx);
                    bytes[3] = (byte) (0xFF&(xx>>8));
                    bytes[5] = 0x55;

                    mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                            } else {
                            }
                        }
                    });
                }

                //设置绿色
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(radioColor.isChecked())) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x1B;
                    bytes[2] = (byte) green;
                    byte[]  bytein = {bytes[1],bytes[2]};
                    int xx =  utilCRC.alex_crc16(bytein,2);
                    bytes[4] = (byte) (0xFF & xx);
                    bytes[3] = (byte) (0xFF&(xx>>8));
                    bytes[5] = 0x55;

                    mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                            } else {
                            }
                        }
                    });
                }

                //设置蓝色
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)&&(radioColor.isChecked())) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x1C;
                    bytes[2] = (byte) blue;
                    byte[]  bytein = {bytes[1],bytes[2]};
                    int xx =  utilCRC.alex_crc16(bytein,2);
                    bytes[4] = (byte) (0xFF & xx);
                    bytes[3] = (byte) (0xFF&(xx>>8));
                    bytes[5] = 0x55;

                    mClient.write(MAC, service, character, bytes, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {
                            } else {
                            }
                        }
                    });
                }
                return false;
            }
        });
        getStatus();
    }

    //获得初始状态
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
                    } else {
                    }
                }
            });
        }
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

    private void updateStatus( byte[] data){
        Log.v("haha", "tttttttt");
        dataRead.setData(data);
        if (dataRead.getAtmosphere() == 0x00) {
            radioColor.setChecked(true);
            radioFlash.setChecked(false);
        } else {
            radioColor.setChecked(false);
            radioFlash.setChecked(true);
            alterTime.setText("time:" + String.valueOf(dataRead.getAtmosphere()) + "S");
        }
        tvBright.setText("brightness:" + String.valueOf(dataRead.getBrightness()));

    }
   // @Override
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
}