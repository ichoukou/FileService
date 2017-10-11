package cn.com.eju.deal.file.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.com.eju.deal.base.service.BaseService;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.util.StringUtil;
import cn.com.eju.deal.dto.file.FileInfoDto;
import cn.com.eju.deal.dto.file.ReqUploadDto;
import cn.com.eju.deal.dto.file.UploadInfoDto;
import cn.com.eju.deal.file.dao.FileConfigMapper;
import cn.com.eju.deal.file.dao.FileMapper;
import cn.com.eju.deal.file.dao.SystemMapper;
import cn.com.eju.deal.file.handle.CricHelper;
import cn.com.eju.deal.file.handle.EssHelper;
import cn.com.eju.deal.file.handle.WeiphotoHelper;
import cn.com.eju.deal.file.model.FileConfig;
import cn.com.eju.deal.file.model.Files;
import cn.com.eju.deal.file.model.Systems;
import cn.com.eju.deal.file.server.cric.CricEncrypter;
import cn.com.eju.deal.file.server.weiphoto.WeiphotoUtil;
import cn.com.eju.deal.file.support.FileConfigParam;

/**
 * 文件渠道服务类
 * 
 * @author (li_xiaodong)
 * @date 2016年6月5日 下午5:01:38
 */
@Service("fileService")
public class FileService extends BaseService<Object>
{
    
    @Resource
    private FileMapper fileMapper;
    
    @Resource
    private FileConfigMapper fileConfigMapper;
    
    @Resource
    private SystemMapper systemMapper;
    
    /**
     * 请求上传
     * 
     * @param systemCode
     *            系统Code
     * @return
     */
    
    public ResultData<ReqUploadDto> reqUpload(String systemCode)
        throws Exception
    {
        
        // 构建返回
        ResultData<ReqUploadDto> resultData = new ResultData<ReqUploadDto>();
        
        // 查询应用系统的渠道配置
        Systems systems = systemMapper.getBySystemCode(systemCode);
        String channelCode = systems.getChannelCode();
        
        // 校验
        if (StringUtil.isEmpty(channelCode))
        {
            resultData.setFail("系统没有渠道配置，请联系运维人员！");
            return resultData;
        }
        
        // 生成fileNo
        String fileNo = UUID.randomUUID().toString();
        
        // 创建一条文件记录
        Files file = new Files();
        file.setFileNo(fileNo);
        file.setSystemCode(systemCode);
        file.setChannelCode(channelCode);
        file.setFileState(Constant.FILE_CHANNEL_FILE_STATE_WAITING);
        fileMapper.create(file);
        
        // 获取渠道配置信息
        Map<String, Object> fileCfgInfoMap = getFileCfgInfo(channelCode);
        
        // 构建返回Dto
        ReqUploadDto reqUploadDto = new ReqUploadDto();
        reqUploadDto.setChannelCode(channelCode);
        reqUploadDto.setFileNo(fileNo);
        reqUploadDto.setFileId(file.getFileId());
        reqUploadDto.setFileCfgInfo(fileCfgInfoMap);
        
        resultData.setReturnData(reqUploadDto);
        
        return resultData;
    }
    
    /**
     * 更新
     * 
     * @param Files
     *            files
     * @return
     */
    public int update(Files files)
        throws Exception
    {
        int count = fileMapper.update(files);
        return count;
    }
    
    /**
     * 获取文件
     * 
     * @param reqMap
     * @return
     */
    
    public ResultData<String> getFilePath(Map<?, ?> reqMap)
        throws Exception
    {
        // 构建返回
        ResultData<String> resultData = new ResultData<String>();
        
        // 文件路径
        String filePath = null;
        
        // check reqMap
        String fileNo = (String)reqMap.get("fileNo");
        if (StringUtil.isEmpty(fileNo))
        {
            resultData.setFail("文件编号不能为空！");
            return resultData;
        }
        
        Files mo = fileMapper.getByFileNo(fileNo);
        if (null == mo)
        {
            resultData.setFail("文件不存在");
            return resultData;
        }
        
        // 文件所在系统
        String channelCode = mo.getChannelCode();
        if (StringUtil.isEmpty(channelCode))
        {
            resultData.setFail("文件在渠道系统中缺失啦所在文件系统编号，请联系运维人员处理！");
            return resultData;
        }
        
        // 文件状态
        Integer fileState = mo.getFileState();
        if (Constant.FILE_CHANNEL_FILE_STATE_FINISH.intValue() != fileState.intValue() && 1002 != fileState.intValue())
        {
            resultData.setFail("文件状态不正确，请联系运维人员处理！");
            return resultData;
        }
        
        // 获取渠道配置信息
        //Map<String, Object> fileCfgInfoMap = getFileCfgInfo(channelCode);
        
        // 文件Code
        String fileCode = mo.getFileCode();
        
        // 文件在CRIC
        if (Constant.FILE_SYSTEM_CRIC.equals(channelCode))
        {
            // 查询路径
            String queryUrl = FileConfigParam.getFileConfigValue("CRIC_queryfile_path");
            // 下载路径
            String downloadUrl = FileConfigParam.getFileConfigValue("CRIC_downloadfile_path");
            // 授权号
            String permitCode = FileConfigParam.getFileConfigValue("CRIC_file_permit_code");
            
            // 请求参数中获取
            String isCricHandle = (String)reqMap.get("isCricHandle");
            @SuppressWarnings("unchecked")
            Map<String, Object> cricHandleMap = (Map<String, Object>)reqMap.get("cricHandleMap");
            
            // 自定义图片规格
            if (CricHelper.UPLOAD_FILE_IS_HANDLE_YES.equals(isCricHandle))
            {
                
                // 预处理文件(pic)路径
                filePath = CricHelper.getFilePath(fileCode,
                    CricHelper.UPLOAD_FILE_IS_HANDLE_YES,
                    queryUrl,
                    downloadUrl,
                    cricHandleMap,
                    permitCode,
                    getMd5PermitCode(permitCode));
                
            }
            else
            {
                // 原文件路径
                filePath = CricHelper.getFilePath(fileCode, queryUrl, downloadUrl, permitCode, getMd5PermitCode(permitCode));
            }
        }
        else if (Constant.FILE_SYSTEM_WEIPHOTO.equals(channelCode))
        {
            
            // 文件大小
            String weiphotoPicSize = (String)reqMap.get("weiphotoPicSize");
            
            // 文件类型
            String fileType = mo.getFileType();
            
            // 查询weiphoto系统文件路径
            String fileViewUrl = null;
            
            if (StringUtil.isEmpty(fileType) || "pic".equals(fileType))
            {
                // 查询weiphoto系统文件（pic）路径
                fileViewUrl = FileConfigParam.getFileConfigValue("wp_view_url");
                
                // 获取特定大小图片
                if (StringUtil.isNotEmpty(weiphotoPicSize))
                {
                    
                    // 预处理文件(pic)路径
                    filePath = WeiphotoHelper.getPicPath(fileViewUrl, fileCode, weiphotoPicSize);
                }
                else
                {
                    // 原文件路径
                    filePath = WeiphotoHelper.getPicPath(fileViewUrl, fileCode);
                }
                
            }
            else
            {
                // 查询weiphoto系统文件（file）路径
                fileViewUrl = FileConfigParam.getFileConfigValue("wp_view_file_url");
                
                // 原文件路径
                filePath = WeiphotoHelper.getFilePath(fileViewUrl, fileCode);
            }
        }
        else if ("ESS".equals(channelCode))
        {
            // 文件 宽
            String essWidth = (String)reqMap.get("essWidth");
            // 文件高
            String essHeight = (String)reqMap.get("essHeight");
            
            // 查询路径
            String queryUrl = FileConfigParam.getFileConfigValue("ESS_view_url");
            
            // 原文件路径
            filePath = EssHelper.getFilePath(queryUrl, fileCode, essWidth, essHeight);
        }
        
        resultData.setReturnData(filePath);
        
        return resultData;
    }
    
    /** 
    * 批量获取文件url
    * @param fileNo
    * @return
    * @throws Exception
    */
    public ResultData<List<Map<String, String>>> getBatchFilePath(String[] fileNoArr)
        throws Exception
    {
        // 构建返回
        ResultData<List<Map<String, String>>> resultData = new ResultData<List<Map<String, String>>>();
        
        // 文件路径
        String filePath = null;
        
        List<Files> moList = fileMapper.getFileList(fileNoArr);
        if (null == moList || moList.isEmpty())
        {
            resultData.setFail("文件不存在");
            return resultData;
        }
        
        //返回List
        List<Map<String, String>> fileUrlList = new ArrayList<>();
        
        //临时变量map
        Map<String, String> fileUrlMap;
        
        //循环获取文件url
        for (Files mo : moList)
        {
            // 文件所在系统
            String channelCode = mo.getChannelCode();
            if (StringUtil.isEmpty(channelCode))
            {
                resultData.setFail("文件在渠道系统中缺失所在文件系统编号，请联系运维人员处理！");
                return resultData;
            }
            
            // 文件状态
            Integer fileState = mo.getFileState();
            if (Constant.FILE_CHANNEL_FILE_STATE_FINISH.intValue() != fileState.intValue()
                && 1002 != fileState.intValue())
            {
                resultData.setFail("文件状态不正确，请联系运维人员处理！");
                return resultData;
            }
            
            // 文件Code
            String fileCode = mo.getFileCode();
            if (StringUtil.isEmpty(fileCode))
            {
                resultData.setFail("文件在渠道系统中缺失fileCode，请联系运维人员处理！");
                return resultData;
            }
            
            //临时变量map
            fileUrlMap = new HashMap<>();
            
            // 文件在CRIC
            if (Constant.FILE_SYSTEM_CRIC.equals(channelCode))
            {
                //
                // 查询路径
                String queryUrl = FileConfigParam.getFileConfigValue("CRIC_queryfile_path");
                // 下载路径
                String downloadUrl = FileConfigParam.getFileConfigValue("CRIC_downloadfile_path");
                // 授权号
                String permitCode = FileConfigParam.getFileConfigValue("CRIC_file_permit_code");
                
                // 原文件路径
                filePath = CricHelper.getFilePath(fileCode, queryUrl, downloadUrl, permitCode, getMd5PermitCode(permitCode));
                
            }
            else if (Constant.FILE_SYSTEM_WEIPHOTO.equals(channelCode))
            {
                
                // 文件类型
                String fileType = mo.getFileType();
                
                // 查询weiphoto系统文件路径
                String fileViewUrl = null;
                
                if (StringUtil.isEmpty(fileType) || "pic".equals(fileType))
                {
                    // 查询weiphoto系统文件（pic）路径
                    fileViewUrl = FileConfigParam.getFileConfigValue("wp_view_url");
                    
                    // 原文件路径
                    filePath = WeiphotoHelper.getPicPath(fileViewUrl, fileCode);
                    
                }
                else
                {
                    // 查询weiphoto系统文件（file）路径
                    fileViewUrl = FileConfigParam.getFileConfigValue("wp_view_file_url");
                    
                    // 原文件路径
                    filePath = WeiphotoHelper.getFilePath(fileViewUrl, fileCode);
                }
            }
            else if ("ESS".equals(channelCode))
            {
                
                // 查询路径
                String queryUrl = FileConfigParam.getFileConfigValue("ESS_view_url");
                
                // 原文件路径
                filePath = EssHelper.getFilePath(queryUrl, fileCode);
            }
            
            //
            fileUrlMap.put(mo.getFileNo(), filePath);
            fileUrlList.add(fileUrlMap);
        }
        
        resultData.setReturnData(fileUrlList);
        
        return resultData;
    }
    
    /**
     * 查询
     * 
     * @param id
     * @return
     */
    @Deprecated
    public Files getByFileNo(String fileNo)
        throws Exception
    {
        Files files = fileMapper.getByFileNo(fileNo);
        return files;
    }
    
    /**
     * 查询
     * 
     * @param id
     * @return
     */
    @Deprecated
    public ResultData<FileInfoDto> getFileInfoByFileNo(String fileNo)
        throws Exception
    {
        // 构建返回
        ResultData<FileInfoDto> resultData = new ResultData<FileInfoDto>();
        
        // Model转换Dto
        FileInfoDto dto = new FileInfoDto();
        
        Files mo = fileMapper.getByFileNo(fileNo);
        if (null == mo)
        {
            resultData.setFail("文件不存在");
            return resultData;
        }
        
        // 文件所在系统
        String channelCode = mo.getChannelCode();
        if (StringUtil.isEmpty(channelCode))
        {
            resultData.setFail("文件在渠道系统中缺失啦所在文件系统编号，请联系运维人员处理！");
            return resultData;
        }
        
        // 文件状态
        Integer fileState = mo.getFileState();
        if (Constant.FILE_CHANNEL_FILE_STATE_FINISH.intValue() != fileState.intValue() && 1002 != fileState.intValue())
        {
            resultData.setFail("文件状态不正确，请联系运维人员处理！");
            return resultData;
        }
        
        // 获取渠道配置信息
        Map<String, Object> fileCfgInfoMap = getFileCfgInfo(channelCode);
        
        dto.setFileCfgInfo(fileCfgInfoMap);
        
        BeanUtils.copyProperties(mo, dto);
        
        resultData.setReturnData(dto);
        
        return resultData;
    }
    
    /**
     * 获取渠道信息
     * 
     * @param param
     * @return
     */
    @Deprecated
    public ResultData<UploadInfoDto> getChannelInfo(Map<?, ?> param)
        throws Exception
    {
        
        // 构建返回
        ResultData<UploadInfoDto> resultData = new ResultData<UploadInfoDto>();
        
        // 拿到sysCode
        String systemCode = (String)param.get("systemCode");
        
        // 查询获取渠道信息
        Systems systems = systemMapper.getBySystemCode(systemCode);
        String channelCode = systems.getChannelCode();
        
        // 转换
        UploadInfoDto uploadInfoDto = new UploadInfoDto();
        uploadInfoDto.setChannelCode(channelCode);
        
        // 创建一条文件记录
        String fileNo = UUID.randomUUID().toString();
        
        Files file = new Files();
        file.setFileNo(fileNo);
        file.setSystemCode(systemCode);
        file.setChannelCode(channelCode);
        file.setFileState(Constant.FILE_CHANNEL_FILE_STATE_WAITING);
        fileMapper.create(file);
        
        uploadInfoDto.setFileId(file.getFileId());
        uploadInfoDto.setFileNo(fileNo);
        
        resultData.setReturnData(uploadInfoDto);
        
        return resultData;
    }
    
    /**
     * 根据fileNo删除
     * 
     * @param fileNo
     * @param operateId
     * @return
     * @throws Exception
     */
    @Deprecated
    public int deleteByFileNo(String fileNo)
        throws Exception
    {
        int count = fileMapper.deleteByFileNo(fileNo);
        return count;
    }
    
    /**
     * 查询-list
     * 
     * @param queryParam
     * @return
     */
    @Deprecated
    public ResultData<?> getCfgByChannelCode(String channelCode)
        throws Exception
    {
        
        // 构建返回
        ResultData<Map<String, Object>> resultData = new ResultData<Map<String, Object>>();
        
        Map<String, Object> rspMap = new HashMap<>();
        
        // 获取配置信息
        List<FileConfig> moList = fileConfigMapper.getCfgByChannelCode(channelCode);
        
        if (null != moList && !moList.isEmpty())
        {
            
            for (FileConfig fileConfig : moList)
            {
                
                rspMap.put(fileConfig.getConfigCode(), fileConfig.getConfigValue());
            }
        }
        
        // 设置文件系统配置--签名header部分的参数组装
        // weiphoto begin
        String securitykey = (String)rspMap.get("wp_securitykey");
        String appId = (String)rspMap.get("wp_H-appId");
        
        // headerMap
        Map<String, String> headerMap = new HashMap<String, String>();
        
        // 渠道号
        headerMap.put("H-appId", appId);
        
        // 签名时间(默认 单位秒)
        headerMap.put("H-timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        
        // 签名
        String sign = WeiphotoUtil.getSign(headerMap, securitykey);
        headerMap.put("H-sign", sign);
        
        rspMap.put("wp_headerMap", headerMap);
        // weiphoto end
        
        // CRIC begin
        String permitCode = (String)rspMap.get("CRIC_file_permit_code");
        String fileCategory = (String)rspMap.get("CRIC_file_category");
        
        Map<String, String> cricMap = new HashMap<String, String>();
        // 授权号
        cricMap.put("permit_code", permitCode);
        // 四位文件类型(系统中心申请注册，如CRIC)
        cricMap.put("file_category", fileCategory);
        // 验证令牌
        cricMap.put("key", getMd5PermitCode(permitCode));
        // 是否需要返回文件路径
        cricMap.put("is_return_path", "true");
        
        rspMap.put("cricMap", cricMap);
        
        // key
        rspMap.put("CRIC_key", getMd5PermitCode(permitCode));
        // CRIC end
        
        
        resultData.setReturnData(rspMap);
        
        return resultData;
        
    }
    
    /**
     * 获取文件系统配置
     * 
     * @param queryParam
     * @return
     */
    public Map<String, Object> getFileCfgInfo(String channelCode)
        throws Exception
    {
        
        // 构建返回
        Map<String, Object> rspMap = new HashMap<String, Object>();
        
        // 获取配置信息
        List<FileConfig> moList = fileConfigMapper.getCfgByChannelCode(channelCode);
        
        if (null != moList && !moList.isEmpty())
        {
            
            for (FileConfig fileConfig : moList)
            {
                
                rspMap.put(fileConfig.getConfigCode(), fileConfig.getConfigValue());
            }
        }
        
        // 设置文件系统配置--签名header部分的参数组装
        // weiphoto begin
        if (Constant.FILE_SYSTEM_WEIPHOTO.equals(channelCode))
        {
            String securitykey = (String)rspMap.get("wp_securitykey");
            String appId = (String)rspMap.get("wp_H-appId");
            
            // headerMap
            Map<String, String> headerMap = new HashMap<String, String>();
            
            // 渠道号
            headerMap.put("H-appId", appId);
            
            // 签名时间(默认 单位秒)
            headerMap.put("H-timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            
            // 签名
            String sign = WeiphotoUtil.getSign(headerMap, securitykey);
            headerMap.put("H-sign", sign);
            
            rspMap.put("wp_headerMap", headerMap);
        }
        
        // weiphoto end
        
        // CRIC begin
        else if (Constant.FILE_SYSTEM_CRIC.equals(channelCode))
        {
            String permitCode = (String)rspMap.get("CRIC_file_permit_code");
            String fileCategory = (String)rspMap.get("CRIC_file_category");
            
            Map<String, String> cricMap = new HashMap<String, String>();
            // 授权号
            cricMap.put("permit_code", permitCode);
            // 四位文件类型(系统中心申请注册，如CRIC)
            cricMap.put("file_category", fileCategory);
            // 验证令牌
            cricMap.put("key", getMd5PermitCode(permitCode));
            // 是否需要返回文件路径
            cricMap.put("is_return_path", "true");
            
            rspMap.put("CRIC_Map", cricMap);
            
            // key
            rspMap.put("CRIC_key", getMd5PermitCode(permitCode));
            
        }
        // CRIC end
        
        // ESS begin
        // ESS end
        
        return rspMap;
        
    }
    
    /**
     * 获取验证令牌(根据授权号和当天时间)
     * 
     * @param permitCode
     *            授权号
     * @return 验证令牌
     */
    private String getMd5PermitCode(String permitCode)
    {
        // 加密解密处理
        CricEncrypter cricEncrypter = new CricEncrypter();
        
        String mD5String = null;
        
        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        
        df.format(new Date());
        
        // 获取验证信息:授权号和当天时间（格式如20151031）的md5值
        try
        {
            mD5String = cricEncrypter.MD5(permitCode + df.format(new Date()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return mD5String;
    }
    
}
