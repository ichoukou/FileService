package cn.com.eju.deal.core.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.eju.deal.core.util.StringUtil;

/**   
* 类型元数据
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:54:45
* @param <T>
*/
public final class MetaObject<T>
{
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(MetaObject.class);

    /** Field缓存 */
    private Map<String, Field> fieldCache = new HashMap<String, Field>();

    /** Method缓存 */
    private Map<String, Method> methodCache = new HashMap<String, Method>();

    /** 目标对象 */
    private T target;

    /** 类对象 */
    private Class<?> clazz;

    /**
     * <p>以目标对象构建元数据。</p>
     *
     * @param target 目标对象
     */
    private MetaObject(T target)
    {
        this.clazz = target.getClass();
        this.target = target;
    }

    /**
     * 从指定的目标对象中获取元数据。
     *
     * @param target 目标对象
     * @return 该对象的元数据
     */
    public static <T> MetaObject<T> fromObject(T target)
    {
        return new MetaObject<T>(target);
    }

    /**
     * 获取目标对象。
     *
     *
     * @return 目标对象
     */
    public T getTarget()
    {
        return target;
    }

    /**
     * <p>给指定属性赋值并返回旧值</p>
     *
     * 如果指定属性不存在或不可访问，则赋值失败，但不会抛出任何异常。<br>
     * 无论赋值成功与否，此方法都将尽最大努力返回赋值之前的属性值。
     * <i>赋值有风险，操作须谨慎。</i>
     *
     * @param property 属性名
     * @param value 属性值
     *
     * @return 旧属性值
     */
    public Object setValue(String property, Object value)
    {
        Object previous = getValue(property);

        // 拼凑赋值器名,尝试使用公开的赋值器
        boolean fail2invoke = true;
        Method method = setter(property);
        if (method != null)
        {
            try
            {
                method.invoke(target, value);
                fail2invoke = false; // 标记方法调用成功，将不会通过属性强行赋值

                if (log.isDebugEnabled())
                {
                    log.debug("invoke setter of [{}] success! value:[{}]->[{}]", property, previous, value);
                }
            }
            catch (Exception e)
            {
                // 标记调用出错
                if (log.isInfoEnabled())
                {
                    log.info("invoke setter of [{}] faild!", property);
                }
            }
        }

        // 若方法调用失败，则尝试使用暴力
        if (fail2invoke)
        {
            Field field = searchField(property);
            if (null != field)
            {
                checkAccess(field);

                try
                {
                    field.set(target, value);
                }
                catch (Exception e)
                {
                    // 发生异常，放弃赋值
                    if (log.isInfoEnabled())
                    {
                        log.info("set property [{}] to [{}] faild!", property, value);
                    }
                }
            }
        }

        return previous;
    }

    /**
     * <p>获取指定的属性值</p>
     *
     * 如果指定属性不存在或不可访问，则获取失败，但不会抛出任何异常。
     * <i>取值有风险，操作须谨慎。</i>
     *
     * @param property 属性名
     *
     * @return 属性值
     */
    public Object getValue(String property)
    {
        // 尝试使用公开的取值器
        boolean fail2invoke = true;
        Object result = null;
        Method method = getter(property);
        if (method != null)
        {
            try
            {
                result = method.invoke(target);
                fail2invoke = false; // 标记方法调用成功，将不会通过属性强行赋值

                if (log.isDebugEnabled())
                {
                    log.debug("invoke getter of [{}] success!", property);
                }
            }
            catch (Exception e)
            {
                // 标记调用出错
                if (log.isInfoEnabled())
                {
                    log.info("invoke getter of [{}] faild!", property);
                }
            }
        }

        // 若方法调用失败，则尝试使用暴力
        if (fail2invoke)
        {
            Field field = searchField(property);
            if (null != field)
            {
                checkAccess(field);

                try
                {
                    result = field.get(target);
                }
                catch (Exception e)
                {
                    // 发生异常，放弃赋值
                    if (log.isInfoEnabled())
                    {
                        log.info("get property [{}] faild!", property);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 测试是否含有指定属性
     *
     * @param property
     * @return true如果有的话
     */
    public boolean hasFiled(String property)
    {
        return searchField(property) != null;
    }

    /**
     * 获得取值器。
     *
     * @param property 属性名
     * @return 指定属性名的取值器
     */
    public Method getter(String property)
    {
        Method method = null;
        if (!StringUtil.isEmpty(property))
        {
            // 尝试使用公开的取值器
            String suffix = Character.toUpperCase(property.charAt(0)) + property.substring(1);
            String getterName = "get" + suffix;
            method = searchMethod(getterName);

            if (method == null)
            {
                /*
                 *  做最后的挣扎
                 *  测试该属性是否为boolean型，
                 *  如果是的话，就尝试isXxx形式的取值器名
                 */
                Field field = searchField(property);
                if (null != field)
                {
                    Class<?> type = field.getType();
                    if (type == boolean.class || type == Boolean.class)
                    {
                        getterName = "is" + suffix;
                        method = searchMethod(getterName);
                    }
                }
            }
        }

        return method;
    }

    public Method setter(String property)
    {
        Method method = null;
        if (!StringUtil.isEmpty(property))
        {
            Field field = searchField(property);
            if (null != field)
            {
                // 拼凑取值器名
                Class<?> type = field.getType();
                String suffix = Character.toUpperCase(property.charAt(0)) + property.substring(1);
                String setterName = "set" + suffix;
                method = searchMethod(setterName, type);
            }
        }

        return method;
    }

    /**
     * <p>检查成员的访问性。</p>
     *
     * 如果是非公开的，则尝试暴力破解访问限制
     *
     * @param member 类成员
     */
    private boolean checkAccess(AccessibleObject member)
    {
        boolean success = true;
        if (!member.isAccessible())
        {
            try
            {
                member.setAccessible(true); // 暴力破解访问限制
            }
            catch (SecurityException e)
            {
                // 发生安全异常，放弃
                success = false;
            }
        }

        return success;
    }

    /**
     * 搜寻Field
     *
     * 搜寻指定类型中指定名称的数据域
     * 向上追溯直到Object类
     * 有属性覆盖的，先到先得
     *
     * @param property 被搜寻的域名
     * @return 实际找到的域，若找不到，则返回null
     */
    private Field searchField(final String property)
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
                catch (NoSuchFieldException e)
                {
                    if (log.isInfoEnabled())
                    {
                        log.info("Field not found:{}", cacheKey);
                    }
                }
                catch (SecurityException e)
                {
                    if (log.isInfoEnabled())
                    {
                        log.info("SecurityException:{}", cacheKey);
                    }
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

    /**
     * 精确搜寻Method
     *
     * 搜寻指定类型中指定名称的方法域
     * 向上追溯直到Object类
     * 有方法覆盖的，先到先得
     *
     * @param methodName 被搜寻的方法名
     * @param argTypes 参数类型列表
     * @return 实际找到的方法域，若找不到，则返回null
     */
    private Method searchMethod(final String methodName, final Class<?>... argTypes)
    {
        /*
         * 创建方法缓存的KEY
         * 规则为类名.方法名(逗号分割的参数类型列表)，以此来标记一个唯一的方法
         */
        StringBuilder cacheKeyBuilder = new StringBuilder(clazz.getName());
        cacheKeyBuilder.append('.');
        cacheKeyBuilder.append(methodName);
        cacheKeyBuilder.append('(');
        for (Class<?> argType : argTypes)
        {
            cacheKeyBuilder.append(argType.getName());
            cacheKeyBuilder.append(',');
        }
        if (cacheKeyBuilder.charAt(cacheKeyBuilder.length() - 1) == ',')
        {
            cacheKeyBuilder.deleteCharAt(cacheKeyBuilder.length() - 1);
        }
        cacheKeyBuilder.append(')');
        String cacheKey = cacheKeyBuilder.toString();

        Method method = null;
        if (methodCache.containsKey(cacheKey))
        {
            method = methodCache.get(cacheKey);
        }
        else
        {
            Class<?> startClass = clazz;
            while (null != startClass)
            {
                try
                {
                    method = startClass.getDeclaredMethod(methodName, argTypes);
                    break;
                }
                catch (NoSuchMethodException e)
                {
                    if (log.isInfoEnabled())
                    {
                        log.info("Method not found:{}", cacheKey);
                    }
                }
                catch (SecurityException e)
                {
                    if (log.isInfoEnabled())
                    {
                        log.info("SecurityException:{}", cacheKey);
                    }
                }

                startClass = startClass.getSuperclass();
            }

            if (null != method)
            {
                methodCache.put(cacheKey, method);
            }
        }

        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable
    {
        fieldCache.clear();
        methodCache.clear();
        target = null;
        clazz = null;
    }

}