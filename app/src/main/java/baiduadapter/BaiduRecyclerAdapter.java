package baiduadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import baidubean.NewsInformaitions;
import utils.Contants;

/**
 * Created by jrm on 2017-5-3.
 * 信息流的适配器
 */

public class BaiduRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<NewsInformaitions> mInfos;
    private LayoutInflater mInfalter;
    private int count;
    /***
     * 监听item点击事件。
     */
    private LYJItemClickListener mItemClickListener;
    /***
     * 监听点击事件接口
     */
    public interface LYJItemClickListener {
        public void onItemClick(View view, int postion);
    }
    /***
     * 设置item点击事件
     * @param listener
     */
    public void setOnItemClickListener(LYJItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public BaiduRecyclerAdapter(Context context,List<NewsInformaitions> infos){
        this.mContext = context ;
        this.mInfos = infos;
        mInfalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()) { //最后一条为FooterView
            return Contants.VIEW_TYPE_FOOTER;
        }
        return Contants.VIEW_TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == Contants.VIEW_TYPE_FOOTER) { //代表到达底部
            return onCreateFooterViewHolder(parent, viewType);
        } else if(viewType == Contants.VIEW_TYPE_ITEM) { //代表正常的Item数据
            return onCreateItemViewHolder(parent, viewType);
        }
        return null;
    }

    /**
     * item绑定数据
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        return null;
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private LYJItemClickListener mListener;//设置点击事件
        public ItemViewHolder(View itemView, LYJItemClickListener listener) {
            super(itemView);

            this.mListener = listener;
            itemView.setOnClickListener(this);//设置点击事件
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
    /**
     * 底部的foot布局
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);

        }
    }

    public int getDatas(){
        return count;
    }
    public void setDatas( int totalCount){
        this.count = totalCount;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return mInfos.size()+1;
    }
}
