package cn.com.eju.deal.dto.student;

import java.util.Date;

import cn.com.eju.deal.core.model.BaseModel;

/**   
* Student  Dto
* @author (li_xiaodong)
* @date 2016年2月17日 下午4:42:54
*/
public class StudentDto extends BaseModel
{
    /**
    * 
    */ 
    private static final long serialVersionUID = -5775427213591322713L;

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
        this.stuName = stuName;
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
