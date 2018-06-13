package com.lansitec.common;

import java.util.HashMap;
import java.util.Map;

public class JqueryGridParser {

    public static String parserGridString(String inStr, Map<String, String> outEditMap){
          Map<String, String> editMap = new HashMap<String, String>();
          
          String[] keySplit=inStr.split("[&]");//split() 方法用于把一个字符串分割成字符串数组
          
          for(int i=0;i<keySplit.length;i++) {
               String []keyValue=keySplit[i].split("[=]");
               //解析出键值
               if(keyValue.length>1) {
                    //正确解析
                    editMap.put(keyValue[0], keyValue[1]);
               }               
          }
          
          String oper = null;
          //只有增删改操作包含oper元素，加载表格和查询不包含此元素，
           //加载和查询包含_search(boolean)元素,false表示加载表格 ,true表示查询操作
          if(editMap.containsKey("oper")) {
               oper = new String(editMap.get("oper"));
          }
          else if(editMap.containsKey("_search")) {
        	  if(editMap.get("_search")=="true") {
        		  oper="query";
        	  }
        	  else {
        		  oper="load";
        	  }
          }
          else {
        	  oper="load";
          }
          
          outEditMap.putAll(editMap);
          return oper;
    }
}
