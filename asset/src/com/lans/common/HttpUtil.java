package com.lans.common;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;

public class HttpUtil {
    /**
     * ��ָ��URL����GET����������
     * 
     * @param url
     *            ���������URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return URL ������Զ����Դ����Ӧ���
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
 
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
            // ����ʵ�ʵ�����
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            //Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
           // for (String key : map.keySet()) {
                //LansUtil.LogDebug(key + "--->" + map.get(key));
           // }
            // ���� BufferedReader����������ȡURL����Ӧ
          //  in = new BufferedReader(new InputStreamReader(
          //          connection.getInputStream(), "UTF-8"));
            
            inputStream = connection.getInputStream();
            if(inputStream == null)
            {
            	System.err.println("sendGet: Null inputStream");
            	return "";
            }
            else
            {
            	inputStreamReader = new InputStreamReader(inputStream, "UTF-8");            	
                in = new BufferedReader(inputStreamReader);
            		
            	String line;
            	while ((line = in.readLine()) != null) {
            		result += line;
            	}
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ��ָ�� URL ����POST����������
     * 
     * @param url
     *            ��������� URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ������Զ����Դ����Ӧ���
     */
    public static String sendPost(String url, String param) {
        String result = "";
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        //StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)conn;
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("contentType", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            
            outputStreamWriter.write(param.toString());
            outputStreamWriter.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                //resultBuffer.append(tempLine);
                result += tempLine;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
        finally{
            try{
            	if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                
                if (outputStream != null) {
                    outputStream.close();
                }
                
                if (reader != null) {
                    reader.close();
                }
                
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    } 
}
