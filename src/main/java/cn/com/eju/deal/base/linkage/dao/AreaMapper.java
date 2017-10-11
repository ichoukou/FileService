package cn.com.eju.deal.base.linkage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.eju.deal.base.linkage.model.Area;
import cn.com.eju.deal.core.dao.IDao;

public interface AreaMapper extends IDao<Area>
{
    /** 
    * 根据行政区DistrictNo获取其板块
    * @return
    */
    List<Area> getAreaByDistrictNo(String districtNo);
    
    /** 
    * 根据城市cityNo获取城市所有板块List
    * @return
    */
    List<Area> getAreaListByCityNo(String cityNo);
    
    /** 
    * 根据板块AreaNo获取其板块 
    * @return
    */
    Area getAreaByAreaNo(String areaNo);
    
    /** 
    * 获取所有板块
    * @return
    */
    List<Area> getAll();
    
    /** 
    * 更新板块名称
    * @return
    */
    int updateByAreaNo(@Param(value="areaNo") String areaNo, @Param(value="areaName") String areaName);
    
    /** 
    * 删除板块
    * @return
    */
    int deleteByAreaNo(String areaNo);
}