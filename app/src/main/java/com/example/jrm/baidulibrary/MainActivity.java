package com.example.jrm.baidulibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import utils.ToastUtils;
import widget.AppBarStateChangeListener;
import widget.BaiduSearchView;
import widget.HorizontalListView;
import widget.RecyclerViewLayout;
import widget.ScrollAwareFABBehavior;

public class MainActivity extends Activity {

    private BaiduSearchView baiDuSearch;
    private AppBarLayout app_bar_layout;
    private FloatingActionButton fad_btn;
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
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private boolean mIsVisiable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_activity);
        if (mNavis!=null || mNavis.size() != 0){
            mNavis.clear();
        }
        initView();
        getToolBarData();
        getNewsInformations();
    }

    private void getNewsInformations() {
        Map<String, String> infosPamarms = ConvertParamsUtils.getInstatnce().getParamsTwo("type", "all", "p", String.valueOf(page));
        RetrofitService.getInstance().githubApi.createInfomationsTwo(infosPamarms).enqueue(new Callback<NewsInformaitions>() {
            @Override
            public void onResponse(Call<NewsInformaitions> call, Response<NewsInformaitions> response) {
                if (response.raw().body() != null){
                    NewsInformaitions newsInfoMation  = (NewsInformaitions)response.body();
                    int count = newsInfoMation.getCount();
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
        SharePresUtils.clearChannleKeyValue(MainActivity.this);
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
                        SharePresUtils.setChannleKeyValue(MainActivity.this,channel_key);
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
    private int getMarginBottom(View v) {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    public void barAnimalOut(HorizontalListView baidu_categroy){
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(baidu_categroy).translationY(-(baidu_categroy.getHeight() + getMarginBottom(baidu_categroy))).alpha(0).setDuration(500).setInterpolator(INTERPOLATOR).withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        public void onAnimationStart(View view) {

                        }
                        public void onAnimationCancel(View view) {

                        }
                        public void onAnimationEnd(View view) {
                            view.setVisibility(View.GONE);
                        }
                    }).start();
        } else {
            baidu_categroy.setVisibility(View.GONE);
        }
    }

    private void barAnimalIn(HorizontalListView baidu_categroy) {
        baidu_categroy.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(baidu_categroy).translationY(0)
                    .setInterpolator(INTERPOLATOR).alpha(1).setDuration(500).withLayer().setListener(null)
                    .start();
        } else {
            baidu_categroy.setVisibility(View.VISIBLE);
        }
    }

    private int mPosition ;
    private AppBarStateChangeListener.State mCurrentState = AppBarStateChangeListener.State.IDLE;
    private void initView() {
        //baidu_img =(ImageView)findViewById(R.id.baidu_img);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        //自定义的百度搜索框
        app_bar_layout = (AppBarLayout)findViewById(R.id.app_bar_layout);
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
                Intent intent = new Intent(MainActivity.this,WebHtmlActivity.class);
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
        recyclerView.setChangeRefreshListener(new RecyclerViewLayout.changeRefreshListener() {
            @Override
            public void isVisableToolBar(int state) {
                int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getRecyclerView().getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                mPosition = firstCompletelyVisibleItemPosition;
                if (state == 1 || state == 2){
                    if (firstCompletelyVisibleItemPosition != 0){
                        mIsVisiable = false;
                        if (mCurrentState == AppBarStateChangeListener.State.COLLAPSED){
                            mIsVisiable = true;
                        }
                    }else {
                        mIsVisiable= true;
                         if (mCurrentState == AppBarStateChangeListener.State.IDLE || mCurrentState == AppBarStateChangeListener.State.EXPANDED){
                            mIsVisiable= false;
                        }
                    }
                    Log.i("Tagv",state+"/"+mIsVisiable+"/"+mPosition+"/"+mCurrentState);
                }
            }
        });
        app_bar_layout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED){ //--->当触发折叠的时候
                    Log.i("Tagv",state+"/"+mIsVisiable+"/"+mPosition);
                    barAnimalOut(baidu_categroy);
                    mCurrentState = State.COLLAPSED;
                }else if (state == State.EXPANDED){  //-->当触发扩展
                    Log.i("Tagv",state+"/"+mPosition+"/"+mIsVisiable);
                    //  barAnimalIn(baidu_categroy);
                    mCurrentState = State.EXPANDED;
                }else if (state == State.IDLE){  //-->当处于中间状态
                    Log.i("Tagv",state+"/"+mIsVisiable+"/"+mPosition);
                    mCurrentState = State.IDLE;
                    if (mIsVisiable){
                        collapsingToolbarLayout.setVisibility(View.GONE);
                        mIsVisiable = false;
                    }else {
                        collapsingToolbarLayout.setVisibility(View.VISIBLE);
                        mIsVisiable = true;
                    }
                }
            }
        });
        //横向滑动的导航栏
        baidu_categroy = (HorizontalListView)findViewById(R.id.baidu_categroy);
        mAdapter = new HorizontalListViewAdapter(MainActivity.this,titles,ids);
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
        //快回回归顶部
        fad_btn = (FloatingActionButton)findViewById(R.id.fad_btn);
        fad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page == 1){
                    fad_btn.setVisibility(View.GONE);
                }else {
                   recyclerView.getRecyclerView().smoothScrollToPosition(0);
                    fad_btn.setVisibility(View.GONE);
                }
            }
        });
    }

    public void goUrl(String paramString){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paramString));
        startActivity(intent);
    }

    private boolean isAPPExit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (mCurrentState == AppBarStateChangeListener.State.COLLAPSED||mCurrentState == AppBarStateChangeListener.State.EXPANDED){
                Log.i("tagv",mCurrentState+"");
                recyclerView.getRecyclerView().smoothScrollToPosition(0);
                collapsingToolbarLayout.setVisibility(View.VISIBLE);
              //  mCurrentState = AppBarStateChangeListener.State.EXPANDED;
                barAnimalIn(baidu_categroy);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!isAPPExit){
            isAPPExit = true;
            ToastUtils.getInstance().setToastTitle(MainActivity.this,"再次点击，退出浏览器");
            mHandler.sendEmptyMessageDelayed(0x11,2000);
        }else {
            super.onBackPressed();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    isAPPExit = false;
                    break;
                default:
                    break;
            }
        };
    };
}
