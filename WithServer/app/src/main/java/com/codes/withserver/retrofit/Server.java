package com.codes.withserver.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
    private static Server instance;
    private ServerAPI api;

    private Server(){
        String url="https://47ad15a9-5eac-473d-95d6-49db671d603c.mock.pstmn.io";
        //서버 하나당 인터페이스 하나 (velly의 경우 몇개가 있던 큐에 넣기만 하면 됨)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api= retrofit.create(ServerAPI.class);
    }
    public static Server getInstance(){
        if(instance==null) instance=new Server();
        return instance;
    }
    public ServerAPI getAPI(){ return api; }
}
