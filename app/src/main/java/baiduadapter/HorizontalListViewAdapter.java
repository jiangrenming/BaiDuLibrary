package baiduadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jrm.baidulibrary.R;

import java.util.List;

import baidubean.NavisBean;
import utils.BitmapUtil;

/**
 * Created by jrm on 2017-5-3.
 * 导航栏的适配器
 */

public class HorizontalListViewAdapter extends BaseAdapter {

    private String[] mTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;
    private int [] mIds;
    private Bitmap mBitmap;
    private List<NavisBean> mNavi;

    public HorizontalListViewAdapter(Context context, String[] titles, int[] ids){
        this.mContext = context;
        this.mTitles = titles;
        this.mIds = ids;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setNavisData(List<NavisBean> navis) {
        this.mNavi = navis;
        this.notifyDataSetChanged();
    }

    public void setNavisAndDefaultData(List<NavisBean> navis, String[] titles, int[] ids) {
        this.mNavi = navis;
        this.mTitles = titles;
        this.mIds = ids;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mNavi == null ){
            return mTitles.length;
        }else if (mNavi.size() < 5){
            return 5;
        }
        return mNavi.size();
    }
    @Override
    public Object getItem(int position) {
        if (mNavi == null){
            return  mTitles[position];
        }else if (mNavi.size() < 5){
            if (position < mNavi.size()){
                return mNavi.get(position);
            }else {
                return mTitles[position-mNavi.size()];
            }
        }
        return mNavi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontal_item, null);
            holder.mTitle=(TextView)convertView.findViewById(R.id.tab_bar_item);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.titleWidth =(LinearLayout)convertView.findViewById(R.id.title_width);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)  holder.mTitle.getLayoutParams();
            layoutParams.width = getScreenWidth(mContext)/5;
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(position == selectIndex){
            convertView.setSelected(true);
        }else{
            convertView.setSelected(false);
        }
        if (mNavi == null){
            holder.mTitle.setText(mTitles[position]);
            mBitmap = getPropThumnail(mIds[position]);
            holder.img.setImageBitmap(mBitmap);
        }else {
            if (mNavi.size() < 5){
                if (position < mNavi.size()){
                    NavisBean navisBean = mNavi.get(position);
                    holder.mTitle.setText(navisBean.getNav());
                    Glide.with(mContext).load(navisBean.getIcon()).asBitmap().into(holder.img);
                }else {
                    holder.mTitle.setText(mTitles[position-mNavi.size()]);
                    mBitmap = getPropThumnail(mIds[position-mNavi.size()]);
                    holder.img.setImageBitmap(mBitmap);
                }
            }else {
                NavisBean navisBean = mNavi.get(position);
                holder.mTitle.setText(navisBean.getNav());
                Glide.with(mContext).load(navisBean.getIcon()).asBitmap().into(holder.img);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle ;
        private LinearLayout titleWidth;
        private ImageView img;
    }
     private Bitmap getPropThumnail(int id){
        Drawable d = mContext.getResources().getDrawable(id);
        Bitmap b = BitmapUtil.drawableToBitmap(d);
        int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
        int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
        Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
        return thumBitmap;
    }
    public void setSelectIndex(int i){
        selectIndex = i;
    }

    //获取屏宽
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
