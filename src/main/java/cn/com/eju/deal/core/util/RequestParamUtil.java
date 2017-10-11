package cn.com.eju.deal.core.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**   
* 参数工具类
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:58:28
*/
public class RequestParamUtil
{
    
    /** 
    * (绑定参数到Map)
    * @param request
    * @return
    */
    public static Map<String, Object> bindParamToMap(HttpServletRequest request)
    {
        Enumeration<?> enumer = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        while (enumer.hasMoreElements())
        {
            String key = (String)enumer.nextElement();
            String val = request.getParameter(key);
            if (!"randomId".equals(key))
            {
                if ("orderBy".equals(key))
                {
                    if (!StringUtil.isEmpty(val))
                    {
                        Object orderByList = JsonUtil.parseToObject(val, List.class);
                        map.put(key, orderByList);
                    }
                    continue;
                }
                map.put(key, val);
            }
        }
        return map;
    }
    
}
