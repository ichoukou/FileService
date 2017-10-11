package cn.com.eju.deal.base.file.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.eju.deal.base.file.dto.FileDto;
import cn.com.eju.deal.base.file.ess.EssHelper;
import cn.com.eju.deal.base.service.BaseService;
import cn.com.eju.deal.base.support.SystemParam;
import cn.com.eju.deal.core.enums.SystemCode;
import cn.com.eju.deal.core.support.ResultData;

/**   
* 调用-文件渠道管理系统
* @author (li_xiaodong)
* @date 2016年2月2日 下午9:30:27
*/
@Service("filesService")
public class FilesService extends BaseService<Object>
{
    
    /** 
     * 请求上传
     * @param fileNo
     * @return
     * @throws Exception
     */
    @Deprecated
    public ResultData<?> reqUpload(String systemCode)
        throws Exception
    {
        //接口 相对url
        String relativeUrl = "files/requpload/{systemCode}";
        
        //调用 接口
        String url = absoluteUrl(SystemCode.SYSTEM_CODE_FILE.getCode(), relativeUrl);
        
        ResultData<?> backResult = get(url, systemCode);
        
        return backResult;
    }
    
    /** 
    * 上传完成
    * @param fileDto
    * @return
    * @throws Exception
    */
    @Deprecated
    public void update(FileDto fileDto)
        throws Exception
    {
        
        //接口 相对url
        String relativeUrl = "files";
        
        //调用 接口
        String url = absoluteUrl(SystemCode.SYSTEM_CODE_FILE.getCode(), relativeUrl);
        
        put(url, fileDto);
        
    }
    
    /** 
     * 获取文件
     * @param reqMap
     * @return
     * @throws Exception
     */
    public ResultData<?> getFilePath(Map<String, Object> reqMap)
        throws Exception
    {
        
        //        //接口相对url
        //        String relativeUrl = "files/{param}";
        //        
        //        //调用 接口
        //        String url = absoluteUrl(SystemCode.SYSTEM_CODE_FILE.getCode(), relativeUrl);
        //        
        //        ResultData<?> backResult = get(url, reqMap);
        
        // 构建返回
        ResultData<String> resultData = new ResultData<String>();
        
        // 文件路径
        String filePath = null;
        
        // 文件编号
        String fileCode = (String)reqMap.get("fileNo");
        
        // 文件 宽
        String essWidth = (String)reqMap.get("essWidth");
        // 文件高
        String essHeight = (String)reqMap.get("essHeight");
        
        // 查询路径
        String queryUrl = SystemParam.getWebConfigValue("ESS_view_url");
        
        // 原文件路径
        filePath = EssHelper.getFilePath(queryUrl, fileCode, essWidth, essHeight);
        
        resultData.setReturnData(filePath);
        
        return resultData;
    }
    
}
