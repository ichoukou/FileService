package cn.com.eju.deal.file.controller;

import java.util.Date;
import java.util.List;
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
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;
import cn.com.eju.deal.dto.file.FileDto;
import cn.com.eju.deal.dto.file.FileInfoDto;
import cn.com.eju.deal.dto.file.ReqUploadDto;
import cn.com.eju.deal.dto.file.UploadInfoDto;
import cn.com.eju.deal.file.model.Files;
import cn.com.eju.deal.file.service.FileService;

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
    
    @Resource(name = "fileService")
    private FileService fileService;
    
    /** 
     * 请求上传 接口
     * @param param
     * @return
     */
    @RequestMapping(value = "requpload/{systemCode}", method = RequestMethod.GET)
    public String reqUpload(@PathVariable String systemCode)
    {
        //构建返回
        ResultData<ReqUploadDto> resultData = new ResultData<ReqUploadDto>();
        
        //
        if (StringUtil.isEmpty(systemCode))
        {
            resultData.setFail("系统编号不能为空");
            return resultData.toString();
        }
        
        try
        {
            resultData = fileService.reqUpload(systemCode);
        }
        catch (Exception e)
        {
            logger.error("FileService", "FileController", "reqUpload", "systemCode=" + systemCode, 0, "", "请求上传异常", e);
            
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
            
            Files mo = new Files();
            
            //赋值
            BeanUtils.copyProperties(dto, mo);
            
            //10002 已上传
            mo.setFileState(Constant.FILE_CHANNEL_FILE_STATE_FINISH);
            mo.setUploadTime(new Date());
            
            fileService.update(mo);
        }
        catch (Exception e)
        {
            logger.error("File", "FileController", "update", "param=" + jsonDto, 0, "", "更新异常", e);
            
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
        ResultData<String> resultData = new ResultData<String>();
        
        //
        if (StringUtil.isEmpty(param))
        {
            resultData.setFail("请求参数不能为空");
            return resultData.toString();
        }
        
        //查询
        try
        {
            Map<?, ?> reqParam = JsonUtil.parseToObject(param, Map.class);
            
            resultData = fileService.getFilePath(reqParam);
            
        }
        catch (Exception e)
        {
            logger.error("File", "FileController", "getFilePath", "param=" + param, 0, "", "获取文件路径异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
     * 获取文件  路径接口
     * @param param  必填项 {"fileNos":"92ec9d4d-68f5-48c2-96d4-374c3137b8c2"}
     * @return
     */
    @RequestMapping(value = "batch/{fileNos}", method = RequestMethod.GET)
    public String getBatchFilePath(@PathVariable String fileNos)
    {
        //构建返回
        ResultData<List<Map<String, String>>> resultData = new ResultData<>();
        
        //
        if (StringUtil.isEmpty(fileNos))
        {
            resultData.setFail("请求参数不能为空");
            return resultData.toString();
        }
        
        String[] fileNoArr = fileNos.split(",");
        
        //查询
        try
        {
            
            resultData = fileService.getBatchFilePath(fileNoArr);
            
        }
        catch (Exception e)
        {
            logger.error("File", "FileController", "getFilePath", "fileNos=" + fileNos, 0, "", "获取文件路径异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
     * 获取渠道配置
     * @param id
     * @return
     */
    @Deprecated
    @RequestMapping(value = "config", method = RequestMethod.GET)
    public String getFileConfig()
    {
        
        String channelCode = null;
        
        //查询
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = fileService.getCfgByChannelCode(channelCode);
        }
        catch (Exception e)
        {
            resultData.setFail();
            logger.error("File", "FileController", "getFileConfig", "", null, "", "", e);
        }
        
        return resultData.toString();
    }
    
    /** 
    * 请求上传接口
    * @param param
    * @return
    */
    @Deprecated
    @RequestMapping(value = "/upload/channelInfo/{param}", method = RequestMethod.GET)
    public String getChannelInfo(@PathVariable String param)
    {
        //构建返回
        ResultData<UploadInfoDto> resultData = new ResultData<UploadInfoDto>();
        
        try
        {
            Map<?, ?> queryParam = JsonUtil.parseToObject(param, Map.class);
            
            resultData = fileService.getChannelInfo(queryParam);
        }
        catch (Exception e)
        {
            logger.error("FileService", "FileController", "getChannelInfo", "param=" + param, 0, "", "请求上传异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 获取文件接口
    * @param fileNo
    * @return
    */
    @Deprecated
    @RequestMapping(value = "/fileNo/{fileNo}", method = RequestMethod.GET)
    public String getByFileNo(@PathVariable String fileNo)
    {
        //构建返回
        ResultData<FileDto> resultData = new ResultData<FileDto>();
        
        //
        if (StringUtil.isEmpty(fileNo))
        {
            resultData.setFail("文件编号不能为空");
            return resultData.toString();
        }
        
        //查询
        try
        {
            Files mo = fileService.getByFileNo(fileNo);
            
            //Model转换Dto
            FileDto dto = new FileDto();
            
            BeanUtils.copyProperties(mo, dto);
            
            resultData.setReturnData(dto);
            
        }
        catch (Exception e)
        {
            logger.error("File", "FileController", "getByFileNo", "fileNo=" + fileNo, 0, "", "获取文件异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 获取文件接口
    * @param fileNo
    * @return
    */
    @Deprecated
    @RequestMapping(value = "/q/{fileNo}", method = RequestMethod.GET)
    public String getFileInfoByFileNo(@PathVariable String fileNo)
    {
        //构建返回
        ResultData<FileInfoDto> resultData = new ResultData<FileInfoDto>();
        
        //
        if (StringUtil.isEmpty(fileNo))
        {
            resultData.setFail("文件编号不能为空");
            return resultData.toString();
        }
        
        //查询
        try
        {
            resultData = fileService.getFileInfoByFileNo(fileNo);
            
        }
        catch (Exception e)
        {
            logger.error("File", "FileController", "getFileInfoByFileNo", "fileNo=" + fileNo, 0, "", "获取文件异常", e);
            
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
    /** 
    * 删除文件
    * @param fileNo
    * @param operateId
    * @return
    */
    @Deprecated
    @RequestMapping(value = "fileNo/{fileNo}/{operateId}", method = RequestMethod.DELETE)
    public String deleteByFileNo(@PathVariable String fileNo, @PathVariable Integer operateId)
    {
        ResultData<FileDto> resultData = new ResultData<>();
        
        try
        {
            fileService.deleteByFileNo(fileNo);
        }
        catch (Exception e)
        {
            logger.error("File", "FileController", "deleteByFileNo", "", operateId, "", "根据fileNo删除异常", e);
            resultData.setFail();
        }
        
        return resultData.toString();
    }
    
}
