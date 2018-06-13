package com.lansitec.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnWapper {
	Logger logger = LoggerFactory.getLogger(HttpConnWapper.class);
	String baseUrl;
	String charset = "UTF-8";     
	
	URL realUrl;
	HttpURLConnection connection;
	
	
	public void initConnection(String url) {
		baseUrl = url;
	}
	
	public void addUrlParaBeforeOpen(String key, String value) {
		if (!baseUrl.contains("?")) {
			baseUrl = baseUrl + "?"+ key + "=" + value;
		} else {
			baseUrl = baseUrl + "&"+ key + "=" + value;
		}
	}
	
	public void openConnection() throws Exception {
		realUrl = new URL(baseUrl);
		connection = (HttpURLConnection)realUrl.openConnection();
		
		connection.setRequestProperty("accept", "application/json");
		connection.setRequestProperty("content-Type", "application/json");
		//keep-alive表示之前的握手还可以用在接下来的请求当中去
        //connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("connection", "close");
        connection.setRequestProperty("accept-Charset", "utf-8");
	}
	
	public void setHeaderParaAfterOpen(String key, String value) {
		connection.setRequestProperty(key, value);
	}
	
	public String doPost(String body) throws Exception {
		String result = "";
		OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String tempLine = null;
        
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("content-Length", String.valueOf(body.length()));
		
		try {
			outputStream = connection.getOutputStream();
	        outputStreamWriter = new OutputStreamWriter(outputStream);
	        
	        outputStreamWriter.write(body);//读取输出
	        outputStreamWriter.flush();
	        
	        logger.info("post message to {}, content {}", baseUrl, body);
	        if (connection.getResponseCode() >= 300) {
	            throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
	        }
	        
	        inputStream = connection.getInputStream();//相当于HttpURLConnection.openconnect();
	        inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
	        reader = new BufferedReader(inputStreamReader);//将输入数据放入缓冲区
	        
	        while ((tempLine = reader.readLine()) != null) {
	            result += tempLine;
	        }
		} catch (Exception e) {
            System.out.println("post Exception "+e);
            throw e;
        } finally {
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
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
		
		return result;
	}
}
