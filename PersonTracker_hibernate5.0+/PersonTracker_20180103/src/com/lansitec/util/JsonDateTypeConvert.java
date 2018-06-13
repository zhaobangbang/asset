package com.lansitec.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonDateTypeConvert extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		//定义日期转化对象
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//转化日期为指定格式的字符串
		String dateStr = sdf.format(date);
		//以json格式输出字符串
		jsonGenerator.writeString(dateStr);
		
	}

}
