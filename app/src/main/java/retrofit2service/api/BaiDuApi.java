package retrofit2service.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by jrm on 2017-5-3.
 */

public interface BaiDuApi {

    public static final String  BASE_URL ="http://suggestion.baidu.com/";

    @GET("su")
    Call<Object> createBaiduData(@QueryMap Map<String, String> value);
}
