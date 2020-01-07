package com.example.android.meetyou;

import android.os.Bundle;
import com.example.android.framework.base.BaseUIActivity;
import com.example.android.framework.manager.MediaPlayManager;
import com.example.android.framework.utils.LogUtils;

public class MainActivity extends BaseUIActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MediaPlayManager playManager = new MediaPlayManager();
        playManager.startPlay(getResources().openRawResourceFd(R.raw.word));

        playManager.setOnProgressListener(new MediaPlayManager.OnMusicProgressListener() {
            @Override
            public void OnProgress(int currentPosition, int pos) {
                LogUtils.e(currentPosition+"");
                LogUtils.e(pos+"%");
            }
        });
    }
}
