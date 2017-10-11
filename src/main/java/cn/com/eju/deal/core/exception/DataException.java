package cn.com.eju.deal.core.exception;

/**   
* (数据异常)
* @author (li_xiaodong)
* @date 2015年10月14日 下午7:42:45
*/
@SuppressWarnings({"serial" })
public class DataException extends RuntimeException {
	
	public DataException(String msg) {
		super(msg);
	}
	
	public DataException(Exception e) {
		super(e);
	}
	
	public DataException(String msg,Exception e) {
		super(msg,e);
	}
	
	
	

}
