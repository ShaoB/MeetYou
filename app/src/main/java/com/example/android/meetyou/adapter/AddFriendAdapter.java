package com.example.android.meetyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.framework.helper.GlideHelper;
import com.example.android.meetyou.Bean.AddFriendModel;
import com.example.android.meetyou.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Founder: shaobin
 * Create Date: 2020/1/15
 * Profile:
 */
public class AddFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //标题
    public static final int TYPE_TITLE = 0;
    //内容
    public static final int TYPE_CONTENT = 1;

    private Context mcontext;
    private List<AddFriendModel> mlist;
    private LayoutInflater mInflater;

    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public AddFriendAdapter(Context mcontext, List<AddFriendModel> mlist) {
        this.mcontext = mcontext;
        this.mlist = mlist;
        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new TitleViewHolder(mInflater.inflate(R.layout.layout_search_title_item, null));
        } else if (viewType == TYPE_CONTENT) {
            return new ContentViewHolder(mInflater.inflate(R.layout.layout_search_user_item, null));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AddFriendModel friendModel = mlist.get(position);

        if (friendModel.getType() == TYPE_TITLE) {
            ((TitleViewHolder) holder).mTvtitle.setText(friendModel.getTitle());
        } else if (friendModel.getType() == TYPE_CONTENT) {
            GlideHelper.loadUrl(mcontext, friendModel.getPhoto(), ((ContentViewHolder) holder).iv_photo);
            ((ContentViewHolder) holder).iv_photo.setImageResource(friendModel.isSex() ? R.mipmap.img_boy_icon : R.mipmap.img_girl_icon);
            ((ContentViewHolder) holder).tv_nickname.setText(friendModel.getNickName());
            ((ContentViewHolder) holder).tv_age.setText(friendModel.getAge()+"岁");
            ((ContentViewHolder) holder).tv_desc.setText(friendModel.getDesc());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener != null){
                        onClickListener.setOnclickListener(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mlist.get(position).getType();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView mTvtitle;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvtitle = itemView.findViewById(R.id.tv_title);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_photo;
        private ImageView iv_sex;
        private TextView tv_nickname;
        private TextView tv_age;
        private TextView tv_desc;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_photo = itemView.findViewById(R.id.iv_photo);
            iv_sex = itemView.findViewById(R.id.iv_sex);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_desc = itemView.findViewById(R.id.tv_desc);
        }
    }

    public interface OnClickListener{
        void setOnclickListener(int postion);
    }
}
