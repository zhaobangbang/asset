<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<form name="login" action="${pageContext.request.contextPath}/login.action">
	<table id="Table_01" width="99.99%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;	font-family:Verdana, Arial, Helvetica, sans-serif;">
  <tr>
    <td valign="top" background="image/flower/tl.jpg"><img src="../images/spacer.gif" width="138" height="1" /></td>
    <td valign="top" background="image/flower/bg.jpg">&nbsp;</td>
    <td valign="top" background="image/flower/m_bg.jpg"><img src="image/flower/tr.jpg" width="80" height="40" /></td>
  </tr>
  <tr> 
    <td valign="top" background="image/flower/m_tl.jpg">&nbsp; </td>
    <td width="100%" valign="top" background="image/flower/bg.jpg" style="height:200px;padding:0 0 70px 30px;">
		<TABLE width="100%" style="cellpadding: 0px; cellspacing: 0px; margin-top: 0px; margin-Left: 0px" style="table-layout: fixed;WORD-BREAK: break-all; WORD-WRAP: break-word">
		<TR>
						<TD style="color:#4c4743;line-height:160%;" valign="top"
							width="30%">
							用户名：
						</TD>
						<TD style="color:#4c4743;line-height:160%;" valign="top">
							<input type="text" name="name" />
						</TD>
					</TR>
					<TR>
						<TD style="color:#4c4743;line-height:160%;" valign="top"
							width="30%">
							密&nbsp;&nbsp;码
						</TD>
						<TD style="color:#4c4743;line-height:160%;" valign="top">
							<input type="text" name="password" />
						</TD>
					</TR>
					<TR>
						<TD style="color:#4c4743;line-height:160%;" valign="top"
							width="30%">
							<input type="submit" value="提交" />
						</TD>
						<TD style="color:#4c4743;line-height:160%;" valign="top">
							<input type="reset" value="重置" />
						</TD>
					</TR>
		</TABLE>	
	</td>
    <td width="47" valign="top" background="image/flower/m_bg.jpg">&nbsp;</td>
  </tr>
</table>
</form>
