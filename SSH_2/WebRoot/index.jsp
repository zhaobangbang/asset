<%@ page language="java" pageEncoding="utf-8"%>

<HTML>
<HEAD>
<TITLE>网络通讯录</TITLE>
<META http-equiv=Content-Language content=zh-cn>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<LINK href="images/enter.css" type=text/css rel=stylesheet>
</HEAD>
<BODY style="BACKGROUND-IMAGE: url(images/2e_bg.jpg)">
<DIV align=center>
<TABLE style="BORDER-COLLAPSE: collapse" height=576 cellPadding=0
	width=990 border=0>
	<TR>
		<TD width=230><!--   left.html --> <%@include file="left.jsp"%>
		<!-- end left.html --></TD>

		<TD width="677" valign="top">

		<table width="656" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="10" colspan="3"></td>
			</tr>
			<tr>
				<td colspan="3"><img src="images/33e_top1.jpg" width="656"
					height="17"></td>
			</tr>
			<tr>
				<td colspan="3"><img src="images/33e_top11.jpg" width="656"
					height="34"></td>
			</tr>
			<tr>
				<td width="2%"><IMG height=519 src="images/33e_left.jpg"
					width=13 border=0></td>
				<td width="95%" align="center" valign="top" bgcolor="#FFFFFF">
				<!-- banner.html  --> <%@include file="banner.html"%>
				<!-- end  banner.html  --> <!-- main.jsp  -->


				<form name="login" action="${pageContext.request.contextPath}/login.action" method="post">
				<table id="Table_01" width="99.99%" border="0" cellpadding="0"
					cellspacing="0"
					style="font-size:12px;	font-family:Verdana, Arial, Helvetica, sans-serif;">
					<tr>
						<td valign="top" background="image/flower/tl.jpg"><img
							src="../images/spacer.gif" width="138" height="1" /></td>
						<td valign="top" background="image/flower/bg.jpg">&nbsp;</td>
						<td valign="top" background="image/flower/m_bg.jpg"><img
							src="image/flower/tr.jpg" width="80" height="40" /></td>
					</tr>
					<tr>
						<td valign="top" background="image/flower/m_tl.jpg">&nbsp;</td>
						<td width="100%" valign="top" background="image/flower/bg.jpg"
							style="height:200px;padding:0 0 70px 30px;">
						<TABLE width="100%"
							style="cellpadding: 0px; cellspacing: 0px; margin-top: 0px; margin-Left: 0px"
							style="table-layout: fixed;WORD-BREAK: break-all; WORD-WRAP: break-word">
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">用户名：</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="name" /></TD>
							</TR>
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">密&nbsp;&nbsp;码</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="password" name="password" /></TD>
							</TR>
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%"><input type="submit" value="提交" /></TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="reset" value="重置" /></TD>
							</TR>
						</TABLE>
						</td>
						<td width="47" valign="top" background="image/flower/m_bg.jpg">&nbsp;</td>
					</tr>
				</table>
				</form>




				<!-- end main.jsp --></td>
				<td width="3%"><img src="images/33e_right.jpg" width="21"
					height="519"></td>
			</tr>
			<tr>
				<td colspan="3"><img src="images/33e_down.jpg" width="656"
					height="15"></td>
			</tr>

		</table>
		</td>


		<TD width=85><!--  tag.jsp   --> <%@include file="tag.jsp"%>
		<!--   end tag.jsp  --></TD>
	</TR>
</TABLE>
</DIV>

<!--  foot.jsp        -->
<%@include file="foot.jsp"%>
<!--  end foot.jsp  -->
</body>
</html>

