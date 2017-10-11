package cn.com.eju.deal.core.enums;

/**   
* (所有常量的代码)
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:50:45
*/
public interface FinalDefine
{
    /** 参数加密密钥 */
    String ENCRYPTION_AND_DECRYPTION_KEY = "g2bdw6e8fo1kmgty";
    
    
    
    //包名相关==============================
    String PRE_PACKAGE_NAME = "cn.com.eju.deal"; //所有Java类的包前缀，从这个包往后才会有所分别
    
    String PRE_PACKAGE_HANDLER_NAME = "cn.com.eju.deal.core.handler."; //系统的Handler包的前缀，内核提供
    
    String PACKAGE_SERVICE_NAME = "service"; //服务的包名
    
    String PACKAGE_HANDLER_NAME = "handler"; //操作者的包名
    
    String HANDLER_SUFFIX = "Handler"; //操作者的后缀名
    
    //=====================================
    
    //日志级别相关==============================
    String LOG4J_LEVEL_INFO = "performance"; //普通级
    
    String LOG4J_LEVEL_ERROR = "error"; //错误级
    
    String LOG4J_LEVEL_DEBUG = "debug"; //跟踪日志
    
    String LOG4J_LEVEL_WARN = "warn"; //提示级
    
    String LOG4J_JOIN_SIGN = "."; //包路径与日志链接符号
    
    //=====================================
    
    //审计日志相关==============================
    String AUDIT_LOG_OPEN = "1"; //打开审计日志
    
    String AUDIT_LOG_CLOSE = "0"; //关闭审计日志
    
    String AUDIT_LOG_LOCAL_PATH = "WEB-INF\\classes\\auditlog.properties"; //审计日志配置文件相对路径
    
    //=====================================
    
    //行权限相关===
    String QUERY_RIGHT_ROW = "right.row"; //行权限占位符key
    
    //=====
    
    //save4LargeAmount，每次允许插入的最大行数 ========
    int MAX_COUN_4_LARGE_AMOUNT = 300;
    
    //==========
    
    String SPLIT_STRING = ",";//前台传递过来的request如果是string数组的话，中间的分隔符号的字符
    
    String SPLIT_STRING_PRIFIX = "'";//在分割符合之前是否新增符号，如数组内容为{“101”，“102”}，则生成出来的结果是：'101','102'
    
    String SPLIT_STRING_UNDERLINE = "_";//下划线
    
    //==========
    
    //系统基础路径定义
    String BASE_PATH = "BASE_PATH";
    
}
