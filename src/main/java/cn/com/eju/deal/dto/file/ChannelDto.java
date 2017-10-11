package cn.com.eju.deal.dto.file;

import java.util.Date;

public class ChannelDto
{
    private Integer channelId;
    
    private String channelCode;
    
    private String channelName;
    
    private String permitCode;
    
    private Date dateCreate;
    
    private String delFlag;
    
    public Integer getChannelId()
    {
        return channelId;
    }
    
    public void setChannelId(Integer channelId)
    {
        this.channelId = channelId;
    }
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }
    
    public String getChannelName()
    {
        return channelName;
    }
    
    public void setChannelName(String channelName)
    {
        this.channelName = channelName == null ? null : channelName.trim();
    }
    
    public String getPermitCode()
    {
        return permitCode;
    }
    
    public void setPermitCode(String permitCode)
    {
        this.permitCode = permitCode == null ? null : permitCode.trim();
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
}