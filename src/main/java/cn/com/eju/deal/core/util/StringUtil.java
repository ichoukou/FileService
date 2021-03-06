package cn.com.eju.deal.core.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.eju.deal.core.enums.FinalDefine;

/**   
* 字符串工具类
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:58:28
*/
public class StringUtil
{
    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);
    
    //===========================================================================
    /**
     * 获取包名，不包括子模块名，只到应用名，如com.yum.boh.bmp
     * TODO 业务相关，移动到fund模块中
     *
     * @param packageName 全包名
     * @return 到应用的包名
     */
    public static String getPrePackageOfPackageName(String packageName)
    {
        return FinalDefine.PRE_PACKAGE_NAME + getModelNameFormPackageName(packageName);
    }
    
    /**
     * 获取应用名，如bmp
     * TODO 业务相关，移动到fund模块中
     * @param packageName 全包名
     * @return 应用名
     */
    public static String getModelNameFormPackageName(String packageName)
    {
        packageName = packageName.substring(12);//core.util.xxx
        packageName = packageName.substring(0, packageName.indexOf("."));//core
        
        return packageName;
    }
    
    //===========================================================================
    
    /**
     * 判断字符串是否为空
     *
     * @param strVal string
     * @return true 为空 false 不为空
     */
    public static boolean isEmpty(String strVal)
    {
        return strVal == null || safeTrim(strVal).isEmpty();
    }
    
    /**
     * 判断字符串是否为空
     *
     * @param strVal string
     * @return true 不为空 false 为空
     */
    public static boolean isNotEmpty(String strVal)
    {
        return !isEmpty(strVal);
    }
    
    /**
     * <p>安全的toString方法。</p>
     *
     * 用于在有null风险的情况下获得对象的字符串表达。
     *
     * @param obj 对象
     * @return 如果参数为null，返回空串，否则返回参数自身的toString()方法结果
     */
    public static String safeToString(Object obj)
    {
        return obj == null ? "" : obj.toString();
    }
    
    /**
     * <p>字符串加密函数。</p>
     *
     * @param sSrc 加密前字符串
     * @param sKey 密钥
     * @return 加密后字符串
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey)
        throws Exception
    {
        // 判断Key是否为空或者16位
        if (sKey == null || sKey.length() != 16)
        {
            return null;
        }
        
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        
        return byte2hex(encrypted).toLowerCase();
    }
    
    /**
     * <p>字符串解密函数。</p>
     *
     * @param sSrc 解密前字符串
     * @param sKey 密钥
     * @return 解密后字符串
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey)
        throws Exception
    {
        // 判断Key是否为空或者16位
        if (sKey == null || sKey.length() != 16)
        {
            return null;
        }
        
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = hex2byte(sSrc);
        try
        {
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     *
     * <p>十六进制转byte数组</p>
     *
     * @param strhex 十六进制
     * @return byte数组
     */
    private static byte[] hex2byte(String strhex)
    {
        if (strhex == null)
        {
            return null;
        }
        
        int l = strhex.length();
        if (l % 2 == 1)
        {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++)
        {
            b[i] = (byte)Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        
        return b;
    }
    
    /**
     *
     * <p>byte数组转十六进制</p>
     *
     * @param strhex byte数组
     * @return 十六进制
     */
    private static String byte2hex(byte[] b)
    {
        
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = java.lang.Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
            {
                hs = hs + "0" + stmp;
            }
            else
            {
                hs = hs + stmp;
            }
        }
        
        return hs.toUpperCase();
    }
    
    /**
     * 如果传入的值不为空的情况 合并字符串
     *
     * @param strUrl 请求的URL地址
     * @return String String
     */
    public static String getPageNameByUrl(String strUrl)
    {
        String strResult = null;
        if (!isEmpty(strUrl))
        {
            int beginIndex = strUrl.lastIndexOf("/");
            int endIndex = strUrl.lastIndexOf("_");
            int endPointIndex = strUrl.lastIndexOf(".");
            String type = null;
            if (endPointIndex != -1)
            {
                type = strUrl.substring(endPointIndex + 1);
            }
            if (!isEmpty(type) && type.equalsIgnoreCase("jsp"))
            {
                if (beginIndex > 0 && endPointIndex > 0)
                {
                    strResult = strUrl.substring(beginIndex + 1, endPointIndex);
                }
            }
            else if (beginIndex > 0 && endIndex > 0)
            {
                strResult = strUrl.substring(beginIndex + 1, endIndex);
            }
        }
        
        return strResult;
    }
    
    /**
     * <p>安全的trim方法。</p>
     *
     * 用于在有null风险的情况下获得{@link String#trim()}相同的效果，但不会抛出异常。<br>
     * 如果对象不为空则将对象转换成字符串并去除空格返回,否则返回空串
     *
     * @param obj 对象
     * @return 返回非空字符串
     */
    public static String safeTrim(Object obj)
    {
        char[] charArr = safeToString(obj).toCharArray();
        for (int i = 0; i < charArr.length; i++)
        {
            if (charArr[i] > ' ' && charArr[i] != '　')
            {
                break;
            }
            else
            {
                if (charArr[i] == '　')
                {
                    charArr[i] = ' ';
                }
            }
        }
        for (int i = charArr.length - 1; i >= 0; i--)
        {
            if (charArr[i] > ' ' && charArr[i] != '　')
            {
                break;
            }
            else
            {
                if (charArr[i] == '　')
                {
                    charArr[i] = ' ';
                }
            }
        }
        
        return new String(charArr).trim();
    }
    
    /**
     * <p>安全的做字符串替换</p>
     *
     * 将<b>源字符串</b>中的<b>目标字符串</b>全部替换成<b>替换字符串</b>
     * 规则如下：
     * <ol>
     *  <li>若source为null,则结果亦 为null</li>
     *  <li>若target为null,则结果为source</li>
     *  <li>若replacement为null,则结果为source中的target全部被剔除后的新字符串</li>
     * </ol>
     *
     * @param source 源字符串
     * @param target 目标字符串
     * @param replacement 替换字符串
     * @return 替换过的字符串
     */
    public static String safeReplace(String source, String target, String replacement)
    {
        if (source == null || source.isEmpty() || target == null || target.isEmpty() || target.equals(replacement))
        {
            return source;
        }
        
        List<Integer> offsets = new ArrayList<Integer>();
        int targetLen = target.length();
        int offset = 0;
        while (true)
        {
            offset = source.indexOf(target, offset);
            if (offset == -1)
            {
                break;
            }
            
            offsets.add(offset);
            offset += targetLen;
        }
        
        String result = source;
        if (!offsets.isEmpty())
        {
            // 计算结果字符串数组长度
            int sourceLen = source.length();
            if (replacement == null)
            {
                replacement = "";
            }
            
            int replacementLen = replacement.length();
            
            int offsetsSize = offsets.size();
            int resultLen = sourceLen + (replacementLen - targetLen) * offsetsSize;
            
            // 源/目标字符数组
            char[] sourceCharArr = source.toCharArray();
            char[] replacementCharArr = replacement.toCharArray();
            char[] destCharArr = new char[resultLen];
            
            // 做第一轮替换
            int firstOffset = offsets.get(0);
            System.arraycopy(sourceCharArr, 0, destCharArr, 0, firstOffset);
            if (replacementLen > 0)
            {
                System.arraycopy(replacementCharArr, 0, destCharArr, firstOffset, replacementCharArr.length);
            }
            
            // 中间替换
            int preOffset = firstOffset; // 前一个偏移量
            int destPos = firstOffset + replacementCharArr.length; // 目标char数组目前的有效长度(即已经填入的字符数量)
            for (int i = 1; i < offsetsSize; i++)
            {
                offset = offsets.get(i); // 当前偏移量
                int fragmentLen = offset - preOffset - targetLen;
                System.arraycopy(sourceCharArr, preOffset + targetLen, destCharArr, destPos, fragmentLen);
                destPos += fragmentLen;
                if (replacementLen > 0)
                {
                    System.arraycopy(replacementCharArr, 0, destCharArr, destPos, replacementCharArr.length);
                }
                preOffset = offset;
                destPos += replacementCharArr.length;
            }
            
            // 做末轮替换
            int lastFragmentLen = sourceLen - preOffset - targetLen;
            System.arraycopy(sourceCharArr, preOffset + targetLen, destCharArr, destPos, lastFragmentLen);
            
            result = new String(destCharArr);
        }
        
        return result;
    }
    
    /**
     * 检查字符串中是否包含某个字符
     *
     * @param str 需要检查的字符
     * @param compare 字符串
     * @return true 包含改字符 false 不包含改字符
     */
    public static boolean contains(String str, String compare)
    {
        boolean bool = true;
        if (isEmpty(str))
        {
            bool = false;
        }
        else
        {
            if (str.indexOf(compare) < 0)
            {
                bool = false;
            }
        }
        
        return bool;
    }
    
    /**
     * 检查字符串中是否包含某个字符
     *
     * @param str 需要检查的字符
     * @param compare 字符串
     * @return true 不包含改字符 false 包含改字符
     */
    public static boolean notContains(String str, String compare)
    {
        return !contains(str, compare);
    }
    
    /**
     * 将首字符大写
     *
     * @param str 字符串
     * @return 首字符大写的字符串
     */
    public static String toFirstLetterUpperCase(String str)
    {
        if (StringUtil.isNotEmpty(str))
        {
            String firstLetter = str.substring(0, 1).toUpperCase();
            return firstLetter + str.substring(1, str.length());
        }
        else
        {
            return str;
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为Short
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return 转换后的Short，失败是返回0
     */
    public static Short toShort(Object value)
        throws NumberFormatException
    {
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return Short.valueOf(safeTrim(val));
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为Integer
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return 转换后的Integer，失败是返回0
     */
    public static Integer toInteger(Object value)
        throws NumberFormatException
    {
        
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return Integer.valueOf(safeTrim(val));
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     *
     * 如果输入null则转变为空,其它情况直接toString()
     *
     * </PRE>
     *
     * @param inObject 入力对象
     * @return String 字符
     */
    public static String toStringWithEmpty(Object inObject)
    {
        if (inObject == null)
        {
            return "";
        }
        else
        {
            return inObject.toString();
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为Integer
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return 转换后的Integer，失败是返回0
     */
    public static Integer toIntegerwithZero(Object value)
        throws NumberFormatException
    {
        String val = toStringWithEmpty(value);
        try
        {
            if (isEmpty(val))
            {
                return 0;
            }
            else
            {
                return Integer.valueOf(safeTrim(val));
            }
        }
        catch (NumberFormatException e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为Long
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return 转换后的Long，失败是返回0
     */
    public static Long toLong(Object value)
        throws NumberFormatException
    {
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return Long.valueOf(safeTrim(val));
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            throw e;
        }
    }
    
    /**
     * 多个字符进行拼装
     * 
     * @param str 字符
     * @return 字符串
     */
    public static String joinStr(String... str)
    {
        if (null == str)
        {
            return null;
        }
        StringBuilder result = new StringBuilder(512);
        
        for (String s : str)
        {
            result.append(s);
        }
        return result.toString();
    }
    
    /**.
     * 字符串转大写
     * 
     * @param str 字符串
     * @return 转换结果
     */
    public static String toUpperStr(String str)
    {
        return isNotEmpty(str) ? str.toUpperCase() : str;
    }
    
    /**.
     * 字符串转小写
     * 
     * @param str 字符串
     * @return 转换结果
     */
    public static String toLowerStr(String str)
    {
        return isNotEmpty(str) ? str.toLowerCase() : str;
    }
    
    /**
     * <PRE>
     * 转换字符串为Float
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return 转换后的Double，失败是返回0
     */
    public static Float toFloat(Object value)
        throws NumberFormatException
    {
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return Float.valueOf(safeTrim(val));
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为Double
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return 转换后的Double，失败是返回0
     */
    public static Double toDouble(Object value)
        throws Exception
    {
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return Double.valueOf(safeTrim(val));
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为BigDecimal
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return BigDecimal 转换后的BigDecimal，失败是返回0
     */
    public static BigDecimal toBigDecimal(Object value)
        throws Exception
    {
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return new BigDecimal(safeTrim(val));
            }
            else
            {
                return null;
            }
            
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     * 转换字符串为BigDecimal
     * </PRE>
     *
     * @param value String
     * @throws StringToNumberException 异常
     * @return BigDecimal 转换后的BigDecimal，失败是返回0
     */
    public static BigDecimal toBigDecimal1(Object value)
        throws Exception
    {
        String val = value != null ? String.valueOf(value) : null;
        try
        {
            if (isNotEmpty(val))
            {
                return new BigDecimal(safeTrim(val));
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * <PRE>
     *
     * 4舍5入,返回不含科学计数法的字符
     *
     * </PRE>
     *
     * @param value 值
     * @param nScal 精度
     * @return String 4舍5入后的值
     */
    public static String toDoubleNoE(double value, int nScal)
    {
        DecimalFormat numberForm = (DecimalFormat)NumberFormat.getNumberInstance(Locale.CHINA);
        numberForm.setGroupingUsed(false);
        numberForm.setMaximumFractionDigits(nScal);
        return numberForm.format(value);
    }
    
    /**
     * 将类名中的下划线去掉，适用于在Entities对象中的key的类名重复的时候加上下划线的情况
     * @param value 带下划线的ClassName或者不带的
     * @return 去掉下划线的ClassName，比如SampleDAO_1，返回则为SampleDAO
     */
    public static String filterUnderLineForClassName(String value)
    {
        String returnValue = null;
        if (StringUtil.isNotEmpty(value) && value.indexOf(FinalDefine.SPLIT_STRING_UNDERLINE) >= 0)
        {
            returnValue = value.substring(0, value.indexOf(FinalDefine.SPLIT_STRING_UNDERLINE));
        }
        else
        {
            returnValue = value;
        }
        return returnValue;
    }
    
    /**
     * <PRE>
     * 转换字符串为Boolean
     * </PRE>
     *
     * @param value 0,1)
     * @return Boolean 1-True/非1-False
     */
    public static Boolean toBoolean(String value)
    {
        if ("1".equals(value))
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }
    
    /**
     * 功能：验证字符串长度是否符合要求，一个汉字等于两个字符
     * @param strParameter 要验证的字符串
     * @return 符合长度ture 超出范围false
     */
    public static int validateStrByLength(String strParameter)
    {
        int temp_int = 0;
        byte[] b = strParameter.getBytes();
        
        for (int i = 0; i < b.length; i++)
        {
            if (b[i] >= 0)
            {
                temp_int = temp_int + 1;
            }
            else
            {
                temp_int = temp_int + 2;
                i++;
            }
        }
        return temp_int;
    }
    
    /**
     * 功能：针对权限二次认证做的共通
     * eg.
     *       onclick="query('searchForm','employeeList');"
     *       替换为：
     *       onclick="secondAuthentication('query', ['searchForm','employeeList']);"
     * @return 符合长度ture 超出范围false
     * @throws JspException
     */
    public static String secondAuthentication(Object function)
        throws JspException
    {
        //首先判断是否传了多个方法,如果传了,直接返回null
        String functionName = safeTrim(function.toString());
        String temp = functionName;
        if (StringUtil.isEmpty(temp))
        {
            return "secondAuthentication()";
        }
        try
        {
            temp = function.toString();
            String temp1 = temp.substring(0, temp.indexOf('('));
            String temp2 = temp.substring(temp1.length() + 1, temp.lastIndexOf(')'));
            String temp3 = temp.substring(temp1.length() + temp2.length() + 2, temp.length());
            temp = temp1 + "[" + temp2 + "]" + temp3;
            String part1 = temp.substring(0, temp.indexOf("["));
            String part2 = temp.substring(part1.length(), temp.length());
            part1 = part1 + "',";
            temp = part1 + part2;
            //判断最后一个是否是空格，如果是，则过滤他。
            int mark = temp.lastIndexOf(";");
            if (mark == temp.length() - 1)
            {
                temp = temp.substring(0, mark);
            }
            temp = "secondAuthentication('" + temp + ")";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new JspException("按钮/链接的事件数不能超过1个。方法：[" + functionName + "]");
        }
        return temp;
    }
    
    /**
     * <p>字符串快速分割。</p>
     *
     * <p>
     * 提供更高效简洁的分割算法，在相同情况下，分割效率约为<br>
     * {@link java.lang.String#split(String)}的4倍<br>
     * 但仅支持单个字符作为分割符。
     * </p>
     *
     * <p>
     * 当待分割字符串为null或者空串时，返回长度为0的数组<br>
     * 该方法不会抛出任何异常
     * </p>
     *
     * @param source 待分割字符串
     * @param splitChar 分隔符
     * @return 分割完毕的字符串数组
     */
    public static String[] split(String source, char splitChar)
    {
        String[] strArr = null;
        List<String> strList = new LinkedList<String>();
        if (null == source || source.isEmpty())
        {
            strArr = new String[0];
        }
        else
        {
            char[] charArr = source.toCharArray();
            int start = 0;
            int end = 0;
            while (end < source.length())
            {
                char c = charArr[end];
                if (c == splitChar)
                {
                    if (start != end)
                    {
                        String fragment = source.substring(start, end);
                        strList.add(fragment);
                    }
                    start = end + 1;
                }
                ++end;
            }
            if (start < source.length())
            {
                strList.add(source.substring(start));
            }
            
            strArr = new String[strList.size()];
            strList.toArray(strArr);
        }
        
        return strArr;
    }
    
    /**
     * <p>字符串快速分割。</p>
     *
     * <p>
     * 提供更高效简洁的分割算法，在相同情况下，分割效率约为<br>
     * {@link java.lang.String#split(String)}的4倍<br>
     * 支持字符串作为分割符。
     * </p>
     *
     * <p>
     * 当待分割字符串为null或者空串时，返回长度为0的数组<br>
     * 该方法不会抛出任何异常<br>
     * 如果传入的字符串中仅含有1个字符，那么该方法会被转交给{@link #split(String, char)}处理
     * </p>
     *
     * @param source 待分割字符串
     * @param splitStr 分隔符字符串
     * @return 分割完毕的字符串数组
     *
     * @see #split(String, char)
     */
    public static String[] split(String source, String splitStr)
    {
        String[] strArr = null;
        if (null == source || source.isEmpty())
        {
            strArr = new String[0];
        }
        else
        {
            if (splitStr.length() == 1)
            {
                strArr = split(source, splitStr.charAt(0));
            }
            else
            {
                int strLen = source.length();
                int splitStrLen = splitStr.length();
                List<String> strList = new LinkedList<String>();
                int start = 0;
                int end = 0;
                while (start < strLen)
                {
                    end = source.indexOf(splitStr, start);
                    if (end == -1)
                    {
                        String fregment = source.substring(start);
                        strList.add(fregment);
                        break;
                    }
                    if (start != end)
                    {
                        String fregment = source.substring(start, end);
                        strList.add(fregment);
                    }
                    start = end + splitStrLen;
                }
                
                strArr = new String[strList.size()];
                strList.toArray(strArr);
            }
        }
        
        return strArr;
    }
    
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    
    /**
       * 将短时间格式时间转换为字符串 yyyy-MM-dd
       *
       * @param dateDate
       * @return
       */
    public static String dateToStr(Date dateDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    
    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @param format
     * @return
     */
    public static String dateToStr(Date dateDate, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    
    /**
       * 特殊的日期转换格式针对iceData控件
       * @param dateDate
       * @return 正确格式的日期
     * @throws ParseException
       */
    public static String dateToStringToStr(String dateDate, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date dateString = null;
        try
        {
            dateString = sdf.parse(dateDate);
        }
        catch (ParseException e)
        {
            if (log.isDebugEnabled())
            {
                log.debug("检测到数据转换错误,StringUtil.dateToStringToStr");
            }
        }
        if (null != dateString)
        {
            if (StringUtil.isNotEmpty(format))
            {
                return StringUtil.dateToStr(dateString, format);
            }
            else
            {
                return StringUtil.dateToStr(dateString, "yyyy-MM-dd HH:mm");
            }
        }
        return null;
    }
    
    /**
     *
     * <p>取得16进制字符串。</p>
     *
     * @param b byte数组
     * @return 16进制字符串
     */
    public static String getHexString(byte[] b)
    {
        String result = "";
        for (int i = 0; i < b.length; i++)
        {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
    
    /**
     *
     * <p>将16进制字符串转换为byte数组。</p>
     *
     * @param str 16进制字符串
     * @return byte数组
     */
    public static byte[] getByteFromHexString(String str)
    {
        byte[] result = null;
        try
        {
            if (StringUtil.isNotEmpty(str) && str.length() > 0 && str.length() % 2 == 0)
            {
                result = new byte[str.length() / 2];
                
                String temp = str;
                int i = 0;
                for (i = 0; temp.length() > 2; i++)
                {
                    result[i] = (byte)Integer.parseInt(temp.substring(0, 2), 16);
                    temp = temp.substring(2);
                }
                if (temp.length() == 2)
                {
                    result[i] = (byte)Integer.parseInt(temp, 16);
                }
            }
        }
        catch (Exception ex)
        {
        }
        
        return result;
    }
    
    public static String subTwentyChar(String text)
    {
        String resultStr = "";
        int i = 0;
        while (validateStrByLength(resultStr) != 19 && validateStrByLength(resultStr) != 20 && validateStrByLength(resultStr) != 21
            && validateStrByLength(resultStr) != 22)
        {
            i++;
            resultStr = text.substring(0, i);
        }
        return resultStr;
    }
    
    /**
     * 将拼接好的字符串(key1:value1,key2:value2,key3:value3)解析成Map
     * 
     * @param strValue
     * @return
     */
    public static Map<String, Object> splitToMap(String strValue)
    {
        if (StringUtil.isEmpty(strValue))
        {
            return null;
        }
        try
        {
            Map<String, Object> map = new Hashtable<String, Object>();
            String[] pairs = strValue.split(",");
            for (String pair : pairs)
            {
                String[] keyValue = pair.split(":");
                if (keyValue.length > 1)
                {
                    map.put(keyValue[0], keyValue[1]);
                }
            }
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 按照正则表达式截取字符串，返回匹配子串的List
     * 
     * @param regex
     * @param strValue
     * @return
     */
    public static List<String> regexSplit(String regex, String strValue)
    {
        List<String> result = new ArrayList<String>();
        if (StringUtil.isEmpty(strValue))
        {
            return result;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strValue);
        while (matcher.find())
        {
            for (int i = 1; i <= matcher.groupCount(); i++)
            {
                result.add(matcher.group(i));
            }
        }
        return result;
    }
    
    /** 
     * 检查参数长度范围
     * @param strParm 请求参数对象
     * @param strLength 请求参数的最大长度
     * @return 检查后的请求参数
     */
    public static String checkParm(String strParm, int strLength)
    {
        String str = null;
        if (StringUtil.isNotEmpty(strParm))
        {
            if (strParm.getBytes().length > strLength)
            {
                str = StringUtil.substringSelf(strParm, strLength);
            }
            else
            {
                str = strParm;
            }
        }
        return str;
    }
    
    /** 
     *  按字节截取字符串  
     * @param orignal 原始字符串
     * @param count  截取位数
     * @return  截取后的字符串  
     * @throws UnsupportedEncodingException 
     *             使用了JAVA不支持的编码格式  
     */
    public static String substringSelf(String orignal, int count)
    {
        // 原始字符不为null，也不是空字符串   
        if (StringUtil.isNotEmpty(orignal))
        {
            // 要截取的字节数大于0，且小于原始字符串的字节数   
            if (count > 0 && count < orignal.getBytes().length)
            {
                StringBuffer buff = new StringBuffer();
                char c;
                for (int i = 0; i < count; i++)
                {
                    c = orignal.charAt(i);
                    buff.append(c);
                    if (isChineseChar(c))
                    {
                        // 遇到中文汉字，截取字节总数减2   
                        count = count - 2;
                    }
                }
                return buff.toString();
            }
        }
        return orignal;
    }
    
    /** 
    * 判断是否是一个中文汉字 
    * @param c 传入的参数单个字符
    * @return true表示是中文汉字，false表示是英文字母
    * @throws UnsupportedEncodingException
    *              使用了JAVA不支持的编码格式 
    */
    public static boolean isChineseChar(char c)
    {
        // 如果字节数大于1，是汉字   
        // 以这种方式区别英文字母和中文汉字
        return String.valueOf(c).getBytes().length > 1;
    }
    
    /** 
     * 检查订单金额是否保留两位小数
     * @param amount 订单金额
     * @return 检查后的订单金额
     */
    public static float checkAmount(float amount)
    {
        return (float)(Math.round(amount * 100)) / 100;
    }
    
    /**
     * 检查字符串 key value 都不为空时才不为空
     *
     * @param str 需要检查的字符
     * @param compare 字符串
     * @return true 包含改字符 false 不包含改字符
     */
    public static boolean isNotEmptyKeyVal(String key, String value)
    {
        boolean bool = true;
        if (isEmpty(key))
        {
            bool = false;
        }
        if (isEmpty(value))
        {
            bool = false;
        }
        
        return bool;
    }
    
    /**
     * 格式是否正确
     * @param fileName  文件的名字
     * @param trueFormat 正确的格式
     * @return
     */
    public static boolean isAvaliableFileFmt(String fileName, String trueFormat)
    {
        //int appointStrNum = getSpecifyStrCountInStr(fileName, ".");
        //if (!StringUtil.isEmpty(fileName) && appointStrNum == 1 && !isDigital(trueFormat))
        if (!StringUtil.isEmpty(fileName) && !isDigital(trueFormat))
        {//文件名不为空并且存在一个“.”
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
            List<String> allowedFmt = Arrays.asList(trueFormat.split(","));
            if (!StringUtil.isEmpty(fileType.toLowerCase()) && allowedFmt.contains(fileType.toLowerCase()))
            {//文件后缀都转换为小写
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取字符串中存在指定字符串数量
     * 
     * @param sourceStr
     *            原字符串
     * @param specifyStr
     *            指定字符
     * @return
     */
    public static int getSpecifyStrCountInStr(String sourceStr, String specifyStr)
    {
        int count = 0;
        int fromIndex = 0;
        while (sourceStr.indexOf(specifyStr, fromIndex) != -1)
        {
            int index = sourceStr.indexOf(specifyStr, fromIndex);
            fromIndex = index + specifyStr.length();
            count++;
        }
        return count;
    }
    
    /**
     * 判断字符串是否为数字类型
     * 
     * @param stringValue
     *            参数字符串
     * @return true-非数字 false-数字
     */
    public static boolean isDigital(String stringValue)
    {
        
        if (StringUtil.isEmpty(stringValue))
        {
            return false;
        }
        
        for (int i = 0; i < stringValue.toCharArray().length; i++)
        {
            char c = stringValue.toCharArray()[i];
            if (!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }
}
