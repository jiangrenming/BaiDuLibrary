package widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jrm.baidulibrary.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

import baiduadapter.BaiduAdapter;
import baidubean.BaiduHistoryBean;
import utils.SharePresUtils;

/**
 * Created by jrm on 2017-4-28.
 */

public class HistoryAndHotPopuWindow extends PopupWindow implements View.OnClickListener, BaiduAdapter.BaiduCallBack {

    private Context mContext;
    private View mView;
    private LayoutInflater  mInflater;
    private ListViewForScrollView mListView;
    private TextView delete_text;


    private BaiduAdapter adapter;
    private List<BaiduHistoryBean> hotListItem;

    public HistoryAndHotPopuWindow(Context context,List<BaiduHistoryBean> list){
        this.mContext =context;
        this.hotListItem = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    private void initView() {
        mView = mInflater.inflate(R.layout.history_layout,null);
        this.setContentView(mView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 需要设置一下此参数，点击外边可消失
        this.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
    //    this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置弹出窗体需要软键盘，
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //再设置模式，和Activity的一样，覆盖。
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        mListView = (ListViewForScrollView) mView.findViewById(R.id.search_result);
        delete_text =(TextView) mView.findViewById(R.id.clear_all_records_tv);

        delete_text.setOnClickListener(this);

        adapter = new BaiduAdapter(mContext,hotListItem);
        mListView.setAdapter(adapter);
        adapter.setBaiduCallBack(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = adapter.getItem(position).getTitle();
                String url = adapter.getItem(position).getUrl();
                mResultCallBack.clickItem(position,HistoryAndHotPopuWindow.this,title,url);
            }
        });

        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear_all_records_tv){
            mResultCallBack.deleteRecorde(this);
        }
    }

    /**
     * 回调填充输入框数据
     * @param position
     */
    @Override
    public void setText(int position) {
        mResultCallBack.addTextData(position);
    }
    /**
     * 点击的接口回调
     */
    private ResultClickCallBack mResultCallBack;
    public void setResultClickCallBack(ResultClickCallBack resultCallBack){
        this.mResultCallBack = resultCallBack;
    }

    public interface  ResultClickCallBack{
        void deleteRecorde(HistoryAndHotPopuWindow popuWindow); //点击删除
        void clickItem(int position,HistoryAndHotPopuWindow popuWindow,String title,String url); //列表项的点击
        void addTextData(int position);  //填充输入框的数据
    }

    public  void setColorText(String txt){
        adapter.setTitle(txt);
    }

    /**
     * 更新适配器数据来源
     * @param bean
     */
    public void setData(List<BaiduHistoryBean> bean){
        adapter.setBaiduValue(bean);
    }

    /**
     * 显示或隐藏删除控件
     * @param isHotFlag
     */
    public void setFlag(boolean isHotFlag){
        if (isHotFlag){
            delete_text.setVisibility(View.GONE);
        }else {
            delete_text.setVisibility(View.VISIBLE);
        }
        delete_text.invalidate();
    }
}
