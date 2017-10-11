package cn.com.eju.deal.base.file.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.base.service.BaseService;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.util.StringUtil;

/**   
* 上传、预览的业务处理 部分
* @author li_xiaodong
* @date 2016年2月2日 下午7:57:09
*/
@Service("fileHelpService")
public class FileHelpService extends BaseService<Object>
{
    
    @Resource(name = "filesService")
    private FilesService filesService;
    
    /**
    * 日志
    */
    private final LogHelper logger = LogHelper.getLogger(this.getClass());
    
    /** 
     * 文件预览--默认使用方法-取原图
     * @param fileNo
     * @return     
     */
    public Map<String, Object> getFilePath(String fileNo)
        throws Exception
    {
        return getFilePath(fileNo, null, null, null, null, null);
    }
    
    /** 
    * 文件预览--weiphoto
    * @param fileNo
    * @param weiphotoPicSize
    * @return
    * @throws Exception
    */
    public Map<String, Object> getFilePath(String fileNo, String weiphotoPicSize)
        throws Exception
    {
        return getFilePath(fileNo, weiphotoPicSize, null, null, null, null);
    }
    
    /** 
    * 文件预览--CRIC
    * 1、获取渠道配置信息
    * 2、获取文件记录
    * 3、获取文件
    * @param fileNo
    * @param isCricHandle
    * @param cricHandleMap
    * @return
    * @throws Exception
    */
    public Map<String, Object> getFilePath(String fileNo, String isCricHandle, Map<String, Object> cricHandleMap)
        throws Exception
    {
        return getFilePath(fileNo, null, null, null, isCricHandle, cricHandleMap);
    }
    
    /** 
    * 文件预览--ESS
    * 1、获取渠道配置信息
    * 2、获取文件记录
    * 3、获取文件
    * @param fileNo
    * @param weiphotoPicSize
    * @return
    * @throws Exception
    */
    public Map<String, Object> getFilePath(String fileNo, String essWidth, String essHeight)
        throws Exception
    {
        return getFilePath(fileNo, null, essWidth, essHeight, null, null);
    }
    
    /** 
     * 文件预览--最大参数集
     * @param fileNo
     * @param weiphotoPicSize
     * @return
     * @throws Exception
     */
    private Map<String, Object> getFilePath(String fileNo, String weiphotoPicSize, String essWidth, String essHeight,
        String isCricHandle, Map<String, Object> cricHandleMap)
        throws Exception
    {
        //返回map
        Map<String, Object> rspMap = new HashMap<String, Object>();
        
        //请求参数
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("fileNo", fileNo);
        
        if (StringUtil.isNotEmpty(weiphotoPicSize))
        {
            reqMap.put("weiphotoPicSize", weiphotoPicSize);
        }
        if (StringUtil.isNotEmpty(essWidth) && StringUtil.isNotEmpty(essHeight))
        {
            reqMap.put("essWidth", essWidth);
            reqMap.put("essHeight", essHeight);
        }
        if (StringUtil.isNotEmpty(isCricHandle))
        {
            reqMap.put("isCricHandle", isCricHandle);
        }
        if (Constant.FILE_SYSTEM_CRIC_IS_HANDLE_YES.equals(isCricHandle))
        {
            reqMap.put("cricHandleMap", cricHandleMap);
        }
        
        String filePath = getFilePath(reqMap);
        
        //返回文件path
        rspMap.put("fileUrl", filePath);
        
        return rspMap;
    }
    
    /** 
    * 获取文件路径
    * @return filePath
    * @throws Exception
    */
    private String getFilePath(Map<String, Object> reqMap)
        throws Exception
    {
        
        //文件路径
        String filePath = null;
        try
        {
            ResultData<?> reback = filesService.getFilePath(reqMap);
            
            //文件路径
            filePath = (String)reback.getReturnData();
            
        }
        catch (Exception e)
        {
            logger.error("file",
                "FileHelpService",
                "getFilePath",
                "input params reqMap= " + reqMap.toString(),
                0,
                "",
                "获取获取文件路径异常",
                e);
            
        }
        
        return filePath;
    }
    

}
