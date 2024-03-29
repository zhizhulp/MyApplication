package com.cn.danceland.myapplication.im.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.im.model.Conversation;
import com.cn.danceland.myapplication.im.model.ProfileSummary;
import com.cn.danceland.myapplication.im.ui.GroupMemberActivity;
import com.cn.danceland.myapplication.im.utils.TimeUtil;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private List<Conversation> objects ;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) view.findViewById(R.id.last_message);
            viewHolder.time = (TextView) view.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) view.findViewById(R.id.unread_num);
            viewHolder.item_layout_cv = view.findViewById(R.id.item_layout_cv);
            view.setTag(viewHolder);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(),80f));
        if (position == 0) {
            layoutParams.setMargins(DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 11f));
        } else if (position == objects.size() - 1) {
            layoutParams.setMargins(DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 11f), DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 16f));
        } else {
            layoutParams.setMargins(DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 11f), DensityUtils.dp2px(getContext(), 16f), DensityUtils.dp2px(getContext(), 11f));
        }
        viewHolder.item_layout_cv.setLayoutParams(layoutParams);

        final Conversation data = getItem(position);
        viewHolder.tvName.setText(data.getName());
       // viewHolder.avatar.setImageResource(data.getAvatar());
        LogUtil.i(data.getAvatarUrl());
        if(data.getAvatarUrl()!=null){
            if(data.getAvatarUrl().equals("")){
                viewHolder.avatar.setImageResource(data.getAvatar());
            }else{
             //   Picasso.with(getContext()).load(data.getAvatarUrl()).into(viewHolder.avatar);
                Glide.with(getContext()).load(data.getAvatarUrl()).into(viewHolder.avatar);
            }
        }else {
            viewHolder.avatar.setImageResource(data.getAvatar());
        }

        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0){
            viewHolder.unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10){
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point1));
            }else{
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point2));
                if (unRead > 99){
                    unReadStr = getContext().getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder{
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;
        public CardView item_layout_cv;

    }
}
