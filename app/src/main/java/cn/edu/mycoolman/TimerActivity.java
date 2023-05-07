package cn.edu.mycoolman;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class TimerActivity extends AppCompatActivity {
    private DataRead dataRead;
    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;

    private ListView listView;
    private ImageButton btReturn;
    private byte timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        listView = findViewById(R.id.lvTimer);
        btReturn = findViewById(R.id.btReturn);

        MAC = getIntent().getStringExtra("mac");
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        service = (UUID) getIntent().getSerializableExtra("service");
        character = (UUID) getIntent().getSerializableExtra("character");
        timer = (byte) getIntent().getIntExtra("timer", 0);
        dataRead = new DataRead();

        mClient.notify(MAC, service, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.v("changed----", "timer changed");
                dataRead.setData(value);
            }

            @Override
            public void onResponse(int code) {
            }
        });

        String[] times = {"0", "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5",
                "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10",
                "10.5", "11", "11.5", "12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                TimerActivity.this, android.R.layout.simple_list_item_1, times);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ((mClient.getConnectStatus(MAC) == Constants.STATUS_DEVICE_CONNECTED) || (dataRead.getPower() == 0x01)) {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) 0xAA;
                    bytes[1] = 0x16;
                    bytes[2] = (byte) (i * 5);
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
        });


        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TimerActivity.this, SettingActivity.class);
                intent.putExtra("mac", MAC);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                startActivity(intent);
            }
        });
    }
}