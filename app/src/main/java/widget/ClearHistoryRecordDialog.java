package widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.jrm.baidulibrary.R;

/**
 * Created by jrm on 2017-4-26.
 */

public class ClearHistoryRecordDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private Button cancle;
    private Button confirm;

    public ClearHistoryRecordDialog(Context context) {
        super(context);
        mContext =context;
        setContentView(R.layout.clear_history_layout);
        init();
    }
    public ClearHistoryRecordDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext =context;
        init();
    }

    private void init() {
         cancle = (Button) findViewById(R.id.cancle);
         confirm =(Button)findViewById(R.id.comfirm);
         confirm.setOnClickListener(this);
         cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancle) {
            this.dismiss();

        } else if (i == R.id.comfirm) {
            dialogCallBack.clearHistoryData(this);

        }
    }


    public void setDialogCallBack(DialogCallBack dialogCallBack){
        this.dialogCallBack = dialogCallBack;
    }
    private DialogCallBack dialogCallBack;
    public interface DialogCallBack{
        void clearHistoryData(ClearHistoryRecordDialog dailog);
    }
}
