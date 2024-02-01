package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BleNotifyResponse {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        mClient = MybluetoothClient.getInstance(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("mac");
            service = (UUID) intent.getSerializableExtra("service");
            character = (UUID) intent.getSerializableExtra("character");
        }
        mClient.notify(MAC, service, character, this);
        dataRead = new DataRead();

        //j进入主页面
        label1.setVisibility(View.VISIBLE);
        label2.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
        lbselect.setVisibility(View.INVISIBLE);
        lbmode.setVisibility(View.INVISIBLE);
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {

    }

    @Override
    public void onResponse(int code) {

    }
}