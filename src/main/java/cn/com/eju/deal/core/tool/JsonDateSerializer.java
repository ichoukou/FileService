package cn.com.eju.deal.core.tool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**   
* SpringMVC日期转换之JsonSerialize
* @author (li_xiaodong)
* @date 2016年2月29日 下午5:12:04
*/
public class JsonDateSerializer extends JsonSerializer<Date>
{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void serialize(Date date, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        
        String formattedDate = dateFormat.format(date);
        jgen.writeString(formattedDate);
    }
}
