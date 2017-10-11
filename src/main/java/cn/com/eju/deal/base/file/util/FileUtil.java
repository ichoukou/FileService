package cn.com.eju.deal.base.file.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
import cn.com.eju.deal.core.support.SystemCfg;
import cn.com.eju.deal.core.util.DateUtil;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;

/**   
* 文件上传工具类
* @author (li_xiaodong)
* @date 2016年12月25日 下午1:08:43
*/
public class FileUtil
{
    private static final String DATE_FORMAT = "HHmmssSSS";
    
    private static final SimpleDateFormat DATE_DIR_SDF = new SimpleDateFormat("yyyyMMdd");
    
    /**
     * 得到一个临时文件绝对路径
     * @param fileName 文件名 如 a.txt
     * @param request
     * @return
     */
    public static String getTmpRealPath(String fileName, HttpServletRequest request)
    {
        
        String tempFilePath = SystemCfg.getString("tempFilePath");
        
        return getTmpRealPath(request, fileName, tempFilePath);
    }
    
    /**
     * 得到一个临时文件绝对路径
     * @param fileName 文件名 如 a.txt
     * @param request
     * @return
     */
    public static String getTmpRealPath(HttpServletRequest request, String fileName, String tempFilePath)
    {
        Date date = new Date();
        tempFilePath = MessageFormat.format(tempFilePath, DATE_DIR_SDF.format(date));
        int index = fileName.lastIndexOf(".");
        if (index > 0)
        {
            fileName = DateUtil.fmtDate(new Date(), DATE_FORMAT) + fileName.substring(index);
        }
        // 创建目录
        File uploadDir = new File(request.getSession().getServletContext().getRealPath(tempFilePath));
        if (!uploadDir.exists())
        {
            uploadDir.mkdirs();
        }
        return uploadDir + File.separator + fileName;
    }
    
    /** 
     * 获得指定文件的byte数组 
     * @param file 上传的文件
     * @return byte数组
     */
    public static byte[] getFileBytes(File file)
    {
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
    
    /** 
     * 复制byte数组 
     * @param bytes byte数组 
     */
    public static byte[] appendBytes(byte[] bs, byte[] bytes)
    {
        byte[] newByte = new byte[bs.length + bytes.length];
        //arraycopy(被复制的数组, 从第几个元素开始复制, 要复制到的数组, 从第几个元素开始粘贴, 一共需要复制的元素个数);
        System.arraycopy(bs, 0, newByte, 0, bs.length);
        System.arraycopy(bytes, 0, newByte, bs.length, bytes.length);
        bs = newByte;
        
        return bs;
    }
    
    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr base64Image
     * @param outfile 文件保存路径
     * @return
     */
    public static File generateImageFile(String imgStr, String outfile)
    {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i)
            {
                if (bytes[i] < 0)
                {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            File f = new File(outfile);
            OutputStream out = new FileOutputStream(f);
            out.write(bytes);
            out.flush();
            out.close();
            return f;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /** 
    * (绑定参数到Map)
    * @param request
    * @return
    */
    public static Map<String, Object> bindParamToMap(HttpServletRequest request)
    {
        Enumeration<?> enumer = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        while (enumer.hasMoreElements())
        {
            String key = (String)enumer.nextElement();
            String val = request.getParameter(key);
            if (!"randomId".equals(key))
            {
                if ("orderBy".equals(key))
                {
                    if (!StringUtil.isEmpty(val))
                    {
                        Object orderByList = JsonUtil.parseToObject(val, List.class);
                        map.put(key, orderByList);
                    }
                    continue;
                }
                map.put(key, val);
            }
        }
        return map;
    }
}
