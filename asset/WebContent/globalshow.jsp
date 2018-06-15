<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="SHORTCUT ICON" href="images/favicon.ico" />

<title></title>
<style type="text/css">
<!--
.STYLE2 {
	font-family: "微软雅黑";
	font-size: 12px;
	color: #333399;
}

.STYLE4 {
	font-family: "微软雅黑";
	font-size: 12px;
	color: #3366CC;
}
-->
</style>
</head>
<%
	String sName = (String)session.getAttribute("usrname");

	if(null == sName || sName.equals(""))
	{
		response.sendRedirect("index.jsp");
		return;
	}
	
	String prio = (String)session.getAttribute("prio");
	if(null == prio || prio.equals(""))
		prio = "普通用户";

	String pageSel = null;
	if(prio.equals("普通用户"))
		pageSel = "trackerDashboard.jsp";
	else if(prio.equals("一级区域管理员"))
		pageSel = "jsp/CityManagerDashboard.jsp";
	else if(prio.equals("二级区域管理员"))
		pageSel = "jsp/AreaManagerDashboard.jsp";
	else if(prio.equals("超级管理员"))
		pageSel = "UsersManagement.jsp";
	else
	{
		System.out.println("bad prio:"+prio + " Length: " + prio.length());
		return;
	}
	//request.getRequestDispatcher(pageSel).forward(request,response);
	response.sendRedirect(response.encodeURL(pageSel));
%>
<body>
	<div align="center">
		<table width="1181" border="0">
			<tr>
				<td width="95" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_yjfwpt.jpeg"
								alt="应急服务平台系统是基于先进信息技术、信息系统（GIS地理信息系统、GPS全球定位系统、物联网、无人机系统等）和应急信息资源的多网整合，软硬件结合的应急保障技术系统."
								width="193" height="128"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="49" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_znju.jpeg"
								alt="构建高效的住宅设施与家庭日程事务的管理系统，提升家居安全性、便利性、舒适性、艺术性，并实现环保节能的居住环境."
								width="193" height="138"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="49" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_zhnmy.jpeg"
								alt="将物联网技术运用到传统农业中去，使传统农业更具有“智慧”。除了精准感知、控制与决策管理外，智慧农业还包括农业电子商务、食品溯源防伪、农业休闲旅游、农业信息服务等方面的内容."
								width="193" height="138"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="49" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_zhly.jpeg"
								alt="利用云计算、物联网等新技术，通过互联网/移动互联网，借助便携的终端上网设备，主动感知旅游资源、旅游经济、旅游活动、旅游者等方面的信息，及时发布，让人们能够及时了解这些信息."
								width="193" height="138"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="129" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
			</tr>
			<tr>
				<td width="95" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td valign="top"><h1 align="center" class="STYLE4">
						应急服务平台</a>
					</h1>
					<p align="left" class="STYLE2">应急服务平台系统是基于先进信息技术、信息系统（GIS地理信息系统+</p></td>
				<td valign="top"><h1 align="center" class="STYLE4">&nbsp;</h1>
				</td>
				<td valign="top"><h1 align="center" class="STYLE4">智能家居系统</h1>
					<p align="left" class="STYLE2">
						构建高效的住宅设施与家庭日程事务的管理系统，提升家居安全性、便+</p></td>
				<td valign="top"><h1 align="center" class="STYLE4">&nbsp;</h1>
				</td>
				<td valign="top"><h1 align="center" class="STYLE4">智慧农牧业</h1>
					<p align="left" class="STYLE2">
						将物联网技术运用到传统农业中去，使传统农业更具有“智慧”。除了+</p></td>
				<td valign="top"><h1 align="center" class="STYLE4">&nbsp;</h1>
				</td>
				<td valign="top"><h1 align="center" class="STYLE4">智慧旅游</h1>
					<p align="left" class="STYLE2">
						利用云计算、物联网等新技术，通过互联网/移动互联网，借助便携的+</p></td>
				<td width="129" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="95" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<a href="<%=pageSel %>"><img src="images/w_znjt.png"
								alt="由交通智慧云平台支撑服务平台。实现、实时路况、停车诱导、综合交通查询、公交站点电子站牌等一系列便捷实用的出行信息服务."
								width="193" height="138" border="0"
								style="border-style: none; vertical-align: top; float: center;" /></a>
						</p>
					</div></td>
				<td width="49" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_znyl.png"
								alt="通过打造健康档案区域医疗信息平台，利用最先进的物联网技术，实现患者与医务人员、医疗机构、医疗设备之间的互动，逐步达到信息化."
								width="193" height="138"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="49" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_zhwl.png"
								alt="建立一个面向未来的具有先进、互联和智能三大特征的供应链，通过感应器、RFID标签、制动器、GPS和其它设备及系统生成实时信息的“智慧供应链”."
								width="193" height="138"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="49" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td width="193" align="center"><div align="center">
						<p align="center">
							<img src="images/w_zhgy.jpeg"
								alt="利用物联信息系统将生产中的供应，制造，销售信息数据化、智慧化，最后达到快速，有效，个人化的产品供应."
								width="193" height="138"
								style="border-style: none; vertical-align: top; float: center;" />
						</p>
					</div></td>
				<td width="129" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
			</tr>
			<tr>
				<td width="95" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
				<td valign="top"><h1 align="center" class="STYLE4">
						<a href="<%=pageSel %>" target="_self">智能交通</a>
					</h1>
					<p align="left" class="STYLE2">由交通智慧云平台支撑服务平台。实现、实时路况、停车诱导、综合交+</p></td>
				<td valign="top"><h1 align="center" class="STYLE4">&nbsp;</h1>
				</td>
				<td valign="top"><h1 align="center" class="STYLE4">智能医疗</h1>
					<p align="left" class="STYLE2">通过打造健康档案区域医疗信息平台，利用最先进的物联网技术，实+</p></td>
				<td valign="top"><h1 align="center" class="STYLE4">&nbsp;</h1>
				</td>
				<td valign="top"><h1 align="center" class="STYLE4">智慧物流</h1>
					<p align="left" class="STYLE2">
						建立一个面向未来的具有先进、互联和智能三大特征的供应链，通过感+</p></td>
				<td valign="top"><h1 align="center" class="STYLE4">&nbsp;</h1>
				</td>
				<td valign="top"><h1 align="center" class="STYLE4">智慧工业</h1>
					<p align="left" class="STYLE2">
						利用物联信息系统将生产中的供应，制造，销售信息数据化、智慧化，+</p></td>
				<td width="129" align="center"><div align="center">
						<p align="center">&nbsp;</p>
					</div></td>
			</tr>
		</table>
	</div>
</body>
</html>