package cn.com.eju.deal.dto.file;

import java.io.Serializable;
import java.util.Date;

public class InterfaceDto implements Serializable
{
    /**
    * 
    */ 
    private static final long serialVersionUID = 8642396300735879479L;

    private Integer interfaceId;
    
    private String interfaceCode;
    
    private String interfaceName;
    
    private String interfaceType;
    
    private String interfaceAddr;
    
    private String channelCode;
    
    private Date dateCreate;
    
    private String delFlag;
    
    public Integer getInterfaceId()
    {
        return interfaceId;
    }
    
    public void setInterfaceId(Integer interfaceId)
    {
        this.interfaceId = interfaceId;
    }
    
    public String getInterfaceCode()
    {
        return interfaceCode;
    }
    
    public void setInterfaceCode(String interfaceCode)
    {
        this.interfaceCode = interfaceCode == null ? null : interfaceCode.trim();
    }
    
    public String getInterfaceName()
    {
        return interfaceName;
    }
    
    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
    }
    
    public String getInterfaceType()
    {
        return interfaceType;
    }
    
    public void setInterfaceType(String interfaceType)
    {
        this.interfaceType = interfaceType == null ? null : interfaceType.trim();
    }
    
    public String getInterfaceAddr()
    {
        return interfaceAddr;
    }
    
    public void setInterfaceAddr(String interfaceAddr)
    {
        this.interfaceAddr = interfaceAddr == null ? null : interfaceAddr.trim();
    }
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
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