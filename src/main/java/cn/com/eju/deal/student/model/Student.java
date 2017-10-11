package cn.com.eju.deal.student.model;

import java.io.Serializable;
import java.util.Date;

import cn.com.eju.deal.core.model.BaseModel;

/**   
* Model类
* @author li_xiaodong
* @date 2016年2月3日 下午2:01:46
*/
public class Student extends BaseModel implements Serializable
{
    /**
    * TODO(用一句话描述这个变量表示什么)
    */ 
    private static final long serialVersionUID = 268231807297165902L;

    private Integer id;
    
    private Integer stuNo;
    
    private String stuName;
    
    private Integer stuAge;
    
    /* 更新者id */
    private Integer updateId;
    
    /* 更新时间 */
    private Date updateTime;
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public Integer getStuNo()
    {
        return stuNo;
    }
    
    public void setStuNo(Integer stuNo)
    {
        this.stuNo = stuNo;
    }
    
    public String getStuName()
    {
        return stuName;
    }
    
    public void setStuName(String stuName)
    {
        this.stuName = stuName == null ? null : stuName.trim();
    }
    
    public Integer getStuAge()
    {
        return stuAge;
    }
    
    public void setStuAge(Integer stuAge)
    {
        this.stuAge = stuAge;
    }
    
    public Integer getUpdateId()
    {
        return updateId;
    }
    
    public void setUpdateId(Integer updateId)
    {
        this.updateId = updateId;
    }
    
    public Date getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
}