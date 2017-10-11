package cn.com.eju.deal.core.exception;

/**   
* (业务异常)
* @author (li_xiaodong)
* @date 2015年10月14日 下午7:42:23
*/
@SuppressWarnings({"serial" })
public class BusinessException extends RuntimeException {
	
	public BusinessException(String msg) {
		super(msg);
	}
	
	public BusinessException(Exception e) {
		super(e);
	}
	
	public BusinessException(String msg,Exception e) {
		super(msg,e);
	}
	
	
	

}
