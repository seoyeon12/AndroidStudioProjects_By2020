package com.codes.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class PlayService extends Service {

    private IBinder binder = new PlayBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class PlayBinder extends Binder {
        PlayService getService() { return PlayService.this; }
    }

    public void requestPlay(){
        play();
    }
    public void requestStop(){
        stop();
    }
    public void seek(int progress){
        if(player != null){
            player.seekTo(progress);
            player.start();
        }
    }

    private MediaPlayer player;

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                int progress=intent.getIntExtra("progress",-1);
                if(progress != -1 && player != null){
                    player.seekTo(progress);
                    player.start();
                }
            }
        }
    };

    private void stop(){
        if(player!=null) {
            player.stop();
            player.release();
            player = null;
        }
        Intent intent = new Intent("com.codes.service.Play");
        intent.putExtra("status", "stop");
        sendBroadcast(intent);
    }
    private void play(){
        if(player != null && player.isPlaying()){ //노래가 나오고 있는데 재생버튼을 또 누를때
            player.seekTo(0);
            return;
        }

        if(player==null)
            player=MediaPlayer.create(this, R.raw.akak);
        player.start(); //노래 재생코드 이밑으로는 1초에 한번씩 브로드캐스트 전송
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(player!=null && player.isPlaying()) {
                    Intent intent = new Intent("com.codes.service.Play");
                    intent.putExtra("status", "play");
                    intent.putExtra("duration", player.getDuration());
                    intent.putExtra("current", player.getCurrentPosition());
                    sendBroadcast(intent);
                }else{
                    timer.cancel();
                }
            }
        }, 0,1000);//,지금즉시, 1초(1000ms)에 한번씩
    }

    public PlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service","onCreate");
        IntentFilter filter=new IntentFilter("com.codes.service.Request");
        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service","onStartCommand");
        play();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service","onDestroy");
        unregisterReceiver(receiver);
        stop();
    }
}
