<%@page import="com.eaglec.win.domain.base.Staff"%>
<%@page import="com.eaglec.win.domain.base.User"%>
<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	Object apply = 0;//是否申请服务权限
	Integer enterpriseId = null;	//全局函数定义登录用户的企业id(不管子账号还是主账号)
	Integer enterpriseType = null;	//登录企业的企业类型	
	if(request.getSession().getAttribute("user") != null){
		Object userType = request.getSession().getAttribute("usertype");
		if(userType.equals(1)){//user
			User user = (User)request.getSession().getAttribute("user");
			if(!user.getIsPersonal()){
				apply = 1;
				enterpriseId = user.getEnterprise().getId();
				enterpriseType = user.getEnterprise().getType();
			}
		}else if(userType.equals(2)){//staff
			Staff staff = (Staff)request.getSession().getAttribute("user");
			apply = staff.getStaffRole().isApply();
			enterpriseId = staff.getParent().getEnterprise().getId();
			enterpriseType = staff.getParent().getEnterprise().getType();
		}
	}
 %>
<c:set value='<%=request.getSession().getAttribute("user")%>' var="msg"/>
    <div class="top-toolbar">
    <div class="wraper clearfix">
    	<ul class="topNav">
    		<!-- <li><a href="http://www.smemall.net" target="_blank">SMEmall首页</a></li>
    		<li class="current"><a href="javascript:void(0);">服务商城</a>&nbsp;<span></span>
    			<ul class="sub-nav">
    				<li><a href="http://smemall.net/mall/index?mallId=1" target="_blank">工业设计商城</a></li>
    				<li><a href="http://smemall.net/mall/index?mallId=2" target="_blank">软件商城</a></li>
    				<li><a href="http://smemall.net/mall/index?mallId=3" target="_blank">物联网商城</a></li>
    				<li><a href="http://smemall.net/mall/index?mallId=4" target="_blank">物流商城</a></li>
    			</ul>
    		</li>
    		<li><a href="http://smemall.net/enter/index" target="_blank">服务机构</a><ul class="sub-nav"></ul></li>
    		<li><a href="http://smemall.net/policy/index" target="_blank">政策指引</a></li>
    		<li><a href="http://smemall.net/policy/reportingIndex" target="_blank">资助一键通 </a></li>
    		<li><a href="" target="_blank">融资服务</a></li>
    		<li><a href="" target="_blank">人力资源</a></li>
    		<li><a href="" target="_blank">法律服务</a></li>
    		<li><a href="" target="_blank">企业信息化</a></li>
    		<li><a href="http://weibo.smemall.net/api/oauth.php" target="_blank">企业社区</a></li>
    		<li><a href="http://baike.smemall.net/api/oauth.php" target="_blank">百科知识</a></li>
    		<li><a href="http://www.szmall.com" target="_blank">深商E天下</a></li> -->
    	</ul>
	<c:choose>
		<c:when test="${empty msg}">
			<div class="login-panel" id="nologin" >
				<a class="login" href="login">登录</a> | <a href="signup">免费注册</a> 
			</div>
		</c:when>
		<c:otherwise>
			<div class="login-panel" id="yselogin" >
				<a class="login" href="ucenter">${msg.userName }</a> | <a href="logout">退出</a>
			</div>
		</c:otherwise>
	</c:choose>    	
    </div>

</div>
<div class="top-container clearfix">
	<div class="choose-window clearfix">
		<div class="logod"><a href=""><img src="resources/images/logo/wlw.png" class="logo-in"/></a></div>
		<div class="chooses clearfix">			
			<div class="drop-arror">
				<ul class="drop-window-name">
					<!-- <li><a href="javascript:void(0)" data-display="fz">@服装</a></li>
					<li><a href="javascript:void(0)" data-display="gazy">@港澳资源</a></li>
					<li><a href="javascript:void(0)" data-display="wl">@物流</a></li>
					<li><a href="javascript:void(0)" data-display="jj">@家具</a></li>
					<li><a href="javascript:void(0)" data-display="jx">@机械</a></li>
					<li><a href="javascript:void(0)" data-display="gysj">@工业设计</a></li>
					<li><a href="javascript:void(0)" data-display="rj">@软件</a></li>
					<li><a href="javascript:void(0)" data-display="wlw">@物联网</a></li>
					<li><a href="javascript:void(0)" data-display="ylqx">@医疗器械</a></li>
					<li><a href="javascript:void(0)" data-display="xcl">@新材料</a></li>
					<li><a href="javascript:void(0)" data-display="led">@LED</a></li>
					<li><a href="javascript:void(0)" data-display="dzzb">@电子装备</a></li>
					<li><a href="javascript:void(0)" data-display="zb">@钟表</a></li>
					<li><a href="javascript:void(0)" data-display="zbao">@珠宝</a></li> -->
				</ul>
			</div>
		</div>	
	</div>
   	<div class="search-box">
        <form method="post" action="${param.search_index == 2 ? 'enter/search?search_index=2':'service/result' }">
	        <div class="selected">
	          	<div class="selected-choose">
	            	<span data-display="${param.search_index == 2 ? '机构':'服务' }" data-index="${param.search_index == 2 ? '2':'1' }" class="txt">${param.search_index == 2 ? '机构':'服务' }</span>
	            	<span class="arrow"></span>
	          	</div>
	          	<ul class="select-list">
		            <li data-display="服务" data-index="1"><a href="javascript:void(0)">服务</a></li>
		            <li data-display="机构" data-index="2"><a href="javascript:void(0)">机构</a></li>
	          	</ul>
	        </div>
	        <div class="controls">
	          	<input type="text" name="name" id="search-input" class="text" placeholder="搜索${param.search_index == 2 ? '机构':'服务' }" />
	          	<button type="submit" class="button"></button>
	        </div>
	         <!-- 搜索服务结果显示方式 -->
	         <input name="type" type="text" value="0" style="display: none;" />
	         <!-- 查询结果排序方式 -->
	         <input name="order" type="text" style="display: none;" />
   		</form>
	</div>
</div>
<!-- 枢纽平台网址 -->
<div id="center_website" style="display: none;">${center_website}</div>

<script type="text/javascript" src="jsLib/jquery/jquery-1.9.1.min.js"></script>
<script src="jsLib/jquery/jquery.form.js" type="text/javascript"></script>
<script type="text/javascript" src="jsLib/artDialog-5.0.3/artDialog.min.js"></script>
<script type="text/javascript" src="jsLib/artDialog-5.0.3/artDialog.plugins.min.js"></script>
<script type="text/javascript" src="resources/js/public/top.js"></script>
<script type="text/javascript">
	var isApply = <%=apply %>;
	var userId = <%=enterpriseId %>;
	var enterType = <%=enterpriseType %>;
	var isLogin = ${not empty user}; 
</script>
<script type="text/javascript">
	function getCookie(c_name){
		if (document.cookie.length>0)
		  {
		  c_start=document.cookie.indexOf(c_name + "=")
		  if (c_start!=-1)
		    { 
		    c_start=c_start + c_name.length+1 
		    c_end=document.cookie.indexOf(";",c_start)
		    if (c_end==-1) c_end=document.cookie.length
		    return unescape(document.cookie.substring(c_start,c_end))
		    } 
		  }
		return ""
	}
	
	function setCookie(c_name,value,expiredays){
		var exdate=new Date()
		exdate.setDate(exdate.getDate()+expiredays)
		document.cookie=c_name+ "=" +escape(value)+
		((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
	}
	
</script>
