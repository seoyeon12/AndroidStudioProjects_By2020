package com.codes.busyrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.codes.busyrestaurant.databinding.ActivityMainBinding;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //핸들러에게 메세지 보내줄 때
    private static final int ORDER=1;
    private static final int COOKING=2;
    private static final int COOK=3;
    private static final int PACKING=4;
    private static final int PACK=5;
    //주문시간,포장시간,요리시간(1000ms=1s)
    private long orderDuration=1000;
    private long cookTime=1000;
    private long packTime=1000;
    //오던 번호, stop/keep flog
    private int orderCount = 0;
    private boolean isStarted = false;
    private boolean isFinish=false;
    //
    private ExecutorService service=null;
    private Handler handler;
    private Queue<String> orders=new LinkedList<>();
    private Queue<String> foods=new LinkedList<>();
    private Queue<String> packs=new LinkedList<>();

    private static final int COUNT=3;

    //Order Runnable
    private Runnable order=()-> {
        Log.i("main", "order started");
        while (!isFinish){
            orderCount++;
            String order = "order" + orderCount;
            orders.add(order);
            //핸들러에게 메세지 보낸다. 주문시작
            handler.sendMessage(Message.obtain(handler, ORDER, order));
            takeSleep(orderDuration);
        }
    };

    //Cook Runnable
    private Runnable cook=()->{
        Log.i("main", "cook started");
        while(!isFinish){
            if(!orders.isEmpty()){
                //큐 remove는 큐의 맨 앞의 값 가져온 후 삭제
                String order=orders.remove();
                //핸들러에게 메세지 보낸다. 주문리스트에서 꺼낼 때
                handler.sendMessage(Message.obtain(handler, COOKING, order));
                takeSleep(cookTime);
                foods.add(order);
                //핸들러에게 메세지 보낸다. 요리시작
                handler.sendMessage(Message.obtain(handler, COOK, order));
            } else {
                takeSleep(1000);
            }
        }
    };

    //Pack Runnable
    private Runnable pack=()->{
        Log.i("main", "pack started");
        while(!isFinish){
            if(!foods.isEmpty()){
                //큐 remove는 큐의 맨 앞의 값 가져온 후 삭제
                String f=foods.remove();
                handler.sendMessage(Message.obtain(handler, PACKING, f));
                takeSleep(packTime);
                packs.add(f);
                handler.sendMessage(Message.obtain(handler, PACK, f));
            } else {
                takeSleep(1000);
            }
        }
    };

    //Thread pool 초기화를 쉽게 하기 위해서
    private Runnable[] runnables={order, cook, pack};
    //시간 지연용 함수
    private void takeSleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //핸들러 (파라메터 => msg)
        handler=new Handler(msg ->{
            Queue<String> list=null;
            TextView view=null;

            //what,obj로 쓰레드에서 받은 파라메터를 찾을 수 있다.
            switch (msg.what){
                default:
                case ORDER:
                    list=orders;
                    view=binding.textViewOrderList;
                    break;
                case COOK:
                    binding.textViewCooking.setText("");
                    list=foods;
                    view=binding.textViewFoodList;
                    break;
                case PACK:
                    binding.textViewPacking.setText("");
                    list=packs;
                    view=binding.textViewPackList;
                    break;
                case COOKING:
                    String str="요리중\n"+msg.obj;
                    binding.textViewCooking.setText(str);
                    break;
                case PACKING: str="포장중\n"+msg.obj;
                    binding.textViewPacking.setText(str);
                    break;
            }

            if (view!=null && list!=null) {
                view.setText("");
                for (String s : list)
                    view.append(s + "\n");
            }
            return false;
        });

        binding.buttonStart.setOnClickListener(v->{
            start();
        });
    }

    private void start(){
        if(!isStarted) {//켜는 코드
            isStarted=true;
            isFinish=false;
            //ui초기화
            binding.textViewOrderList.setText("");
            binding.textViewFoodList.setText("");
            binding.textViewPackList.setText("");
            binding.buttonStart.setText("Stop");

            //시간(사용자의 경우 s단위, 사용되는 단위는 ms) 그리고 소수점 제거
            orderDuration=Math.round(Double.parseDouble(binding.editTextOrder.getText().toString()) * 1000);
            cookTime=Math.round(Double.parseDouble(binding.editTextCook.getText().toString()) * 1000);
            packTime=Math.round(Double.parseDouble(binding.editTextPack.getText().toString()) * 1000);

            //정해진 갯수의 쓰레드 풀을 만든다. 각각의 쓰레드에게 러너블주면서 실행
            service = Executors.newFixedThreadPool(COUNT);
            for (int i = 0; i < COUNT; i++)
                service.execute(runnables[i]);
        }else {
            //끄는 코드, 쓰레드 클리어
            isStarted=false;
            isFinish=true;
            orders.clear();
            foods.clear();
            packs.clear();
            orderCount=0;
            binding.buttonStart.setText("Start");
            if(service!=null) service.shutdownNow(); //쓰레드 셧다운(쓰레드 종료)
        }
    }
}
