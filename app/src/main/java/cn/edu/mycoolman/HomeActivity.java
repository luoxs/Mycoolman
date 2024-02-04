package cn.edu.mycoolman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HomeActivity extends AppCompatActivity {
    private ImageButton btscanqr;
    private ImageButton btbluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btbluetooth = (ImageButton) findViewById(R.id.btBluetooth);
        btbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, ListDevice.class);
                startActivity(intent);
            }
        });

        //扫描二维码
        ImageButton btscanqr = findViewById(R.id.btscanqr);
        btscanqr.setOnClickListener(view -> initScan());
    }

    public void initScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        // integrator.setDesiredBarcodeFormats();
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCaptureActivity(CaptureActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("Please shoot sqrcode"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null && scanResult.getContents() != null) {
                String result = scanResult.getContents();
                Log.d("扫码返回: ", result);
                if (result != null) {
                    String devicename = getFieldValue(result, "BLE");
                    if (devicename.startsWith("CCP15R") || devicename.startsWith("CCP20R")) {
                        Intent qrintent = new Intent();
                        qrintent.putExtra("device", devicename);
                        qrintent.setClass(HomeActivity.this, HandeQR.class);
                        startActivity(qrintent);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Wrong").setMessage("This is a wrong QR code!");
                    // 获取AlertDialog
                    AlertDialog dialog = builder.create();
                    // 显示
                    dialog.show();
                }
                // tvMsg.setText(result);
            }
        }
    }

    private String getFieldValue(String urlStr, String field) {
        String result = "";
        Pattern pXM = Pattern.compile(field + "=([^&]*)");
        Matcher mXM = pXM.matcher(urlStr);
        while (mXM.find()) {
            result += mXM.group(1);
        }
        return result;
    }
}