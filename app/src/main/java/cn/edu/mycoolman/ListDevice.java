package cn.edu.mycoolman;

import static com.inuker.bluetooth.library.Code.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import cn.edu.mycoolman.MybluetoothClient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListDevice extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayMAC;
    private BluetoothClient mClient;
    private ArrayAdapter<String> adapter;
    private Button btDiscover;
    private String  myDeviceName;

    private String MAC;
    private UUID service;
    private UUID character;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_device);
        setContentView(R.layout.activity_list_device);
        ListView lstv = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        arrayMAC = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrayList);
        //lstv.setAdapter(adapter);
        boolean locationEnable = isLocationEnabled();
        if (!locationEnable) {
            Toast.makeText(ListDevice.this, "Please turn on location", Toast.LENGTH_SHORT).show();
        }

        // 检测PHONE_STATE 如果已授权
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //做你想做的
            Log.v("hhhhhhh","okokokokokookok");
        }else{
            String[] permission = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};
            ActivityCompat.requestPermissions(this,permission, 1);
        }

        MybluetoothClient mClient = MybluetoothClient.getInstance(getApplicationContext());

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
                if ((!arrayList.contains(device.getName()) && (device.getName().startsWith("CCP15R") || device.getName().startsWith("CCP20R")))) {
                    arrayList.add(device.getName());
                    arrayMAC.add(device.getAddress());
                    lstv.setAdapter(adapter);
                    //保存信息
                    try {
                        SharedPreferences sharedPref = ListDevice.this.getSharedPreferences(getString(R.string.filekey), Context.MODE_PRIVATE);
                        String fileString = getResources().getString(R.string.filekey);
                        String MACString = sharedPref.getString(getString(R.string.MACkey), fileString);
                        UUID service = UUID.fromString(sharedPref.getString(getString(R.string.serviceKey), fileString));
                        UUID character = UUID.fromString(sharedPref.getString(getString(R.string.characterKey), fileString));
                        if(MACString.equals(device.getAddress())){
                            mClient.stopSearch();
                            progressDialog = ProgressDialog.show(ListDevice.this,"Connect","Connect device...");
                            BleConnectOptions options = new BleConnectOptions.Builder()
                                    .setConnectRetry(3)   // 连接如果失败重试3次
                                    .setConnectTimeout(30000)   // 连接超时30s
                                    .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                                    .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                                    .build();
                            mClient.connect(MACString,options, new BleConnectResponse() {
                                @Override
                                public void onResponse(int code, BleGattProfile data) {
                                    Intent intent = new Intent(ListDevice.this,MainActivity.class);
                                    // intent.putExtra("devicename",arrayList.get(i));
                                    intent.putExtra("mac",MACString);
                                    intent.putExtra("service",service);
                                    intent.putExtra("character",character);
                                    startActivity(intent);
                                }
                            });
                        }
                    }catch (Exception ex){
                        Log.v("file read error",ex.toString());
                    }
                }
            }

            @Override
            public void onSearchStopped() {
                Log.v("over", "-------scanstopped");
                for (String name :
                        arrayList) {
                    Log.v("----name---", name);
                }

                for (String mac :
                        arrayMAC) {
                    Log.v("----mac---", mac);
                }
            }

            @Override
            public void onSearchCanceled() {
            }
        });


        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mClient.stopSearch();
                progressDialog = ProgressDialog.show(ListDevice.this,"Connect","Connect device...");
                BleConnectOptions options = new BleConnectOptions.Builder()
                        .setConnectRetry(3)   // 连接如果失败重试3次
                        .setConnectTimeout(30000)   // 连接超时30s
                        .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                        .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                        .build();

                myDeviceName =  arrayList.get(i);
                if (myDeviceName.startsWith("CCP15R") || myDeviceName.startsWith("CC20RP")) {
                    mClient.connect(arrayMAC.get(i), options, new BleConnectResponse() {
                        @Override
                        public void onResponse(int code, BleGattProfile profile) {
                            if (code == REQUEST_SUCCESS) {
                                Log.d("connect", "---Connected successfully!---");
                                List<BleGattService> listServices = profile.getServices();
                                if (listServices.size() > 0) {
                                    service = listServices.get(2).getUUID();
                                    List<BleGattCharacter> listCharacters = listServices.get(2).getCharacters();
                                    if (listCharacters.size() > 0) {
                                        character = listCharacters.get(0).getUuid();
                                        //updateStatus();
                                        progressDialog.dismiss();
                                        //保存信息
                                        SharedPreferences sharedPref = ListDevice.this.getSharedPreferences(getString(R.string.filekey),Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(getString(R.string.MACkey), arrayMAC.get(i));
                                        editor.putString(getString(R.string.serviceKey), service.toString());
                                        editor.putString(getString(R.string.characterKey), character.toString());
                                        editor.apply();

                                        Intent intent = new Intent(ListDevice.this,MainActivity.class);
                                        // intent.putExtra("devicename",arrayList.get(i));
                                        intent.putExtra("mac",arrayMAC.get(i));
                                        intent.putExtra("service",service);
                                        intent.putExtra("character",character);
                                        startActivity(intent);
                                    }
                                }
                                progressDialog.dismiss();
                            }else if(code == REQUEST_FAILED){
                                progressDialog.dismiss();
                            }
                        }
                    });
                }}
        });

        btDiscover = (Button) findViewById(R.id.discover);
        btDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                mClient.search(request, new SearchResponse() {
                    @Override
                    public void onSearchStarted() {
                        progressDialog = ProgressDialog.show(ListDevice.this,"Search","descover devices...");
                    }

                    @Override
                    public void onDeviceFounded(SearchResult device) {
                        Beacon beacon = new Beacon(device.scanRecord);
                        if((!arrayList.contains(device.getName()) && (device.getName().startsWith("GCA") || device.getName().startsWith("CCA")))){
                            arrayList.add(device.getName());
                            arrayMAC.add(device.getAddress());
                            lstv.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onSearchStopped() {
                        progressDialog.dismiss();;
                    }

                    @Override
                    public void onSearchCanceled() {

                    }
                });
            }
        });
    }

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
