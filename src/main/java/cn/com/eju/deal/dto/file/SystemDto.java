package cn.com.eju.deal.dto.file;

import java.util.Date;

public class SystemDto
{
    private Integer systemId;
    
    private String systemCode;
    
    private String systemName;
    
    private String channelCode;
    
    private String authCode;
    
    private Date dateCreate;
    
    private String delFlag;
    
    public Integer getSystemId()
    {
        return systemId;
    }
    
    public void setSystemId(Integer systemId)
    {
        this.systemId = systemId;
    }
    
    public String getSystemCode()
    {
        return systemCode;
    }
    
    public void setSystemCode(String systemCode)
    {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }
    
    public String getSystemName()
    {
        return systemName;
    }
    
    public void setSystemName(String systemName)
    {
        this.systemName = systemName == null ? null : systemName.trim();
    }
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }
    
    public String getAuthCode()
    {
        return authCode;
    }
    
    public void setAuthCode(String authCode)
    {
        this.authCode = authCode == null ? null : authCode.trim();
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