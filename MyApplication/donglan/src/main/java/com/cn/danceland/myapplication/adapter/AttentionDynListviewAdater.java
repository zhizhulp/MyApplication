package com.cn.danceland.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.DynHomeActivity;
import com.cn.danceland.myapplication.activity.UserHomeActivity;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequsetDynInfoBean;
import com.cn.danceland.myapplication.bean.RequsetSimpleBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.IntEvent;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.pictureviewer.ImagePagerActivity;
import com.cn.danceland.myapplication.pictureviewer.PictureConfig;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DensityUtils;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.NoScrollGridView;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static com.cn.danceland.myapplication.R.id.iv_comment;
import static com.cn.danceland.myapplication.R.id.iv_transpond;
import static com.cn.danceland.myapplication.R.id.iv_zan;
import static com.cn.danceland.myapplication.R.id.tv_guanzhu;

/**
 * Created by shy on 2017/10/24 17:40
 * Email:644563767@qq.com
 */


public class AttentionDynListviewAdater extends BaseAdapter {
    public List<RequsetDynInfoBean.Data.Content> data = new ArrayList<RequsetDynInfoBean.Data.Content>();
    private LayoutInflater mInflater;
    private Context context;
    boolean isMe = false;

    public AttentionDynListviewAdater(Context context, ArrayList<RequsetDynInfoBean.Data.Content> data) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }


    public void addFirst(RequsetDynInfoBean.Data.Content bean) {
        data.add(0, bean);
    }

    public void addFirstList(ArrayList<RequsetDynInfoBean.Data.Content> bean) {
        data.addAll(0, bean);
    }

    public void addLast(RequsetDynInfoBean.Data.Content bean) {
        data.add(bean);
    }

    public void addLastList(ArrayList<RequsetDynInfoBean.Data.Content> bean) {
        data.addAll(bean);
    }


    public void setData(ArrayList<RequsetDynInfoBean.Data.Content> bean) {

        data = bean;
    }

    /**
     *
     */
    public void setGzType(boolean isMe) {
        this.isMe = isMe;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // LogUtil.i(data.size() + "");
//        if (data.size() == 0) {
//            return 1;
//        } else {
        return data.size();
        //    }


    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    private AnimationSet mAnimationSet;

    private void buildAnima(final int pos) {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(500);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
        mAlphaAnimation.setDuration(500);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(500);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                data.get(pos).setAnimationFlag(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        LogUtil.i("data.size()"+data.size()+"+++++++++++");
//        LogUtil.i(data.toString());
        //如果没有数据


        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_dynamic, null);
            viewHolder.tv_nick_name = (TextView) convertView.findViewById(R.id.tv_nick_name);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_guanzhu = (TextView) convertView.findViewById(tv_guanzhu);
            viewHolder.tv_location = convertView.findViewById(R.id.tv_location);
            viewHolder.ll_location = convertView.findViewById(R.id.ll_location);
            viewHolder.tv_content = convertView.findViewById(R.id.tv_content);
            viewHolder.tv_zan_num = convertView.findViewById(R.id.tv_zan_num);
            viewHolder.iv_avatar = convertView.findViewById(R.id.iv_avatar);
            viewHolder.iv_zan = convertView.findViewById(iv_zan);
            viewHolder.iv_comment = convertView.findViewById(iv_comment);
            viewHolder.iv_transpond = convertView.findViewById(iv_transpond);
            viewHolder.gridView = convertView.findViewById(R.id.gridview);
            viewHolder.jzVideoPlayer = convertView.findViewById(R.id.videoplayer);
            viewHolder.ll_item = convertView.findViewById(R.id.ll_item);
            viewHolder.ll_zan = convertView.findViewById(R.id.ll_zan);
            viewHolder.tv_pinglun = convertView.findViewById(R.id.tv_pinglun);
            //  viewHolder.tv_no_data = convertView.findViewById(R.id.tv_no_data);
            viewHolder.rl_more = convertView.findViewById(R.id.rl_more);
            viewHolder.iv_pic = convertView.findViewById(R.id.iv_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (data == null) {
            return convertView;
        }

//        if (data.size() == 0) {
//            viewHolder.ll_item.setVisibility(View.GONE);
//            viewHolder.tv_no_data.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.ll_item.setVisibility(View.VISIBLE);
//            viewHolder.tv_no_data.setVisibility(View.GONE);
//        }

        // LogUtil.i(data.size() + "");
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//点击整个页面
                // ToastUtils.showToastShort(position + "");
                context.startActivity(new Intent(context, DynHomeActivity.class).putExtra("msgId", data.get(position).getId()).putExtra("userId", data.get(position).getAuthor()));

            }
        });
        //设置评论数量
        viewHolder.tv_pinglun.setText(data.get(position).getReplyNumber() + "");
        //设置点赞数量
        viewHolder.tv_zan_num.setText(data.get(position).getPriaseNumber() + "");

        if (data.get(position).isPraise()) {//设置点赞
            viewHolder.iv_zan.setImageResource(R.drawable.img_xin1);
        } else {
            viewHolder.iv_zan.setImageResource(R.drawable.img_xin);
        }

        if (data.get(position).isAnimationFlag()) {
            buildAnima(position);
            viewHolder.iv_zan.clearAnimation();
            viewHolder.iv_zan.setAnimation(mAnimationSet);
        }

        viewHolder.ll_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点赞

                data.get(position).setAnimationFlag(true);

                if (data.get(position).isPraise()) {//已点赞

                    int pos = position;
                    try {
                        addZan(data.get(position).getId(), false, pos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {//未点赞
                    int pos = position;
                    try {
                        addZan(data.get(position).getId(), true, pos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                notifyDataSetChanged();

            }
        });





        viewHolder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     ToastUtils.showToastShort("评论");
                context.startActivity(new Intent(context, DynHomeActivity.class).putExtra("msgId", data.get(position).getId()).putExtra("userId", data.get(position).getAuthor()).putExtra("from",1));
                EventBus.getDefault().post(new IntEvent(100,8902));
            }
        });
        viewHolder.iv_transpond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToastShort("转发");
            }
        });

        viewHolder.rl_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = position;
                //判断是否是本人的动态
                if (TextUtils.equals(data.get(position).getAuthor(), SPUtils.getString(Constants.MY_USERID, null))) {

                    showListDialogSelf(pos);
                } else {
                    showListDialog(pos);
                }

            }
        });

        if (isMe) {//是否是个人页面
            viewHolder.tv_guanzhu.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tv_guanzhu.setVisibility(View.VISIBLE);
        }


        if (data.get(position).isFollower()) {
            viewHolder.tv_guanzhu.setText("已关注");
            viewHolder.tv_guanzhu.setTextColor(Color.GRAY);
        } else {
            viewHolder.tv_guanzhu.setText("+关注");
            viewHolder.tv_guanzhu.setTextColor(Color.BLACK);
        }

        if (TextUtils.equals(data.get(position).getAuthor(), SPUtils.getString(Constants.MY_USERID, null))) {

            viewHolder.tv_guanzhu.setText("");

        }

        viewHolder.tv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.get(position).isFollower()) {//未关注添加关注
                    int pos = position;
                    addGuanzhu(data.get(position).getAuthor(), true, pos);
                }


            }
        });


        //  LogUtil.i(data.get(position).getNickName());
     //   if (!TextUtils.isEmpty(data.get(position).getNickName())) {
            viewHolder.tv_nick_name.setText(data.get(position).getNickName());
       // }


        //   viewHolder.tv_time.setText(data.get(position).getPublishTime());
        viewHolder.tv_time.setText(TimeUtils.timeLogic(data.get(position).getPublishTime()));

        if (TextUtils.isEmpty(data.get(position).getContent())) {
            viewHolder.tv_content.setVisibility(View.GONE);
        } else {//内容不为空赋值
            viewHolder.tv_content.setText(data.get(position).getContent());
            viewHolder.tv_content.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(data.get(position).getPublishPlace())) {
            viewHolder.ll_location.setVisibility(View.GONE);
        } else {//地点不为空赋值
            viewHolder.ll_location.setVisibility(View.VISIBLE);
            viewHolder.tv_location.setText(data.get(position).getPublishPlace());
        }


        RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);
        Glide.with(context)
                .load(data.get(position).getSelfUrl())
                .apply(options)
                .into(viewHolder.iv_avatar);
        viewHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          //      context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getAuthor()));
                context.startActivity(new Intent(context, UserHomeActivity.class).putExtra("id", data.get(position).getAuthor()).putExtra("from",1));
                EventBus.getDefault().post(new IntEvent(100,8902));
            }
        });

        if (data.get(position).getVedioUrl() != null && data.get(position).getMsgType() == 1) {//如果是视频消息
            viewHolder.jzVideoPlayer.setVisibility(View.VISIBLE);

            HttpProxyCacheServer proxy = MyApplication.getProxy(context);//增加视频缓存
            String proxyUrl = proxy.getProxyUrl(data.get(position).getVedioUrl());

            viewHolder.jzVideoPlayer.setUp(
                    proxyUrl, JZVideoPlayer.SCREEN_WINDOW_LIST,
                    "");
//            viewHolder.jzVideoPlayer.setUp(
//                    data.get(position).getVedioUrl(), JZVideoPlayer.SCREEN_WINDOW_LIST,
//                    "");


            Glide.with(convertView.getContext())
                    .load(data.get(position).getVedioImg())
                    .into(viewHolder.jzVideoPlayer.thumbImageView);
            //  viewHolder.jzVideoPlayer.loop  = true;//是否循环播放
            viewHolder.jzVideoPlayer.positionInList = position ;
         //   LogUtil.i(position + "");

//            viewHolder.jzVideoPlayer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    viewHolder.jzVideoPlayer .startFullscreen(context, JZVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "嫂子辛苦了");
//                    //直接进入全屏
//                }
//            });

        } else {
            viewHolder.jzVideoPlayer.setVisibility(View.GONE);
        }

        if (data.get(position).getImgList() != null && data.get(position).getImgList().size() > 0) {

            viewHolder.gridView.setVisibility(View.VISIBLE);


            if (data.get(position).getImgList().size() == 1) {
                viewHolder.gridView.setVisibility(View.GONE);
                //  int height = DensityUtils.dp2px(context,100f);//此处的高度需要动态计算
                //   int width = DensityUtils.dp2px(context,100f);//此处的宽度需要动态计算
                RequestOptions options1 = new RequestOptions()
                        //  .placeholder(R.drawable.img_loading)//加载占位图
                        .error(R.drawable.img_loadfail)//
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .priority(Priority.HIGH);

                viewHolder.iv_pic.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder(data.get(position).getImgList().get(0));
                sb.insert(data.get(position).getImgList().get(0).length() - 4, "_400X400");
                String[] b = sb.toString().split("_");
                String[] c = b[1].toString().toString().split("X");

                LogUtil.i(b[1].toString());

                LogUtil.i(c[0]);
                LogUtil.i(c[1]);
                LogUtil.i(sb.toString());
                if (Float.parseFloat(c[0]) >= Float.parseFloat(c[1])) {
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(context, 200f), DensityUtils.dp2px(context, 200f * Float.parseFloat(c[1]) / Float.parseFloat(c[0])));
                    linearParams.setMargins(DensityUtils.dp2px(context, 15f),  DensityUtils.dp2px(context, 5f), 0, 0);
                    viewHolder.iv_pic.setLayoutParams(linearParams);
                } else {
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(context, 200f * Float.parseFloat(c[0]) / Float.parseFloat(c[1])), DensityUtils.dp2px(context, 200f));
                    linearParams.setMargins(DensityUtils.dp2px(context, 15f),  DensityUtils.dp2px(context, 5f), 0, 0);
                    viewHolder.iv_pic.setLayoutParams(linearParams);
                }

                Glide.with(context)
                        .load(sb.toString())
                        // .apply(options1)
                        .into(viewHolder.iv_pic);
                viewHolder.iv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PictureConfig config = new PictureConfig.Builder()
                                .setListData((ArrayList<String>) data.get(position).getImgList())//图片数据List<String> list
                                .setPosition(0)//图片下标（从第position张图片开始浏览）
                                .setDownloadPath("DCIM")//图片下载文件夹地址
                                .setIsShowNumber(false)//是否显示数字下标
                                .needDownload(true)//是否支持图片下载
                                .setPlacrHolder(R.drawable.img_loading)//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                                .build();
                        ImagePagerActivity.startActivity(context, config);
                    }
                });
                LinearLayout.LayoutParams linearParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams1.setMargins(DensityUtils.dp2px(context, 15f),  DensityUtils.dp2px(context, 5f), 0, 0);
                viewHolder.gridView.setLayoutParams(linearParams1); //使设置好的布局参数应用到控件
//


            }
            else   if (data.get(position).getImgList().size()==4){
                //  int height = DensityUtils.dp2px(context,100f);//此处的高度需要动态计算
                viewHolder.gridView.setNumColumns(2);
                int width = DensityUtils.dp2px(context,205f);//此处的宽度需要动态计算
                LinearLayout.LayoutParams linearParams =new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams.setMargins( DensityUtils.dp2px(context,15f), DensityUtils.dp2px(context, 5f),0,0);
                viewHolder.gridView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

            }else {
                viewHolder.gridView.setNumColumns(3);
                int width = DensityUtils.dp2px(context,310f);//此处的宽度需要动态计算
                LinearLayout.LayoutParams linearParams =new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams.setMargins( DensityUtils.dp2px(context,15f), DensityUtils.dp2px(context, 5f),0,0);
                viewHolder.gridView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            }

            viewHolder.gridView.setAdapter(new ImageGridAdapter(context, data.get(position).getImgList()));
            /**
             * 图片列表点击事件
             */
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


//                Intent intent = new Intent(context, ImagePagerActivity.class);
//                //intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Parcelable) list);
//                intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) list);
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
//                context.startActivity(intent);

                    PictureConfig config = new PictureConfig.Builder()
                            .setListData((ArrayList<String>) data.get(position).getImgList())//图片数据List<String> list
                            .setPosition(i)//图片下标（从第position张图片开始浏览）
                            .setDownloadPath("DCIM")//图片下载文件夹地址
                            .setIsShowNumber(true)//是否显示数字下标
                            .needDownload(true)//是否支持图片下载
                            .setPlacrHolder(R.drawable.img_loading)//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                            .build();
                    ImagePagerActivity.startActivity(context, config);


                }
            });
        } else {
            viewHolder.gridView.setVisibility(View.GONE);
            viewHolder.iv_pic.setVisibility(View.GONE);

        }


        return convertView;


    }

    class ViewHolder {
        TextView tv_nick_name;
        TextView tv_time;
        hani.momanii.supernova_emoji_library.Helper.EmojiconTextView tv_content;
        TextView tv_location;//地点
        TextView tv_zan_num;//点赞数量
        TextView tv_guanzhu;
        //  TextView tv_no_data;
        ImageView iv_avatar;
        ImageView iv_zan;//点赞
        ImageView iv_transpond;//转发
        ImageView iv_comment;//评论
        LinearLayout ll_location;
        NoScrollGridView gridView;
        JZVideoPlayerStandard jzVideoPlayer;
        LinearLayout ll_item;
        TextView tv_pinglun;//评论数
        RelativeLayout rl_more;//更多
        LinearLayout ll_zan;
        ImageView iv_pic;
    }

    private void showListDialog(final int pos) {
        final String[] items = {"举报"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(context);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        ToastUtils.showToastShort("已举报");
                        break;
                    case 1:

                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showListDialogSelf(final int pos) {
        final String[] items = {"删除动态"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(context);
        //listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        showConfirmDialog(pos);

                        break;
                    case 1:

                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    /**
     * 确认对话
     */
    private void showConfirmDialog(final int pos) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(context);
        //   dialog.setTitle("提示");
        dialog.setMessage("是否删除该动态");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                delDyn(data.get(pos).getId(), pos);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    class StrBean {
        public boolean is_praise;
        public String msg_id;

    }

    /**
     * 点赞
     *
     * @param isPraise
     * @param isPraise
     */
    private void addZan(final String msgId, final boolean isPraise, final int pos) throws JSONException {

        StrBean strBean = new StrBean();
        strBean.is_praise = isPraise;
        strBean.msg_id = msgId;
        Gson gson=new Gson();
        JSONObject jsonObject=new JSONObject(gson.toJson(strBean).toString());
        LogUtil.i(gson.toJson(strBean).toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.HOST + "appPraise/giveThumbs",jsonObject , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                LogUtil.i(json.toString());
                Gson gson = new Gson();
                RequsetSimpleBean requestInfoBean = new RequsetSimpleBean();
                requestInfoBean = gson.fromJson(json.toString(), RequsetSimpleBean.class);

                if (requestInfoBean.isSuccess()) {

                    if (data.get(pos).isPraise()) {//如果已点赞
                        data.get(pos).setPraise(false);
                        int i = data.get(pos).getPriaseNumber() - 1;
                        data.get(pos).setPriaseNumber(i);
                        ToastUtils.showToastShort("取消点赞成功");
                    } else {
                        data.get(pos).setPraise(true);
                        int i = data.get(pos).getPriaseNumber() + 1;
                        data.get(pos).setPriaseNumber(i);
                        ToastUtils.showToastShort("点赞成功");
                    }


                    notifyDataSetChanged();

                } else {
                    ToastUtils.showToastShort("点赞失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showToastShort("请检查手机网络！");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }
        };

        request.setTag("addzan");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    class StrBean1 {
        public boolean is_follower;
        public String user_id;

    }



    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b, final int pos) {

        StrBean1 strBean1=new StrBean1();
        strBean1.is_follower=b;
        strBean1.user_id=id;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.ADD_GUANZHU,new Gson().toJson(strBean1), new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(jsonObject.toString(), RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
//                    data.get(pos).setFollower(true);
//                    notifyDataSetChanged();

                    ToastUtils.showToastShort("关注成功");
                    EventBus.getDefault().post(new StringEvent(data.get(pos).getAuthor(), EventConstants.ADD_GUANZHU));

                } else {
                    ToastUtils.showToastShort("关注失败");
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请查看网络连接");
            }

        }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    /**
     * 删除动态
     *
     * @param msgId//动态id
     */
    private void delDyn(final String msgId, final int pos) {

        String Params = Constants.DEL_DYN_MSG + "/" + msgId;


                final StringRequest request = new StringRequest(Request.Method.DELETE, Params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        LogUtil.i(s);

                        Gson gson = new Gson();
                        RequsetSimpleBean requestInfoBean = new RequsetSimpleBean();
                        requestInfoBean = gson.fromJson(s, RequsetSimpleBean.class);
                        if (requestInfoBean.isSuccess()) {
                            ToastUtils.showToastShort("删除成功");
                            // data.remove(pos);
                            //   notifyDataSetChanged();
                            EventBus.getDefault().post(new StringEvent("", EventConstants.DEL_DYN));
                            EventBus.getDefault().post(new IntEvent(pos, EventConstants.DEL_DYN));
                        } else {
                            ToastUtils.showToastShort("删除失败");
                        }
//
//                Gson gson = new Gson();
//                 requestInfoBean = new RequestInfoBean();
//                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
//                if (requestInfoBean.getSuccess()) {
//                    ToastUtils.showToastShort("删除成功");
//                    // data.remove(pos);
//                    //   notifyDataSetChanged();
//                    EventBus.getDefault().post(new StringEvent("", EventConstants.DEL_DYN));
//                    EventBus.getDefault().post(new IntEvent(pos, EventConstants.DEL_DYN));
//                } else {
//                    ToastUtils.showToastShort("删除失败：" + requestInfoBean.getErrorMsg());
//                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                LogUtil.i(volleyError.toString());
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hm = new HashMap<String, String>();
                String token = SPUtils.getString(Constants.MY_TOKEN, "");
                hm.put("Authorization", token);
                return hm;
            }

        };
        MyApplication.getHttpQueues().add(request);
    }
}
