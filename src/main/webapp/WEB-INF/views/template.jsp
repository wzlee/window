<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>深圳市中小企业公共服务窗口平台</title>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<link href="resources/css/style.css" rel="stylesheet" type="text/css" />
	<link href="resources/css/common.css" rel="stylesheet" type="text/css" />
	<link href="jsLib/jquery.slider/skitter.styles.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="header">
	  	<%@ include file="layout/top.jsp"%>
	  	<!-- 导航栏 -->
	  	<jsp:include page="/navigator" />
	</div>
	<!-- /header -->
	<div class="wrap">
	  	<div class="column-l">
		  	<!-- 服务分类组件 -->
		    <%@ include file="layout/category.jsp"%>
		    <!-- 新闻组件 -->
		    <jsp:include page="/newsComponent" />
		    <!-- 服务机构排行组件 -->
		    <jsp:include page="/topTen" />
	  	</div>
	  	<div class="column-r">
	    	<jsp:include page="/banner" />
	    	
	    	<!-- 此处插入自定义代码 -->
	    	
	    	
	  	</div>
	  	<div class="clearer"></div>
  	</div>
	<!-- 底部 -->
	<%@ include file="layout/bottom.jsp"%>
	
	<script src="jsLib/jquery/jquery-1.8.2.js" type="text/javascript"></script>
	<script src="jsLib/jquery/jquery.lazyload.min.js" type="text/javascript"></script>
	<script src="jsLib/jquery.slider/jquery.easing.1.3.js" type="text/javascript"></script>
	<script src="jsLib/jquery.slider/jquery.skitter.js" type="text/javascript"></script>
	<script src="resources/js/main.js" type="text/javascript"></script>
	<script src="resources/js/public/category.js" type="text/javascript"></script>
</body>
</html>