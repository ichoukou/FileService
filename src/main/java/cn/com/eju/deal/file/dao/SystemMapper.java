package cn.com.eju.deal.file.dao;

import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.file.model.Systems;

public interface SystemMapper extends IDao<Systems>
{
    /** 
    * 根据系统code获取
    * @param systemCode
    * @return
    */
    Systems getBySystemCode(String systemCode);
}