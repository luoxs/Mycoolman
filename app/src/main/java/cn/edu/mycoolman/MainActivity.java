package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
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
    private Button btadd;
    private Button btdrop;

    private Button bthigh;
    private Button btmedium;
    private Button btlow;
    private Button bton;
    private Button btoff;
    private Button btpower;
    private Button bttemp;
    private Button btbattery;
    private Button btturbo;

    private byte bytepass1;
    private byte bytepass2;
    private byte bytepass3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}