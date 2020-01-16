package com.example.android.framework.base;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Founder: shaobin
 * Create Date: 2020/1/16
 * Profile: RecyclerView 万能适配器
 */
public class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    private List<T> mList;

    private OnBindDataListener<T> onBindDataListener;
    private OnBindMoreDataListener<T> onBindMoreDataListener;

    public CommonAdapter(List<T> mList, OnBindDataListener<T> onBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onBindDataListener;
    }

    public CommonAdapter(List<T> mList, OnBindMoreDataListener<T> onBindMoreDataListener) {
        this.mList = mList;
        this.onBindDataListener = onBindMoreDataListener;
        this.onBindMoreDataListener = onBindMoreDataListener;
    }

    //绑定数据
    public interface OnBindDataListener<T> {
        void onBindViewHolder(T model, CommonViewHolder viewHolder, int type, int position);

        int getLayoutId(int type);
    }

    //绑定多类型数据
    public interface OnBindMoreDataListener<T> extends OnBindDataListener<T>{
        int getItemType(int position);
    }

    @Override
    public int getItemViewType(int position) {
        if(onBindMoreDataListener != null){
            return onBindMoreDataListener.getItemType(position);
        }
        return 0;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = onBindDataListener.getLayoutId(viewType);
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(parent, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        onBindDataListener.onBindViewHolder(mList.get(position),holder,getItemViewType(position),position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
