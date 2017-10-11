package cn.com.eju.deal.base.linkage.dao;

import java.util.List;

import cn.com.eju.deal.base.linkage.model.Province;
import cn.com.eju.deal.core.dao.IDao;

public interface ProvinceMapper extends IDao<Province>
{
    /** 
    * 获取所有省
    * @return
    */
    List<Province> getAll();
}