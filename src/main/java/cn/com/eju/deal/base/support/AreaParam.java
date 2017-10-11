package cn.com.eju.deal.base.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.eju.deal.base.linkage.dao.AreaMapper;
import cn.com.eju.deal.base.linkage.dao.CityMapper;
import cn.com.eju.deal.base.linkage.dao.DistrictMapper;
import cn.com.eju.deal.base.linkage.model.Area;
import cn.com.eju.deal.base.linkage.model.City;
import cn.com.eju.deal.base.linkage.model.District;
import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.core.helper.SpringConfigHelper;
import cn.com.eju.deal.core.util.StringUtil;

/**   
* 系统参数缓存，包括了系统通用参数，行权限参数等各种参数，都是以单例的形式存在
* @author (li_xiaodong)
* @date 2015年11月9日 下午3:25:12
*/
public class AreaParam
{
    
    //City District Area
    
    /**
     * City对象List，所有的cityList对象都会缓存在这个对象中
     */
    private static List<City> cityList;
    
    /**
     * 所有的City对象都会缓存在这个对象中
     */
    private static Map<String, String> cityMap;
    
    /**
     * 所有的City对象都会缓存在这个对象中
     */
    private static Map<String, String> cityNameMap;
    
    /**
     * District对象List，所有的DistrictList对象都会缓存在这个对象中
     */
    private static List<District> districtList;
    
    /**
     * 所有的district对象都会缓存在这个对象中
     */
    private static Map<String, String> districtMap;
    
    /**
     * 所有的district对象都会缓存在这个对象中
     */
    private static Map<String, String> districtNameMap;
    
    /**
     * Area对象List，所有的AreaList对象都会缓存在这个对象中
     */
    private static List<Area> areaList;
    
    /**
     * 所有的area对象都会缓存在这个对象中
     */
    private static Map<String, String> areaMap;
    
    /**
     * 所有的area对象都会缓存在这个对象中
     */
    private static Map<String, String> areaNameMap;
    
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(AreaParam.class);
    
    /** 
    * 根据cityNo获取cityName
    * @param cityNo
    * @return
    */
    public static String getCityNameByCityNo(String cityNo)
    {
        if (cityMap == null)
        {
            initCityMap();
        }
        
        String cityName = "";
        
        if (StringUtil.isNotEmpty(cityNo))
        {
            
            cityName = cityMap.get(cityNo);
        }
        
        return cityName;
    }
    
    /** 
    * 根据cityName获取cityNo
    * @param cityName
    * @return
    */
    public static String getCityNoByCityName(String cityName)
    {
        if (cityNameMap == null)
        {
            initCityMap();
        }
        
        String cityNo = "";
        
        if (StringUtil.isNotEmpty(cityName))
        {
            
            cityNo = cityNameMap.get(cityName);
        }
        
        return cityNo;
    }
    
    /** 
    * 根据districtNo获取districtName
    * @param districtNo
    * @return
    */
    public static String getDistrictNameByDistrictNo(String districtNo)
    {
        if (districtMap == null)
        {
            initCityMap();
        }
        
        String districtName = "";
        
        if (StringUtil.isNotEmpty(districtNo))
        {
            
            districtName = districtMap.get(districtNo);
        }
        
        return districtName;
    }
    
    /** 
    * 根据districtName获取districtNo
    * @param districtNo
    * @return
    */
    public static String getDistrictNoByDistrictName(String districtName)
    {
        if (districtNameMap == null)
        {
            initCityMap();
        }
        
        String districtNo = "";
        
        if (StringUtil.isNotEmpty(districtName))
        {
            
            districtNo = districtNameMap.get(districtName);
        }
        
        return districtNo;
    }
    
    /** 
    * 根据areaNo获取areaName
    * @param areaNo
    * @return
    */
    public static String getAreaNameByAreaNo(String areaNo)
    {
        if (areaMap == null)
        {
            initCityMap();
        }
        
        String areaName = "";
        
        if (StringUtil.isNotEmpty(areaNo))
        {
            
            areaName = areaMap.get(areaNo);
        }
        
        return areaName;
    }
    
    /** 
    * 根据areaName获取areaNo
    * @param areaName
    * @return
    */
    public static String getAreaNoByAreaName(String areaName)
    {
        if (areaNameMap == null)
        {
            initCityMap();
        }
        
        String areaNo = "";
        
        if (StringUtil.isNotEmpty(areaName))
        {
            
            areaNo = areaNameMap.get(areaName);
        }
        
        return areaNo;
    }
    
    /** 
     * <p>初始化码表信息到内存中。</p>
     */
    private synchronized static void initCityMap()
    {
        
        cityMap = new HashMap<String, String>();
        cityNameMap = new HashMap<String, String>();
        districtMap = new HashMap<String, String>();
        districtNameMap = new HashMap<String, String>();
        areaMap = new HashMap<String, String>();
        areaNameMap = new HashMap<String, String>();
        
        if (null == cityList || null == districtList || null == areaList)
        {
            initCityListPram();
        }
        
        if (null != cityList && !cityList.isEmpty())
        {
            for (City city : cityList)
            {
                //cityNo
                String cityNo = city.getCityNo();
                
                //cityName
                String cityName = city.getCityName();
                
                //拼装codeMap
                if (!cityMap.containsKey(cityNo))
                {
                    cityMap.put(cityNo, cityName);
                }
                if (!cityNameMap.containsKey(cityName))
                {
                    cityNameMap.put(cityName, cityNo);
                }
                
            }
        }
        
        if (null != districtList && !districtList.isEmpty())
        {
            for (District district : districtList)
            {
                String districtNo = district.getDistrictNo();
                
                String districtName = district.getDistrictName();
                
                //拼装
                if (!districtMap.containsKey(districtNo))
                {
                    districtMap.put(districtNo, districtName);
                }
                if (!districtNameMap.containsKey(districtName))
                {
                    districtNameMap.put(districtName, districtNo);
                }
                
            }
        }
        
        if (null != areaList && !areaList.isEmpty())
        {
            for (Area area : areaList)
            {
                String areaNo = area.getAreaNo();
                
                String areaName = area.getAreaName();
                
                //拼装
                if (!areaMap.containsKey(areaNo))
                {
                    areaMap.put(areaNo, areaName);
                }
                if (!areaNameMap.containsKey(areaName))
                {
                    areaNameMap.put(areaName, areaNo);
                }
                
            }
        }
    }
    
    /**
    *
    * <p>初始化CityList/districtList/areaList信息到内存中。</p>
    *
    */
    private synchronized static void initCityListPram()
    {
        
        //取得CityList数据
        cityList = loadCityData();
        districtList = loadDistrictData();
        areaList = loadAreaData();
        
    }
    
    /** 
     * 查询出CityList
     * @return
     */
    public static List<City> loadCityData()
    {
        final IDao<?> cityMapper = SpringConfigHelper.getDaoBeanByDaoClassName("cityMapper");
        
        final CityMapper dao = (CityMapper)cityMapper;
        
        Map<String, Object> queryParam = new HashMap<String, Object>();
        
        List<City> cityList = dao.queryList(queryParam);
        
        return cityList;
    }
    
    /** 
     * 查询出districtList
     * @return
     */
    public static List<District> loadDistrictData()
    {
        final IDao<?> districtMapper = SpringConfigHelper.getDaoBeanByDaoClassName("districtMapper");
        
        final DistrictMapper dao = (DistrictMapper)districtMapper;
        
        Map<String, Object> queryParam = new HashMap<String, Object>();
        
        List<District> districtList = dao.queryList(queryParam);
        
        return districtList;
    }
    
    /** 
     * 查询出AreaList
     * @return
     */
    public static List<Area> loadAreaData()
    {
        final IDao<?> areaMapper = SpringConfigHelper.getDaoBeanByDaoClassName("areaMapper");
        
        final AreaMapper dao = (AreaMapper)areaMapper;
        
        Map<String, Object> queryParam = new HashMap<String, Object>();
        
        List<Area> areaList = dao.queryList(queryParam);
        
        return areaList;
    }
    
    /**
     * 刷新字典表、配置表信息
     */
    public static void refreshCodeMap()
        throws Exception
    {
        //初始化信息
//        initCityListPram();
        
        initCityMap();
        
    }
    
}
