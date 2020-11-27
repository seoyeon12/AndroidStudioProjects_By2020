package com.codes.intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codes.intent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWidget();
    }

    private void initWidget(){
        binding.buttonApply.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==Activity.RESULT_OK){
                if(data!=null) {
                    String message = data.getStringExtra("message");
                    if(message != null) Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonApply) {
            Intent intent = new Intent(this,ConfirmActivity.class);
            intent.putExtra("name",binding.editTextName.getText().toString());
            intent.putExtra("phone",binding.editTextPhone.getText().toString());
            String userClass = "Adult";
            if(binding.radioButtonStudent.isChecked()) userClass="Student";
            intent.putExtra("class",userClass);
            startActivityForResult(intent,1);
        }else {
            updateProgress();
        }
    }

    private void updateProgress() {
    }
}
