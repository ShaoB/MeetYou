package com.example.android.framework.eventbus;

/**
 * Created by shishaobin on 2019/8/28
 */
public class UpdateTextEventbus {
    public String name;

    public UpdateTextEventbus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
