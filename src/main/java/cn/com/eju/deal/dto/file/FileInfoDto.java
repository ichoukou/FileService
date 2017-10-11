package cn.com.eju.deal.dto.file;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class FileInfoDto implements Serializable
{
    /**
    * 
    */
    private static final long serialVersionUID = -493428751234642260L;
    
    private Integer fileId;
    
    private String fileNo;
    
    private String fileCode;
    
    private Integer fileState;
    
    private String channelCode;
    
    private String systemCode;
    
    private Date uploadTime;
    
    private String remark;
    
    private Date dateCreate;
    
    private String delFlag;
    
    private String fileType;
    
    /**
    * 文件系统配置信息
    */
    private Map<String, Object> fileCfgInfo;
    
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
        this.fileNo = fileNo == null ? null : fileNo.trim();
    }
    
    public String getFileCode()
    {
        return fileCode;
    }
    
    public void setFileCode(String fileCode)
    {
        this.fileCode = fileCode == null ? null : fileCode.trim();
    }
    
    public Integer getFileState()
    {
        return fileState;
    }
    
    public void setFileState(Integer fileState)
    {
        this.fileState = fileState;
    }
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }
    
    public String getSystemCode()
    {
        return systemCode;
    }
    
    public void setSystemCode(String systemCode)
    {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }
    
    public Date getUploadTime()
    {
        return uploadTime;
    }
    
    public void setUploadTime(Date uploadTime)
    {
        this.uploadTime = uploadTime;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark == null ? null : remark.trim();
    }
    
    public Date getDateCreate()
    {
        return dateCreate;
    }
    
    public void setDateCreate(Date dateCreate)
    {
        this.dateCreate = dateCreate;
    }
    
    public String getDelFlag()
    {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }
    
    public String getFileType()
    {
        return fileType;
    }
    
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }
    
    public Map<String, Object> getFileCfgInfo()
    {
        return fileCfgInfo;
    }
    
    public void setFileCfgInfo(Map<String, Object> fileCfgInfo)
    {
        this.fileCfgInfo = fileCfgInfo;
    }
    
    @Override
    public String toString()
    {
        return "FileInfoDto [fileId=" + fileId + ", fileNo=" + fileNo + ", fileCode=" + fileCode + ", fileState="
            + fileState + ", channelCode=" + channelCode + ", systemCode=" + systemCode + ", uploadTime=" + uploadTime
            + ", remark=" + remark + ", dateCreate=" + dateCreate + ", delFlag=" + delFlag + ", fileType=" + fileType
            + ", fileCfgInfo=" + fileCfgInfo + "]";
    }
    
}