package com.lansitec.handle.data;

public class ParamentSave {
	public static int records = 0;
	public synchronized static int getParamentStart(int page,int row){
		int start=((page-1)*row);
	    
	    if(start < 0)
	    	start = 0;
	   return start;
	}
	
	public synchronized static int respTotal(int records,int row){
		
		int total = records/row;//get all pageNumber
		if (records%row != 0) {
			total += 1;
		}
		return total;
	}
	
	public synchronized static int resPage(int page){
		if(page-1 != 0){
			page = page - 1;
		}
		return page;
	}
	
	@SuppressWarnings("null")
	public synchronized static String getJoinSQL(String tbName,String paramentOneStr,String paramentOneValue,String paramentTwoStr,String paramentTwoValue,
			                                     String paramentThreeStr,String paramentThreeValue){
		String hqlBefore = "from "+tbName+" ";
		String hql = "";
		if(((null == paramentOneStr) || (paramentOneStr.equals(""))) && ((null == paramentTwoStr) || (paramentTwoStr.equals(""))) && (((null == paramentThreeStr) || (paramentThreeStr.equals(""))))){
			hql = hqlBefore;
			return hql;
		}else if(((null != paramentOneStr) || (!paramentOneStr.equals(""))) && ((null == paramentTwoStr) || (paramentTwoStr.equals(""))) && (((null == paramentThreeStr) || (paramentThreeStr.equals(""))))){
			if(tbName.equals("dev_info_tbl")){
				hql = "from DevInfo dev "+"where "+paramentOneStr+" = '"+paramentOneValue+"'" + " ORDER BY deveui ";
			}else{
				hql = "from DevInfo dev "+"where "+paramentOneStr+" = '"+paramentOneValue+"'";
			}
		}else if(((null == paramentOneStr) || (paramentOneStr.equals(""))) && ((null != paramentTwoStr) || (!paramentTwoStr.equals(""))) && (((null == paramentThreeStr) || (paramentThreeStr.equals(""))))){
			hql = hqlBefore+"where "+paramentTwoStr+" = "+paramentTwoValue+"";
		}else if(((null == paramentOneStr) || (paramentOneStr.equals(""))) && ((null == paramentTwoStr) || (paramentTwoStr.equals(""))) && (((null != paramentThreeStr) || (!paramentThreeStr.equals(""))))){
			hql = hqlBefore+"where "+paramentThreeStr+" = "+paramentThreeValue+"";
		}else if(((null != paramentOneStr) || (!paramentOneStr.equals(""))) && ((null != paramentTwoStr) || (!paramentTwoStr.equals(""))) && (((null == paramentThreeStr) || (paramentThreeStr.equals(""))))){
			hql = hqlBefore+"where "+paramentOneStr+" = "+paramentOneValue+" and "+paramentTwoStr+" = '"+paramentTwoValue+"'";
		}else if(((null != paramentOneStr) || (!paramentOneStr.equals(""))) && ((null == paramentTwoStr) || (paramentTwoStr.equals(""))) && (((null != paramentThreeStr) || (!paramentThreeStr.equals(""))))){
			hql = hqlBefore+"where "+paramentOneStr+" = '"+paramentOneValue+"' and "+paramentThreeStr+" = '"+paramentThreeValue+"'";
		}else if(((null == paramentOneStr) || (paramentOneStr.equals(""))) && ((null != paramentTwoStr) || (!paramentTwoStr.equals(""))) && (((null != paramentThreeStr) || (!paramentThreeStr.equals(""))))){
			hql = hqlBefore+"where "+paramentTwoStr+" = '"+paramentTwoValue+"' and "+paramentThreeStr+" = '"+paramentThreeValue+"'";
		}else if(((null != paramentOneStr) || (!paramentOneStr.equals(""))) && ((null != paramentTwoStr) || (!paramentTwoStr.equals(""))) && (((null != paramentThreeStr) || (!paramentThreeStr.equals(""))))){
			if((tbName.equals("status_record_tbl")) || (tbName.equals("position_record_tbl"))){
				hql = hqlBefore+"where "+paramentOneStr+" = '"+paramentOneValue+"' and time BETWEEN '"+paramentTwoValue+"' and '"+paramentThreeValue+"'";
			}else{
				hql = hqlBefore+"where "+paramentOneStr+" = '"+paramentOneValue+"' and "+paramentTwoStr+" = '"+paramentTwoValue+"' and "+paramentThreeStr+" = '"+paramentThreeValue+"'";
			}
		}
		return hql;
	}
}
