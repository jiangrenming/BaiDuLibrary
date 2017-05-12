package com.example.jrm.baidulibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import baiduadapter.HorizontalListViewAdapter;
import baidubean.InformationsResult;
import baidubean.Navis;
import baidubean.NavisBean;
import baidubean.NewsInformaitions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2_callback.BaseCallModel;
import retrofit2_callback.MyCallBack;
import retrofit2service.RetrofitService;
import utils.ConvertParamsUtils;
import utils.SharePresUtils;
import widget.BaiduSearchView;
import widget.HorizontalListView;
import widget.RecyclerViewLayout;

/**
 * Created by jrm on 2017-5-9.
 */

public class NewMainActivity extends Activity{


    private BaiduSearchView baiDuSearch;
    //导航相关
    private HorizontalListView baidu_categroy;
    private HorizontalListViewAdapter mAdapter;
    private String[] titles = {"网址", "新闻", "小说", "影视", "图片"};
    final int[] ids = {R.mipmap.web,R.mipmap.news,R.mipmap.book,R.mipmap.video,R.mipmap.imag};
    private List<NavisBean> mNavis = new ArrayList<>();
    //信息流相关
    private int page = 1;
    private RecyclerViewLayout recyclerView;
    private int flag =10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mNavis!=null || mNavis.size() != 0){
            mNavis.clear();
        }
        initView();
        getToolBarData();
        getNewsInformations();
    }

    private void initView() {

        baiDuSearch =(BaiduSearchView)findViewById(R.id.serach_view);
        recyclerView =(RecyclerViewLayout)findViewById(R.id.recyclerView);
        /**
         * item点击事件
         */
        recyclerView.setItemClick(new RecyclerViewLayout.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                InformationsResult informationsResult = recyclerView.getAdapter().data.get(position);
                String news_url = informationsResult.getNews_url();
                Intent intent = new Intent(NewMainActivity.this,WebHtmlActivity.class);
                intent.putExtra("html",news_url);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
            }
        });

        /**
         * 动态改变字体的颜色
         */
        recyclerView.setChangeTextBgListener(new RecyclerViewLayout.onChangeTextBgListener() {
            @Override
            public void onChangeTextColor(View view) {
            }
        });
        recyclerView.setRefresh(new RecyclerViewLayout.OnRecylerRefresh() {
            @Override
            public void onRefresh() {  //--->刷新
                flag = 10;
                page++;
                getNewsInformations();
            }

            @Override
            public void onLoadMore() {  //--->加载更多
                flag = 11;
                page++;
                getNewsInformations();
            }
        });
        //横向滑动的导航栏
        baidu_categroy = (HorizontalListView)findViewById(R.id.baidu_categroy);
        mAdapter = new HorizontalListViewAdapter(NewMainActivity.this,titles,ids);
        baidu_categroy.setAdapter(mAdapter);
        baidu_categroy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelectIndex(position);
                mAdapter.notifyDataSetChanged();
                if (mNavis == null || mNavis.size() == 0){  //--->跳转默认的路径
                    switch (position){
                        case 0:  //网址
                            goUrl("https://www.baidu.com/");
                            break;
                        case  1:  //新闻
                            goUrl("http://news.baidu.com/");
                            break;
                        case  2:  //小说
                            goUrl("https://wenku.baidu.com/");
                            break;
                        case 3:  //影视
                            goUrl("http://v.baidu.com");
                            break;
                        case 4: //图片
                            goUrl("http://image.baidu.com");
                            break;
                        default: //默认
                            goUrl("http://www.baidu.com");
                            break;
                    }
                }else if (mNavis.size() < 5){  //-->跳转默认路径+网络返回路径
                    if (position < mNavis.size()){
                        String url = mNavis.get(position).getUrl();
                        goUrl(url);
                    }else {
                        switch (position - mNavis.size()){
                            case 0:  //网址
                                goUrl("https://www.baidu.com/");
                                break;
                            case  1:  //新闻
                                goUrl("http://news.baidu.com/");
                                break;
                            case  2:  //小说
                                goUrl("https://wenku.baidu.com/");
                                break;
                            case 3:  //影视
                                goUrl("http://v.baidu.com");
                                break;
                        }
                    }
                }else {  //--->跳转网络路径
                    String url = mNavis.get(position).getUrl();
                    goUrl(url);
                }
            }
        });
    }
    private void getNewsInformations() {
        Map<String, String> infosPamarms = ConvertParamsUtils.getInstatnce().getParamsTwo("type", "all", "p", String.valueOf(page));
        RetrofitService.getInstance().githubApi.createInfomationsTwo(infosPamarms).enqueue(new Callback<NewsInformaitions>() {
            @Override
            public void onResponse(Call<NewsInformaitions> call, Response<NewsInformaitions> response) {
                Log.i("Taga",response.raw().body().toString());
                if (response.raw().body() != null){
                    NewsInformaitions newsInfoMation  = (NewsInformaitions)response.body();
                    int count = newsInfoMation.getCount();
                    Log.i("Taga",count+"");
                    ArrayList<InformationsResult> data = newsInfoMation.getData();
                    if (data != null && data.size() !=0){
                        recyclerView.setTotalCount(count);
                        if (flag == 10){
                            recyclerView.endRefresh();
                            recyclerView.setData(data);
                        }else {
                            recyclerView.endLoadMore();
                            recyclerView.addData(data);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<NewsInformaitions> call, Throwable t) {
                Log.i("Taga---", t.getMessage());
            }
        });
    }
    private void getToolBarData() {
        SharePresUtils.clearChannleKeyValue(NewMainActivity.this);
        Map<String, String> paramsOne = ConvertParamsUtils.getInstatnce().getParamsOne("", "");
        RetrofitService.getInstance().githubApi.createNaviData(paramsOne).enqueue(new MyCallBack<BaseCallModel<Navis>>() {
            @Override
            protected void ertryConnection() {}
            @Override
            public void onSucess(Response<BaseCallModel<Navis>> response) {
                if (response != null){
                    Navis navis = response.body().getData();
                    if (navis != null){
                        String channel_key = navis.getChannel_key();
                        SharePresUtils.setChannleKeyValue(NewMainActivity.this,channel_key);
                        List<NavisBean> navi = navis.getNav();
                        if (navi != null ){
                            Collections.sort(navi,new Comparator<NavisBean>(){
                                @Override
                                public int compare(NavisBean o1, NavisBean o2) {
                                    if (o1.getSort()  <  o2.getSort()){
                                        return  1;
                                    }else if (o1.getSort()  ==  o2.getSort()){
                                        return 0;
                                    }else {
                                        return  -1;
                                    }
                                }
                            });
                            Collections.reverse(navi);
                            mNavis.addAll(navi);
                            if (navi.size() < 5){
                                mAdapter.setNavisAndDefaultData(navi,titles,ids);
                            }else {
                                mAdapter.setNavisData(mNavis);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(String message) {
                try {
                    System.out.print(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void goUrl(String paramString){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paramString));
        startActivity(intent);
    }
}
