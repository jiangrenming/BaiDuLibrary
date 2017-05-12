package baidubean;

import java.io.Serializable;

/**
 * Created by jrm on 2017-5-3.
 */
public class NavisBean implements Serializable{

    private String nav;
    private String url;
    private int sort;
    private String icon;

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
