package com.codes.virtualrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codes.virtualrestaurant.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWidgets();
    }

    private void initWidgets(){
        binding.eventBtn.setOnClickListener(this);
        binding.locationBtn.setOnClickListener(this);
        binding.menuBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menuBtn:
                //Toast.makeText(MainActivity.this, "메뉴 클릭하였습니다.", Toast.LENGTH_LONG).show();
                binding.menus.setVisibility(View.VISIBLE);
                break;
            case R.id.locationBtn:
                //Toast.makeText(MainActivity.this, "위치 클릭하였습니다.", Toast.LENGTH_LONG).show();
                binding.locate.setVisibility(View.VISIBLE);
                break;
            case R.id.eventBtn:
                //Toast.makeText(MainActivity.this, "이벤트 클릭하였습니다.", Toast.LENGTH_LONG).show();
                binding.events.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(MainActivity.this, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                break;
        }
    }
    private  void allGone(){
        binding.menus.setVisibility(View.GONE);
        binding.locate.setVisibility(View.GONE);
        binding.events.setVisibility(View.GONE);
    }
}
