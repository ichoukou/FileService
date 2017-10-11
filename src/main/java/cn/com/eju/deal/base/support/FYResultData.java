package cn.com.eju.deal.base.support;

import java.io.Serializable;

import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.support.ReturnMsg;
import cn.com.eju.deal.core.util.JsonUtil;

/**   
* Rest服务 返回 结果类--RMS
* @author TODO (创建人)
* @date 2017年1月15日 下午7:12:38
* @param <T>
*/
public class FYResultData<T> implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = -7675268884737656024L;

    private String dataCode;

    private String message;

    private T result;

    private int totalCount;
    
    public FYResultData() {
        //实例化默认设置成功
        setSuccess();
    }

    /**
     * @return the dataCode
     */
    public String getDataCode() {
        return dataCode;
    }

    /**
     * @param dataCode the dataCode to set
     */
    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }


    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the result
     */
    public T getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 设置成功
     */
    public void setSuccess() {
        this.setSuccess(ReturnMsg.SUCCESS_MSG);
    }

    /**
     * 设置成功
     */
    public void setSuccess(String successMsg) {
        this.setDataCode(ReturnCode.FY_SUCCESS);
        this.setMessage(successMsg);
    }

    /**
     * 设置失败
     */
    public void setFail() {
        this.setFail(ReturnMsg.FAILURE_MSG);
    }

    /**
     * 设置失败
     */
    public void setFail(String failMsg) {
        this.setDataCode(ReturnCode.FY_FAILURE);
        this.setMessage(failMsg);
    }

    /**
     * 设置失败
     *
     * @param dataCode 错误编码
     * @param failMsg  失败信息提示
     */
    public void setFail(String dataCode, String failMsg) {
        this.setDataCode(dataCode);
        this.setMessage(failMsg);
    }

    @Override
    public String toString() {
        return JsonUtil.parseToJson(this);
    }

    public FYResultData(String dataCode, String message) {
        this.dataCode = dataCode;
        this.message = message;
    }

    public FYResultData(String dataCode, String message,T resultData) {
        this.dataCode = dataCode;
        this.message = message;
        this.result = resultData;
    }

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
