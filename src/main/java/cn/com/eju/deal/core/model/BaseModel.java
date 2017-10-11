package cn.com.eju.deal.core.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**   
* model类的抽象基类
* @author li_xiaodong
* @date 2016年2月1日 上午11:43:27
*/
public abstract class BaseModel implements Serializable
{
    
    /**
    * TODO(用一句话描述这个变量表示什么)
    */ 
    private static final long serialVersionUID = -7541640040604241417L;

    /* 创建者id */
    private Integer userIdCreate;
    
    /* 创建时间 */
    private Date dateCreate;
    
    /* 逻辑删除标志(Y删除/N未删除) */
    private String delFlag;
    
    public Integer getUserIdCreate()
    {
        return userIdCreate;
    }
    
    public void setUserIdCreate(Integer userIdCreate)
    {
        this.userIdCreate = userIdCreate;
    }
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  
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
        this.delFlag = delFlag;
    }
    
    @Override
    public String toString()
    {
        return "BaseModel [userIdCreate=" + userIdCreate + ", dateCreate=" + dateCreate + ", delFlag=" + delFlag + "]";
    }
    
}
