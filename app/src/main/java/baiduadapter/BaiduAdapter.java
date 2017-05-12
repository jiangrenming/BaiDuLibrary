package baiduadapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jrm.baidulibrary.R;

import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import baidubean.BaiduHistoryBean;

/**
 * Created by jrm on 2017-4-25.
 * 搜索历史记录和联想词的适配器
 */

public class BaiduAdapter extends BaseAdapter {

    private Context mContext;
    private List<BaiduHistoryBean> baiduHistoryBeen;
    private LayoutInflater mInflater;
    private int HISTORY_DELETE_TYPE =1;
    private int SEARCH_TYPE = 0;
    private String inputText;

    public BaiduAdapter(Context context,List<BaiduHistoryBean> historys){
        this.mContext = context;
        this.baiduHistoryBeen = historys;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setBaiduValue(List<BaiduHistoryBean> mList){
        this.baiduHistoryBeen = mList;
        this.notifyDataSetChanged();
    }

    public void setTitle(String title){
        this.inputText = title;
    }

    @Override
    public int getCount() {
        if (baiduHistoryBeen == null){
            return  0;
        }
        return baiduHistoryBeen.size();
    }

    @Override
    public BaiduHistoryBean getItem(int position) {
        if (baiduHistoryBeen == null){
            return null;
        }
        return baiduHistoryBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.auto_list_item,null);
            holder.goImg = (ImageView) convertView.findViewById(R.id.search_copy);
            holder.title = (TextView) convertView.findViewById(R.id.auto_text);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        BaiduHistoryBean baiduHistoryBean = baiduHistoryBeen.get(position);
        String title = baiduHistoryBean.getTitle();
        if (inputText != null){
            SpannableStringBuilder sb  = new SpannableStringBuilder();
            SpannableString spannableString = new SpannableString(title);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#4787ED")),0,inputText.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append(spannableString);
            holder.title.setText(sb);
        }else {
            holder.title.setText(title);
        }
        holder.goImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.setText(position);
            }
        });
        return convertView;
    }

    public class ViewHolder{
        private TextView title;
        private ImageView goImg;
    }

    public void setBaiduCallBack(BaiduCallBack callBack){
        this.callBack = callBack;
    }
    private BaiduCallBack callBack;
    public interface  BaiduCallBack{
        void setText(int position);
    }
}
