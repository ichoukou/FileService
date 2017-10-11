package cn.com.eju.deal.base.code.model;

public class IpWhite
{
    private Integer id;
    
    private Long ipStartInt;
    
    private String ipStartStr;
    
    private Long ipEndInt;
    
    private String ipEndStr;
    
    private Integer ipWhiteType;
    
    private String wsNo;
    
    private String delFlag;
    
    private String ipDesc;
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public Long getIpStartInt()
    {
        return ipStartInt;
    }
    
    public void setIpStartInt(Long ipStartInt)
    {
        this.ipStartInt = ipStartInt;
    }
    
    public String getIpStartStr()
    {
        return ipStartStr;
    }
    
    public void setIpStartStr(String ipStartStr)
    {
        this.ipStartStr = ipStartStr;
    }
    
    public Long getIpEndInt()
    {
        return ipEndInt;
    }
    
    public void setIpEndInt(Long ipEndInt)
    {
        this.ipEndInt = ipEndInt;
    }
    
    public String getIpEndStr()
    {
        return ipEndStr;
    }
    
    public void setIpEndStr(String ipEndStr)
    {
        this.ipEndStr = ipEndStr;
    }
    
    public Integer getIpWhiteType()
    {
        return ipWhiteType;
    }
    
    public void setIpWhiteType(Integer ipWhiteType)
    {
        this.ipWhiteType = ipWhiteType;
    }
    
    public String getWsNo()
    {
        return wsNo;
    }
    
    public void setWsNo(String wsNo)
    {
        this.wsNo = wsNo;
    }
    
    public String getDelFlag()
    {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }
    
    public String getIpDesc()
    {
        return ipDesc;
    }
    
    public void setIpDesc(String ipDesc)
    {
        this.ipDesc = ipDesc;
    }
    
}
