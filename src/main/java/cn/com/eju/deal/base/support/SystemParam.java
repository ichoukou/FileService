package cn.com.eju.deal.base.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.eju.deal.base.code.dao.CommonCodeMapper;
import cn.com.eju.deal.base.code.dao.WebConfigMapper;
import cn.com.eju.deal.base.code.model.Code;
import cn.com.eju.deal.base.code.model.WebConfig;
import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.core.helper.SpringConfigHelper;
import cn.com.eju.deal.core.util.StringUtil;

/**   
* 系统参数缓存，包括了系统通用参数，行权限参数等各种参数，都是以单例的形式存在
* @author (li_xiaodong)
* @date 2015年11月9日 下午3:25:12
*/
public class SystemParam
{
    
    /**
     * 码表对象List，所有的码表对象都会缓存在这个对象中
     */
    private static Map<String, List<Code>> codeListMap;
    
    /**
     * 码表对象，所有的码表对象都会缓存在这个对象中
     */
    private static Map<String, Code> codeMap;
    
    /**
     * 用以存储 <dicCode,dicValue>结构 
     */
    private static Map<Integer, String> dicCodeMap;
    
    /**
     * 用以存储 <dicValue,dicCode>结构 
     */
    private static Map<String, Integer> dicNameMap;
    
    /**
     * 数据配置map对象
     */
    private static Map<String, String> webConfigMap = new HashMap<String, String>();
    
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(SystemParam.class);
    
    /**
     * 根据dicCode 获取dicValue (旧方法)
     * @param mapKey 码表的key
     * @return List<Code>
     */
    @Deprecated
    public static String getDicValueByDicCode(String dicCode)
    {
        if (codeMap == null)
        {
            initCodeMap();
        }
        
        String dicValue = "";
        
        if (StringUtil.isNotEmpty(dicCode))
        {
            
            Code code = codeMap.get(dicCode);
            
            if (null != code)
            {
                dicValue = code.getDicValue();
            }
        }
        
        return dicValue;
    }
    
    /** 
    * 根据dicCode 获取dicValue 
    * @param dicCode
    * @return
    */
    public static String getDicValueByDicCode(Integer dicCode)
    {
        if (dicCodeMap == null)
        {
            initCodeMap();
        }
        
        String dicValue = null;
        
        if (null != dicCode)
        {
            
            dicValue = dicCodeMap.get(dicCode);
            
        }
        
        return dicValue;
    }
    
    /** 
    * 根据dicValue 获取 dicCode
    * @param dicValue
    * @return
    */
    public static Integer getDicCodeByDicValue(String dicValue)
    {
        if (dicNameMap == null)
        {
            initCodeMap();
        }
        
        Integer dicCode = null;
        
        if (StringUtil.isNotEmpty(dicValue))
        {
            
            dicCode = dicNameMap.get(dicValue);
            
        }
        
        return dicCode;
    }
    
    /**
     * 根据码表的key获取码表对象
     * @param mapKey 码表的key
     * @return List<Code>
     */
    public static Code getCodeMapByKey(String dicCode)
    {
        if (codeMap == null)
        {
            initCodeMap();
        }
        
        return codeMap.get(dicCode);
    }
    
    /**
     * 根据码表的key和代码获取码值内容
     * @param mapKey 码表的key
     * @param dicCode 码值
     * @return 码值内容（名称）
     */
    public static String getCodeNameByMapKeyAndCode(String mapKey, String dicCode)
    {
        
        List<Code> codeMap = getCodeListByKey(mapKey);
        if (codeMap != null && codeMap.size() > 0)
        {
            for (Code code : codeMap)
            {
                if (null != dicCode && dicCode.equals(code.getDicCode()))
                {
                    return code.getDicValue();
                }
            }
        }
        return "";
    }
    
    /** 
    * 根据webConfigKey获取配置值 value 
    * @param key
    * @return
    */
    public static String getWebConfigValue(String webConfigKey)
    {
        
        if (webConfigMap.isEmpty())
        {
            //初始化配置表信息
            initWebConfigPram();
        }
        
        String value = null;
        if (webConfigMap.containsKey(webConfigKey))
        {
            value = webConfigMap.get(webConfigKey);
            if (log.isDebugEnabled())
            {
                log.debug("获取配置信息成功:{}={}", webConfigKey, value);
            }
        }
        else
        {
            if (log.isWarnEnabled())
            {
                log.warn("获取配置信息失败:{}", webConfigKey);
            }
        }
        
        return value;
    }
    
    /**
     * 根据码表的key获取码表对象List
     * @param mapKey 码表的key
     * @return List<Code>
     */
    public static List<Code> getCodeListByKey(String typeId)
    {
        if (codeListMap == null)
        {
            initCodeMap();
        }
        
        return codeListMap.get(typeId);
    }
    
    /** 
     * <p>初始化码表信息到内存中。</p>
     */
    private synchronized static void initCodeMap()
    {
        codeListMap = new HashMap<String, List<Code>>();
        
        codeMap = new HashMap<String, Code>();
        
        //用以存储 <dicCode,dicValue>结构
        dicCodeMap = new HashMap<>();
        
        //用以存储 <dicValue,dicCode>结构
        dicNameMap = new HashMap<>();
        
        //临时变量
        Integer dicCode = null;
        String dicValue = null;
        
        //查询出码表
        List<Code> codeMapList = loadCodeData();
        
        for (Code code : codeMapList)
        {
            //dicCode
            dicCode = code.getDicCode();
            String dicCodeKey = "" + dicCode;
            
            //divValue
            dicValue = code.getDicValue();
            
            //typeId
            String typeIdKey = "" + code.getTypeId();
            
            //拼装codeMap
            if (!codeMap.containsKey(dicCodeKey))
            {
                codeMap.put(dicCodeKey, code);
            }
            
            //拼装codeListMap
            if (codeListMap.containsKey(typeIdKey))
            {
                codeListMap.get(typeIdKey).add(code);
            }
            else
            {
                List<Code> codeList = new ArrayList<Code>();
                codeList.add(code);
                codeListMap.put(typeIdKey, codeList);
            }
            
            //拼装 <dicCode,dicValue>结构
            if (!dicCodeMap.containsKey(dicCode))
            {
                dicCodeMap.put(dicCode, dicValue);
            }
            
            //拼装 <dicValue,dicCode>结构
            if (!dicNameMap.containsKey(dicValue))
            {
                dicNameMap.put(dicValue, dicCode);
            }
            
        }
        
    }
    
    /** 
    * 查询出码表
    * @return
    */
    public static List<Code> loadCodeData()
    {
        final IDao<?> commonCodeMapper = SpringConfigHelper.getDaoBeanByDaoClassName("commonCodeMapper");
        
        final CommonCodeMapper dao = (CommonCodeMapper)commonCodeMapper;
        
        Map<String, Object> queryParam = new HashMap<String, Object>();
        
        List<Code> codeMapList = dao.queryList(queryParam);
        
        return codeMapList;
    }
    
    /**
    *
    * <p>初始化Web数据配置信息到内存中。</p>
    *
    */
    private synchronized static void initWebConfigPram()
    {
        
        webConfigMap = new HashMap<String, String>();
        
        //取得系统表数据
        List<WebConfig> webConfigInfoList = loadWebConfigData();
        
        for (WebConfig webConfig : webConfigInfoList)
        {
            String key = webConfig.getWebConfigName();
            
            if (!webConfigMap.containsKey(key))
            {
                
                webConfigMap.put(key, webConfig.getWebConfigValue());
            }
            
        }
        
    }
    
    /** 
     * 查询出数据配置表
     * @return
     */
    public static List<WebConfig> loadWebConfigData()
    {
        final IDao<?> WebConfigMapper = SpringConfigHelper.getDaoBeanByDaoClassName("webConfigMapper");
        
        final WebConfigMapper dao = (WebConfigMapper)WebConfigMapper;
        
        Map<String, Object> queryParam = new HashMap<String, Object>();
        
        List<WebConfig> WebConfigList = dao.queryList(queryParam);
        
        return WebConfigList;
    }
    
    /**
     * 刷新字典表、配置表信息
     */
    public static void refreshCodeMap()
        throws Exception
    {
        //初始化字典表信息
        initCodeMap();
        
        //初始化配置表信息
        initWebConfigPram();
        
    }
    
}
