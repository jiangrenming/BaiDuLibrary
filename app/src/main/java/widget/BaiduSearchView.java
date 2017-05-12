package widget;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.NoiseSuppressor;
import android.net.Uri;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jrm.baidulibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import baiduadapter.BaiduAdapter;
import baidubean.BaiduHistoryBean;
import retrofit2service.RetrofitService;
import utils.SharePresUtils;

/**
 * Created by jrm on 2017-4-24.
 * 搜索框的实现
 */

public class BaiduSearchView extends LinearLayout implements TextWatcher,View.OnClickListener,View.OnFocusChangeListener
,TextView.OnEditorActionListener, HistoryAndHotPopuWindow.ResultClickCallBack,View.OnTouchListener{


    private EditText search_editText;
    private Button baidu_btn;
    private View view;
    private ImageView delete_img;
    private  final String headerURL ="http://m.baidu.com/s?from=";
    private  final String footerURL ="&word=";
   // private LinearLayout search_content_show_ll;
   // private View recordsHistoryView;
   // private TextView delete_history;
    /**
     * 弹出列表
     */
   // private ListViewForScrollView search_result;

    private Context mContext;
    private boolean hasFouse = false;
    private   List<BaiduHistoryBean> hotSearchData = new ArrayList<>();
    private int maxLength = 10;
    private ClearHistoryRecordDialog dialog;
    private HistoryAndHotPopuWindow mPopuWindow;
    private int flag;
    private String channleKeyValue;

    /**
     * 历史记录adapter
     */
   // private BaiduAdapter adapter;
    public BaiduSearchView(Context context) {
        super(context);
    }

    public BaiduSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.searchview_layout, this);
        channleKeyValue = SharePresUtils.getChannleKeyValue(mContext);
        initView(view);
    }

    private void initView(View mView) {
       // initHistoryView();

        search_editText = (EditText) mView.findViewById(R.id.search_et_input);
        baidu_btn = (Button) mView.findViewById(R.id.baidu_btn);
        delete_img = (ImageView) mView.findViewById(R.id.search_iv_delete);

        //search_content_show_ll = (LinearLayout) mView.findViewById(R.id.search_content_show_ll);
        //search_content_show_ll.addView(recordsHistoryView);

        search_editText.addTextChangedListener(this);
        search_editText.setOnFocusChangeListener(this);
        search_editText.setOnClickListener(this);
        search_editText.setOnEditorActionListener(this);
        search_editText.setOnTouchListener(this);
        delete_img.setOnClickListener(this);
        baidu_btn.setOnClickListener(this);
        mPopuWindow = new HistoryAndHotPopuWindow(mContext,hotSearchData);
        mPopuWindow.setResultClickCallBack(this);
       // delete_history.setOnClickListener(this);

       // adapter = new BaiduAdapter(mContext,hotSearchData);
      //  search_result.setAdapter(adapter);
      //  adapter.setBaiduCallBack(this);

       /* search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //直接跳转界面搜索,同时存储
                String title = adapter.getItem(position).getTitle();
                search_content_show_ll.setVisibility(View.GONE);
                notifyStartSearching(title);
            }
        });*/
    }

    /*private void initHistoryView() {
        recordsHistoryView = LayoutInflater.from(mContext).inflate(R.layout.history_layout,null);
        search_result = (ListViewForScrollView) recordsHistoryView.findViewById(R.id.search_result);
        delete_history = (TextView) recordsHistoryView.findViewById(R.id.clear_all_records_tv);
    }*/

    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        if (hasFouse){
            if (TextUtils.isEmpty(s)){
                delete_img.setVisibility(GONE);
                if (mPopuWindow != null && mPopuWindow.isShowing()){
                    mPopuWindow.dismiss();
                }
               // search_content_show_ll.setVisibility(GONE);
            }else {
                flag = 10;
                //这里需要操作：当输入数据时，自动更新联想词<请求的接口>，更新适配器;
                delete_img.setVisibility(VISIBLE);
               // search_content_show_ll.setVisibility(VISIBLE);
              // delete_history.setVisibility(GONE);
                if (hotSearchData != null && hotSearchData.size() != 0){
                    hotSearchData.clear();
                }
                getData(s.toString()); //得到联想词数据
            }
        }
    }


    /**
     * 百度联想词的获取，精准度和最新度有待提高
     * @param inputStr
     */
    private void getData(final String inputStr) {
        final HashMap<String,String> params  = new HashMap<>();
        params.put("action","opensearch");
        params.put("wd",inputStr.toString());
        RetrofitService.getInstance().postAppInfoMapParamsBiadu(new RetrofitService.MyCallBack() {
            @Override
            public <T> void onSucess(T object) {
                final String str = object.toString();
                if (str != null){
                    int i = str.indexOf("]");
                    String substring = str.substring(0, i);
                    String replace = substring.replace("[", "");
                    String s = ", ";
                    String[] split = replace.split(s);
                    if (split.length - 6 > 0){
                        for (int j = 0; j < split.length-(split.length -6); j++) {
                            BaiduHistoryBean baidu = new BaiduHistoryBean();
                            baidu.setUrl(headerURL+channleKeyValue+footerURL+split[j]); //加入路径
                            baidu.setTitle(split[j]);
                            hotSearchData.add(baidu);
                        }
                    }else {
                        for (int j = 0; j <split.length ; j++) {
                            BaiduHistoryBean baidu = new BaiduHistoryBean();
                            baidu.setUrl(headerURL+channleKeyValue+footerURL+split[j]); //加入路径
                            baidu.setTitle(split[j]);
                            hotSearchData.add(baidu);
                        }
                    }
                    mPopuWindow.setData(hotSearchData);
                    mPopuWindow.setColorText(inputStr);
                    mPopuWindow.setFlag(true);
                    mPopuWindow.showAsDropDown(findViewById(R.id.layout));
                   // adapter.setBaiduValue(hotSearchData);
                }
            }
            @Override
            public void onFailure(String error) {
                try {
                      getData(search_editText.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },params);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.search_iv_delete) {
            if (!TextUtils.isEmpty(search_editText.getText().toString())) {
                search_editText.setText("");
                delete_img.setVisibility(GONE);
                mPopuWindow.setColorText(null);
            }

        } else if (i == R.id.baidu_btn) {
            String destUrl = null;
            if (!TextUtils.isEmpty(search_editText.getText().toString())) {
                List<BaiduHistoryBean> sharePrefData = SharePresUtils.getSharePrefData(mContext, BaiduHistoryBean.class);
                if (sharePrefData == null || sharePrefData.size() == 0) {
                    List<BaiduHistoryBean> historys = new ArrayList<>();
                    BaiduHistoryBean hisBean = new BaiduHistoryBean();
                    hisBean.setTime(SystemClock.currentThreadTimeMillis());
                    hisBean.setTitle(search_editText.getText().toString());
                    hisBean.setUrl(headerURL + channleKeyValue + footerURL + search_editText.getText().toString());
                    destUrl = headerURL + channleKeyValue + footerURL + search_editText.getText().toString();
                    historys.add(hisBean);
                    SharePresUtils.setSharePrefData(mContext, historys);
                } else {
                    if (flag == 10) {
                        destUrl = headerURL + channleKeyValue + footerURL + search_editText.getText().toString();
                    } else {
                        destUrl = url;
                    }
                    saveHistoryValues(sharePrefData, search_editText.getText().toString(), destUrl);
                }
                //跳转界面
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(destUrl));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                return;
            }

        } else if (i == R.id.search_et_input) {
            if (hasFouse) {
                if (TextUtils.isEmpty(search_editText.getText().toString())) {
                    //此时展现历史记录
                    List<BaiduHistoryBean> sharePrefData = SharePresUtils.getSharePrefData(mContext, BaiduHistoryBean.class);
                    if (sharePrefData != null && sharePrefData.size() != 0) {
                        mPopuWindow.setData(sharePrefData);
                        mPopuWindow.setFlag(false);
                        mPopuWindow.showAsDropDown(findViewById(R.id.layout));
                        //  search_content_show_ll.setVisibility(VISIBLE);
                        //  delete_history.setVisibility(VISIBLE);
                        //  adapter.setBaiduValue(sharePrefData);
                    } else {
                        // search_content_show_ll.setVisibility(GONE);
                        // adapter.setBaiduValue(null);
                        mPopuWindow.setData(null);
                        if (mPopuWindow != null && mPopuWindow.isShowing()) {
                            mPopuWindow.dismiss();
                        }
                    }
                }
            }

            /*case R.id.clear_all_records_tv:  //弹出：清空搜索记录的弹出窗
                dialog = new ClearHistoryRecordDialog(mContext);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setDialogCallBack(new ClearHistoryRecordDialog.DialogCallBack() {
                    @Override
                    public void clearHistoryData(ClearHistoryRecordDialog dailog) {
                        try {
                            dialog.dismiss();
                           // adapter.setBaiduValue(null);
                            SharePresUtils.clearShareValues(mContext);
                           // search_content_show_ll.setVisibility(GONE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                dialog.show();
                break;*/
        }
    }

    /**
     * 存在相同数据的情况下，需要删除后再重新赋值存储
     * @param sharePrefData
     * @param values
     */
    private void saveHistoryValues(List<BaiduHistoryBean> sharePrefData, String values,String url) {
        BaiduHistoryBean baiDuHistory = new BaiduHistoryBean();
        for (int i = 0; i < sharePrefData.size(); i++) {
            if (values.equals(sharePrefData.get(i).getTitle())){
                sharePrefData.remove(i);
                break ;
            }
        }
        if (sharePrefData.size() >= maxLength){ //存入最大的长度
            sharePrefData.remove(sharePrefData.size()-1);
        }
        baiDuHistory.setTime(SystemClock.currentThreadTimeMillis());
        baiDuHistory.setTitle(values);
        baiDuHistory.setUrl(url);
        sharePrefData.add(0,baiDuHistory);
        SharePresUtils.setSharePrefData(mContext,sharePrefData);
    }


    /**
     * 这里需要实现：当获取焦点的那一刻来判断是否存在历史记录并且更新列表
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        try {
            this.hasFouse = hasFocus;
            if (hasFocus){
                if (TextUtils.isEmpty(search_editText.getText().toString())){
                    //此时展现历史记录
                    List<BaiduHistoryBean> sharePrefData = SharePresUtils.getSharePrefData(mContext, BaiduHistoryBean.class);
                    if (sharePrefData != null && sharePrefData.size() != 0 ){
                       // search_content_show_ll.setVisibility(VISIBLE);
                        mPopuWindow.setData(sharePrefData);
                        mPopuWindow.setFlag(false);
                        mPopuWindow.showAsDropDown(findViewById(R.id.layout));
                       // adapter.setBaiduValue(sharePrefData);
                    }else {
                        mPopuWindow.setData(null);
                       // search_content_show_ll.setVisibility(GONE);
                      //  adapter.setBaiduValue(null);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 当输入框输入数据之后，点击键盘上的回车键才会触发的动作
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          //  search_content_show_ll.setVisibility(GONE);
            notifyStartSearching(search_editText.getText().toString(),headerURL+channleKeyValue+footerURL+search_editText.getText().toString());
        }
        return true;
    }

    /**
     * 触发跳转回调
     * @param inputTxt :输入框数据
     */
    private void notifyStartSearching(String inputTxt,String url) {
        if (inputTxt != null){
            List<BaiduHistoryBean> sharePrefData = SharePresUtils.getSharePrefData(mContext, BaiduHistoryBean.class);
            if (sharePrefData == null || sharePrefData.size() == 0){
                BaiduHistoryBean baiDu = new BaiduHistoryBean();
                baiDu.setTitle(inputTxt);
                baiDu.setTime(System.currentTimeMillis());
                baiDu.setUrl(url);
                sharePrefData.add(baiDu);
                SharePresUtils.setSharePrefData(mContext,sharePrefData);
            }else {
                saveHistoryValues(sharePrefData,inputTxt,url);
            }
            //隐藏软键盘
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            //跳转界面
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
    /**
     * 删除的回调
     * @param popuWindow
     */
    @Override
    public void deleteRecorde(final HistoryAndHotPopuWindow popuWindow) {
        dialog = new ClearHistoryRecordDialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setDialogCallBack(new ClearHistoryRecordDialog.DialogCallBack() {
            @Override
            public void clearHistoryData(ClearHistoryRecordDialog dailog) {
                try {
                    dialog.dismiss();
                    mPopuWindow.setData(null);
                    // adapter.setBaiduValue(null);
                    SharePresUtils.clearShareValues(mContext);
                    // search_content_show_ll.setVisibility(GONE);
                    if (mPopuWindow != null && mPopuWindow.isShowing()){
                        mPopuWindow.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    /**
     * 点击列表项的回调
     * @param position
     * @param popuWindow
     */
    @Override
    public void clickItem(int position, HistoryAndHotPopuWindow popuWindow,String title,String url) {
        search_editText.setText(title);
        search_editText.setSelection(search_editText.getText().length());
        mPopuWindow.setColorText(title);
        notifyStartSearching(title,url);
    }

    /**
     * 填充输入框的回调
     * @param position
     */
    private String url;
    @Override
    public void addTextData(int position) {
        BaiduHistoryBean baiduHistoryBean = null;
        flag = 20;
        if (!TextUtils.isEmpty(search_editText.getText().toString())){
            baiduHistoryBean = hotSearchData.get(position);
        }else {
            List<BaiduHistoryBean> sharePrefData = SharePresUtils.getSharePrefData(mContext, BaiduHistoryBean.class);
            baiduHistoryBean = sharePrefData.get(position);
        }
        if (baiduHistoryBean != null){
            url = baiduHistoryBean.getUrl();
            search_editText.setText(baiduHistoryBean.getTitle());
            search_editText.setSelection(search_editText.getText().length());
        }
        if (mPopuWindow != null && mPopuWindow.isShowing()){
            try {
                mPopuWindow.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 处理输入框的上下滑动的处理
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.search_et_input && canVerticalScroll(search_editText))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param search_editText 需要判断的EditText
     * @return true：可以滚动  false：不可以滚动
     */
    private boolean canVerticalScroll(EditText search_editText) {
        //滚动的距离
        int scrollY = search_editText.getScrollY();
        //控件内容的总高度
        int scrollRange = search_editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = search_editText.getHeight() - search_editText.getCompoundPaddingTop() -search_editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if(scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPopuWindow.isShowing() && mPopuWindow != null){
            mPopuWindow.dismiss();
            mPopuWindow = null;
        }
    }
}
