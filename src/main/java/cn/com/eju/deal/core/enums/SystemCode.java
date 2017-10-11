package cn.com.eju.deal.core.enums;

/**   
* 应用系统Code
* @author li_xiaodong
* @date 2017年1月31日 下午5:33:37
*/
public enum SystemCode
{
    /**
     * 应用系统Code-"OP"
     */
    SYSTEM_CODE_OP("OP", "OP"),
    
    /**
     * 应用系统Code-"OMS"
     */
    SYSTEM_CODE_OMS("OMS", "OMS"),
    
    /**
     * 应用系统Code-"CRM"
     */
    SYSTEM_CODE_CRM("CRM", "CRM"),
    
    /**
     * 应用系统Code-"TMS"
     */
    SYSTEM_CODE_TMS("TMS", "TMS"),
    
    /**
     * 应用系统Code-"UMS"
     */
    SYSTEM_CODE_UMS("UMS", "UMS"),
    
    /**
     * 应用系统Code-"RMS"
     */
    SYSTEM_CODE_RMS("RMS", "RMS"),
    
    /**
     * 应用系统Code-"Task"
     */
    SYSTEM_CODE_TASK("Task", "Task"),
    
    /**
     * 应用系统Code-"Youyou"
     */
    SYSTEM_CODE_YOUYOU("Youyou", "Youyou"),
    
    /**
     * 应用系统Code-"File"
     */
    SYSTEM_CODE_FILE("File", "File");
    
    /**
     * 枚举Code
     */
    private String code;
    
    /**
     * 枚举内容
     */
    private String name;
    
    private SystemCode(String code, String name)
    {
        this.code = code;
        this.name = name;
        
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
}
