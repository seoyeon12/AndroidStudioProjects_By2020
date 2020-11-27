package com.codes.widget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.codes.widget.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWidgets();
    }

    private void initWidgets(){
        binding.editTextName.addTextChangedListener(this);
        binding.editTextPhone.addTextChangedListener(this);
        binding.radioButtonAdult.setOnClickListener(this);
        binding.radioButtonStudent.setOnClickListener(this);
        binding.checkBoxTerms.setOnCheckedChangeListener(this);
        binding.buttonApply.setOnClickListener(this);
    }

    //name과 phone 리스너
    @Override
    public void afterTextChanged(Editable s) {
        Log.i("Main", "after=" + s.toString());
        updateProgress();
    }

    //radio btn 리스너
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonApply) {
            Intent intent = new Intent(this,ConfirmActivity.class);
            intent.putExtra("name",binding.editTextName.getText().toString());
            intent.putExtra("phone",binding.editTextPhone.getText().toString());
            String userClass = "Adult";
            if(binding.radioButtonStudent.isChecked()) userClass="Student";
            intent.putExtra("class",userClass);
            intent.putExtra("marketing",binding.checkBoxMarketing.isChecked());
            startActivityForResult(intent,1);
        }else {
            updateProgress();
        }
    }

    //이용약관 리스너
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateProgress();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    private void updateProgress() {
        int progress = 0;
        if(binding.editTextName.getText().length() > 0) progress ++;
        if(binding.editTextPhone.getText().length() > 0) progress ++;
        if(binding.radioButtonAdult.isChecked() || binding.radioButtonStudent.isChecked()) progress ++;
        if(binding.checkBoxTerms.isChecked()) progress ++;

        binding.progressBar.setProgress(progress);
        if(progress == 4) binding.buttonApply.setVisibility(View.VISIBLE);
        else binding.buttonApply.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                if(data!=null) {
                    String message = data.getStringExtra("message");
                    if(message != null) Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
