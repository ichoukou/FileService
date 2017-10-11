package cn.com.eju.deal.dto.file;

/**   
* 文件渠道系统配置表model
* @author (li_xiaodong)
* @date 2016年7月10日 下午5:13:50
*/
public class FileConfigDto
{
    private Integer id;
    
    private String configCode;
    
    private String configValue;
    
    private String configDesc;
    
    private String channelCode;
    
    private Boolean delFlag;
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getConfigCode()
    {
        return configCode;
    }
    
    public void setConfigCode(String configCode)
    {
        this.configCode = configCode == null ? null : configCode.trim();
    }
    
    public String getConfigValue()
    {
        return configValue;
    }
    
    public void setConfigValue(String configValue)
    {
        this.configValue = configValue == null ? null : configValue.trim();
    }
    
    public String getConfigDesc()
    {
        return configDesc;
    }
    
    public void setConfigDesc(String configDesc)
    {
        this.configDesc = configDesc == null ? null : configDesc.trim();
    }
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }
    
    public Boolean getDelFlag()
    {
        return delFlag;
    }
    
    public void setDelFlag(Boolean delFlag)
    {
        this.delFlag = delFlag;
    }
}