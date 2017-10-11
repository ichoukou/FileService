package cn.com.eju.deal.base.file.cric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.com.eju.deal.base.file.util.FileUtil;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.support.ReturnMsg;
import cn.com.eju.deal.core.util.StringUtil;

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
    * 文件上传
    * @param request(文件上传是否预处理isHandle(非必填){width、height、cutType、waterPosition、waterPic} (多文件上传用途标示("fileSign")) )
    * @return {returnCode=200,returnMsg=上传文件成功, data=[{returnCode=200, returnMsg=上传文件成功, fileCode=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,
    *  fileSign="**",fileName="**" pic_height=373, flag=true, pic_width=458, pic_size=40826,
    *  pic_path=/BHME/Source_pic/18/d3/BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,pic_id=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg}]
    *  }
    */
    public static Map<String, Object> upload(HttpServletRequest request, Map<?, ?> configMap)
    {
        //总返回Map
        Map<String, Object> rpMap = new HashMap<String, Object>();
        
        //返回数据list
        List<Map<String, Object>> rspList = new ArrayList<Map<String, Object>>();
        
        //获取请求参数
        Map<String, Object> reqMap = FileUtil.bindParamToMap(request);
        
        //预处理标示
        String isHandle = (String)reqMap.get("isHandle");
        
        Map<String, Object> optionalMap = new HashMap<String, Object>();
        
        //需要预处理
        if (CricHandle.UPLOAD_FILE_IS_HANDLE_YES.equals(isHandle))
        {
            optionalMap.put("width", reqMap.get("width"));
            optionalMap.put("height", reqMap.get("height"));
            optionalMap.put("cutType", reqMap.get("cutType"));
            optionalMap.put("waterPosition", reqMap.get("waterPosition"));
            optionalMap.put("waterPic", reqMap.get("waterPic"));
        }
        
        //返回map
        Map<String, Object> rspMap = new HashMap<String, Object>();
        String base64Str = request.getParameter("base64Str");
        //filePath 为页面上传值，为拍照后文件的文件流
        if (StringUtil.isEmpty(base64Str))
        {
            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
            
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request))
            {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext())
                {
                    MultipartFile importFile = multiRequest.getFile(iter.next());
                    
                    //文件名
                    String fileName = importFile.getOriginalFilename();
                    
                    try
                    {
                        //实例化类
                        CricHandle cricHandle = new CricHandle();
                        
                        //获取绝对路径
                        String absolutePath = FileUtil.getTmpRealPath(fileName, request);
                        
                        //生成临时文件
                        File tempFile = new File(absolutePath);
                        importFile.transferTo(tempFile);
                        
                        //上传操作
                        rspMap = cricHandle.upload(tempFile, fileName, isHandle, optionalMap, configMap);
                        tempFile.delete();//删除临时文件
                        //上传返回失败
                        //if (rspMap.get("flag").equals("false"))
                        
                        Boolean rspFlag = (Boolean)rspMap.get("flag");
                        
                        if (null == rspFlag || !rspFlag)
                        {
                            //                            rspMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                            //                            rspMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                            //                            rspMap.put("fileSign", importFile.getName());
                            //                            rspMap.put("fileName", fileName);
                            //                            rspList.add(rspMap);
                            
                            //只要有一个上传失败，设置返回失败吗
                            rpMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                            rpMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                            
                            return rpMap;
                        }
                        
                        rspMap.put(Constant.RETURN_CODE_KEY, ReturnCode.SUCCESS);
                        rspMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.SUCCESS_UPLOAD_FILE_MSG);
                        rspMap.put("fileSign", importFile.getName());
                        rspMap.put("fileName", fileName);
                        rspMap.put("fileCode", rspMap.get("pic_id"));
                        rspList.add(rspMap);
                        
                        logger.info("文件上传结果：" + rspList);
                        
                    }
                    catch (FileNotFoundException e)
                    {
                        logger.error("", e);
                    }
                    catch (IOException e)
                    {
                        logger.error("", e);
                    }
                }
            }
        }
        else
        {
            //拍照上传，只支持单个上传
            String fileName = request.getParameter("fileName");
            String strFileNames = request.getParameter("strFileNames");
            //实例化类
            CricHandle cricHandle = new CricHandle();
            //获取文件保存路径【服务器临时路径】
            String filePath = FileUtil.getTmpRealPath(fileName, request);
            //生成临时文件
            File tempFile = FileUtil.generateImageFile(base64Str, filePath);
            //上传操作
            rspMap = cricHandle.upload(tempFile, fileName, isHandle, optionalMap, configMap);
            if (StringUtil.isNotEmpty(strFileNames))
            {//为多张文件上传时，则需要删除合成pdf文件的图片文件
                String[] strFileNamesArray = strFileNames.split(",");
                for (int i = 0; i < strFileNamesArray.length; i++)
                {
                    File tempFileStr = new File(strFileNamesArray[i]);//根据路径找到临时文件进行删除
                    if (null != tempFileStr)
                    {
                        tempFileStr.delete();
                    }
                }
            }
            
            tempFile.delete();//删除临时文件
            
            //上传返回失败
            if (rspMap.get("flag").equals("false"))
            {
                //                rspMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                //                rspMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                //                rspMap.put("fileSign", tempFile.getName());
                //                rspMap.put("fileName", fileName);
                //                rspList.add(rspMap);
                
                //只要有一个上传失败，设置返回失败吗
                rpMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                rpMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                
                return rpMap;
            }
            
            rspMap.put(Constant.RETURN_CODE_KEY, ReturnCode.SUCCESS);
            rspMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.SUCCESS_UPLOAD_FILE_MSG);
            rspMap.put("fileSign", tempFile.getName());
            rspMap.put("fileName", fileName);
            rspMap.put("fileCode", rspMap.get("pic_id"));
            rspList.add(rspMap);
            
            logger.info("文件上传结果：" + rspList);
            
        }
        rpMap.put("data", rspList);
        
        return rpMap;
    }
    
    /** 
    * 获取文件路径
    * @param fileCode 文件编号
    * @return
    */
//    @Deprecated
//    public static String getFilePath(String fileCode, String queryUrl, String downloadUrl, String permitCode,
//        String cricKey)
//    {
//        return getFilePath(fileCode,
//            CricHandle.UPLOAD_FILE_IS_HANDLE_NO,
//            queryUrl,
//            downloadUrl,
//            null,
//            permitCode,
//            cricKey);
//    }
    
    /** 
    * 获取文件路径
    * @param fileCode 文件码
    * @param isHandle  是否预处理
    * @param handleMap 预处理参数
    * @return "http://get.file.dc.cric.com/BHME1db22b048ab9b4b97cc743ffcc67e338_1X1_3_3_0.jpg"
    */
//    @Deprecated
//    public static String getFilePath(String fileCode, String isHandle, String queryUrl, String downloadUrl,
//        Map<String, Object> handleMap, String permitCode, String cricKey)
//    {
//        //获取文件ID
//        String picID = fileCode.substring(0, fileCode.lastIndexOf("."));
//        //获取文件后缀名
//        String suffixStr = fileCode.substring(fileCode.lastIndexOf("."));
//        StringBuilder sb = new StringBuilder();
//        
//        //自定义图片规格
//        if (CricHandle.UPLOAD_FILE_IS_HANDLE_YES.equals(isHandle))
//        {
//            
//            sb.append(queryUrl + "/" + picID + "_");
//            
//            sb.append(handleMap.get("width") + "X" + handleMap.get("height"));
//            sb.append("_" + handleMap.get("waterPic") + "_" + handleMap.get("waterPosition") + "_"
//                + handleMap.get("cutType"));
//            sb.append(suffixStr);
//        }
//        else
//        {
//            //实例化类
//            //            CricHandle cricHandle = new CricHandle();
//            
//            //授权号
//            //key
//            //            String key = "";
//            //            try
//            //            {
//            //                key = cricHandle.getMd5PermitCode(permitCode);
//            //            }
//            //            catch (Exception e1)
//            //            {
//            //                e1.printStackTrace();
//            //            }
//            sb.append(downloadUrl + "?file=" + picID + suffixStr + "&permit_code=" + permitCode + "&key=" + cricKey);
//            //默认图片规格(1:宽高自适应  3:房友在线水印图片  3:左下角   0:不裁剪)
//            //            sb.append("1X1_3_3_0");
//        }
//        
//        return sb.toString();
//    }
//    
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
