package com.example.android.framework;

public class Framework {
    private volatile static  Framework mframework;

    private Framework() {

    }

    public static Framework getFramework(){
        if(mframework == null){
            synchronized (Framework.class){
                if(mframework == null){
                    mframework = new Framework();
                }
            }
        }
        return mframework;
    }
}
