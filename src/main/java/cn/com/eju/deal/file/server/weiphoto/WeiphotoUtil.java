package cn.com.eju.deal.file.server.weiphoto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WeiphotoUtil
{
    /**
     * 数字签名
     * @param headers
     * @param timeStamp
     * @return
     */
    public static String getSign(Map<String, String> headerMap, String singKey)
        throws Exception
    {
        
        //待签名signMap
        Map<String, String> signMap = new HashMap<String, String>();
        
        //遍历headerMap
        for (Entry<String, String> entry : headerMap.entrySet())
        {
            signMap.put(entry.getKey(), entry.getValue());
        }
        
        return WeiphotoEncrypter.sha1Sign(signMap, singKey);
    }
}
