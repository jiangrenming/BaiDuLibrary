package retrofit2service.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import baidubean.InformationsResult;
import baidubean.Navis;
import baidubean.NavisBean;
import baidubean.NewsInformaitions;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2_callback.BaseCallModel;


/**
 * Created by jrm on 2017-3-29.
 * 存放接口的地方
 */

public interface GithubApi {

    public static final String  BASE_URL ="http://zm.youmeng.com/";
    public String url ="Folder/getPageFolderSoft";
    @FormUrlEncoded
    @POST(url)
    Call<Object> createFolder(@Field("floderId") int floderId, @Field("pageSize") int pageSize, @Field("pageNumber") int pageNumber);


    @FormUrlEncoded
    @POST("Folder/getPageFolderSoft")
    Call<Object> createFolderMap(@FieldMap Map<String, String> value);

    @FormUrlEncoded
    @POST(url)
    Call<BaseCallModel<Object>> createFolderMapTwo(@FieldMap Map<String, String> value);

    //导航栏数据
    @FormUrlEncoded
    @POST("Api/App/getMobileDesktopNav")
    Call<BaseCallModel<Navis>> createNaviData(@FieldMap Map<String, String> value);

    //信息流数据,用到底层封装的接口
    @FormUrlEncoded
    @POST("api/app/getNews")
    Call<BaseCallModel<ArrayList<InformationsResult>>> createInfomations(@FieldMap Map<String, String> value);

    //信息流数据，
    @GET("home/index/toutiaoNews")
    Call<Object> createBaiduInfomations(@QueryMap Map<String, String> value);


    @FormUrlEncoded
    @POST("api/app/getNews")
    Call<NewsInformaitions> createInfomationsTwo(@FieldMap Map<String, String> value);
}
