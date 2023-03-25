package cn.edu.mycoolman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Thread mThread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);//使程序休眠3秒
                    Intent intent=new Intent(getApplicationContext(),ListDevice.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }
}