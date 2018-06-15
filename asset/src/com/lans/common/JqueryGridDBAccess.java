package com.lans.common;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JqueryGridDBAccess {
	static Logger logger = LoggerFactory.getLogger(JqueryGridDBAccess.class);
    public static int insert(DataBaseMgr dbm, String tbName, Map<String,String> editmap) {
    	int ret=0;
        String rowName="";
        String rowValue="";
        
        editmap.remove("oper");
        if(editmap.containsKey("id")) {
        	editmap.remove("id");
        }
         
        for (Map.Entry<String, String> entry : editmap.entrySet()) {
        	rowName=rowName+entry.getKey()+",";
        	rowValue=rowValue+"?,";
        }
        rowName=rowName.substring(0,rowName.length()-1);//删除最后一个“,”
        rowValue=rowValue.substring(0,rowValue.length()-1);
        rowName="("+rowName+")";
        rowValue="("+rowValue+")";
         
        String sql="insert into "+tbName+rowName+" values"+rowValue;
        logger.info("DBAccess "+ sql);
        
        ret = dbm.executePrepareStatement(sql, editmap);
        return ret;
    }
    
    public static int update(DataBaseMgr dbm, String tbName,Map<String,String> editmap) {
    	int ret=0;
        String rowName="";
        String rowValue="";
        String setStr="";
        
        editmap.remove("oper");
        String idStr=editmap.get("id");
        if (null == idStr) {
        	return 0;
        }
         
        editmap.remove("id");
        
        for (Map.Entry<String, String> entry : editmap.entrySet()) {
        	rowName=entry.getKey();
        	rowValue=entry.getValue();
        	if (rowName.equals("whereField") || rowName.equals("whereString")) {
        		continue;
        	}
        	setStr=setStr+rowName+"=\""+rowValue+"\",";
        }
        setStr=setStr.substring(0,setStr.length()-1);
        
        int id=Integer.parseInt(idStr); 
        String sql="update "+tbName+" set "+setStr+" where id="+id;
        
        String whereF = editmap.get("whereField");
        if (null != whereF) {
        	String whereStr = editmap.get("whereString");
        	sql = sql + " and " + whereF+"=\""+whereStr+"\"";
        }
        
        logger.info(sql);
        
        ret = dbm.executeUpdate(sql);
        return ret;
   }

   public static int delete(DataBaseMgr dbm, String tbName,Map<String,String> editmap) {
        int ret = 0;

        editmap.remove("oper");
        String idStr = editmap.get("id");
        if (null == idStr) {
        	return 0;
        }
        String[] delId =  idStr.split(",");
        StringBuffer idList = new StringBuffer();
        for(int index=0; index <delId.length; index++)
        	  idList.append("\'"+delId[index]+"\',");
        if(idList.length() > 1)
		{
        	  idList.deleteCharAt(idList.length()-1);
		}
        String sql="delete from "+tbName+" where id in ("+idList + ")";
        
        String whereF = editmap.get("whereField");
        if (null != whereF) {
        	String whereStr = editmap.get("whereString");
        	sql = sql + " and " + whereF+"=\""+whereStr+"\"";
        }

        logger.info("DBAccess "+ sql);
        ret = dbm.executeUpdate(sql);
        
        return ret;
   }
   
   public static String load(DataBaseMgr dbm,String tbName,Map<String,String> editmap) {
        int page;//要显示的页码
        int row;//每页的行数
        int records=0;//总的记录数
        int total=0;//总的页数
        String rowName="";
        String rowValue="";
        String loadStr = "";
        String sidx = "";
        String sord = "";
        boolean condInPost = false;
        boolean whereFieldExist = false;
        boolean condStringExist = false;//存在拼装的条件字符串
        
        page=Integer.parseInt(editmap.get("page"));
        row=Integer.parseInt(editmap.get("rows"));
        
        int start=((page-1)*row);
        
        if(start < 0)
        	start = 0;
        
        sidx = editmap.get("sidx");
        sord = editmap.get("sord");
        
        //remove unused fields for later process
        editmap.remove("_search");
        editmap.remove("nd");
        editmap.remove("rows");
        editmap.remove("page");
        editmap.remove("sidx");
        editmap.remove("sord");
        
        String gridStr=null;
        String sql="select * from "+tbName;
        String sqlTotal;
        
        //this is to process some load/query condition comes from JQGrid
        for (Map.Entry<String, String> entry : editmap.entrySet()) {
        	rowName=entry.getKey();
        	rowValue=entry.getValue();
        	if (rowName.equals("whereField") || rowName.equals("whereString") || rowName.equals("condString")) {
        		continue;
        	}
        	loadStr=loadStr+rowName+"=\""+rowValue+"\" and ";
        	condInPost = true;
        }
        
        if (true == condInPost) {
        	loadStr=loadStr.substring(0,loadStr.length()-5);
        }
        
      //this is to process the specific whereField condition added by servlet
        String whereF = editmap.get("whereField");
        String whereStr = null;
        if (null != whereF) {
        	whereStr = editmap.get("whereString");
        	whereFieldExist = true;
        }
        
        String usrDefStr = editmap.get("condString");
        if(null != usrDefStr)
        {
        	condStringExist = true;
        }
        
        if ((condInPost == true) || (whereFieldExist == true) || (true == condStringExist)) {
        	sql += " where ";
        	if (condInPost == true) {
        		sql += loadStr+" and ";
        	}
        	if (whereFieldExist == true) {
        		sql += whereF+"=\""+whereStr+"\""+" and ";
        	}
            if(true == condStringExist)
            {
            	sql += usrDefStr + " and ";
            }
        	sql = sql.substring(0,sql.length()-5);
        }
         
        sqlTotal = sql;
        
        if(null != sidx)
        {
        	if(null == sord)
        		sord = "asc";
        	sql = sql + " order by " + sidx + " " + sord;
        }
        sql = sql + " limit "+start+","+row;
        
        logger.debug("DBAccess "+ sql);
        
        List<JSONObject> jsonList=new ArrayList<JSONObject>();
        JSONArray json=new JSONArray();
        ResultSet rs = null;
        try{
	       	rs = dbm.executeQuery(sql);
	       	ResultSetMetaData data=rs.getMetaData(); 
	       	
	       	rs.beforeFirst();
	       	while(rs.next()) {
	       		JSONObject jsonObject=new JSONObject();
	       		for(int i = 1 ; i<= data.getColumnCount() ; i++) { 
	       			String fieldName = data.getColumnName(i);
	       			String value = rs.getString(fieldName);
	       			if (null == value) {
	       				value = "NA";
	       			}
	       			jsonObject.put(fieldName, value);
	       		}
	       		
	       		logger.debug("got one line "+ jsonObject.toString());
	       		jsonList.add(jsonObject);
	       	}//while
	       	json = JSONArray.fromObject(jsonList);
	    } catch(SQLException ex) {
	    	logger.error("executeQuery1:" + ex.getMessage());
	    }
        
		//get total numbers of records matching whereField
        /*
		String sqlTotal = "select * from "+tbName;
		if(whereFieldExist == true || condStringExist == true)
		{
			sqlTotal += " where ";
			if (whereFieldExist == true) {
				sqlTotal += whereF+"=\""+whereStr+"\" and ";
			}
			
			if(condStringExist == true){
				sqlTotal += usrDefStr;
			}
			else
			{
				sqlTotal = sqlTotal.substring(0,sqlTotal.length()-5);
			}
		
		}
		sqlTotal += ";";
		*/
	    rs=dbm.executeQuery(sqlTotal);	
        
	    try {
	    	rs.last();
	    	records = rs.getRow();
	    } catch(SQLException ex) {
	    	logger.error("executeQuery2:" + ex.getMessage());
	    }
	   
		total=records/row;
		if (records%row != 0) {
			total += 1;
		}
		
	    gridStr="{\"page\":"+page+","+" \"total\":"+total+","+"\"records\":"+records+","+"\"rows\":"+json.toString()+"}";
			
		return gridStr;
   }
   
   /*
   public static String query(DataBaseMgr dbm,String tbName,Map<String,String> editmap) {
       int page;//要显示的页码
       int row;//每页的行数
       int records=0;//总的记录数
       int total=0;//总的页数
 
       
       page=Integer.parseInt(editmap.get("page"));
       row=Integer.parseInt(editmap.get("rows"));
       int start=((page-1)*row);
       
       String gridStr=null;
       String sql="select * from "+tbName;
       
       String whereF = editmap.get("whereField");
       if (null != whereF) {
       	String whereStr = editmap.get("whereString");
       	sql = sql + " where " + whereF+"=\""+whereStr+"\"";
       }
       
       sql = sql + " limit "+start+","+row;
       
       List<JSONObject> jsonList=new ArrayList<JSONObject>();
       JSONArray json=new JSONArray();
       ResultSet rs = null;
       try{
	       	rs = dbm.executeQuery(sql);
	       	ResultSetMetaData data=rs.getMetaData(); 
	       	
	       	rs.beforeFirst();
	       	while(rs.next()) {
	       		JSONObject jsonObject=new JSONObject();
	       		for(int i = 1 ; i<= data.getColumnCount() ; i++) { 
	       			String fieldName = data.getColumnName(i);
	       			String value = rs.getString(fieldName);
	       			if (null == value) {
	       				value = "NA";
	       			}
	       			jsonObject.put(fieldName, value);
	       		}
	       		
	       		//System.out.println("got one line "+ jsonObject.toString());
	       		jsonList.add(jsonObject);
	       	}//while
	       	json = JSONArray.fromObject(jsonList);
	    } catch(SQLException ex) {
	    	System.err.println("executeQuery1:" + ex.getMessage());
	    }
       
		//获取总的记录数
		String sql1="select * from "+tbName+";";
	    rs=dbm.executeQuery(sql1);

	    try {
	    	rs.last();
	    	records = rs.getRow();
	    } catch(SQLException ex) {
	    	System.err.println("executeQuery2:" + ex.getMessage());
	    }
	    
		total=records/row+1;
	    gridStr="{\"page\":"+page+","+" \"total\":"+total+","+"\"records\":"+records+","+"\"rows\":"+json.toString()+"}";
		
		return gridStr;
  	}*/
}
