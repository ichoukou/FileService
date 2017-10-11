package cn.com.eju.deal.core.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.com.eju.deal.core.model.BaseModel;

/**   
* 所有反射功能的工具类
* @author (li_xiaodong)
* @date 2015年10月23日 下午7:26:14
*/
public class ReflectHelper
{
    /**
     * 缓存所有的反射的方法对象
     */
    private static Map<String, Method> cachedMethod = new HashMap<String, Method>();
    
    /* Field缓存 */
    private static final Map<String, Field> fieldCache = new HashMap<String, Field>();
    
    /**
     * 根据泛型实例化一个对象，调用默认的构造函数
     * @param <T> 实体泛型
     * @param clazz 泛型元类型
     * @return 实例化了的实体对象
     * @throws Exception 抛出异常
     */
    public static <T> T getModelInstance(Class<T> clazz)
        throws Exception
    {
        //return clazz.getConstructor(String.class).newInstance();
        //Type type =
        return clazz.newInstance();
        //return clazz.getConstructor().newInstance();
    }
    
    /**
     * 填充实体属性
     * @param <T> 实体泛型
     * @param obj 实体对象
     * @param data 需要设置在实体对象中的属性值的集合
     */
    public static <T> void setModelData(T obj, Map<String, String[]> data)
    {
        //未完成，完成实体属性的填充
    }
    
    /**
     * 根据某一个属性名来设置实体的该属性内容
     * @param <T> 实体泛型
     * @param obj 实体对象
     * @param propertyName 属性名
     * @param value 需要设置的值
     */
    public static <T> void setModelDataByPropertyName(T obj, String propertyName, Object value)
    {
        //未完成，完成实体属性的设置
    }
    
    /**
     * 根据操作者名称将系统抽象的操作者返回
     * @param handlerName 操作者名称
     * @return 操作者对象（系统抽象好了的）
     * @throws Exception
     */
//    public static IHandler getHander(String handlerName)
//        throws Exception
//    {
//        //具体实现
//        return null;
//        //return clazz.getConstructor().newInstance();
//    }
    
    /**
     * 新增时设置实体基本属性
     * @param obj 基本实体
     * @param userId 操作用户
     */
//    public static void setNewModelBaseValue(BaseModel obj, int userId)
//    {
//        
//        if (null != obj)
//        {
//            
//            obj.setDateCreate(new Date());
//            
//            
//            //设置DelFlag
//            if (StringUtil.isEmpty(obj.getDelFlag()))
//            {
//                obj.setDelFlag(Constant.DEL_FLAG_N_CODE_KEY);
//            }
//            
//            obj.setUserIdCreate(userId);
//        }
//        
//    }
    
    /**
     * 新增时设置实体基本属性（基本的六个属性）
     * @param obj 基本实体
     * @param userID 操作用户
     */
    public static void setUpdateUserInfo(BaseModel obj, String userID)
    {
        //设置更新时间
        Date currentDate = new Date();
        obj.setDateCreate(currentDate);
        
        //        obj.setUserIdCreate(userID);
        //        obj.setUpdateTime(currentDate);
        //        //设置userID
        //        obj.setUpdateId(userID);
    }
    
    /**
     * 修改时设置实体基本属性（基本的六个属性）
     * @param obj 基本实体
     * @param userID 操作用户
     */
    public static void setUpdateModelBaseValue(BaseModel obj, String userID)
    {
        //设置更新时间
        Date currentDate = new Date();
        //        obj.setUpdateTime(currentDate);
        //        //设置更新人的GUID
        //        obj.setUpdateId(userID);
    }
    
    /**
     * 根据类型和方法名获取反射方法对象
     * @param clazz 类型
     * @param propName 属性名
     * @param startWith 前缀，是get还是set
     * @return 反射方法对象
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static Method getGetAndSetMethod(Class<?> clazz, String propName, String startWith)
        throws SecurityException, NoSuchMethodException
    {
        propName = String.format("%s%s%s", startWith, Character.toUpperCase(propName.charAt(0)), propName.substring(1));
        String key = String.format("%s.%s.%s", clazz.getPackage().getName(), clazz.getName(), propName);
        if (cachedMethod.containsKey(key))
        {
            return cachedMethod.get(key);
        }
        else
        {
            Method method = clazz.getDeclaredMethod(propName);
            if (method != null)
            {
                cachedMethod.put(key, method);
                return cachedMethod.get(key);
            }
            else
            {
                return null;
            }
        }
    }
    
    /**
     * 根据方法名从方法缓存对象中获取方法对象
     * @param clazz 类对象
     * @param methodName 方法名
     * @return 方法对象
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes)
    {
        String key = String.format("%s.%s", clazz.getName(), methodName);
        Method method = null;
        
        if (cachedMethod.containsKey(key))
        {
            method = cachedMethod.get(key);
        }
        else
        {
            try
            {
                method = clazz.getMethod(methodName, paramTypes);
                cachedMethod.put(key, method);
            }
            catch (SecurityException e)
            {
            }
            catch (NoSuchMethodException e)
            {
            }
        }
        
        return method;
    }
    
    /**
     * 通用赋值方法
     *
     * 赋值有风险，操作须谨慎。
     * 不过即使失败，也不会抛出任何异常，可放心使用
     *
     * @param target 要赋值的目标
     * @param property 要赋值的属性
     * @param value 要赋的值
     */
    public static void setValue(Object target, String property, Object value)
    {
        // 拼凑赋值器名,尝试使用公开的赋值器
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getMethods();
        String setterName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        
        boolean fail2invoke = true;
        for (Method method : methods)
        {
            int argLen = method.getParameterTypes().length;
            String name = method.getName();
            if (1 == argLen && setterName.equals(name))
            {
                try
                {
                    method.invoke(target, value);
                    fail2invoke = false; // 标记方法调用成功，将不会通过属性强行赋值
                }
                catch (Exception e)
                {
                    // 标记调用出错
                }
                
                break;
            }
        }
        
        // 若方法调用失败，则尝试使用暴力
        if (fail2invoke)
        {
            Field field = searchField(clazz, property);
            if (null != field)
            {
                int mod = field.getModifiers();
                if (Modifier.isPrivate(mod) || Modifier.isProtected(mod))
                {
                    try
                    {
                        field.setAccessible(true); // 暴力破解访问限制
                    }
                    catch (SecurityException e)
                    {
                        // 发生安全异常，放弃
                    }
                }
                
                try
                {
                    field.set(target, value);
                }
                catch (Exception e)
                {
                    // 发生异常，放弃赋值
                }
            }
        }
    }
    
    /**
     * 通用取值方法
     *
     * 取值有风险，操作须谨慎。
     * 不过即使失败，也不会抛出任何异常，可放心使用
     *
     * @param target 要取值的目标
     * @param property 要取值的属性
     * @param valueClass 值类型类对象
     * @param <T> 获取的值类型
     *
     * @return 要获取的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object target, String property, Class<T> valueClass)
    {
        // 拼凑取值器名,尝试使用公开的取值器
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getMethods();
        String getterName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        
        boolean fail2invoke = true;
        T result = null;
        for (Method method : methods)
        {
            int argLen = method.getParameterTypes().length;
            String name = method.getName();
            if (0 == argLen && getterName.equals(name))
            {
                try
                {
                    result = (T)method.invoke(target);
                    fail2invoke = false; // 标记方法调用成功，将不会通过属性强行赋值
                }
                catch (Exception e)
                {
                    // 标记调用出错
                }
                
                break;
            }
        }
        
        // 若方法调用失败，则尝试使用暴力
        if (fail2invoke)
        {
            Field field = searchField(clazz, property);
            if (null != field)
            {
                int mod = field.getModifiers();
                if (Modifier.isPrivate(mod) || Modifier.isProtected(mod))
                {
                    try
                    {
                        field.setAccessible(true); // 暴力破解访问限制
                    }
                    catch (SecurityException e)
                    {
                        // 发生安全异常，放弃
                    }
                }
                
                try
                {
                    result = (T)field.get(target);
                }
                catch (Exception e)
                {
                    // 发生异常，放弃赋值
                }
            }
        }
        
        return result;
    }
    
    /** 
    * 搜寻Field
     *
     * 搜寻指定类型中指定名称的数据域
     * 向上追溯直到Object类
     * 有属性覆盖的，先到先得
     *
     * @param clazz 被搜寻的类
     * @param property 被搜寻的域名
     * @return 实际找到的域，若找不到，则返回null
    */
    private static Field searchField(final Class<?> clazz, final String property)
    {
        String className = clazz.getName();
        String cacheKey = className + "." + property;
        Field field = null;
        if (fieldCache.containsKey(cacheKey))
        {
            field = fieldCache.get(cacheKey);
        }
        else
        {
            Class<?> startClass = clazz;
            while (null != startClass)
            {
                try
                {
                    field = startClass.getDeclaredField(property);
                    break;
                }
                catch (Exception e)
                {
                }
                
                startClass = startClass.getSuperclass();
            }
            
            if (null != field)
            {
                fieldCache.put(cacheKey, field);
            }
        }
        
        return field;
    }
    
}
