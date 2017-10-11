package cn.com.eju.deal.file.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.core.helper.SpringConfigHelper;
import cn.com.eju.deal.file.dao.FileConfigMapper;
import cn.com.eju.deal.file.model.FileConfig;

/**   
* 文件系统配置
* @author li_xiaodong
* @date 2017年3月10日 下午5:21:20
*/
public class FileConfigParam
{
    
    /**
     * 数据配置map对象
     */
    private static Map<String, String> fileConfigMap = new HashMap<String, String>();
    
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(FileConfigParam.class);
    
    /** 
    * 根据fileConfigCode获取配置值 value 
    * @param fileConfigCode
    * @return
    */
    public static String getFileConfigValue(String fileConfigCode)
    {
        
        if (fileConfigMap.isEmpty())
        {
            //初始化配置表信息
            initFileConfigParam();
        }
        
        String value = null;
        if (fileConfigMap.containsKey(fileConfigCode))
        {
            value = fileConfigMap.get(fileConfigCode);
            if (log.isDebugEnabled())
            {
                log.debug("获取配置信息成功:{}={}", fileConfigCode, value);
            }
        }
        else
        {
            if (log.isWarnEnabled())
            {
                log.warn("获取配置信息失败:{}", fileConfigCode);
            }
        }
        
        return value;
    }
    
    /**
    *
    * <p>初始化Web数据配置信息到内存中。</p>
    *
    */
    private synchronized static void initFileConfigParam()
    {
        
        fileConfigMap = new HashMap<String, String>();
        
        //取得系统表数据
        List<FileConfig> fileConfigList = loadFileConfigData();
        
        for (FileConfig fileConfig : fileConfigList)
        {
            String key = fileConfig.getConfigCode();
            
            if (!fileConfigMap.containsKey(key))
            {
                
                fileConfigMap.put(key, fileConfig.getConfigValue());
            }
            
        }
        
    }
    
    /** 
     * 查询出数据配置表
     * @return
     */
    public static List<FileConfig> loadFileConfigData()
    {
        final IDao<?> fileConfigMapper = SpringConfigHelper.getDaoBeanByDaoClassName("fileConfigMapper");
        
        final FileConfigMapper dao = (FileConfigMapper)fileConfigMapper;
        
        List<FileConfig> fileConfigList = dao.getCfgByChannelCode(null);
        
        return fileConfigList;
    }
    
    /**
     * 刷新字典表、配置表信息
     */
    public static void refreshCodeMap()
        throws Exception
    {
        
        //初始化配置表信息
        initFileConfigParam();
        
    }
    
}
