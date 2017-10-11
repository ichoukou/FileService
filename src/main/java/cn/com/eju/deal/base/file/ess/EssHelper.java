package cn.com.eju.deal.base.file.ess;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.eju.deal.core.util.StringUtil;

/**   
 * weiphoto 帮助类
* weiphoto文件上传--获取文件路径、文件下载 
* @author (li_xiaodong)
* @date 2016年5月28日 下午4:46:55
*/
public class EssHelper
{
    
    /** 
    * 文件上传
    * @param request(文件上传是否预处理isHandle(非必填){width、height、cutType、waterPosition、waterPic} (多文件上传用途标示("fileSign")) )
    * @return {returnCode=200,returnMsg=上传文件成功, data=[{returnCode=200, returnMsg=上传文件成功, fileCode=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,
    *  fileSign="**",fileName="**" ,
    *  pic_path=/BHME/Source_pic/18/d3/BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,pic_id=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg}]
    *  }
    */
    public static Map<String, Object> upload(HttpServletRequest request, String fileNo, Map<?, ?> configMap)
        throws Exception
    {
        
        //上传图片地址
        String uploadUrl = (String)configMap.get("ESS_upload_url");
        
        return EssHandle.upload(request, uploadUrl, fileNo);
        
    }
    
    public static String getFilePath(String fileUrl, String fileCode)
        throws Exception
    {
        return getFilePath(fileUrl, fileCode, "2", null, null);
    }
    
    public static String getFilePath(String fileUrl, String fileCode, String width, String height)
        throws Exception
    {
        return getFilePath(fileUrl, fileCode, "2", width, height);
    }
    
    /** 
    * 获取图片路径
    * @param fileUrl 上传文件Url (http://demo1.essintra.ejudata.com/%s@imageView2/%s/w/%s/h/%s)
    * @param fileCode 文件Code
    * @param modeType 预览模式  （模式0适合移动设备上做缩略图，模式2适合PC上做缩略图。）
    * @param width   
    * @param height
    *        
    * （/2/w/<Width>/h/<Height> 限定缩略图的宽最多为 <Width> ，高最多为 <Height> ，
    * 进行等比缩放，不裁剪。如果只 指定 w 参数则表示限定宽（长自适应），只指定 h 参数则表示限定长（宽自适 应）。
    * 它和模式0类似，区别只是限定宽和高，不是限定长边和短边。从应用场景来 说，模式0适合移动设备上做缩略图，模式2适合PC上做缩略图。）
    * 
    * @return
    * @throws Exception
    */
    public static String getFilePath(String fileUrl, String fileCode, String modeType, String width, String height)
        throws Exception
    {
        //文件路径
        String filePath = null;
        
        //取原图，截取fileUrl
        if (StringUtil.isEmpty(width) && StringUtil.isEmpty(height))
        {
            fileUrl = fileUrl.substring(0, fileUrl.indexOf("@"));
            filePath = String.format(fileUrl, fileCode);
        }
        else
        {
            filePath = String.format(fileUrl, fileCode, modeType, width, height);
        }
        
        return filePath;
    }
    
}
