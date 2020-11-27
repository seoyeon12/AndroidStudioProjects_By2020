package com.codes.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codes.widget.databinding.ActivityConfirmBinding;

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
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        String userClass=intent.getStringExtra("class");
        boolean marketing = intent.getBooleanExtra("marketing",false);

        if(name != null) binding.textViewName.setText(name);
        if(phone != null) binding.textViewPhone.setText(phone);
        if(userClass != null) binding.textViewClass.setText(userClass);
        Log.i( "ConfirmActivity", "marketing = "+marketing);

        binding.buttonSMS.setOnClickListener(v->{
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto:010-8285-4511"));
            if(smsIntent.resolveActivity(getPackageManager()) != null){
                startActivity(smsIntent);
            }
        });
    }
}
