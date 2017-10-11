package cn.com.eju.deal.base.file.cric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.eju.deal.base.file.util.FileEmulator;
import cn.com.eju.deal.base.file.util.FileUtil;

import com.alibaba.fastjson.JSONObject;

/**   
* 文件操作(上传、下载、查看)帮助类,供CricHelper类调用
* @author mimi.sun
* @date 2015年11月2日 下午5:37:56
*/
public class CricHandle
{
    
    /**
    * 文件上传是否预处理-是
    */
    public static final String UPLOAD_FILE_IS_HANDLE_YES = "1";
    
    /**
     * 文件上传是否预处理-否
     */
    public static final String UPLOAD_FILE_IS_HANDLE_NO = "0";
    
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(CricHandle.class);
    
    /** 
     * 上传操作--默认
     * @return 响应结果 {{pic_height=352, flag=true, pic_width=441, pic_size=43317, 
     * pic_path=/BHME/Source_pic/cd/62/BHMEc6dfcb94569a9ec6a43ecb1aa3402c2d.jpg, 
     * pic_id=BHMEc6dfcb94569a9ec6a43ecb1aa3402c2d.jpg}}
     */
    public Map<String, Object> upload(File file, String fileName, String isHandle, Map<String, Object> optionalMap,
        Map<?, ?> configMap)
    {
        return upload(file, null, fileName, isHandle, optionalMap, configMap);
    }
    
    /** 
     * 上传操作--流
     * @return 响应结果 {{pic_height=352, flag=true, pic_width=441, pic_size=43317, 
     * pic_path=/BHME/Source_pic/cd/62/BHMEc6dfcb94569a9ec6a43ecb1aa3402c2d.jpg, 
     * pic_id=BHMEc6dfcb94569a9ec6a43ecb1aa3402c2d.jpg}}
     */
    public Map<String, Object> upload(byte[] b, String fileName, String isHandle, Map<String, Object> optionalMap,
        Map<?, ?> configMap)
    {
        return upload(null, b, fileName, isHandle, optionalMap, configMap);
    }
    
    /** 
    * 上传操作
    * @return 响应结果 {{pic_height=352, flag=true, pic_width=441, pic_size=43317, 
    * pic_path=/BHME/Source_pic/cd/62/BHMEc6dfcb94569a9ec6a43ecb1aa3402c2d.jpg, 
    * pic_id=BHMEc6dfcb94569a9ec6a43ecb1aa3402c2d.jpg}}
    */
    private Map<String, Object> upload(File file, byte[] b, String fileName, String isHandle,
        Map<String, Object> optionalMap, Map<?, ?> configMap)
    {
        //构建响应
        Map<String, Object> rspMap = new HashMap<String, Object>();
        
        try
        {
          //上传图片地址
            String uploadUrl = (String)configMap.get("CRIC_uploadfile_path");
            
            //文本类型的参数
            //Map<String, String> params = new HashMap<String, String>();
            
            //headerMap{"permit_code":"","file_category":"","key":"","is_return_path":""} 授权号/四位文件类型/验证令牌/是否需要返回文件路径
            @SuppressWarnings("unchecked")
            Map<String, String> params = (Map<String, String>)configMap.get("CRIC_Map");
            
            //授权号
            //params.put("permit_code", permitCode);
            //四位文件类型(系统中心申请注册，如CRIC)
            //params.put("file_category", fileCategory);
            //验证令牌
            //params.put("key", getMd5PermitCode(permitCode));
            
            //上传文件的byte数组
            byte[] bs = new byte[0];
            
            //将文件内容转成byte数组
            if (null != file)
            {
                bs = FileUtil.appendBytes(bs, FileUtil.getFileBytes(file));
            }
            else if (null != b)
            {
                bs = b;
            }
            
            //文件内容的md5
            //String md5fileInfo = cricEncrypter.MD5(bs.toString());
            //文件编号
            //params.put("fileid", fileCategory + md5fileInfo);
            //是否需要返回文件路径
            //params.put("is_return_path", "true");
            
            //选择预处理(进行图片处理)
            if (UPLOAD_FILE_IS_HANDLE_YES.equals(isHandle))
            {
                JSONObject obj = new JSONObject();
                JSONObject obj2 = new JSONObject();
                //图片宽度
                obj2.put("width", optionalMap.get("width"));
                //图片高度
                obj2.put("height", optionalMap.get("height"));
                //裁剪类型
                obj2.put("cut_type", optionalMap.get("cutType"));
                //水印位置
                obj2.put("water_position", optionalMap.get("waterPosition"));
                //水印图片
                obj2.put("water_pic", optionalMap.get("waterPic"));
                obj.put("1", obj2);
                //预处理参数
                params.put("params", obj.toJSONString());
            }
            
            //数据流的形式
            Map<String, byte[]> files = new HashMap<String, byte[]>();
            
            //获取文件名称
            //String fileName = file.getName();
            files.put(fileName, bs);
            
            // 请求文件上传操作
            FileEmulator fileEmulator = new FileEmulator();
            rspMap = fileEmulator.executeCricUpload(uploadUrl, files, params);
            
        }
        catch (FileNotFoundException e)
        {
            logger.error("", e);
            rspMap.put("flag", false);
            rspMap.put("result", e.getMessage());
        }
        catch (IOException e)
        {
            logger.error("", e);
            rspMap.put("flag", false);
            rspMap.put("result", e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            rspMap.put("flag", false);
            rspMap.put("result", e.getMessage());
        }
        return rspMap;
    }
    
}
