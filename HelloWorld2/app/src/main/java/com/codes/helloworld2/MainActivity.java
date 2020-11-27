package com.codes.helloworld2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textView;
    private EditText editTextNum1, editTextNum2;
    private Button buttonAdd, buttonSub, buttonMul, buttonDiv;
    //(*)세번째: 객체를 만들어 사용 + 람다식.ver
    /*private View.OnClickListener listener = (v) -> {
        float num1 = Float.parseFloat(editTextNum1.getText().toString());
        float num2 = Float.parseFloat(editTextNum2.getText().toString());
        textView.setText(String.valueOf(num1*num2));
    };
    //(*)세번째: 객체를 만들어 사용 + new 연산.ver
    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            float num1 = Float.parseFloat(editTextNum1.getText().toString());
            float num2 = Float.parseFloat(editTextNum2.getText().toString());
            textView.setText(String.valueOf(num1*num2));
         }
     };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("Simple Calculator!!!");

        editTextNum1 = findViewById(R.id.editTextNum1);
        editTextNum2 = findViewById(R.id.editTextNum2);

        //(+)첫번째 방법:자기 자신을 리스너로 등록
        /*buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this); */

        //(-)두번째:익명객체 사용 시, 전통적인 방법
        /*buttonSub = findViewById(R.id.buttonSub);
        //람다식.ver
        buttonSub.setOnClickListener((v)->{
            float num1 = Float.parseFloat(editTextNum1.getText().toString());
            float num2 = Float.parseFloat(editTextNum2.getText().toString());
            textView.setText(String.valueOf(num1-num2));
        });
        //new 연산.ver
        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num1 = Float.parseFloat(editTextNum1.getText().toString());
                float num2 = Float.parseFloat(editTextNum2.getText().toString());
                textView.setText(String.valueOf(num1-num2));
            }
        });*/

        //(*)세번째: 객체를 만들어 사용
        /*buttonMul = findViewById(R.id.buttonMul);
        buttonMul.setOnClickListener(listener);*/
    }

    @Override
    public void onClick(View v) {
        float num1=0, num2=0, id = v.getId();
        try {
            num1 = Float.parseFloat(editTextNum1.getText().toString());
            num2 = Float.parseFloat(editTextNum2.getText().toString());
        }catch (NumberFormatException e){
            textView.setText("Invalid Number");
            return;
        }
        if (id == R.id.buttonAdd){ num1 += num2; }
        else if(id == R.id.buttonSub){ num1 -= num2; }
        else if(id == R.id.buttonMul){ num1 *= num2; }
        else if(id == R.id.buttonDiv){
            if(num2 == 0){ textView.setText("Divided by Zero"); return;}
            else num1 /= num2;
        }
        textView.setText(String.valueOf(num1));
    }

    //(+)첫번째 방법:자기 자신을 리스너로 등록
    /*@Override
    public void onClick(View v) {
        float num1 = 0, num2= 0;
        try{
            num1 = Float.parseFloat(editTextNum1.getText().toString());
            num2 = Float.parseFloat(editTextNum2.getText().toString());
        }catch (NumberFormatException e){
            textView.setText("Invalid Number");
            return;
        }
        textView.setText(String.valueOf(num1+num2));
    }*/

    //(/)네번째:특정 함수를 만들어 클릭이벤트 처리
    /*public void onDivision(View v){
        float num1 = Float.parseFloat(editTextNum1.getText().toString());
        float num2 = Float.parseFloat(editTextNum2.getText().toString());
        if(num2==0) textView.setText("Divided by zero");
        else textView.setText(String.valueOf(num1/num2));
    }*/
}