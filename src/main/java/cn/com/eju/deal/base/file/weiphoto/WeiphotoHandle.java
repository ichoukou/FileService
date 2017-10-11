package cn.com.eju.deal.base.file.weiphoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import cn.com.eju.deal.base.file.util.FileEmulator;
import cn.com.eju.deal.base.file.util.FileUtil;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.support.ReturnMsg;
import cn.com.eju.deal.core.support.SystemCfg;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;

/**   
 * weiphoto 处理类
* weiphoto文件系统-文件处理入口
* @author (li_xiaodong)
* @date 2016年6月29日 下午9:35:39
*/
public class WeiphotoHandle
{
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(WeiphotoHelper.class);
    
    /**
     * 最大文件大小
     */
    //public static final long UPLOAD_FILE_MAX_SIZE = 1024 * 1024 * 10;
    
    /** 
    * 文件--默认
    * @param request
    * @param headerMap
    * @param uploadUrl
    * @param fileNo
    * @param uploadFileUrl
    * @return
    * @throws Exception
    */
    public static Map<String, Object> upload(HttpServletRequest request, Map<String, String> headerMap,
        String uploadUrl, String fileNo, String uploadFileUrl)
        throws Exception
    {
        return upload(request, headerMap, uploadUrl, fileNo, uploadFileUrl, null);
    }
    
    /** 
    * 文件--流
    * @param request
    * @param headerMap
    * @param uploadUrl
    * @param fileNo
    * @param uploadFileUrl
    * @return
    * @throws Exception
    */
    public static Map<String, Object> upload(HttpServletRequest request, InputStream inputStream,
        String uploadUrl, String fileNo, String uploadFileUrl)
        throws Exception
    {
        return upload(request, null, uploadUrl, fileNo, uploadFileUrl, inputStream);
    }
    
    /** 
    * 文件上传
    * @param request(文件上传是否预处理isHandle(非必填){width、height、cutType、waterPosition、waterPic} (多文件上传用途标示("fileSign")) )
    * @return {returnCode=200,returnMsg=上传文件成功, data=[{returnCode=200, returnMsg=上传文件成功, fileCode=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,
    *  fileSign="**",fileName="**" ,
    *  pic_path=/BHME/Source_pic/18/d3/BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,pic_id=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg}]
    *  }
    */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> upload(HttpServletRequest request, Map<String, String> headerMap,
        String uploadUrl, String fileNo, String uploadFileUrl, InputStream inputStream)
        throws Exception
    {
        
        //总返回Map
        Map<String, Object> rpMap = new HashMap<String, Object>();
        
        //返回数据list
        List<Map<String, Object>> rspList = new ArrayList<Map<String, Object>>();
        
        //返回map
        Map<String, Object> rspMap = new HashMap<String, Object>();
        
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
                
                //上传到Url
                String url = uploadUrl;
                
                //判断文件类型是否是图片
                String uploadPicSuffix = SystemCfg.getString("uploadPicSuffix");
                if (StringUtil.isEmpty(uploadPicSuffix))
                {
                    uploadPicSuffix = "jpg,png,jpeg,bmp";
                }
                
                String fileType = "pic";
                if (!StringUtil.isAvaliableFileFmt(fileName, uploadPicSuffix))
                {
                    fileType = "file";
                    url = uploadFileUrl;
                }
                
                try
                {
                    
                    //获取绝对路径
                    String absolutePath = FileUtil.getTmpRealPath(fileName, request);
                    
                    //生成临时文件
                    File tempFile = new File(absolutePath);
                    importFile.transferTo(tempFile);
                    
                    //上传操作
                    
                    //fileItemMap 设定要上传的文件
                    Map<String, String> fileItemMap = new HashMap<String, String>();
                    //fileType类型(图片pic/文件file)
                    fileItemMap.put(fileType, absolutePath);
                    
                    //上传操作
                    FileEmulator fileEmulator = new FileEmulator();
                    String response = fileEmulator.executeWeiphotoUpload(url, fileItemMap, headerMap, inputStream);
                    
                    System.out.println("Response from server is: " + response);
                    
                    rspMap = JsonUtil.parseToObject(response, Map.class);
                    
                    //删除临时文件
                    tempFile.delete();
                    
                    //上传返回失败
                    
                    //接口返回状态
                    //String apistatus = (String)rspMap.get("apistatus");
                    
                    //接口结果返回
                    Map<String, Object> resultMap = (Map<String, Object>)rspMap.get("result");
                    
                    //返回id
                    String fileId = null;
                    
                    //图片上传
                    if ("pic".equals(fileType))
                    {
                        fileId = (String)resultMap.get("picid");
                        //文件上传    
                    }
                    else
                    {
                        fileId = (String)resultMap.get("fileid");
                    }
                    
                    if (StringUtil.isEmpty(fileId))
                    {
                        rspMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                        rspMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                        rspMap.put("fileSign", importFile.getName());
                        rspMap.put("fileName", fileName);
                        rspList.add(rspMap);
                        
                        //只要有一个上传失败，设置返回失败吗
                        rpMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                        rpMap.put(Constant.RETURN_MSG_KEY, "调用weiphoto接口异常");
                        rpMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                        
                        return rpMap;
                    }
                    
                    rspMap.put(Constant.RETURN_CODE_KEY, ReturnCode.SUCCESS);
                    rspMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.SUCCESS_UPLOAD_FILE_MSG);
                    rspMap.put("fileSign", importFile.getName());
                    rspMap.put("fileName", fileName);
                    rspMap.put("fileCode", fileId);
                    //文件类型
                    rspMap.put("fileType", fileType);
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
                catch (Exception e)
                {
                    e.printStackTrace();
                    logger.error("", e);
                }
            }
        }
        
        rpMap.put("data", rspList);
        
        return rpMap;
    }
    
}
