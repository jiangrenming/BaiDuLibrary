package utils;

/**
 * Created by jrm on 2017-5-3.
 * 上拉加载，下拉刷新的常量值
 */

public class Contants {
    //针对正常数据与底部的类型
    public static  final  int VIEW_TYPE_ITEM  =1;
    public static  final  int VIEW_TYPE_FOOTER  =2;

    ///针对在VIEW_TYPE_ITEM下的三种不同的布局的type
    public static  final  int VIEW_TYPE_ITEM_ONE  = 0;
    public static  final  int VIEW_TYPE_ITEM_TWO  = 1;
    public static  final  int VIEW_TYPE_ITEM_THREE = 2;

    //初始化,刷新，加载更多的常量
    public static  final  int INIT_DATA  =3;
    public static  final  int LISTVIEW_REFRESH  =4;
    public static  final  int LISTVIEW_REFRESH_MORE  = 5;
}
