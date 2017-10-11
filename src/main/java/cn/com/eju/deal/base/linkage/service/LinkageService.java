package cn.com.eju.deal.base.linkage.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.com.eju.deal.base.linkage.dao.AreaMapper;
import cn.com.eju.deal.base.linkage.dao.CityMapper;
import cn.com.eju.deal.base.linkage.dao.DistrictMapper;
import cn.com.eju.deal.base.linkage.dao.ProvinceMapper;
import cn.com.eju.deal.base.linkage.dto.AreaDto;
import cn.com.eju.deal.base.linkage.dto.CityDto;
import cn.com.eju.deal.base.linkage.dto.DistrictDto;
import cn.com.eju.deal.base.linkage.model.Area;
import cn.com.eju.deal.base.linkage.model.City;
import cn.com.eju.deal.base.linkage.model.District;
import cn.com.eju.deal.base.service.BaseService;
import cn.com.eju.deal.core.support.ResultData;

/**   
* 省、市、区、板块 联动  服务类
* @author li_xiaodong
* @date 2016年2月2日 下午7:57:09
*/
@Service("linkageService")
public class LinkageService extends BaseService<Object>
{
    @Resource
    private ProvinceMapper provinceMapper;
    
    @Resource
    private CityMapper cityMapper;
    
    @Resource
    private DistrictMapper districtMapper;
    
    @Resource
    private AreaMapper areaMapper;
    
    /** 
     * 获取城市list
     * @param queryParam
     * @return
     */
    public ResultData<?> getCityList()
        throws Exception
    {
        
        //构建返回
        ResultData<List<CityDto>> resultData = new ResultData<List<CityDto>>();
        
        //查询
        final List<City> moList = cityMapper.getAll();
        
        //转换
        List<CityDto> dtoList = convertCityData(moList);
        
        resultData.setTotalCount(String.valueOf(dtoList.size()));
        
        resultData.setReturnData(dtoList);
        
        return resultData;
    }
    
    /** 
     * 根据城市CityNo获取其行政区List
     * @param queryParam
     * @return
     */
    public ResultData<?> getDistrictListByCityNo(String cityNo)
        throws Exception
    {
        
        //构建返回
        ResultData<List<DistrictDto>> resultData = new ResultData<List<DistrictDto>>();
        
        //查询
        final List<District> moList = districtMapper.getDistrictByCityNo(cityNo);
        
        //转换
        List<DistrictDto> dtoList = convertDistrictListData(moList);
        
        resultData.setTotalCount(String.valueOf(dtoList.size()));
        
        resultData.setReturnData(dtoList);
        
        return resultData;
    }
    
    /** 
     * 根据行政区DistrictNo获取其板块List
     * @param queryParam
     * @return
     */
    public ResultData<?> getAreaListByDistrictNo(String districtNo)
        throws Exception
    {
        
        //构建返回
        ResultData<List<AreaDto>> resultData = new ResultData<List<AreaDto>>();
        
        //查询
        final List<Area> moList = areaMapper.getAreaByDistrictNo(districtNo);
        
        //转换
        List<AreaDto> dtoList = convertAreaListData(moList);
        
        resultData.setTotalCount(String.valueOf(dtoList.size()));
        
        resultData.setReturnData(dtoList);
        
        return resultData;
    }
    
    /** 
     * 根据城市cityNo获取城市所有板块List
     * @param cityNo
     * @return
     */
    public ResultData<?> getAreaListByCityNo(String cityNo)
        throws Exception
    {
        
        //构建返回
        ResultData<List<AreaDto>> resultData = new ResultData<List<AreaDto>>();
        
        //查询
        final List<Area> moList = areaMapper.getAreaListByCityNo(cityNo);
        
        //转换
        List<AreaDto> dtoList = convertAreaListData(moList);
        
        resultData.setTotalCount(String.valueOf(dtoList.size()));
        
        resultData.setReturnData(dtoList);
        
        return resultData;
    }
    
    /** 
     * 根据城市districtNo获取其行政区District
     * @param districtNo
     * @return
     */
    public ResultData<?> getByDistrictNo(String districtNo)
        throws Exception
    {
        
        //构建返回
        ResultData<DistrictDto> resultData = new ResultData<DistrictDto>();
        
        //查询
        final District mo = districtMapper.getByDistrictNo(districtNo);
        
        //转换
        DistrictDto dto = convertDistrictData(mo);
        
        resultData.setReturnData(dto);
        
        return resultData;
    }
    
    /** 
     * 根据板块AreaNo获取其板块 
     * @param areaNo
     * @return
     */
    public ResultData<?> getAreaByAreaNo(String areaNo)
        throws Exception
    {
        
        //构建返回
        ResultData<AreaDto> resultData = new ResultData<AreaDto>();
        
        //查询
        final Area mo = areaMapper.getAreaByAreaNo(areaNo);
        
        //转换
        AreaDto dto = convertAreaData(mo);
        
        resultData.setReturnData(dto);
        
        return resultData;
    }
    
    /** 
     * 对象转换MO--DTO
     * @param stuList
     * @return List<StudentDto>
     */
    private List<CityDto> convertCityData(List<City> moList)
        throws Exception
    {
        List<CityDto> dtoList = new ArrayList<CityDto>();
        
        if (null != moList && !moList.isEmpty())
        {
            CityDto dto = null;
            for (City mo : moList)
            {
                dto = new CityDto();
                BeanUtils.copyProperties(mo, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
    
    /** 
     * 对象转换MO--DTO
     * @param stuList
     * @return List<StudentDto>
     */
    private List<DistrictDto> convertDistrictListData(List<District> moList)
        throws Exception
    {
        List<DistrictDto> dtoList = new ArrayList<DistrictDto>();
        
        if (null != moList && !moList.isEmpty())
        {
            DistrictDto dto = null;
            for (District mo : moList)
            {
                dto = new DistrictDto();
                BeanUtils.copyProperties(mo, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
    
    /** 
     * 对象转换MO--DTO
     * @param stuList
     * @return List<StudentDto>
     */
    private List<AreaDto> convertAreaListData(List<Area> moList)
        throws Exception
    {
        List<AreaDto> dtoList = new ArrayList<AreaDto>();
        
        if (null != moList && !moList.isEmpty())
        {
            AreaDto dto = null;
            for (Area mo : moList)
            {
                dto = new AreaDto();
                BeanUtils.copyProperties(mo, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
    
    /** 
     * 对象转换MO--DTO
     * @param stuList
     * @return List<StudentDto>
     */
    private AreaDto convertAreaData(Area mo)
        throws Exception
    {
        AreaDto dto = new AreaDto();
        
        if (null != mo)
        {
            BeanUtils.copyProperties(mo, dto);
        }
        return dto;
    }
    
    /** 
     * 对象转换MO--DTO
     * @param stuList
     * @return List<StudentDto>
     */
    private DistrictDto convertDistrictData(District mo)
        throws Exception
    {
        DistrictDto dto = new DistrictDto();
        
        if (null != mo)
        {
            dto = new DistrictDto();
            BeanUtils.copyProperties(mo, dto);
        }
        
        return dto;
    }
    
    /** 
     * 创建
     * @param param
     * @return
     */
    
    public int create(Area area)
        throws Exception
    {
        int count = areaMapper.create(area);
        return count;
    }
    
    /** 
     * 更新
     * @param param
     * @return
     */
    
    public int updateByAreaNo(String areaNo, String areaName)
        throws Exception
    {
        int count = areaMapper.updateByAreaNo(areaNo, areaName);
        return count;
    }
    
    /** 
    * 删除
    * @param id 
    * @param updateId 更新人
    * @param updateTime 更新时间
    * @return
    */
    
    public int deleteByAreaNo(String areaNo)
        throws Exception
    {
        int count = areaMapper.deleteByAreaNo(areaNo);
        return count;
    }
    
}
