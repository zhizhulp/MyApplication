package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.bca.bcaanalysis.BcaAnalysis;
import com.cn.danceland.myapplication.bean.bca.bcaanalysis.BcaAnalysisRequest;
import com.cn.danceland.myapplication.bean.bca.bcaoption.BcaOption;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestion;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionCond;
import com.cn.danceland.myapplication.bean.bca.bcaquestion.BcaQuestionRequest;
import com.cn.danceland.myapplication.bean.bca.bcaresult.BcaResult;
import com.cn.danceland.myapplication.utils.CustomGridView;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/4/12.
 */

public class BodyTiXingActivity extends Activity {


    DongLanTitleView rl_bodybase_title;
    ListView lv_bodybase;
    View footView;
    private BcaQuestionRequest request;
    private Gson gson;
    List<BcaQuestion> list;
    BodyBaseAdapter bodyBaseAdapter;
    CustomGridView gv_bodybase;
    BodyBaseGridAdapter bodyBaseGridAdapter;
    Button body_button;
    List<BcaResult> resultList;
    EditText editText;
    Long que_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodybase);
        initHost();
        initView();
        queryList();
    }

    private void initHost() {

        request = new BcaQuestionRequest();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        list = new ArrayList<>();
        resultList = (List<BcaResult>)getIntent().getSerializableExtra("resultList");
        if(resultList==null){
            resultList = new ArrayList<>();
        }
    }

    private void initView() {

        rl_bodybase_title = findViewById(R.id.rl_bodybase_title);
        rl_bodybase_title.setTitle("体型体态分析");
        lv_bodybase = findViewById(R.id.lv_bodybase);
        footView = View.inflate(BodyTiXingActivity.this, R.layout.commit_button, null);

        bodyBaseAdapter = new BodyBaseAdapter();
        lv_bodybase.setAdapter(bodyBaseAdapter);



        body_button = footView.findViewById(R.id.body_button);
        body_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText!=null){
                    BcaResult bcaResult = new BcaResult();
                    bcaResult.setQuestion_id(que_id);
                    bcaResult.setContent(editText.getText().toString());
                    bcaResult.setOpt_id((Long)editText.getTag());
                    resultList.add(bcaResult);
                }
                deleteEqualsItem();
                startActivity(new Intent(BodyTiXingActivity.this,BodyZongHeActivity.class).putExtra("resultList",(Serializable) resultList));
            }
        });

    }


    /**
     * @方法说明:按条件查询问题题干列表
     **/
    public void queryList() {
        BcaQuestionCond cond = new BcaQuestionCond();
        cond.setType(Byte.valueOf("5"));
        request.queryList(cond, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<List<BcaQuestion>> result = gson.fromJson(json.toString(), new TypeToken<DLResult<List<BcaQuestion>>>() {
                }.getType());
                list.clear();
                if (result.isSuccess()) {
                    list = result.getData();
                    bodyBaseAdapter.notifyDataSetChanged();
                    lv_bodybase.addFooterView(footView);
                } else {
                    ToastUtils.showToastShort("查询分页列表失败,请检查手机网络！");
                }
            }
        });
    }

    private class BodyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {
            View view = View.inflate(BodyTiXingActivity.this, R.layout.bodybase_item, null);
            LinearLayout ll_ed_parent = view.findViewById(R.id.ll_ed_parent);


            gv_bodybase = view.findViewById(R.id.gv_bodybase);

            TextView tv_tigan = view.findViewById(R.id.tv_tigan);
            tv_tigan.setText(list.get(pos).getOrder_no()+". "+list.get(pos).getCentent());

            List<BcaOption> options = list.get(pos).getOptions();
            List<BcaOption> options1 = new ArrayList<>();
            List<String> editList = new ArrayList<>();
            ArrayList<Long> options2 = new ArrayList<>();
            if(options!=null){
                for(int i = 0;i<options.size();i++){
                    String type = options.get(i).getType().toString();
                    if("1".equals(type)){
                        options1.add(options.get(i));
                    }
                    if("2".equals(type)){
                        editList.add(options.get(i).getTitle());
                        options2.add(options.get(i).getId());
                    }
                }
            }
            for(int n  = 0;n<editList.size();n++){
                final EditText editText = new EditText(BodyTiXingActivity.this);
                editText.setBackgroundResource(R.drawable.rect_body);
                editText.setHint(editList.get(n));
                editText.setHintTextColor(Color.parseColor("#dcdcdc"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(0,15,0,0);
                editText.setLayoutParams(lp);
                editText.setMaxLines(1);
                editText.setTag(options2.get(n));
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus){
                            BcaResult bcaResult = new BcaResult();
                            bcaResult.setQuestion_id(list.get(pos).getId());
                            bcaResult.setContent(editText.getText().toString());
                            bcaResult.setOpt_id((Long)editText.getTag());
                            resultList.add(bcaResult);
                            //deleteEqualsItem();
                        }else{
                            setEditText(editText,list.get(pos).getId());
                        }
                    }
                });
                ll_ed_parent.addView(editText);
            }


            bodyBaseGridAdapter = new BodyBaseGridAdapter(options1,list.get(pos).getId());
            gv_bodybase.setAdapter(bodyBaseGridAdapter);

            return view;
        }
    }

    private void setEditText(EditText editText,Long id){
        this.editText = editText;
        this.que_id = id;
    }

    private void deleteEqualsItem(){
        for(int i = 0;i<resultList.size();i++){
            for(int j = i+1;j<resultList.size();j++){
                if(resultList.get(i).equals(resultList.get(j))){
                    resultList.remove(i);
                    i--;
                    break;
                }
            }
        }
    }


    private class BodyBaseGridAdapter extends BaseAdapter{


        Long que_id;

        List<BcaOption> options;

        BodyBaseGridAdapter(List<BcaOption> options,Long que_id){
            this.options = options;
            this.que_id = que_id;
        }

        @Override
        public int getCount() {
            return options.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = View.inflate(BodyTiXingActivity.this, R.layout.bodybase_grid_item, null);
            final CheckBox rb_grid = view.findViewById(R.id.rb_grid);
            rb_grid.setText(options.get(position).getTitle());
            rb_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rb_grid.isChecked()){
                        BcaResult bcaResult = new BcaResult();
                        bcaResult.setOpt_id(Long.valueOf(position+""));
                        bcaResult.setQuestion_id(Long.valueOf(que_id+""));
                        resultList.add(bcaResult);
                        //deleteEqualsItem();
                    }
                }
            });


            return view;
        }

    }
}