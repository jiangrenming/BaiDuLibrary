package baidubean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrm on 2017-5-2.
 * 信息流数据
 */

public class NewsInformaitions implements Serializable{
    private int code;
    private String msg;
    private ArrayList<InformationsResult> data;
    private int page;
    private int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<InformationsResult> getData() {
        return data;
    }

    public void setData(ArrayList<InformationsResult> data) {
        this.data = data;
    }
}
