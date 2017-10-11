package cn.com.eju.deal.core.support;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**   
* (加载本地配置文件)
* @author (li_xiaodong)
* @date 2015年10月14日 下午7:53:15
*/
public class SystemCfg
{
    
    public static Properties properties = new Properties();
    
    //本地配置文件
    private static final String BUNDLE_NAME = "systemCfg";
    
    static
    {
        //开启本地读取模式
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        Enumeration<String> e = bundle.getKeys();
        while (e.hasMoreElements())
        {
            String key = e.nextElement();
            properties.put(key, bundle.getString(key));
        }
        
    }
    
    public static Map<?, ?> getAllConfig()
    {
        return properties;
    }
    
    public static String getString(String key)
    {
        return properties.getProperty(key);
    }
    
    
}
