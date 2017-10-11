package cn.com.eju.deal.dto.file;

import java.io.Serializable;

public class UploadInfoDto implements Serializable
{
    /**
    * 
    */
    private static final long serialVersionUID = 8619578418099224389L;
    
    private String channelCode;
    
    private Integer fileId;
    
    private String fileNo;
    
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
    
}