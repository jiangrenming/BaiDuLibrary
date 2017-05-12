package baidubean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jrm on 2017-5-3.
 */

public class BaiDuNavi implements Serializable {

    private int code;
    private String msg;
    private Navis data;

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

    public Navis getData() {
        return data;
    }

    public void setData(Navis data) {
        this.data = data;
    }
}
