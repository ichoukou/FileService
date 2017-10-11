package cn.com.eju.deal.core.support;

import java.io.Serializable;

import cn.com.eju.deal.core.util.JsonUtil;

/**   
* Rest服务 返回 结果类
* @author (li_xiaodong)
* @date 2016年1月19日 下午5:30:20
* @param <T>
*/
public class ResultData<T> implements Serializable
{
    /**
    * 序列化
    */
    private static final long serialVersionUID = -7675268884737656024L;
    
    private String returnCode;
    
    private String returnMsg;
    
    private T returnData;
    
    //Extension拓展字段，供分页用
    
    //** 总条数 *//*
    private String totalCount;
    
    public ResultData()
    {
        //实例化默认设置成功
        setSuccess();
    }
    
    public String getReturnCode()
    {
        return returnCode;
    }
    
    public void setReturnCode(String returnCode)
    {
        this.returnCode = returnCode;
    }
    
    public String getReturnMsg()
    {
        return returnMsg;
    }
    
    public void setReturnMsg(String returnMsg)
    {
        this.returnMsg = returnMsg;
    }
    
    public T getReturnData()
    {
        return returnData;
    }
    
    public void setReturnData(T returnData)
    {
        this.returnData = returnData;
    }
    
    public String getTotalCount()
    {
        return totalCount;
    }
    
    public void setTotalCount(String totalCount)
    {
        this.totalCount = totalCount;
    }
    
    /** 
    * 设置成功
    */
    public void setSuccess()
    {
        this.setSuccess(ReturnMsg.SUCCESS_MSG);
    }
    
    /** 
    * 设置成功
    */
    public void setSuccess(String successMsg)
    {
        this.setReturnCode(ReturnCode.SUCCESS);
        this.setReturnMsg(successMsg);
    }
    
    /** 
    * 设置失败
    */
    public void setFail()
    {
        this.setFail(ReturnMsg.FAILURE_MSG);
    }
    
    /** 
    * 设置失败
    */
    public void setFail(String failMsg)
    {
        this.setReturnCode(ReturnCode.FAILURE);
        this.setReturnMsg(failMsg);
    }
    
    @Override
    public String toString()
    {
        return JsonUtil.parseToJson(this);
    }
}
