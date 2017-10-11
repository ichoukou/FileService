package cn.com.eju.deal.core.support;

/**   
* (查询分页排序相关常量)
* @author (li_xiaodong)
* @date 2015年10月21日 下午1:49:11
*/
public interface QueryConst
{
/*    *//** 页面号 *//*
    String PAGE_IDX = "page.pageIndex";

    *//** 页大小 *//*
    String PAGE_SIZE = "page.pageSize";

    *//** 总条数 *//*
    String TOTAL_COUNT = "page.totalCount";

    *//** 排序名称 *//*
    String ORDER_NAME = "page.orderName";

    *//** 排序类型 *//*
    String ORDER_TYPE = "page.orderType";*/

    //*****************easyui************************************//
/*    *//** 页面号 *//*
    String PAGE_IDX = "page";

    *//** 页大小 *//*
    String PAGE_SIZE = "rows";

    *//** 总条数 *//*
    String TOTAL_COUNT = "total";

    *//** 排序名称 *//*
    String ORDER_NAME = "sort";

    *//** 排序类型 *//*
    String ORDER_TYPE = "order";*/
    
    
    //** 页面号 *//*
    String PAGE_IDX = "pageIndex";

    //** 页大小 *//*
    String PAGE_SIZE = "pageSize";

    //** 排序名称 *//*
    String ORDER_NAME = "orderName";

    //** 排序类型 *//*
    String ORDER_TYPE = "orderType";
    
    //** 总条数 *//*
    String TOTAL_COUNT = "totalCount";
    
    //** 多少页 *//*
    String PAGE_COUNT = "pageCount";

    /** 排序类型:正序 */
    String ORDER_TYPE_ASC = "ASC";

    /** 排序类型:逆序 */
    String ORDER_TYPE_DESC = "DESC";

    /** 默认页面号 */
    int DEFAULT_PAGE_IDX = 1;

    /** 默认页面大小 */
    int DEFAULT_PAGE_SIZE = 10;
}
