package cn.com.eju.deal.core.helper;

import cn.com.eju.deal.core.util.StringShiftUti;

/**   
* <p>HTML帮助类。</p>
* 提供常用的HTML文本处理。
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:51:18
*/
public final class HtmlHelper
{
    /**
     * <p>转义HTML特殊字符。</p>
     *
     * <b>转义对照表：</b>
     * <table border="1">
     *  <tr>
     *      <th>名称</th>
     *      <th align="center">字符</th>
     *      <th>转义</th>
     *  </tr>
     *  <tr>
     *      <td>和号</td>
     *      <td align="center">&amp;</td>
     *      <td>&amp;amp;</td>
     *  </tr>
     *  <tr>
     *      <td>小于号</td>
     *      <td align="center">&lt;</td>
     *      <td>&amp;lt;</td>
     *  </tr>
     *  <tr>
     *      <td>大于号</td>
     *      <td align="center">&gt;</td>
     *      <td>&amp;gt;</td>
     *  </tr>
     *  <tr>
     *      <td>单引号</td>
     *      <td align="center">&acute;</td>
     *      <td>&amp;acute;</td>
     *  </tr>
     *  <tr>
     *      <td>双引号</td>
     *      <td align="center">&quot;</td>
     *      <td>&amp;quot;</td>
     *  </tr>
     *  <tr>
     *      <td>版权符</td>
     *      <td align="center">&copy;</td>
     *      <td>&amp;copy;</td>
     *  </tr>
     *  <tr>
     *      <td>注册符</td>
     *      <td align="center">&reg;</td>
     *      <td>&amp;reg;</th>
     *  </tr>
     *  <tr>
     *      <td>空格</td>
     *      <td align="center">&nbsp;</td>
     *      <td>&amp;nbsp;</th>
     *  </tr>
     * </table>
     *
     * 本操作与{@link #restore(String)}互为逆操作
     *
     * @return 转义后的字符串
     *
     * @see #restore(String)
     */
    public static String escape(String str)
    {
        StringShiftUti st = new StringShiftUti(str);
        st.replace("&", "&amp;");
        st.replace("<", "&lt;");
        st.replace(">", "&gt;");
        st.replace("\'", "&#39;");
        st.replace("\"", "&#34;");
        st.replace("©", "&copy;");
        st.replace("®", "&reg;");
        st.replace(" ", "&nbsp;");
        st.replace("\n", "<br>");

        return st.toString();
    }

    /**
     * <p>针对编辑框转义HTML特殊字符。</p>
     * 与{@link #escape(String)}}不同的是对空格的处理，<br>
     * 此方法将空格处理为&amp;#32;即原始空格，而不是HTML中的不间断空格&amp;nbsp;<br>
     *
     * <b>转义对照表：</b>
     * <table border="1">
     *  <tr>
     *      <th>名称</th>
     *      <th align="center">字符</th>
     *      <th>转义</th>
     *  </tr>
     *  <tr>
     *      <td>和号</td>
     *      <td align="center">&amp;</td>
     *      <td>&amp;amp;</td>
     *  </tr>
     *  <tr>
     *      <td>小于号</td>
     *      <td align="center">&lt;</td>
     *      <td>&amp;lt;</td>
     *  </tr>
     *  <tr>
     *      <td>大于号</td>
     *      <td align="center">&gt;</td>
     *      <td>&amp;gt;</td>
     *  </tr>
     *  <tr>
     *      <td>单引号</td>
     *      <td align="center">&acute;</td>
     *      <td>&amp;acute;</td>
     *  </tr>
     *  <tr>
     *      <td>双引号</td>
     *      <td align="center">&quot;</td>
     *      <td>&amp;quot;</td>
     *  </tr>
     *  <tr>
     *      <td>版权符</td>
     *      <td align="center">&copy;</td>
     *      <td>&amp;copy;</td>
     *  </tr>
     *  <tr>
     *      <td>注册符</td>
     *      <td align="center">&reg;</td>
     *      <td>&amp;reg;</th>
     *  </tr>
     *  <tr>
     *      <td>空格</td>
     *      <td align="center">&nbsp;</td>
     *      <td>&amp;#32;</th>
     *  </tr>
     * </table>
     *
     * 本操作与{@link #restore(String)}互为逆操作
     *
     * @return 转义后的字符串
     *
     * @see #restore(String)
     */
    public static String escape4edit(String str)
    {
        StringShiftUti st = new StringShiftUti(str);
        st.replace("&", "&amp;");
        st.replace("<", "&lt;");
        st.replace(">", "&gt;");
        st.replace("\'", "&#39;");
        st.replace("\"", "&#34;");
        st.replace("©", "&copy;");
        st.replace("®", "&reg;");
        st.replace(" ", "&#32;");

        return st.toString();
    }

    /**
     * <p>回复HTML转义字符。</p>
     *
     * <b>转义对照表：</b>
     * <table border="1">
     *  <tr>
     *      <th>名称</th>
     *      <th align="center">字符</th>
     *      <th>转义</th>
     *  </tr>
     *  <tr>
     *      <td>和号</td>
     *      <td align="center">&amp;</td>
     *      <td>&amp;amp;</td>
     *  </tr>
     *  <tr>
     *      <td>小于号</td>
     *      <td align="center">&lt;</td>
     *      <td>&amp;lt;</td>
     *  </tr>
     *  <tr>
     *      <td>大于号</td>
     *      <td align="center">&gt;</td>
     *      <td>&amp;gt;</td>
     *  </tr>
     *  <tr>
     *      <td>单引号</td>
     *      <td align="center">&acute;</td>
     *      <td>&amp;acute;</td>
     *  </tr>
     *  <tr>
     *      <td>双引号</td>
     *      <td align="center">&quot;</td>
     *      <td>&amp;quot;</td>
     *  </tr>
     *  <tr>
     *      <td>版权符</td>
     *      <td align="center">&copy;</td>
     *      <td>&amp;copy;</td>
     *  </tr>
     *  <tr>
     *      <td>注册符</td>
     *      <td align="center">&reg;</td>
     *      <td>&amp;reg;</th>
     *  </tr>
     *  <tr>
     *      <td>空格</td>
     *      <td align="center">&nbsp;</td>
     *      <td>&amp;nbsp;</th>
     *  </tr>
     * </table>
     *
     * 本操作与{@link #escape(String)}互为逆操作
     *
     * @return 转义前的字符串
     *
     * @see #escape(String)
     */
    public static String restore(String str)
    {
        StringShiftUti st = new StringShiftUti(str);
        st.replace("&nbsp;", " ");
        st.replace("&reg;", "®");
        st.replace("&copy;", "©");
        st.replace("&acute;", "\'");
        st.replace("&quot;", "\"");
        st.replace("&gt;", ">");
        st.replace("&lt;", "<");
        st.replace("&amp;", "&");

        return st.toString();
    }
}
