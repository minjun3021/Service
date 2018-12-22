package test.kmj.com.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.util.Log;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MyService extends Service {
    Boolean exit=true;
    Boolean Created=false;
    BluetoothSPP bt=new BluetoothSPP(this);

    IBinder mBinder = new MyBinder();

    class MyBinder extends Binder {
        MyService getService() { // 서비스 객체를 리턴
            return MyService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("test", "서비스의 onBind");
        return mBinder;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        blueToothStart();



        Log.e("test", "서비스의 onCreate");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("test", "서비스의 onStartCommand");

        blueToothStart();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        bt.stopService();
        Log.e("test", "서비스의 onDestroy");
    }

    void blueToothStart(){
        if (!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                bt.autoConnect("HC-06");
            }
        }


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                //ㄷㅔ이터 받아질때 매번 실행
                Log.e("test", message+" "+bt.getConnectedDeviceName());
                Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);



            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener()

        {
            public void onDeviceConnected(String name, String address) {

                //연결됐을때
                Log.e("test", name+address);
                Log.e("test", "connect");

            }

            public void onDeviceDisconnected() {    //연결끊김
                Log.e("test", "consad");

            }

            public void onDeviceConnectionFailed() {
                //연결 실패했을때
                Log.e("test", "sad");
            }
        });
    }

}

