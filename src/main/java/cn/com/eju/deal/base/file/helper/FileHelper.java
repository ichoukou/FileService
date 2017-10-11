package cn.com.eju.deal.base.file.helper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.com.eju.deal.base.file.cric.CricHelper;
import cn.com.eju.deal.base.file.ess.EssHelper;
import cn.com.eju.deal.base.file.weiphoto.WeiphotoHelper;

/**   
* 文件上传、获取文件路径--
* （根据channelCode配置分发到不同文件的系统） 
* @author (li_xiaodong)
* @date 2015年10月30日 上午10:01:56
*/
public class FileHelper
{
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(FileHelper.class);
    
    /** 
    * 上传--默认
    * @param request
    * @param fileNo
    * @param channelCode
    * @param configMap
    * @return
    * @throws Exception
    */
    public static Map<String, Object> upload(HttpServletRequest request, String fileNo, String channelCode,
        Map<?, ?> configMap)
        throws Exception
    {
        return upload(request, null, null, fileNo, channelCode, configMap);
    }
    
    /** 
    * 上传-流方式
    * @param inputStream
    * @param fileName
    * @param fileNo
    * @param channelCode
    * @param configMap
    * @return
    * @throws Exception
    */
    public static Map<String, Object> upload(InputStream inputStream, String fileName, String fileNo,
        String channelCode, Map<?, ?> configMap)
        throws Exception
    {
        return upload(null, inputStream, fileName, fileNo, channelCode, configMap);
    }
    
    /** 
    * 文件上传
    * @param request(文件上传是否预处理isHandle(非必填){width、height、cutType、waterPosition、waterPic} (多文件上传用途标示("fileSign")) )
    * @return {returnCode=200,returnMsg=上传文件成功, data=[{returnCode=200, returnMsg=上传文件成功, fileCode=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,
    *  fileSign="**",fileName="**" ,
    *  pic_path=/BHME/Source_pic/18/d3/BHME1db22b048ab9b4b97cc743ffcc67e338.jpg,pic_id=BHME1db22b048ab9b4b97cc743ffcc67e338.jpg}]
    *  }
    */
    private static Map<String, Object> upload(HttpServletRequest request, InputStream inputStream, String fileName,
        String fileNo, String channelCode, Map<?, ?> configMap)
        throws Exception
    {
        //上传返回
        Map<String, Object> upMap = new HashMap<String, Object>();
        
        //weiphoto文件系统
        if ("weiphoto".equals(channelCode))
        {
            upMap = WeiphotoHelper.upload(request, fileNo, configMap);
            
        }
        //cric 文件系统
        else if ("CRIC".equals(channelCode))
        {
            
            upMap = CricHelper.upload(request, configMap);
        }
        //ESS 文件系统
        else if ("ESS".equals(channelCode))
        {
            
            upMap = EssHelper.upload(request, fileNo, configMap);
        }
        
        return upMap;
        
    }
    
    /** 
    * 获取文件路径 --重写最小集
    * @param channelCode 渠道编号(weiphoto/CRIC/ESS)
    * @param fileCode 文件编号
    * @param configMap 来自渠道系统对文件系统的配置信息
    * @return
    * @throws Exception
    */
    //    @Deprecated
    //    public static Map<String, Object> getFilePath(String channelCode, String fileCode, Map<?, ?> configMap)
    //        throws Exception
    //    {
    //        return getFilePath(channelCode, fileCode, configMap, null, null, null, null, null, null);
    //    }
    
    /** 
    * 获取文件路径 (weiphoto/cric/ess)--重写 -- 可指定weiphoto图片大小
    * @param channelCode 渠道编号(weiphoto CRIC)
    * @param fileCode 文件编号
    * @param configMap 来自渠道系统对文件系统的配置信息
    * @param fileType 文件类型(pic/file) 只对 weiphoto
    * @param weiphotoPicSize 图片文件大小  只对weiphoto
    * @param isCricHandle  图片是否需要预处理 只对CRIC
    * @param cricHandleMap 图片预处理信息 只对CRIC
    * @return
    */
    //    @Deprecated
    //    public static Map<String, Object> getFilePath(String channelCode, String fileCode, Map<?, ?> configMap,
    //        String fileType, String weiphotoPicSize)
    //        throws Exception
    //    {
    //        return getFilePath(channelCode, fileCode, configMap, fileType, weiphotoPicSize, null, null, null, null);
    //    }
    
    /** 
    * 获取文件路径 (weiphoto/cric/ess)--最大参数集
    * @param channelCode 渠道编号(weiphoto CRIC)
    * @param fileCode 文件编号
    * @param configMap 来自渠道系统对文件系统的配置信息
    * @param fileType 文件类型(pic/file) 只对 weiphoto
    * @param weiphotoPicSize 图片文件大小  只对weiphoto
    * @param isCricHandle  图片是否需要预处理 只对CRIC
    * @param cricHandleMap 图片预处理信息 只对CRIC
    * @param essWidth 图片预处理信息 只对ESS
    * @param essHeight 图片预处理信息 只对ESS
    * @return
    */
    //    @Deprecated
    //    public static Map<String, Object> getFilePath(String channelCode, String fileCode, Map<?, ?> configMap,
    //        String fileType, String weiphotoPicSize, String isCricHandle, Map<String, Object> cricHandleMap,
    //        String essWidth, String essHeight)
    //        throws Exception
    //    {
    //        //返回map
    //        Map<String, Object> rspMap = new HashMap<String, Object>();
    //        
    //        //文件路径
    //        String filePath = null;
    //        
    //        if ("CRIC".equals(channelCode))
    //        {
    //            // 查询路径
    //            String queryUrl = (String)configMap.get("CRIC_queryfile_path");
    //            // 下载路径
    //            String downloadUrl = (String)configMap.get("CRIC_downloadfile_path");
    //            // 授权号
    //            String permitCode = (String)configMap.get("CRIC_file_permit_code");
    //            // 授权号
    //            String cricKey = (String)configMap.get("CRIC_key");
    //            
    //            //自定义图片规格
    //            if (CricHandle.UPLOAD_FILE_IS_HANDLE_YES.equals(isCricHandle))
    //            {
    //                
    //                //预处理文件(pic)路径
    //                filePath =
    //                    CricHelper.getFilePath(fileCode,
    //                        CricHandle.UPLOAD_FILE_IS_HANDLE_YES,
    //                        queryUrl,
    //                        downloadUrl,
    //                        cricHandleMap,
    //                        permitCode,
    //                        cricKey);
    //                
    //            }
    //            else
    //            {
    //                //原文件路径
    //                filePath = CricHelper.getFilePath(fileCode, queryUrl, downloadUrl, permitCode, cricKey);
    //            }
    //            
    //        }
    //        else if ("weiphoto".equals(channelCode))
    //        {
    //            // 查询weiphoto系统文件路径
    //            String fileViewUrl = null;
    //            
    //            if (StringUtil.isEmpty(fileType) || "pic".equals(fileType))
    //            {
    //                // 查询weiphoto系统文件（pic）路径
    //                fileViewUrl = (String)configMap.get("wp_view_url");
    //                
    //                //获取特定大小图片
    //                if (StringUtil.isNotEmpty(weiphotoPicSize))
    //                {
    //                    
    //                    //预处理文件(pic)路径
    //                    filePath = WeiphotoHelper.getPicPath(fileViewUrl, fileCode, weiphotoPicSize);
    //                }
    //                else
    //                {
    //                    //原文件路径
    //                    filePath = WeiphotoHelper.getPicPath(fileViewUrl, fileCode);
    //                }
    //                
    //            }
    //            else
    //            {
    //                // 查询weiphoto系统文件（file）路径
    //                fileViewUrl = (String)configMap.get("wp_view_file_url");
    //                
    //                //原文件路径
    //                filePath = WeiphotoHelper.getFilePath(fileViewUrl, fileCode);
    //            }
    //            
    //        }
    //        else if ("ESS".equals(channelCode))
    //        {
    //            // 查询路径
    //            String queryUrl = (String)configMap.get("ESS_view_url");
    //            
    //            //原文件路径
    //            filePath = EssHelper.getFilePath(queryUrl, fileCode, essWidth, essHeight);
    //        }
    //        
    //        //返回文件path
    //        rspMap.put("fileUrl", filePath);
    //        
    //        return rspMap;
    //    }
    
}
