package cn.com.eju.deal.core.helper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.core.enums.FinalDefine;
import cn.com.eju.deal.core.model.BaseModel;

/**   
* 获取Spring的ApplicationContext对象的工具类
* @author (li_xiaodong)
* @date 2015年10月23日 下午7:27:44
*/
public class SpringConfigHelper implements ApplicationContextAware, FinalDefine
{
    private static ApplicationContext context;
    
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(SpringConfigHelper.class);
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        SpringConfigHelper.context = applicationContext;
    }
    
    /** 
    * 根据名字获取bean
    * @param beanName
    * @return
    */
    public static Object getBean(String beanName)
    {
        
        Object o = context.getBean(beanName);
        
        return o;
    }
    
    /** 
    * 根据名字类型获取bean
    * @param name
    * @param clazz
    * @return
    */
    public static <T> T getBean(String name, Class<T> clazz)
    {
        T bean = context.getBean(name, clazz);
        return bean;
    }
    
    /**
     * 根据类型获取Spring容器中的对象集合
     * @param clazz 类型
     * @return 对象集合
     */
    public static Map<String, ?> getBeansByType(Class<?> clazz)
    {
        Map<String, ?> beans = context.getBeansOfType(clazz);
        return beans;
    }
    
    /**
     * 根据DAO的类名来获取类对象
     * @param daoClassName dao对象类名
     * @return dao对象
     */
    @SuppressWarnings("unchecked")
    public static IDao<BaseModel> getDaoBeanByDaoClassName(String daoClassName)
    {
        IDao<BaseModel> dao = null;
        if (null != daoClassName)
        {
            try
            {
                dao = context.getBean(daoClassName, IDao.class);
            }
            catch (BeansException e)
            {
                //doLog(e, daoClassName);
                log.warn("Can not find the bean", e);
            }
        }
        
        return dao;
    }
    
}
