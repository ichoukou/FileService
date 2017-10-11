package cn.com.eju.deal.base.file.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.support.ReturnMsg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**   
 * weiphoto--上传类
* weiphoto文件系统 -- 上传主方法
* @author (li_xiaodong)
* @date 2016年6月29日 上午11:42:13
*/
public class FileEmulator
{
    
    /** 
    * weiphoto
    * @param serverUrl 服务器URL 
    * @param formMap
    * @param fileItemMap
    * @param headerMap
    * @param inputStream
    * @return
    * @throws Exception
    */
    public String executeWeiphotoUpload(String serverUrl, Map<String, String> fileItemMap, Map<String, String> headerMap,
        InputStream inputStream)
        throws Exception
    {
        // 每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";
        
        // 向服务器发送post请求  
        URL url = new URL(serverUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        
        // 设置HttpURLConnection参数
        
        // 允许输入
        conn.setDoInput(true);
        // 允许输出
        conn.setDoOutput(true);
        // 不允许使用缓存
        conn.setUseCaches(false);
        // 设定请求的方法为"POST"
        conn.setRequestMethod("POST");
        // 设定传送的内容类型
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        
        //遍历headerMap
        for (Entry<String, String> entry : headerMap.entrySet())
        {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        
        // 头  
        String boundary = BOUNDARY;
        // 传输内容  
        StringBuffer contentBody = new StringBuffer("--" + BOUNDARY);
        // 尾  
        String endBoundary = "\r\n--" + boundary + "--\r\n";
        OutputStream out = conn.getOutputStream();
        
        String boundaryMessage1 = contentBody.toString();
        out.write(boundaryMessage1.getBytes("utf-8"));
        
        // 2. 处理文件上传  
        //遍历fileItemMap
        for (Entry<String, String> entry : fileItemMap.entrySet())
        {
            
            contentBody = new StringBuffer();
            contentBody.append("\r\n").append("Content-Disposition:form-data; name=\"").append(entry.getKey() + "\"; ")
            // form中field的名称  
                .append("filename=\"")
                .append(entry.getValue() + "\"")
                // 上传文件的文件名，包括目录  
                .append("\r\n")
                .append("Content-Type:application/octet-stream")
                .append("\r\n\r\n");
            String boundaryMessage2 = contentBody.toString();
            out.write(boundaryMessage2.getBytes("utf-8"));
            
            // 开始真正向服务器写文件 
            
            //file
            if (null != fileItemMap && !fileItemMap.isEmpty())
            {
                File file = new File(entry.getValue());
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[(int)file.length()];
                bytes = dis.read(bufferOut);
                out.write(bufferOut, 0, bytes);
                dis.close();
            }
            
            //流
            if (null != inputStream)
            {
                byte[] dataBuf = new byte[2048];
                BufferedInputStream bis = new BufferedInputStream(inputStream, 8192);
                int count = 0;
                while ((count = bis.read(dataBuf)) != -1)
                {
                    out.write(dataBuf, 0, count);
                }
            }
            
            contentBody.append("------------HV2ymHFg03ehbqgZCaKO6jyH");
            String boundaryMessage = contentBody.toString();
            out.write(boundaryMessage.getBytes("utf-8"));
            
        }
        
        out.write("------------HV2ymHFg03ehbqgZCaKO6jyH--\r\n".getBytes("UTF-8"));
        
        // 3. 写结尾  
        out.write(endBoundary.getBytes("utf-8"));
        out.flush();
        out.close();
        
        // 4. 从服务器获得回答的内容  
        String strLine = "";
        String strResponse = "";
        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while ((strLine = reader.readLine()) != null)
        {
            strResponse += strLine + "\n";
        }
        
        in.close();
        
        System.out.print("从服务器获得的应答是：" + strResponse);
        return strResponse;
        
    }
    
    /** 
    * 请求文件上传--Cric
    * @param conn HttpUrlConnection对象
    * @param sb 组拼后的文本类型参数
    * @param files 文件数据
    * @return 响应结果
    * @throws FileNotFoundException
    * @throws IOException
    */
    public Map<String, Object> executeCricUpload(String serverUrl, Map<String, byte[]> files, Map<String, String> params)
        throws Exception
    {
        //构建返回
        Map<String, Object> hashMap = new HashMap<String, Object>();
        
        //界限字符串
        String boundary = "-------" + new Date();
        
        // 设置文件上传的路径
        URL url = new URL(serverUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        
        // 设置HttpUrlConnection参数
        StringBuilder sb = new StringBuilder();
        
        // 允许输入
        conn.setDoInput(true);
        // 允许输出
        conn.setDoOutput(true);
        // 不允许使用缓存
        conn.setUseCaches(false);
        // 设定请求的方法为"POST"
        conn.setRequestMethod("POST");
        // 设定传送的内容类型
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        
        //组拼文本类型的参数
        
        for (Entry<String, String> entry : params.entrySet())
        {
            sb.append("--");
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + "\r\n");
            sb.append("\r\n");
            sb.append(entry.getValue());
            sb.append("\r\n");
        }
        
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        
        // 发送文件数据
        if (files != null)
        {
            for (Entry<String, byte[]> file : files.entrySet())
            {
                StringBuilder sb1 = new StringBuilder();
                sb1.append("--");
                sb1.append(boundary);
                sb1.append("\r\n");
                sb1.append("Content-Disposition: form-data; name=\"pfile\"; filename=\"" + file.getKey() + "\""
                    + "\r\n");
                sb1.append("Content-Type: application/octet-stream; charset=UTF-8" + "\r\n");
                sb1.append("\r\n");
                outStream.write(sb1.toString().getBytes());
                
                outStream.write(file.getValue());
                
                outStream.write("\r\n".getBytes());
            }
            // 请求结束标志
            byte[] end_data = ("--" + boundary + "--" + "\r\n").getBytes();
            outStream.write(end_data);
            outStream.flush();
            
            StringBuilder strBui = null;
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200)
            {
                InputStream in = conn.getInputStream();
                int ch;
                strBui = new StringBuilder();
                while ((ch = in.read()) != -1)
                {
                    strBui.append((char)ch);
                }
                
                in.close();
                
                // 将json字符串转换成jsonObject
                JSONObject jsonObj = JSONObject.parseObject(strBui.toString());
                JSONObject jsonObj2 = jsonObj.getJSONObject("result");
                hashMap = reflect(jsonObj2);
                hashMap.put("flag", jsonObj.get("flag"));
            }
            else
            {
                hashMap.put("flag", false);
                hashMap.put("result", res);
            }
            
            outStream.close();
            conn.disconnect();
        }
        else
        {
            hashMap.put("flag", false);
            hashMap.put("result", "上传文件是空文件!");
        }
        //服务器返回来的数据
        return hashMap;
    }
    
    /** 
    * ESS 上传
    * @param uploadUrl 文件服务器url
    * @param fileKey 文件唯一编码，提前生成
    * @param filePath 文件绝对路径
    * @return
    */
    public Map<String, Object> executeEssUpload(String uploadUrl, String fileKey, String filePath)
    {
        
        //总返回Map
        Map<String, Object> rpMap = new HashMap<String, Object>();
        
        /**
         * Spring提供的用于访问Rest服务的客户端
         */
        RestTemplate restTemplate = new RestTemplate();
        
        //上传地址
        String serverUrl = String.format(uploadUrl, fileKey);
        
        //设置上传对象的路径
        PathResource pathResource = new PathResource(filePath);
        
        /*
         * 根据需求设置对应的header，设计到的header种类比较多，下面只是 列举出了常见的header，
         * 更多的hader配置参考【ESS API (6)：File Operation API -- Put File_v0.3】 帮助文档。
         */
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Content-Type", "image/png");
        //      headers.set("Content-MD5", Md5Utils.md5AsBase64(pathResource.getFile()));
        //      headers.set("Content-Encoding", "UTF-8");
        
        /*
         * 发起put请求，上传成功以后返回状态码【200】，更多的状态码定义信息参考文档 ESS API (4)：Error
         * Responses_v0.1
         */
        HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(pathResource, headers);
        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.PUT, httpEntity, String.class);
        
        if (HttpStatus.OK.equals(response.getStatusCode()))
        {
            rpMap.put(Constant.RETURN_CODE_KEY, ReturnCode.SUCCESS);
            rpMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.SUCCESS_UPLOAD_FILE_MSG);
        }
        else
        {
            rpMap.put(Constant.RETURN_CODE_KEY, ReturnCode.FAILURE);
            rpMap.put(Constant.RETURN_MSG_KEY, ReturnMsg.ERROR_UPLOAD_FILE_MSG);
        }
        
        return rpMap;
    }
    
    /** 
     * 将JSONObjec对象转换成Map-List集合 
     * @see JSONHelper#reflect(JSONArray) 
     * @param json 需要转换的JSON对象
     * @return 转换后的Map对象
     */
    private Map<String, Object> reflect(JSONObject json)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<?> keys = json.keySet();
        for (Object key : keys)
        {
            Object o = json.get(key);
            if (o instanceof JSONObject)
                map.put((String)key, reflect((JSONObject)o));
            else
                map.put((String)key, o);
        }
        return map;
    }
}
