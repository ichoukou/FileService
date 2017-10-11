package cn.com.eju.deal.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**   
* ( Web Util 类)
* @author (li_xiaodong)
* @date 2015年10月14日 下午7:55:13
*/
public class WebUtil
{
    
    public static final int COOKIE_DEFAULT_TIME_YEAR = 365 * 24 * 60 * 60;
    
    public static String getString(Double db)
    {
        if (db == null)
        {
            return "";
        }
        return db.toString();
    }
    
    private static String getString(String str)
    {
        if (str == null || str.equals("null"))
        {
            return "";
        }
        return str;
    }
    
    public static Integer getInteger(String str)
    {
        if (str == null || str.equals("null") || "".equals(str))
            return null;
        return new Integer(str);
    }
    
    public static int getInt(String str)
    {
        if (str == null || str.equals("null"))
            return 0;
        return new Integer(str).intValue();
    }
    
    public static String getParamByRegex(HttpServletRequest request, String regex)
    {
        
        Enumeration<?> en = request.getParameterNames();
        while (en.hasMoreElements())
        {
            String name = (String)en.nextElement();
            if (name.matches(regex))
            {
                return request.getParameter(name);
            }
        }
        return null;
    }
    
    /**
     * ֵ
     * 
     * @param request
     * @param paramName
     * @param def
     * @return
     */
    public static int getInt(HttpServletRequest request, String paramName, int def)
    {
        String str = getString(request.getParameter(paramName));
        if (str.equals(""))
        {
            return def;
        }
        else
        {
            return new Integer(str).intValue();
        }
    }
    
    public static Integer getInteger(HttpServletRequest request, String paramName)
    {
        String str = getString(request.getParameter(paramName));
        return getInteger(str);
    }
    
    public static String getString(HttpServletRequest request, String paramName)
    {
        return getString(request.getParameter(paramName));
    }
    
    public static String[] getStrings(HttpServletRequest request, String paramName)
    {
        return request.getParameterValues(paramName);
    }
    
    /**
     * 获取访问IP地址
     * 
     * @param request
     * @return
     */
    public static String getIPAddress(HttpServletRequest request)
    {
        if (request.getHeader("x-forwarded-for") == null)
        {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
    
    /** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
     * 
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
     * 192.168.1.100 
     * 
     * 用户真实IP为： 192.168.1.110 
     * 
     * @param request 
     * @return 
     */
    public static String getRealIpAddress(HttpServletRequest request) { 
      String ip = request.getHeader("X-Real-IP"); 
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Forwarded-For"); 
      } 
  /*    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
        ip = request.getHeader("Proxy-Client-IP"); 
      } 
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
        ip = request.getHeader("WL-Proxy-Client-IP"); 
      } 
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
        ip = request.getHeader("HTTP_CLIENT_IP"); 
      } 
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
        ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
      } */
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
        ip = request.getRemoteAddr(); 
      } 
      return ip; 
    }
    
    /**
     * 设置Session
     * 
     * @param request
     * @param SessionName
     * @param value
     */
    public static void addSession(HttpServletRequest request, String sessionName, Object value)
    {
        if (value != null)
        {
            request.getSession().setAttribute(sessionName, value);
        }
    }
    
    /**
     * 移除Session
     * 
     * @param request
     * @param SessionName
     * @param value
     */
    public static void removeSession(HttpServletRequest request, String sessionName)
    {
        if (sessionName != null)
        {
            request.getSession().removeAttribute(sessionName);
        }
    }
    
    /**
     * 取Session中值
     * 
     * @param request
     * @param SessionName
     * @param value
     */
    public static Object getValueFromSession(HttpServletRequest request, String key)
    {
        if (key != null || !"".equals(key))
        {
            return request.getSession().getAttribute(key);
        }
        return null;
    }
    
    /**
     * 设置cookie，value值(URLEncoder.encode)
     * 
     * @param response
     * @param cookieName
     * @param value
     * @param maxAge
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String value, int maxAge)
    {
        
        String val = value;
        
        try
        {
            
            val = URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            
            e.printStackTrace();
        }
        
        Cookie cookie = new Cookie(cookieName, val);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    /**
     * 设置cookie 默认为一年
     * 
     * @param response
     * @param cookieName
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String value)
    {
        addCookie(response, cookieName, value, COOKIE_DEFAULT_TIME_YEAR);
    }
    
    /**
     * 获取cookie的值，value值(URLEncoder.encode)
     * 
     * @param response
     * @param cookieName
     * @param value
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName)
    {
        
        Cookie cookie = getCookie(request, cookieName);
        if (cookie == null)
        {
            return null;
        }
        
        String cookieVal = "";
        
        try
        {
            cookieVal = URLDecoder.decode(cookie.getValue(), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return cookieVal;
        
    }
    
    /**
     * 删除Cookie信息。(指定了cookie域名)
     * 
     * @param HttpServletResponse
     *            response
     * @param HttpServletRequest
     *            request
     * @param String
     *            cookieName
     * @param String
     *            cookieDomain
     * @return
     * 
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
        String cookieDomain)
    {
        if (!StringUtil.isEmpty(cookieName))
        {
            Cookie cookie = getCookie(request, cookieName);
            if (cookie != null)
            {
                cookie.setMaxAge(0);
                if (!StringUtil.isEmpty(cookieDomain))
                {
                    cookie.setDomain(cookieDomain);
                }
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }
    
    /**
     * 返回Cookie
     * 
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName)
    {
        
        Cookie[] cookie = request.getCookies();
        if (cookie != null)
        {
            for (Cookie c : cookie)
            {
                if (c.getName().equals(cookieName))
                {
                    return c;
                }
            }
        }
        return null;
    }
    
}
