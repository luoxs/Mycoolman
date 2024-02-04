package cn.edu.mycoolman;

import static com.inuker.bluetooth.library.Code.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.UUID;

public class HandeQR extends AppCompatActivity {
    private final UUID service4UUID = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    private final UUID charAUUID = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayMAC;
    private BluetoothClient mClient;
    private ArrayAdapter<String> adapter;
    private String myDeviceName;
    private String MAC;
    private UUID service;
    private UUID character;
    private ProgressDialog progressDialog;
    private String passstr;
    private DataRead dataRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hande_qr);

        Intent intent = getIntent();
        myDeviceName = intent.getStringExtra("device");

        arrayList = new ArrayList<String>();
        arrayMAC = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrayList);
        //lstv.setAdapter(adapter);

        boolean locationEnable = isLocationEnabled();
        if (!locationEnable) {
            Toast.makeText(HandeQR.this, "Please turn on location", Toast.LENGTH_SHORT).show();
        }

        // 检测PHONE_STATE 如果已授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //做你想做的
            Log.v("hhhhhhh", "okokokokokookok");
        } else {
            String[] permission = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
            ActivityCompat.requestPermissions(this, permission, 1);
        }

        mClient = MybluetoothClient.getInstance(getApplicationContext());

        // BluetoothClient mClient = new BluetoothClient(this);
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                // Beacon beacon = new Beacon(device.scanRecord);
                // BluetoothLog.v(String.format("----beacon for %s\n%s", device.getAddress(), beacon.toString()));
                if ((device.getName().equals(myDeviceName))) {
                    arrayList.add(device.getName());
                    arrayMAC.add(device.getAddress());
                    ConnetDevice(arrayMAC.get(0));
                }
            }

            @Override
            public void onSearchStopped() {
                Log.v("over", "-------scanstopped");
                for (String name : arrayList) {
                    Log.v("----name---", name);
                }
                for (String mac : arrayMAC) {
                    Log.v("----mac---", mac);
                }
            }

            @Override
            public void onSearchCanceled() {
            }
        });
    }

    private void ConnetDevice(String devicename) {
        mClient.stopSearch();
        progressDialog = ProgressDialog.show(HandeQR.this, "Connect", "Connect device...");
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  //
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();
        //  if (devicename == myDeviceName) {
        mClient.connect(arrayMAC.get(0), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    Log.d("connect", "---Connected successfully!---");
                    service = service4UUID;
                    character = charAUUID;
                    checkpass(arrayMAC.get(0));
                    progressDialog.dismiss();
                } else if (code == REQUEST_FAILED) {
                    progressDialog.dismiss();
                }
            }
        });
        //}
    }

    //看连接的设备是否有保存过的密码
    private void checkpass(String mac) {
        try {
            SharedPreferences sharepre = getSharedPreferences("datafile", Context.MODE_PRIVATE);
            String MacStr = sharepre.getString(mac, "");
            if (MacStr != "") {
                Intent intent = new Intent(HandeQR.this, MainActivity.class);
                // intent.putExtra("devicename",arrayList.get(i));
                intent.putExtra("mac", mac);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                // mClient = null;
                startActivity(intent);
            } else {
                Intent intent = new Intent(HandeQR.this, PassActivity.class);
                // intent.putExtra("devicename",arrayList.get(i));
                intent.putExtra("mac", mac);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                //    mClient = null;
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.v("data store errr", e.toString());
        }
    }

//    //对广播反应
//    @Override
//    public void onResponse(int code) {
//        if (code == REQUEST_SUCCESS) {
//            //进入密码输入框
//        }
//
//    }

    //判断用户是否开启定位
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}