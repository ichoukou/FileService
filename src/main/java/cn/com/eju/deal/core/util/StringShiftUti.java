package cn.com.eju.deal.core.util;

/**   
 * 字符串变换器。
 *
 * 用于对字符串进行各种变换，支持链式操作<br>
 * 线程不安全，多线程环境下请谨慎操作!<br>
 *
 * 并通过{@link #toString()}方法获得当前处理结果<br>
 * 在任意情况下，对本类对象所做的任意操作，均不会影响源对象。<br>
 * 除{@link #toString()}方法会返回{@link String}外，其余方法均返回变换器自身
 *
 * <b>特别的：</b>
 * 在操作过程中任意的参数若为null,则该参数会被当空串处理)
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:55:53
*/
public final class StringShiftUti
{
    /* 原始字符串 */
    private String source;

    /* builder */
    private StringBuilder builder = new StringBuilder();

    /**
     * 通过{@link CharSequence}来构造变换器
     *
     * @param source 源
     */
    public StringShiftUti(CharSequence source)
    {
        super();
        if (source != null)
        {
            builder.append(source);
            this.source = builder.toString();
        }
    }

    /**
     * 安全的做字符串替换。
     *
     * 将<b>源</b>中的<b>目标字符串</b>全部替换成<b>替换字符串</b>
     * 规则如下：
     * <ol>
     *  <li>无论source是否为null,则结果总不为null</li>
     *  <li>若target为null,则结果为source</li>
     *  <li>若replacement为null,则结果为source中的target全部被剔除后的新字符串</li>
     * </ol>
     *
     * @param target 目标字符串
     * @param replacement 替换字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti replace(String target, String replacement)
    {
        /*
         * 以下情况不做任何处理，直接返回
         * 1.源为空
         * 2.查找目标对象为null或是空串
         * 3.查找目标对象与替换自字符串相等
         */
        if (builder.length() == 0 || target == null || target.isEmpty() || target.equals(replacement))
        {
            return this;
        }

        /*
         * 循环从source中搜寻target，定位后删除target并将replacement插入进去
         * 之后将下一轮的"搜寻起始偏移量"定位至此轮"偏移量"与replacement长度之和，
         * 以使replacement中的字符串避免被下一轮搜寻命中
         */
        int targetLen = target.length(); // 目标字符串长度
        int replacementLen = 0;
        if (replacement != null)
        {
            replacementLen = replacement.length(); // 替换字符串长度
        }
        int offset = 0;
        while(true)
        {
            offset = builder.indexOf(target, offset);
            if (offset == -1)
            {
                break;
            }

            // 先删后插，完成替换
            builder.delete(offset, offset + targetLen);
            if (replacementLen > 0)
            {
                builder.insert(offset, replacement);
            }

            //定位下一轮起始位置
            offset += replacementLen;
        }

        return this;
    }

    /**
     * 替换第一个字符串。
     *
     * 将<b>源</b>中的第一个<b>目标字符串</b>替换成<b>替换字符串</b>
     * 规则如下：
     * <ol>
     *  <li>无论source是否为null,则结果总不为null</li>
     *  <li>若target为null,则结果为source</li>
     *  <li>若replacement为null,则结果为source中的target全部被剔除后的新字符串</li>
     * </ol>
     *
     * @param target 目标字符串
     * @param replacement 替换字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti replaceFirst(String target, String replacement)
    {
        /*
         * 以下情况不做任何处理，直接返回
         * 1.源为空
         * 2.查找目标对象为null或是空串
         * 3.查找目标对象与替换自字符串相等
         */
        if (builder.length() == 0 || target == null || target.isEmpty() || target.equals(replacement))
        {
            return this;
        }

        /*
         * 循环从source中搜寻target，定位后删除target并将replacement插入进去
         * 之后将下一轮的"搜寻起始偏移量"定位至此轮"偏移量"与replacement长度之和，
         * 以使replacement中的字符串避免被下一轮搜寻命中
         */
        int targetLen = target.length(); // 目标字符串长度
        int replacementLen = 0;
        if (replacement != null)
        {
            replacementLen = replacement.length(); // 替换字符串长度
        }
        int offset = builder.indexOf(target);
        if (offset != -1)
        {
            // 先删后插，完成替换
            builder.delete(offset, offset + targetLen);
            if (replacementLen > 0)
            {
                builder.insert(offset, replacement);
            }
        }

        return this;
    }

    /**
     * 移除源中的指定字符串。
     *
     * @param target 指定要移除掉的目标字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti remove(String target)
    {
        return replace(target, null);
    }

    /**
     * 移除源中的首个指定字符串。
     *
     * @param target 指定要移除掉的目标字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti removeFirst(String target)
    {
        return replaceFirst(target, null);
    }

    /**
     * 增加前缀。
     *
     * @param prefix 前缀字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti addPrefix(String prefix)
    {
        if (StringUtil.isNotEmpty(prefix))
        {
            builder.insert(0, prefix);
        }

        return this;
    }

    /**
     * 增加后缀。
     *
     * @param suffix 后缀字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti addSuffix(String suffix)
    {
        if (StringUtil.isNotEmpty(suffix))
        {
            builder.append(suffix);
        }

        return this;
    }

    /**
     * 用指定字符串包裹当前字符串。
     *
     * 其操作结果等同于增加相同的前缀和后缀。
     *
     * @param warpStr 包裹字符串
     *
     * @return 变换器自身
     */
    public StringShiftUti warp(String warpStr)
    {
        if (StringUtil.isNotEmpty(warpStr))
        {
            addPrefix(warpStr).addSuffix(warpStr);
        }

        return this;
    }

    /**
     * 字符串逆置。
     *
     * 即将字符串中的字符反转<br>
     *
     * 例如将"abc"逆置的结果为"cba"
     *
     * @return 变换器自身
     */
    public StringShiftUti reverse()
    {
        // 此处调用StringBuilder中的reverse方法来完成
        builder.reverse();

        return this;
    }

    /**
     * 重置。
     *
     * 放弃已有更改，将字符串重置至上次保存的值，如果没有保存过，则为初始值。<br>
     * 调用{@link #store()}会影响此方法的返回值。
     *
     * @return 变换器自身
     * @see #store()
     */
    public StringShiftUti reset()
    {
        builder.delete(0, builder.length());
        builder.append(source);
        return this;
    }

    /**
     * 存储结果。
     *
     * 存储当前变换的结果，该操作会影响{@link #reset()}的操作<br>
     *
     * @return 变换器自身
     * @see #reset()
     */
    public StringShiftUti store()
    {
        source = builder.toString();
        return this;
    }

    /**
     * 获取处理结果。
     *
     * 如果在调用此方法后由做了进一步操作，那么这些操作仅会影响后续结果的获取
     *
     * @return 当前处理结果
     */
    @Override
    public String toString()
    {
        return builder.toString();
    }

}
