package cn.com.eju.deal.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**   
* SQL语句工具类。
* 提供SQL相关的文本处理。
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:55:25
*/
public final class SqlUtil
{
    private static final String DEFAULT_SPLIT_STR = "," + (char)28;
    
    /**
     * SQL清理。
     *
     * 用于处理从配置文件中读取出来的SQL，
     * 清理其中存在大量空格、制表符等字符，
     * 非可见字符、制表符会被当作空白处理，
     * 连续的空白将会被当作1个空白处理
     *
     * @param sql 待处理的SQL语句
     * @return 清理过后的SQL语句,如果参数为null,那么结果也为null
     */
    public static String cleanSql(final String sql)
    {
        String result = null;
        if (sql != null)
        {
            String commentRemovedSql = removeComment(sql);
            result = removeBlank(commentRemovedSql);
        }
        
        return result;
    }
    
    /*
     * 去除SQL语句中的注释
     *
     * @param sql 待处理的SQL
     * @return 无注释的SQL
     */
    private static String removeComment(final String sql)
    {
        assert sql != null : "The parameter sql SHOULD NOT be null!";
        
        // 第1轮 先清除/* ... */ 块注释
        String[] sqlFragmentArr = StringUtil.split(sql, "/*");
        StringBuilder builder1 = new StringBuilder();
        for (String sqlFragment : sqlFragmentArr)
        {
            int blockCommentIdx = sqlFragment.indexOf("*/");
            if (blockCommentIdx != -1)
            {
                sqlFragment = sqlFragment.substring(blockCommentIdx + 2);
            }
            
            builder1.append(sqlFragment);
        }
        
        // 第2轮 清除-- .... 行注释
        byte[] data = builder1.toString().getBytes();
        InputStream in = new ByteArrayInputStream(data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
        StringBuilder builder2 = new StringBuilder();
        String line = null;
        while (true)
        {
            try
            {
                line = reader.readLine();
            }
            catch (IOException e)
            {
                // 发生异常即可终止读取
            }
            
            if (null == line)
            {
                break;
            }
            
            String sqlFragment = line;
            int lineCommentStrIdx = line.indexOf("--");
            if (lineCommentStrIdx != -1)
            {
                sqlFragment = line.substring(0, lineCommentStrIdx);
            }
            
            builder2.append(sqlFragment);
            builder2.append("\r\n");
        }
        
        return builder2.toString();
    }
    
    /*
     * 去除SQL语句中的空白
     *
     * @param sql 待处理的SQL
     * @return 处理后的SQL
     */
    private static String removeBlank(String sql)
    {
        assert sql != null : "The parameter sql SHOULD NOT be null!";
        
        StringBuilder builder = new StringBuilder();
        
        String trimedSql = sql.trim();
        if (!trimedSql.isEmpty())
        {
            char[] charArr = trimedSql.toCharArray();
            boolean hasBlank = false;
            boolean isInString = true;
            for (int i = 0; i < charArr.length; i++)
            {
                char c = charArr[i];
                if (c == '\'')
                {
                    builder.append(c);
                    isInString = !isInString;
                }
                else if (c <= ' ') // 判定为空白字符
                {
                    if (isInString && hasBlank)
                    {
                        continue;
                    }
                    else
                    {
                        builder.append(' ');
                        hasBlank = true;
                    }
                }
                else
                {
                    //builder.append(isInString ? Character.toUpperCase(c) : c);
                    builder.append(isInString ? c : c);
                    hasBlank = false;
                }
            }
        }
        
        return builder.toString();
    }
    
    public static String[] split(String param)
    {
        return StringUtil.split(param, DEFAULT_SPLIT_STR);
    }
    
    /**
     * 规整SQL语句中in语法中的参数。
     *
     * 默认按文本处理，将会用单引号包裹起每一个段
     *
     * @param sqlFragment 待处理的SQL片段
     * @return 规整后的参数
     *
     * @see #format4in(String, char, boolean)
     *
     * @deprecated 废弃方法
     */
    @Deprecated
    public static String format4in(String sqlFragment)
    {
        return format4in(sqlFragment, DEFAULT_SPLIT_STR, false);
    }
    
    /**
     * 规整SQL语句中in语法中的参数。
     *
     * 默认按文本处理，将会用单引号包裹起每一个段
     *
     * @param sqlFragment 待处理的SQL片段
     * @param splitChar 分隔符
     * @return 规整后的参数
     *
     * @see #format4in(String, char, boolean)
     */
    public static String format4in(String sqlFragment, char splitChar)
    {
        return format4in(sqlFragment, splitChar, false);
    }
    
    /**
     * 规整SQL语句中in语法中的参数。
     *
     * 给定的分隔符和参数类型将决定处理的结果，
     * 数字参数为逗号分割，文本类型将会用单引号包裹起每一个段
     *
     * @param sqlFragment 待处理的SQL片段
     * @param splitChar 分隔符
     * @param isNumber 是否为数字
     * @return 规整后的参数
     */
    public static String format4in(String sqlFragment, char splitChar, boolean isNumber)
    {
        String[] fragmentArr = StringUtil.split(sqlFragment, splitChar);
        return rebuild(fragmentArr, String.valueOf(splitChar), isNumber);
    }
    
    /**
     * 规整SQL语句中in语法中的参数。
     *
     * 默认按文本处理，将会用单引号包裹起每一个段
     *
     * @param sqlFragment 待处理的SQL片段
     * @param splitStr 分隔符
     * @return 规整后的参数
     *
     * @see #format4in(String, char, boolean)
     */
    public static String format4in(String sqlFragment, String splitStr)
    {
        return format4in(sqlFragment, splitStr, false);
    }
    
    /**
     * 规整SQL语句中in语法中的参数。
     *
     * 给定的分隔符和参数类型将决定处理的结果，
     * 数字参数为逗号分割，文本类型将会用单引号包裹起每一个段
     *
     * @param sqlFragment 待处理的SQL片段
     * @param splitStr 分隔符
     * @param isNumber 是否为数字
     * @return 规整后的参数
     */
    public static String format4in(String sqlFragment, String splitStr, boolean isNumber)
    {
        String[] fragmentArr = StringUtil.split(sqlFragment, splitStr);
        return rebuild(fragmentArr, splitStr, isNumber);
    }
    
    /*
     * 规整SQL语句中in语法中的参数。
     *
     * 给定的分隔符和参数类型将决定处理的结果，
     * 数字参数为逗号分割，文本类型将会用单引号包裹起每一个段
     *
     * @param sqlFragment 待处理的SQL片段
     * @param splitChar 分隔符
     * @param isNumber 是否为数字
     * @return 规整后的参数
     */
    private static String rebuild(String[] fragmentArr, String splitStr, boolean isNumber)
    {
        assert fragmentArr != null : "The parameter fragmentArr SHOULD NOT be null!";
        assert splitStr != null : "The parameter splitStr SHOULD NOT be null!";
        assert !splitStr.isEmpty() : "The parameter splitStr SHOULD NOT be empty!";
        
        /*
         * 1. 切割字符串
         * 2. 处理转义字符
         *      2.1 当发现切割后的碎片末尾是转义标记字符'\'时，判定为过度切割
         *      2.2 放入缓冲区
         *      2.3 继续检查后续碎片，直到找到非过度切割位置
         *      2.4 拼接过度切割碎片，使其合并
         * 3. 切割修正完毕，根据isNumber的值决定是否给每段碎片两端包裹单引号
         * 4. 拼接成结果
         */
        List<String> fragmentList = new LinkedList<String>();
        StringBuilder buff = new StringBuilder();
        for (String fragment : fragmentArr)
        {
            buff.append(fragment);
            if (fragment.endsWith("\\"))
            {
                buff.delete(buff.length() - 1, buff.length());
                buff.append(splitStr);
            }
            else
            {
                fragmentList.add(buff.toString());
                buff.delete(0, buff.length());
            }
        }
        
        StringBuilder fragmentBuff = new StringBuilder();
        for (String fragment : fragmentList)
        {
            if (!isNumber)
            {
                fragmentBuff.append('\'');
                fragmentBuff.append(fragment);
                fragmentBuff.append('\'');
            }
            else
            {
                fragmentBuff.append(fragment);
            }
            fragmentBuff.append(',');
        }
        
        if (fragmentBuff.length() > 0)
        {
            fragmentBuff.delete(fragmentBuff.length() - 1, fragmentBuff.length());
        }
        
        String result = fragmentBuff.toString();
        if (result.trim().isEmpty())
        {
            result = null;
        }
        
        return result;
    }
    
}
