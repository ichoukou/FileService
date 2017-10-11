package cn.com.eju.deal.core.support;

import java.util.HashMap;

import cn.com.eju.deal.core.util.JsonUtil;

/**   
* 服务返回 封装
* @author (li_xiaodong)
* @date 2016年2月17日 下午9:41:43
* @param <K>
* @param <V>
*/
public class ReturnView<K, V> extends HashMap<Object, Object>
{
    
      private static final long serialVersionUID = -3304884098644942260L;
    
    public static final String RETURN_OPERATE_TYPE = "0"; // 操作类型
    
    public static final String RETURN_GET_DATA_TYPE = "1"; // 获取数据类型
    
    private static final String RETURN_OPERATE_TYPE_KEY = "returnType";
    
    private static final String RETURN_DATA_KEY = "returnValue";
    
    public ReturnView()
    {
        setSuccess();
    }
    
    public void addAttribute(String key, Object value)
    {
        put(key, value);
    }
    
    public Object getArttribute(String key)
    {
        return get(key);
    }
    
    public void setSuccess()
    {
        put(Constant.RETURN_CODE_KEY, ReturnCode.SUCCESS);
    }
    
    public void setFail()
    {
        put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
    }
    

    /**
     * 设置成操作类型
     */
    public void setOperateReturnType() {
        setReturnType(RETURN_OPERATE_TYPE);
    }
    
    /**
     * 设置成查询类型
     */
    public void setSearchReturnType() {
        setReturnType(RETURN_GET_DATA_TYPE);
    }
    
    private void setReturnType(String type) {
        put(RETURN_OPERATE_TYPE_KEY, type);
    }
    
    public void setReturnValue(Object data) {
        put(RETURN_DATA_KEY, data);
    }
    public void setReturnMsg(String msg)
    {
        put("returnMsg", msg);
    }
    
    public String getReturnCode()
    {
        return (String)get("returnCode");
    }
    
    public void setReturnCode(String returnCode)
    {
        put("returnCode", returnCode);
    }
    
    public String getReturnMsg()
    {
        return (String)get("returnMsg");
    }
    
    @Override
    public String toString()
    {
        return JsonUtil.parseToJson(this);
    }
    
}
