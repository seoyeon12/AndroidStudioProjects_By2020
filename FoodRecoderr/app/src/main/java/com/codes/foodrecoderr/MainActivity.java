package com.codes.foodrecoderr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codes.foodrecoderr.data.FoodRecord;
import com.codes.foodrecoderr.data.FoodRecordDatabase;
import com.codes.foodrecoderr.data.FoodRecordOpenHelper;
import com.codes.foodrecoderr.databinding.ActivityMainBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    //private FoodRecordOpenHelper helper;
    private FoodRecordDatabase db;

    private View.OnClickListener onSave= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = preferences.edit();
            String food = binding.editText.getText().toString();
            if (!food.isEmpty()) {
                editor.putString("food", food);
                String now = LocalDateTime.now().toString();
                editor.putString("time", now);
                editor.apply();
                //helper.addRecord(new FoodRecord(food, now));
                save(new FoodRecord(food,now));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //helper = new FoodRecordOpenHelper(this, "db", null, 1);
        //ArrayList<FoodRecord> list = helper.getRecords();
        //for(FoodRecord r: list)
        //    Log.i("Main", r.getFood()+r.getTime());
        db = FoodRecordDatabase.getInstance(getApplicationContext());
        getList();

        preferences=getSharedPreferences("food", MODE_PRIVATE);
        String lastfood=preferences.getString("food",null);
        String lastTime=preferences.getString("time",null);
        displayRecord(lastfood, lastTime);
        binding.buttonRecord.setOnClickListener(onSave);

        binding.buttonShowAll.setOnClickListener(v->{
            startActivity(new Intent(this, RecordActivity.class));
        });
    }

    private void displayRecord(String lastFood, String saveTime) {
        if(lastFood==null){
            binding.textViewRecord.setText("저장된 기록이 없습니다.");
            binding.textViewElapsed.setText("경과 시간이 없습니다.");
        }else{
            LocalDateTime startTime = LocalDateTime.parse(saveTime);
            LocalDateTime endTime = LocalDateTime.now();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm", Locale.KOREA);
            String timeStr = startTime.format(formatter);
            String foodMessage = String.format("%s - %s",timeStr, lastFood);
            binding.textViewRecord.setText(foodMessage);

            long hours = ChronoUnit.HOURS.between(startTime, endTime);
            long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
            minutes -= hours*60;
            binding.textViewElapsed.setText(
                    String.format(Locale.KOREA,"%d시간 %02d분 경과", hours, minutes));
        }
    }
    private void save(FoodRecord record){
        new Thread(() -> db.foodRecordDAO().addRecord(record)).start();
    }
    private void getList(){
        new Thread(() -> {
            List<FoodRecord> result = db.foodRecordDAO().getRecords();
            for(FoodRecord e:result)
                Log.i("Main",e.time + e.food);
        }).start();
    }
}
