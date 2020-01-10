package com.example.android.meetyou.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Founder: shaobin
 * Create Date: 2020/1/10
 * Profile:
 */
public class Person extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
