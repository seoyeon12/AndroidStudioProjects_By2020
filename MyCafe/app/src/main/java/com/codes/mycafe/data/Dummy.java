package com.codes.mycafe.data;

import java.util.ArrayList;
import java.util.List;

public class Dummy {
    private static Dummy instance = new Dummy();
    private List<CafeMenu> menus;

    private Dummy(){
        menus = new ArrayList<>();
        CafeMenu m1 = new CafeMenu("c1","아메리카노",2500,"핫/아이스 둘다 가능","");
        CafeMenu m2 = new CafeMenu("c2","바닐라라떼",4500,"초심자를 위한 커피","");
        CafeMenu m3 = new CafeMenu("c3","아이스티",3000,"찜질방가면 먹을 수 있는 갓갓음료","");
        CafeMenu m4 = new CafeMenu("c4","카페모카",3500,"쌉싸름한 커피","");
        CafeMenu m5 = new CafeMenu("c5","애플민트티",3000,"핫/아이스 둘다 가능","");

        menus.add(m1);
        menus.add(m2);
        menus.add(m3);
        menus.add(m4);
        menus.add(m5);
    }
    public static Dummy getInstance(){ return instance; }

    public List<CafeMenu> getMenus(){ return menus; }
}
