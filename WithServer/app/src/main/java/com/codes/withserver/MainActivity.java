package com.codes.withserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codes.withserver.data.User;
import com.codes.withserver.databinding.ActivityMainBinding;
import com.codes.withserver.retrofit.Server;
import com.codes.withserver.volley.SingleQueue;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Call<User> request;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //1.AsyncTask 방법
        //new SingleRequestTask().execute("1");

        //2.volly 방법 (큐안에 request를 넣는다.) - 중앙집중식
        /*
        String url="https://47ad15a9-5eac-473d-95d6-49db671d603c.mock.pstmn.io/users/100";
        JsonObjectRequest request=new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response->{ //응답 성공(json을 자바 객체로 안 바꿔준다...직접 구현해야됨 gson으로 한 것처럼)
                    Log.i("Main-Volley", response.toString());
                },
                error->{} //응답 실패(에러)
        );
        //위에서 request 만들어서 밑에 줄은 큐에 request 를 넣는 작업
        //(이후 작업들은 volly 라이브러리가 알아서..)
        request.setTag("getUser"); // tag 설정 후 닫기? close 작업
        SingleQueue.getInstance(this).getRequestQueue().add(request);
        */

        //3.retrofit 방법 (requset가 스스로 큐에 들어간다.) - 각각 알아서 하는 식
        //Call<User>이랑 Callback<User>, User로 데이터 포맷을 맞춰준다.
        request = Server.getInstance().getAPI().getUser("1000");
        //enqueue => 서버로 보내세요
        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //Gson이 필요없다. body를 부르면 user 타입으로 톡 튀어나온다.
                if(response.code()==200) { //정상작동이라면
                    User user = response.body();
                    if(user != null){ //user가 null이 아니라면
                        binding.userid.setText(user.userid);
                        binding.userName.setText(user.name);
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    class SingleRequestTask extends AsyncTask<String, Void, User> {
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Log.i("Main", user.name);
        }

        @Override
        protected User doInBackground(String... strings) {
            String urlString = "https://47ad15a9-5eac-473d-95d6-49db671d603c.mock.pstmn.io/users/" + strings[0];
            String result;
            User user = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //GET 이여서 Input~~. 만약, POST 라면 output~~까지 해야한다.
                InputStream is = conn.getInputStream();
                StringBuilder builder = new StringBuilder();
                //InputStreamReader => 한 글자씩 읽어오기 를 BufferedReader로 감싸면 한줄로 읽는다.
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                result = builder.toString();
                Log.i("Main", result);
                //json string을 gson으로 user객체로 바꾸어 준다.
                Gson gson = new Gson();
                user = gson.fromJson(result, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }
    }
}
