package cn.com.eju.deal.base.fesb;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.eju.deal.base.model.PageInfo;
import cn.com.eju.deal.base.support.SystemParam;
import cn.com.eju.deal.core.support.ResultData;

/**   
* fesb 服务
* @author (li_xiaodong)
* @date 2016年2月2日 下午9:30:27
*/
@Service("fesbService")
public class FesbService extends FesbBaseService<Object>
{
	
    /** 
    * httpGet 请求
    * @param reqMap
    * @return
    * @throws Exception
    */
    public ResultData<?> httpGet(Map<String, Object> reqMap, String resourceCode)
        throws Exception
    {
        
        return httpGet(reqMap, null, resourceCode);
        
    }
    
    /** 
    * httpGet 请求
    * @param reqMap
    * @return
    * @throws Exception
    */
    public ResultData<?> httpGet(Map<String, Object> reqMap, PageInfo pageInfo, String resourceCode)
        throws Exception
    {
        
        //调用 接口
        String url = SystemParam.getWebConfigValue("FESBRestServer") + "route/{param}";
        
        ResultData<?> reback = exchange(url, reqMap, pageInfo,resourceCode);
        
        return reback;
        
    }
    
    /** 
    * httpPost 请求
     * @param <T>
    * @param studentDto
    * @return
    * @throws Exception
    */
    public <T> ResultData<?> httpPost(T dto, String resourceCode)
        throws Exception
    {
        
        //调用 接口
        String url = SystemParam.getWebConfigValue("FESBRestServer") + "route";
        
        ResultData<?> backResult = postFesb(url, dto, true, resourceCode);
        
        return backResult;
    }
    
    

    
}
