package com.codes.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codes.intent.databinding.ActivityConfirmBinding;
import com.codes.intent.databinding.ActivityMainBinding;

public class ConfirmActivity extends AppCompatActivity {

    private ActivityConfirmBinding binding;

    public void OnButton(View v){
        if(v.getId()==R.id.buttonOk){
            Intent intent = new Intent();
            intent.putExtra("message","User confirmed");
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setResult(Activity.RESULT_CANCELED);

        Intent intent=getIntent();
        String Name=intent.getStringExtra("name");
        String Phone=intent.getStringExtra("phone");
        String Class=intent.getStringExtra("class");

        if(Name != null) binding.textViewName.setText(Name);
        if(Phone != null) binding.textViewPhone.setText(Phone);
        if(Class != null) binding.textViewClass.setText(Class);
    }
}
