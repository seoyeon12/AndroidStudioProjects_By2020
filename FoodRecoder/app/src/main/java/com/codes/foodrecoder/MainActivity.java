package com.codes.foodrecoder;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.codes.foodrecoder.databinding.ActivityMainBinding;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences preferences;

    private View.OnClickListener onSave=v->{
        SharedPreferences.Editor editor=preferences.edit();
        String food = binding.editText.getText().toString();
        if(!food.isEmpty()){
            editor.putString("food",food);
            String now = LocalDateTime.now().toString();
            editor.putString("time", now);
            editor.apply();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences=getSharedPreferences("food", MODE_PRIVATE);
        String lastfood=preferences.getString("food",null);
        String lastTime=preferences.getString("time",null);
        displayRecord(lastfood, lastTime);
        binding.buttonRecord.setOnClickListener(onSave);
    }

    private void displayRecord(String lastFood, String savedTime){
        if(lastFood==null){
            binding.textViewRecord.setText("저장된 기록이 없습니다.");
            binding.textViewElapsed.setText("경과 시간이 없습니다.");
        }else{
            LocalDateTime startTime = LocalDateTime.parse(savedTime);
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

    /*private void displayRecord(String lastfood, String lastTime){
        binding.textViewElapsed.setText(lastfood);
        binding.textViewRecord.setText(lastTime);
    }

    private void onSave(){
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDate.format(mDate);
        String getfood = binding.editText.getText().toString();

        SharedPreferences pref = getSharedPreferences("food",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("food",getfood);
        editor.putString("time",getTime);
        displayRecord(getfood,getTime);
        editor.apply();
    }*/

    /*@Override
    public void onClick(View v) {
        onSave();
    }*/
}
