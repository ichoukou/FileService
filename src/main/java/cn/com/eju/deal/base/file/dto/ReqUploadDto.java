package cn.com.eju.deal.base.file.dto;

import java.io.Serializable;
import java.util.Map;


/**   
* 请求上传Dto
* @author li_xiaodong
* @date 2017年1月26日 下午10:05:25
*/
public class ReqUploadDto implements Serializable
{
    /**
    * 
    */
    private static final long serialVersionUID = 8619578418099224389L;
    
    /**
    * 渠道编号
    */ 
    private String channelCode;
    
    /**
    * 文件Id
    */ 
    private Integer fileId;
    
    /**
    * 文件编号
    */ 
    private String fileNo;
    
    /**
    * 文件系统配置信息
    */ 
    private Map<String, Object> fileCfgInfo;
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }
    
    public Integer getFileId()
    {
        return fileId;
    }
    
    public void setFileId(Integer fileId)
    {
        this.fileId = fileId;
    }
    
    public String getFileNo()
    {
        return fileNo;
    }
    
    public void setFileNo(String fileNo)
    {
        this.fileNo = fileNo;
    }
    
    public Map<String, Object> getFileCfgInfo()
    {
        return fileCfgInfo;
    }
    
    public void setFileCfgInfo(Map<String, Object> fileCfgInfo)
    {
        this.fileCfgInfo = fileCfgInfo;
    }
    
}