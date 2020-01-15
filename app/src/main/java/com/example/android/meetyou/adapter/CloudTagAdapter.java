package com.example.android.meetyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.meetyou.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

/**
 * FileName: CloudTagAdapter
 * Founder: LiuGuiLin
 * Profile: 3D星球适配器
 */
public class CloudTagAdapter extends TagsAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;

    public CloudTagAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_star_view_item, null);
        TextView tv_name = view.findViewById(R.id.tv_star_name);
        ImageView iv_star = view.findViewById(R.id.iv_star_icon);
        tv_name.setText(mList.get(position));
        iv_star.setImageResource(R.mipmap.img_star_card_1);
        return view;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }

    class ViewHolder {
        private ImageView iv_star_icon;
        private TextView tv_star_name;
    }
}
