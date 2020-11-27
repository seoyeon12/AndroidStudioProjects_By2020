package com.codes.mycafe.data;

public class CafeMenu {
    public String menuId;
    public String name;
    public int price;
    public String info;
    public String photo;
    public int count;
    public CafeMenu(String id, String name, int price, String info, String photo) {
        this.menuId = id;
        this.name = name;
        this.price = price;
        this.info = info;
        this.photo = photo;
        this.count = 0;
    }
}
