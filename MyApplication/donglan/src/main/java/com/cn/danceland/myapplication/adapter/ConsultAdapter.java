package com.cn.danceland.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.ConsultBean;
import com.cn.danceland.myapplication.utils.TimeUtils;

import java.util.List;

/**
 * 咨询列表adapter
 * Created by ${yxx} on 2018/8/30.
 */

public class ConsultAdapter extends BaseAdapter {

    private List<ConsultBean> datas;
    private Context context;

    public ConsultAdapter(Context context, List<ConsultBean> datas) {
        super();
        this.datas = datas;
        this.context = context;
    }

    public void setData(List<ConsultBean> datas) {
        this.datas = datas;
    }

    public void clear() {
        datas.clear();//先清除这个
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_consult, null);
            viewHolder = new ViewHolder();
            viewHolder.consult_reply_tv = view.findViewById(R.id.consult_reply_tv);
            viewHolder.first_reply_tv =  view.findViewById(R.id.first_reply_tv);
            viewHolder.btn_consult_state =  view.findViewById(R.id.btn_consult_state);
            viewHolder.consult_icon_image =  view.findViewById(R.id.consult_icon_image);
            viewHolder.consult_title_tv =  view.findViewById(R.id.consult_title_tv);
            viewHolder.consult_accept_tv =  view.findViewById(R.id.consult_accept_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.consult_reply_tv.setText(TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(datas.get(i).getCreate_time(), "yyyy-MM-dd HH:mm:ss").toString(), "yyyy.MM.dd"));
        if(datas.get(i).getAccept_time()!=null){
            viewHolder.consult_accept_tv.setText(TimeUtils.timeStamp2Date(TimeUtils.date2TimeStamp(datas.get(i).getAccept_time(), "yyyy-MM-dd HH:mm:ss").toString(), "yyyy.MM.dd"));
        }
//        viewHolder.first_reply_tv.setText(new SimpleDateFormat("HH:mm").format(new Date(datas.get(i).getEndTime())).toString());//暂时没有此处
        switch (datas.get(i).getType()) {
            case "1"://加盟
                viewHolder.consult_title_tv.setText("开店托管");
                viewHolder.consult_icon_image.setImageDrawable(context.getResources().getDrawable(R.drawable.img_join));
                break;
            case "2"://培训
                viewHolder.consult_title_tv.setText("培训教练");
                viewHolder.consult_icon_image.setImageDrawable(context.getResources().getDrawable(R.drawable.img_train));
                break;
            case "3"://购买
                viewHolder.consult_title_tv.setText("购买软件");
                viewHolder.consult_icon_image.setImageDrawable(context.getResources().getDrawable(R.drawable.img_buy));
                break;
        }
        switch (datas.get(i).getStatus()) {
            case "0"://0=咨询中
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.consult_state_in_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.adcise_status_red_bg));
                break;
            case "1"://1 = 完成
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.consult_state_succeed_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.adcise_status_white_bg));
                break;
            case "2"://2 = 失败
                viewHolder.btn_consult_state.setText(context.getResources().getString(R.string.consult_state_fails_text));
                viewHolder.btn_consult_state.setBackground(context.getResources().getDrawable(R.drawable.adcise_status_gary_bg));
                break;
        }
        return view;
    }

    static class ViewHolder {
        TextView consult_reply_tv;
        TextView first_reply_tv;
        TextView consult_title_tv;
        TextView consult_accept_tv;
        Button btn_consult_state;
        ImageView consult_icon_image;
    }
}
