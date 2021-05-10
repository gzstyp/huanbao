package com.fwtai.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用的service生成json数据格式-仅供参考
 * 这个是目前使用最多，而且也是最推荐的一个集合，实现也是比较复杂的一个。
 * 我们看源码其实是可以发现里面的线程安全是通过cas+synchronized+volatile来实现的，其中也可看出它的锁是分段锁，
 * 所以它的性能相对来说是比较好的。整体实现还是比较复杂的
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-04-20 23:15
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class JsonValue extends ConcurrentHashMap<String,Object>{

    public JsonValue(){
        put("code",200);
    }

    public JsonValue(final int code){
        put("code",code);
    }

    public static JsonValue exception() {
        return exception("未知异常,请联系管理员");
    }

    public static JsonValue exception(final String msg) {
        return exception(500,msg);
    }

    public static JsonValue exception(final int code,final String msg) {
        final JsonValue json = new JsonValue();
        json.put("code", code);
        json.put("msg", msg);
        return json;
    }

    /**默认msg=操作成功,code=0*/
    public static JsonValue succeed(){
        return succeed("操作成功");
    }

    public static JsonValue succeed(final String msg) {
        final JsonValue json = new JsonValue();
        json.put("msg", msg);
        return json;
    }

    public static JsonValue succeed(final Map<String, Object> map) {
        final JsonValue json = new JsonValue();
        json.putAll(map);
        return json;
    }

    /**默认msg=操作失败,code=199*/
    public static JsonValue fail(){
        return fail("操作失败");
    }

    public static JsonValue fail(final String msg){
        return exception(199,msg);
    }

    @Override
    public JsonValue put(final String key,final Object value) {
        super.put(key,value);
        return this;
    }
}