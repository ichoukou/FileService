package cn.com.eju.deal.base.file.ess;

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

import cn.com.eju.deal.base.file.util.FileEmulator;
import cn.com.eju.deal.base.file.util.FileUtil;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.support.ReturnMsg;

/**   
* ESS 处理类
* @author (li_xiaodong)
* @date 2017年1月15日 下午4:47:58
*/
public class EssHandle
{
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(EssHandle.class);
    
    /**
     * 最大文件大小
     */
    public static final long UPLOAD_FILE_MAX_SIZE = 1024 * 1024 * 10;
    
    /** 
    * 文件上传
    * @param request(文件上传是否预处理isHandle(非必填){width、height、cutType、waterPosition、waterPic} (多文件上传用途标示("fileSign")) )
    * @return {returnCode=200,returnMsg=上传文件成功, data=[{returnCode=200, returnMsg=上传文件成功, fileCode=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,
    *  fileSign="**",fileName="**" ,
    *  pic_path=/BHME/Source_pic/18/d3/BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,pic_id=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg}]
    *  }
    */
    public static Map<String, Object> upload(HttpServletRequest request, String uploadUrl, String fileNo)
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
                
                //文件后缀
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                
                //文件Key
                String fileKey = fileNo + "." + fileSuffix;
                //String fileKey = fileNo;
                
                try
                {
                    
                    //获取绝对路径
                    String absolutePath = FileUtil.getTmpRealPath(fileName, request);
                    
                    //生成临时文件
                    File tempFile = new File(absolutePath);
                    importFile.transferTo(tempFile);
                    
                    //上传操作
                    FileEmulator fileEmulator = new FileEmulator();
                    
                    rspMap = fileEmulator.executeEssUpload(uploadUrl, fileKey, absolutePath);
                    
                    //删除临时文件
                    tempFile.delete();
                    
                    if (!ReturnCode.SUCCESS.equals(rspMap.get(Constant.RETURN_CODE_KEY)))
                    {
                        
                        //只要有一个上传失败，设置返回失败吗
                        rpMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
                        rpMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
                        
                        return rpMap;
                    }
                    
                    rspMap.put("fileSign", importFile.getName());
                    rspMap.put("fileName", fileName);
                    rspMap.put("fileType", fileSuffix);
                    rspMap.put("fileCode", fileKey);
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
