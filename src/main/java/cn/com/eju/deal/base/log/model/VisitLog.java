package cn.com.eju.deal.base.log.model;

import java.util.Date;

/**   
* 访问Model
* @author li_xiaodong
* @date 2017年2月10日 上午10:27:27
*/
public class VisitLog
{
    
    private Integer id;
    
    private String url;
    
    private String ip;
    
    private String port;
    
    private String method;
    
    private Date dateCreate;
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public String getPort()
    {
        return port;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public String getMethod()
    {
        return method;
    }
    
    public void setMethod(String method)
    {
        this.method = method;
    }
    
    public Date getDateCreate()
    {
        return dateCreate;
    }
    
    public void setDateCreate(Date dateCreate)
    {
        this.dateCreate = dateCreate;
    }
    
    @Override
    public String toString()
    {
        return "VisitLog [id=" + id + ", url=" + url + ", ip=" + ip + ", port=" + port + ", method=" + method
            + ", dateCreate=" + dateCreate + "]";
    }
    
}
