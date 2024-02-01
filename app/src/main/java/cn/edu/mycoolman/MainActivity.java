package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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

        lbselect = findViewById(R.id.lbselected);
        lbcurrent = findViewById(R.id.lbcurrent);
        lbsetting = findViewById(R.id.lbsetting);
        btadd = findViewById(R.id.btadd);
        btdrop = findViewById(R.id.btdrop);
        bthigh = findViewById(R.id.bthigh);
        btmedium = findViewById(R.id.btmedium);
        btlow = findViewById(R.id.btlow);
        bton = findViewById(R.id.bton);
        btoff = findViewById(R.id.btoff);
        btpower = findViewById(R.id.btpower);
        bttemp = findViewById(R.id.bttemp);
        btbattery = findViewById(R.id.btbattery);
        btturbo = findViewById(R.id.btturbo);

    }
}