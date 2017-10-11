package cn.com.eju.deal.base.file.weiphoto;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**   
 * weiphoto 帮助类
* weiphoto文件上传--获取文件路径、文件下载 
* @author (li_xiaodong)
* @date 2016年5月28日 下午4:46:55
*/
public class WeiphotoHelper
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
        //headerMap{"H-appId":"","H-timeStamp":"","H-sign":""} 渠道号/签名时间(默认 单位秒)/签名
        @SuppressWarnings("unchecked")
        Map<String, String> headerMap = (Map<String, String>)configMap.get("wp_headerMap");
        
        //上传图片地址
        String uploadUrl = (String)configMap.get("wp_upload_url");
        
        //上传文件地址
        String uploadFileUrl = (String)configMap.get("wp_upload_file_url");
        
        return WeiphotoHandle.upload(request, headerMap, uploadUrl, fileNo, uploadFileUrl);
        
    }
    
    /** 
    * 获取图片路径(原图片文件)
    * @param fileCode "720-960-42e972556d09a1fdc280f0eb8e391264"
    * @param size 100
    * @return http://183.136.160.249:2080/pic/{pid}/{size}
    */
    //    @Deprecated
    //    public static String getPicPath(String fileUrl, String fileCode)
    //        throws Exception
    //    {
    //        String filePath = String.format(fileUrl, fileCode, "2000");
    //        
    //        return filePath;
    //    }
    
    /** 
    * 获取图片路径(特定图片文件)
    * @param fileCode "720-960-42e972556d09a1fdc280f0eb8e391264"
    * @param size 100
    * @return http://183.136.160.249:2080/pic/{pid}/{size}
    */
    //    @Deprecated
    //    public static String getPicPath(String fileUrl, String fileCode, String size)
    //        throws Exception
    //    {
    //        String filePath = String.format(fileUrl, fileCode, size);
    //        
    //        return filePath;
    //    }
    
    /** 
    * 获取文件路径
    * @param fileCode "720-960-42e972556d09a1fdc280f0eb8e391264"
    * @param size 100
    * @return http://183.136.160.249:2080/file/{pid}
    */
    //    @Deprecated
    //    public static String getFilePath(String fileUrl, String fileCode)
    //        throws Exception
    //    {
    //        String filePath = String.format(fileUrl, fileCode);
    //        
    //        return filePath;
    //    }
    
}
