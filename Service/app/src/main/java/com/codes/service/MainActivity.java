package com.codes.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.SeekBar;

import com.codes.service.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
                            implements SeekBar.OnSeekBarChangeListener {

    private ActivityMainBinding binding;
    private PlayService service;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibinder) {
            PlayService.PlayBinder binder=(PlayService.PlayBinder)ibinder;
            service=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IntentFilter filter=new IntentFilter("com.codes.service.Play");
        registerReceiver(receiver, filter);

        binding.seekBar.setOnSeekBarChangeListener(this);
        binding.seekBar.setEnabled(false);

        binding.buttonPlay.setOnClickListener(v->{
            //Intent intent = new Intent(this, PlayService.class);
            //startService(intent);
            if(service!=null)
                service.requestPlay();
        });
        binding.buttonStop.setOnClickListener(v->{
            //Intent intent = new Intent(this,PlayService.class);
            //stopService(intent);
            if(service!=null)
                service.requestStop();
        });

        Intent intent=new Intent(this, PlayService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(connection);
    }

    BroadcastReceiver receiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String status = intent.getStringExtra("status");
                if(status != null){
                    if(status.equals("play")){
                        int duration = intent.getIntExtra("duration", 0);
                        int current = intent.getIntExtra("current", 0);
                        binding.seekBar.setEnabled(true);
                        binding.seekBar.setMax(duration);
                        binding.seekBar.setProgress(current);
                    } else if(status.equals("stop")){
                        binding.seekBar.setProgress(0);
                        binding.seekBar.setEnabled(false);
                    }
                }
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //사용자가 건드려서 바뀌었는가?(fromUser)
        if(fromUser){
            //Intent intent=new Intent("com.codes.service.Request");
            //intent.putExtra("progress",progress);
            //sendBroadcast(intent);
            if(service!=null)
                service.seek(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { } //터치를 하는 순간

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { } //터치를 떼는 순간
}
