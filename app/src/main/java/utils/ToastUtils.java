package utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jrm.baidulibrary.R;

/**
 * Created by jrm on 2017-5-12.
 * 自定义弹出窗
 */

public class ToastUtils {

    public static  ToastUtils toastUtils;
    public static  ToastUtils getInstance(){
        if (toastUtils == null){
            toastUtils = new ToastUtils();
        }
        return  toastUtils;
    }

    public void setToastTitle(Context context,String name){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout  = inflater.inflate(R.layout.toast_item,null);
        TextView title = (TextView) layout.findViewById(R.id.toast);
        title.setText(name);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,40);
        toast.setView(layout);
        toast.show();
    }
}
