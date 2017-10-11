package cn.com.eju.deal.base.file.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.eju.deal.base.controller.BaseController;
import cn.com.eju.deal.base.file.dto.FileDto;
import cn.com.eju.deal.base.file.service.FilesService;
import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;

/**
 * 文件渠道接口
 *
 * @author (li_xiaodong)
 * @date 2016年1月19日 下午6:05:44
 */

@RestController
@RequestMapping(value = "files")
public class FileController extends BaseController
{
    
    /**
     * 日志
     */
    private final LogHelper logger = LogHelper.getLogger(this.getClass());
    
    @Resource(name = "filesService")
    private FilesService filesService;
    
    /** 
     * 请求上传 接口
     * @param param
     * @return
     */
    @RequestMapping(value = "requpload/{systemCode}", method = RequestMethod.GET)
    public String reqUpload(@PathVariable String systemCode)
    {
        //构建返回
        ResultData<?> resultData = new ResultData<>();
        
        //
        if (StringUtil.isEmpty(systemCode))
        {
            resultData.setFail("系统编号不能为空");
            return resultData.toString();
        }
        
        try
        {
            resultData = filesService.reqUpload(systemCode);
        }
        catch (Exception e)
        {
            logger.error("BaseService File",
                "FileController",
                "reqUpload",
                "systemCode=" + systemCode,
                0,
                "",
                "请求上传异常",
                e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 上传完成  接口
    * @param jsonDto
    * @return
    */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public String update(@RequestBody String jsonDto)
    {
        
        //构建返回
        ResultData<FileDto> resultData = new ResultData<FileDto>();
        
        if (StringUtil.isEmpty(jsonDto))
        {
            resultData.setFail("参数不能为空");
            return resultData.toString();
        }
        
        try
        {
            FileDto dto = JsonUtil.parseToObject(jsonDto, FileDto.class);
            
            filesService.update(dto);
        }
        catch (Exception e)
        {
            logger.error("BaseService File", "FileController", "update", "param=" + jsonDto, 0, "", "更新异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
     * 获取文件  路径接口
     * @param param  必填项 {"fileNo":"92ec9d4d-68f5-48c2-96d4-374c3137b8c2"}
     * @return
     */
    @RequestMapping(value = "/{param}", method = RequestMethod.GET)
    public String getFilePath(@PathVariable String param)
    {
        //构建返回
        ResultData<?> resultData = new ResultData<String>();
        
        //
        if (StringUtil.isEmpty(param))
        {
            resultData.setFail("请求参数不能为空");
            return resultData.toString();
        }
        
        //查询
        try
        {
            @SuppressWarnings("unchecked")
            Map<String, Object> reqParam = JsonUtil.parseToObject(param, Map.class);
            
            resultData = filesService.getFilePath(reqParam);
            
        }
        catch (Exception e)
        {
            logger.error("BaseService File", "FileController", "getFilePath", "param=" + param, 0, "", "获取文件路径异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
}
