package cn.com.eju.deal.file.handle;

import java.util.Map;

import org.apache.log4j.Logger;

/**   
* 文件上传、获取文件路径、文件下载 
* @author (li_xiaodong)
* @date 2015年10月30日 上午10:01:56
*/
public class CricHelper
{
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(CricHelper.class);
    
    /**
    * 文件上传是否预处理-是
    */
    public static final String UPLOAD_FILE_IS_HANDLE_YES = "1";
    
    /**
     * 文件上传是否预处理-否
     */
    public static final String UPLOAD_FILE_IS_HANDLE_NO = "0";
    
    /** 
    * 获取文件路径
    * @param fileCode 文件编号
    * @return
    */
    public static String getFilePath(String fileCode, String queryUrl, String downloadUrl, String permitCode,
        String cricKey)
    {
        return getFilePath(fileCode, UPLOAD_FILE_IS_HANDLE_NO, queryUrl, downloadUrl, null, permitCode, cricKey);
    }
    
    /** 
    * 获取文件路径
    * @param fileCode 文件码
    * @param isHandle  是否预处理
    * @param handleMap 预处理参数
    * @return "http://get.file.dc.cric.com/BHME1db22b048ab9b4b97cc743ffcc67e338_1X1_3_3_0.jpg"
    */
    public static String getFilePath(String fileCode, String isHandle, String queryUrl, String downloadUrl,
        Map<String, Object> handleMap, String permitCode, String cricKey)
    {
        //获取文件ID
        String picID = fileCode.substring(0, fileCode.lastIndexOf("."));
        //获取文件后缀名
        String suffixStr = fileCode.substring(fileCode.lastIndexOf("."));
        StringBuilder sb = new StringBuilder();
        
        //自定义图片规格
        if (UPLOAD_FILE_IS_HANDLE_YES.equals(isHandle))
        {
            
            sb.append(queryUrl + "/" + picID + "_");
            
            sb.append(handleMap.get("width") + "X" + handleMap.get("height"));
            sb.append("_" + handleMap.get("waterPic") + "_" + handleMap.get("waterPosition") + "_"
                + handleMap.get("cutType"));
            sb.append(suffixStr);
        }
        else
        {
            //实例化类
            //            CricHandle cricHandle = new CricHandle();
            
            //授权号
            //key
            //            String key = "";
            //            try
            //            {
            //                key = cricHandle.getMd5PermitCode(permitCode);
            //            }
            //            catch (Exception e1)
            //            {
            //                e1.printStackTrace();
            //            }
            sb.append(downloadUrl + "?file=" + picID + suffixStr + "&permit_code=" + permitCode + "&key=" + cricKey);
            //默认图片规格(1:宽高自适应  3:房友在线水印图片  3:左下角   0:不裁剪)
            //            sb.append("1X1_3_3_0");
        }
        
        return sb.toString();
    }
    
    /** 
    * 文件下载操作
    * @param response
    * @param fileCode 文件码
    * @param downloadFileName 用户自定义下载文件名
    */
    //    public static void downloadFile(HttpServletResponse response, String fileCode, String downloadFileName,
    //        String downloadUrl, String permitCode)
    //    {
    //        try
    //        {
    //            //实例化类
    //            CricHandle fileHelper = new CricHandle();
    //            
    //            //获取文件ID
    //            String picID = fileCode.substring(0, fileCode.lastIndexOf("."));
    //            
    //            //获取文件后缀名(如：.jpg)
    //            String suffixStr = fileCode.substring(fileCode.lastIndexOf("."));
    //            
    //            //组拼后的文件名【默认图片规格(1:宽高自适应  3:[房友在线]水印图片  3:左下角   0:不裁剪)】
    //            String fileName = picID + "_1X1_3_3_0" + suffixStr;
    //            
    //            //授权号
    //            
    //            //获取验证令牌:授权号和当天时间（格式如20151031）的md5值
    //            String mD5String = fileHelper.getMd5PermitCode(permitCode);
    //            
    //            //文本类型的参数
    //            Map<String, String> params = new HashMap<String, String>();
    //            
    //            //文件名(如:BHMEfafa5efeaf3cbe3b23b2748d13e629a1_1X1_3_3_0.jpg)
    //            params.put("file", fileName);
    //            
    //            //授权号
    //            params.put("permit_code", permitCode);
    //            
    //            //验证令牌
    //            params.put("key", mD5String);
    //            
    //            // 设置下载文件的路径
    //            //            URL url = new URL(SystemCfg.getString("downloadfile_path"));
    //            URL url = new URL(downloadUrl);
    //            
    //            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    //            
    //            // 设置HttpUrlConnection参数
    //            StringBuilder sb = fileHelper.setConnectionInfo(conn, params);
    //            
    //            //请求文件下载
    //            fileHelper.executeDownload(fileCode, downloadFileName, conn, sb, response);
    //        }
    //        catch (IOException e)
    //        {
    //            logger.error("", e);
    //        }
    //        catch (Exception e)
    //        {
    //            logger.error("", e);
    //        }
    //    }
    
}
