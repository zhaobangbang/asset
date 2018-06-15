package com.lans.common;

import java.util.HashMap;
import java.util.Map;

public class JqueryGridParser {

    public static String parserGridString(String inStr, Map<String, String> outEditMap){
          Map<String, String> editMap = new HashMap<String, String>();
          
          String[] keySplit=inStr.split("[&]");//split() �������ڰ�һ���ַ����ָ���ַ�������
          
          for(int i=0;i<keySplit.length;i++) {
               String []keyValue=keySplit[i].split("[=]");
               //��������ֵ
               if(keyValue.length>1) {
                    //��ȷ����
                    editMap.put(keyValue[0], keyValue[1]);
               }               
          }
          
          String oper = null;
          //ֻ����ɾ�Ĳ�������operԪ�أ����ر��Ͳ�ѯ��������Ԫ�أ�
           //���غͲ�ѯ����_search(boolean)Ԫ��,false��ʾ���ر�� ,true��ʾ��ѯ����
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
