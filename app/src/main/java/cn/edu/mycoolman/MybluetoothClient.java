package cn.edu.mycoolman;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;

import java.io.Serializable;

public class MybluetoothClient extends BluetoothClient implements Serializable {
    private static volatile MybluetoothClient instance;

    MybluetoothClient(Context context) {
        super(context);
    }

    public static MybluetoothClient getInstance(Context context) {
        if (instance == null) {
            synchronized (MybluetoothClient.class) {
                if (instance == null) {
                    instance = new MybluetoothClient(context);
                }
            }
        }
        return instance;
    }
}
