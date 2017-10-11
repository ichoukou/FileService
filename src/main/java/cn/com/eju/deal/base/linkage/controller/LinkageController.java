package cn.com.eju.deal.base.linkage.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.eju.deal.base.controller.BaseController;
import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.base.linkage.dto.AreaDto;
import cn.com.eju.deal.base.linkage.model.Area;
import cn.com.eju.deal.base.linkage.service.LinkageService;
import cn.com.eju.deal.base.support.AreaParam;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;

/**   
* 城市、行政区、板块 联动
* @author (li_xiaodong)
* @date 2016年3月20日 下午12:29:31
*/
@RestController
@RequestMapping(value = "linkages")
public class LinkageController extends BaseController
{
    
    /**
    * 日志
    */
    private final LogHelper logger = LogHelper.getLogger(this.getClass());
    
    @Resource(name = "linkageService")
    private LinkageService linkageService;
    
    /** 
     * 获取城市list
     * @param
     * @return
     */
    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public String getCityList()
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        
        try
        {
            resultData = linkageService.getCityList();
        }
        catch (Exception e)
        {
            logger.error("Linkage", "LinkageController", "getCityList", "", 0, "", "获取城市list失败", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 根据城市CityNo获取其行政区List
    * @param cityNo
    * @return
    */
    @RequestMapping(value = "/district/{cityNo}", method = RequestMethod.GET)
    public String getDistrictListByCityNo(@PathVariable String cityNo)
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = linkageService.getDistrictListByCityNo(cityNo);
        }
        catch (Exception e)
        {
            logger.error("Linkage",
                "LinkageController",
                "getDistrictListByCityNo",
                "cityNo=" + cityNo,
                0,
                "",
                "根据城市CityNo获取其行政区List失败",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 根据districtNo获取其行政区
    * @param cityNo
    * @return
    */
    @RequestMapping(value = "/district/q/{districtNo}", method = RequestMethod.GET)
    public String getByDistrictNo(@PathVariable String districtNo)
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = linkageService.getByDistrictNo(districtNo);
        }
        catch (Exception e)
        {
            logger.error("Linkage",
                "LinkageController",
                "getByDistrictNo",
                "districtNo=" + districtNo,
                0,
                "",
                "根据districtNo获取其行政区异常",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    *根据行政区DistrictNo获取其板块List
    * @param districtNo
    * @return
    */
    @RequestMapping(value = "/area/{districtNo}", method = RequestMethod.GET)
    public String getAreaListByDistrictNo(@PathVariable String districtNo)
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = linkageService.getAreaListByDistrictNo(districtNo);
        }
        catch (Exception e)
        {
            logger.error("Linkage",
                "LinkageController",
                "getAreaListByDistrictNo",
                "districtNo=" + districtNo,
                0,
                "",
                "根据行政区DistrictNo获取其板块List失败",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    *根据行政区DistrictNo获取其板块List
    * @param districtNo
    * @return
    */
    @RequestMapping(value = "/area/q/{areaNo}", method = RequestMethod.GET)
    public String getAreaByAreaNo(@PathVariable String areaNo)
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = linkageService.getAreaByAreaNo(areaNo);
        }
        catch (Exception e)
        {
            logger.error("Linkage",
                "LinkageController",
                "getAreaByAreaNo",
                "areaNo=" + areaNo,
                0,
                "",
                "根据areaNo获取板块异常",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
     * 根据城市cityNo获取城市所有板块List
     * @param cityNo
     * @return
     */
    @RequestMapping(value = "/area/all/{cityNo}", method = RequestMethod.GET)
    public String getAreaListByCityNo(@PathVariable String cityNo)
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = linkageService.getAreaListByCityNo(cityNo);
        }
        catch (Exception e)
        {
            logger.error("Linkage",
                "LinkageController",
                "getAreaListByCityNo",
                "cityNo=" + cityNo,
                0,
                "",
                "根据城市cityNo获取城市所有板块List",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
     * 根据城市cityNo获取城市所有板块List
     * @param cityNo
     * @return
     */
    @RequestMapping(value = "areas/{param}", method = RequestMethod.GET)
    public String getAreaList(@PathVariable String param)
    {
        
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        
        Map<?, ?> queryParam = JsonUtil.parseToObject(param, Map.class);
        
        String cityNo = (String)queryParam.get("cityNo");
        String districtNo = (String)queryParam.get("districtNo");
        
        //不能同时为空或null
        if (StringUtil.isEmpty(cityNo) && StringUtil.isEmpty(districtNo))
        {
            resultData.setFail("cityNo和districtNo不能同时为空");
            return resultData.toString();
        }
        
        try
        {
            if (StringUtil.isEmpty(districtNo))
            {
                resultData = linkageService.getAreaListByCityNo(cityNo);
            }
            else
            {
                resultData = linkageService.getAreaListByDistrictNo(districtNo);
            }
        }
        catch (Exception e)
        {
            logger.error("Linkage",
                "LinkageController",
                "getAreaList",
                "param=" + param,
                0,
                "",
                "根据城市cityNo districtNo 获取城市所有板块List异常",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 创建
    * @param jsonDto 对象字符串
    * @return
    */
    @RequestMapping(value = "area", method = RequestMethod.POST)
    public String create(@RequestBody String jsonDto)
    {
        
        //构建返回
        ResultData<AreaDto> resultData = new ResultData<AreaDto>();
        
        try
        {
            AreaDto dto = JsonUtil.parseToObject(jsonDto, AreaDto.class);
            
            Area mo = new Area();
            
            //赋值
            BeanUtils.copyProperties(dto, mo);
            
            linkageService.create(mo);
            AreaParam.refreshCodeMap();
            
        }
        catch (Exception e)
        {
            resultData.setFail();
            logger.error("linkage",
                "LinkageController",
                "create",
                "input param; jsonDto=" + jsonDto,
                0,
                "",
                "创建板块名板块异常",
                e);
        }
        
        return resultData.toString();
    }
    
    /** 
     * 更新
     * @param param
     * @return
     */
    @RequestMapping(value = "area", method = RequestMethod.PUT)
    public String updateByAreaNo(@RequestBody String jsonDto)
    {
        
        //构建返回
        ResultData<String> resultData = new ResultData<>();
        
        try
        {
            AreaDto dto = JsonUtil.parseToObject(jsonDto, AreaDto.class);
            
            String areaNo = dto.getAreaNo();
            String areaName = dto.getAreaName();
            
            linkageService.updateByAreaNo(areaNo, areaName);
            
            AreaParam.refreshCodeMap();
            
        }
        catch (Exception e)
        {
            resultData.setFail();
            logger.error("linkage",
                "LinkageController",
                "updateByAreaNo",
                "input param; dtoJson=" + jsonDto,
                0,
                "",
                "更新板块名板块异常",
                e);
        }
        
        return resultData.toString();
    }
    
    /** 
     * 删除
     * @param param
     * @return
     */
    @RequestMapping(value = "area/{areaNo}", method = RequestMethod.DELETE)
    public String deleteByAreaNo(@PathVariable String areaNo)
    {
        //构建返回
        ResultData<String> resultData = new ResultData<>();
        
        try
        {
            linkageService.deleteByAreaNo(areaNo);
            AreaParam.refreshCodeMap();
        }
        catch (Exception e)
        {
            resultData.setFail();
            logger.error("linkage",
                "LinkageController",
                "deleteByAreaNo",
                "input param; areaNo=" + areaNo,
                0,
                "",
                "删除板块异常",
                e);
        }
        
        return resultData.toString();
    }
    
}
