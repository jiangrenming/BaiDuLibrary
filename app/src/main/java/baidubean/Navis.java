package baidubean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jrm on 2017-5-3.
 */
public class Navis implements Serializable{

    private List<NavisBean> nav;
    private String channel_key;

    public List<NavisBean> getNav() {
        return nav;
    }

    public void setNav(List<NavisBean> nav) {
        this.nav = nav;
    }

    public String getChannel_key() {
        return channel_key;
    }

    public void setChannel_key(String channel_key) {
        this.channel_key = channel_key;
    }
}
