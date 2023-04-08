package cn.edu.mycoolman;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class FanActivity extends AppCompatActivity {
    private DataRead dataRead;
    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;

    private ImageButton btReturn;
    private RadioGroup fanGroup;
    private RadioButton sacle0;
    private RadioButton sacle1;
    private RadioButton sacle2;
    private RadioButton sacle3;
    private RadioButton sacle4;
    private byte scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
        btReturn = (ImageButton) findViewById(R.id.btReturn);

        MAC = getIntent().getStringExtra("mac");
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        service = (UUID) getIntent().getSerializableExtra("service");
        character = (UUID) getIntent().getSerializableExtra("character");
        scale = (byte) getIntent().getIntExtra("scale",0);
        dataRead = new DataRead();

        //fanGroup.check(scale);
          //fanGroup.
//        scale = 0;
//        int id = fanGroup.getCheckedRadioButtonId();
//        RadioButton rbChecked = (RadioButton)findViewById(id);
//        String name = rbChecked.getText().toString();
//        switch (name){
//            case "scale0" :scale = 0; break;
//            case "scale1" :scale = 1; break;
//            case "scale2" :scale = 2; break;
//            case "scale3" :scale = 3; break;
//            case "scale4" :scale = 4; break;
//            default:scale = 0 ;break;
//        }

        addListenerRadio();
        mClient.notify(MAC, service, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.v("changed----","fan scale has changed");
                dataRead.setData(value);
            }
            @Override
            public void onResponse(int code) {
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FanActivity.this,SettingActivity.class);
                intent.putExtra("mac",MAC);
                intent.putExtra("service",service);
                intent.putExtra("character",character);
                startActivity(intent);
            }
        });
    }

    private void addListenerRadio(){
        fanGroup = (RadioGroup) findViewById(R.id.fanGroup);
        fanGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = fanGroup.getCheckedRadioButtonId();
                RadioButton rbChecked = (RadioButton)findViewById(id);
                String name = rbChecked.getText().toString();
                switch (name){
                    case "Speed0" :scale = 0; break;
                    case "Speed1" :scale = 1; break;
                    case "Speed2" :scale = 2; break;
                    case "Speed3" :scale = 3; break;
                    case "Speed4" :scale = 4; break;
                    default:scale = 0 ;break;
                }

                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED)||(dataRead.getPower()==0x01)){
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x12;
                    bytes[2] = scale;
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
}