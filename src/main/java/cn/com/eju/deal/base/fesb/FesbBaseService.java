package cn.com.eju.deal.base.fesb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.base.model.PageInfo;
import cn.com.eju.deal.base.support.SystemParam;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.QueryConst;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.support.SystemCfg;
import cn.com.eju.deal.core.util.AuthUtil;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;

/**   
* 调用REST服务类的基类
* @author (li_xiaodong)
* @date 2016年2月16日 下午3:10:06
* @param <T>
*/
public abstract class FesbBaseService<T>
{
    
    /**
    * 日志
    */
    private final LogHelper logger = LogHelper.getLogger(this.getClass());
    
    /**
    * Spring提供的用于访问Rest服务的客户端
    */
    private RestTemplate restTemplate = new RestTemplate();
    
    /** 
    * exchange,   分页、带参数，参数类型（Map<String, ?>）
    * @param url
    * @param reqMap
    * @return
    * @throws Exception
    */
    public ResultData<?> exchange(String url, Map<String, Object> reqMap, PageInfo pageInfo, String resourceCode)
        throws Exception
    {
        if (null != pageInfo)
        {
            reqMap.put(QueryConst.PAGE_IDX, pageInfo.getCurPage().toString());
            reqMap.put(QueryConst.PAGE_SIZE, pageInfo.getPageLimit().toString());
        }
        
        //请求参数jsonStr
        String jsonStr = JsonUtil.parseToJson(reqMap);
        
        //日志记录begin
        long startTime = System.currentTimeMillis();
        logger.info("FesbBaseService request url=" + url + "; request params:" + JsonUtil.parseToJson(reqMap));
        
        HttpHeaders headers = new HttpHeaders();
        
        //设置FESB认证信息
        setFesbAuthInfo(headers, jsonStr, resourceCode);
        
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("param", JsonUtil.parseToJson(reqMap));
        
        //HTTP GET
        ResponseEntity<ResultData> rebackk = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ResultData.class, map);
        
        ResultData<?> reback = rebackk.getBody();
        
        //日志记录end
        long endTime = System.currentTimeMillis();
        logger.info("FesbBaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(reback) + "; cost time:(" + (endTime - startTime) + ") ms");
        
        if (null != pageInfo && null != reback)
        {
            
            //记录总数
            String total = (String)reback.getTotalCount();
            
            if (StringUtil.isNotEmpty(total))
            {
                
                pageInfo.setDataCount(Integer.valueOf(total));
            }
        }
        
        return reback;
        
    }
    
    /** 
    * post 请求 -- 鉴权
    * @param url
    * @param dto
    * @return
    * @throws Exception
    */
    public ResultData<?> postFesb(String url, T dto, boolean isFesbAuth, String resourceCode)
        throws Exception
    {
        
        //日志记录begin
        long startTime = System.currentTimeMillis();
        logger.info("FesbBaseService request url=" + url + "; request params:" + dto.toString());
        
        HttpHeaders headers = new HttpHeaders();
        
        //需要鉴权
        if (isFesbAuth)
        {
            String jsonStr = JsonUtil.parseToJson(dto);
            
            //设置认证信息
            setFesbAuthInfo(headers, jsonStr, resourceCode);
            
        }
        
        HttpEntity<T> formEntity = new HttpEntity<T>(dto, headers);
        
        ResultData<?> backResult = new ResultData<T>();
        
        try
        {
            //HTTP POST
            backResult = restTemplate.postForObject(url, formEntity, ResultData.class);
        }
        catch (Exception e)
        {
            //logger.error(moduleName, className, methodName, dto.toString(), operateId, ipAddress, description, e);
            throw e;
        }
        
        //日志记录end
        long endTime = System.currentTimeMillis();
        logger.info("FesbBaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(backResult) + "; cost time:(" + (endTime - startTime) + ") ms");
        
        return backResult;
    }
    
    /** 
    * 设置FESB认证信息
    * @param reqMap
    * @param headers
    * @param dto
    * @param resourceCode
    * @throws Exception     
    */
    public void setFesbAuthInfo(HttpHeaders headers, String jsonStr, String resourceCode)
        throws Exception
    {
        //访问签名：sign
        String sign = AuthUtil.sign(SystemParam.getWebConfigValue("FESBAppSecret"), jsonStr);
        
        //签名sign
        headers.add(Constant.FESB_COMMON_PARAM_SIGN, sign);
        
        //应用端appCode
        headers.add("appcode", SystemCfg.getString("systemCode"));
        
        //应用端appKey
        headers.add(Constant.FESB_COMMON_PARAM_APPKEY, SystemCfg.getString("systemName"));
        
        //访问时间戳：timestamp
        headers.add(Constant.FESB_COMMON_PARAM_TIMESTAMP, System.currentTimeMillis() / 1000 + "");
        
        //应用端resourceCode
        headers.add(Constant.FESB_COMMON_PARAM_METHOD, resourceCode);
        
        //版本
        headers.add(Constant.FESB_COMMON_PARAM_APIVERSION, "1.0");
        
    }
    
}
