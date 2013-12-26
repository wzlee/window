<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.eaglec.win.utils.StrUtils"%>
<%@ page import="com.eaglec.win.view.ServiceView" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<title>深圳市中小企业公共服务平台</title>
<meta charset="UTF-8">
<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
<link type="text/css" rel="stylesheet" href="resources/css/common.css" />
<link type="text/css" rel="stylesheet" href="resources/css/list.css" />
<style type="text/css">
.footer {
	background: none repeat scroll 0 0 #FFFFFF;
    border-top: 1px solid #EAEAEA;
    clear: both;
    height: 100px;
    padding-top: 34px;
    font: 12px/1 Arial,"宋体",sans-serif;
}
</style>

</head>

<body>
<div class="header">
<!-- header -->
<jsp:include page="../../layout/top.jsp" flush="true"/>
<!-- /header -->
<!-- 导航栏 -->
<jsp:include page="/navigator" />
</div>
<div class="list-wrap">
  <div class="column-l">
  	<!-- <div class="all-cate-column">
    	<h3>全部服务分类</h3>
    	<ul class="mod-cate" id="frist_list">
    		<span class="cate-loading"></span>
    	</ul>
    </div> -->
    <%@ include file="../../layout/category.jsp"%>
  </div>
  <div class="column-r">
    <div class="crumb-nav"><strong>${pcategoryName}</strong> > ${categoryName}</div>
    <!-- <div class="pager">
      <p class="amount">显示<strong><c:choose><c:when test="${total == 0}">0</c:when><c:when test="${(fn:length(serviceViews)) == 1}">${start + 1}</c:when><c:otherwise>${start + 1}-${start + (fn:length(serviceViews))}</c:otherwise></c:choose></strong>条 共<strong>${total}</strong>条</p>
      <div class="limiter">
        <label>排列</label>
        <select onchange="setLocation(this.value)" class="speedC">
          <option selected="selected" value="">评分</option>
          <option value="">XX</option>
        </select>
      </div>
    </div> -->
    <div class="service-list">
      <ul>
      	<c:forEach items="${serviceViews}" var="serviceView">
      		<li><a href="service/detail?id=${serviceView.id}" target="blank">
      			<c:if test="${serviceView.picture.contains('http')}">
      				<img width="232" height="200" src="${serviceView.picture}" onerror="this.src='resources/images/service/default_service_pic.gif'"/> 
      			</c:if>
      			<c:if test="${!serviceView.picture.contains('http')}">
      				<img width="232" height="200" src="upload/${serviceView.picture}" onerror="this.src='resources/images/service/default_service_pic.gif'"/> 
      			</c:if>
      			
      			<%-- <img class="lazy" src="resources/images/service/default_service_pic.gif" 
									data-original="${serviceView.picture}" width="232" height="200" alt="${serviceView.serviceName }" 
									title="${serviceView.serviceName }" onerror="nofind();"/> --%>
									</a>
	          <div class="content">
	            <h3 class="name"><a href="service/detail?id=${serviceView.id}">${serviceView.serviceName}</a></h3>
	            <%
		        	/* 截取服务介绍字符串,并去掉html标签 */
					ServiceView serviceView = (ServiceView)pageContext.getAttribute("serviceView");
					String temp = StrUtils.replaceHTML(serviceView.getServiceProcedure(), 0, 70);
					serviceView.setServiceProcedure(temp);
				%>
	            <div class="intro">${serviceView.serviceProcedure}</div>
	          </div>
	          <h3 class="apply" style="display: none;"><a class="apply-btn" href="service/detail?id=${serviceView.id}" target="_blank">查看详情</a></h3>
	        </li>
      	</c:forEach>
        <!-- <li><img width="232" height="200" src="resources/images/main/block_1.gif">
          <div class="content">
            <h3 class="name"><a href="">产品服务名称产品服务名产品服务名</a></h3>
            <div class="intro">产品服务介绍产品服务介绍产品服务介绍产品服务介绍产品服务介绍产品服务介绍产品服务介产品服务介绍产品服务介绍</div>
          </div>
          <h3 class="apply" style="display: none;"><a href="">申请服务</a></h3>
        </li> -->
      </ul>
    </div>
    <!--/分页放下面-->
    <div class="plist">
    <c:if test="${total>0}">
		<pg:pager scope="request" maxItems="9" maxIndexPages="10" index="center" maxPageItems="9"
			 url="service/paging" items="${total}" export="currentPageNumber=pageNumber">
			<pg:param name="cid"  value="${cid}" />
	    	<pg:first><a href="${pageUrl}">&#8249;&#8249;</a></pg:first>
	    	<c:out value="${pageUrl}"></c:out>
	        <pg:prev><a href="${pageUrl}">&#8249;</a></pg:prev>
	  		<pg:page>
	  		</pg:page>
	        <pg:pages>
	            <c:choose>
	            <c:when test="${pageNumber eq currentPageNumber }">
	            <a class="on" href="javascript:;">${pageNumber }</a>
	            </c:when>
	            <c:otherwise>
	            <a href="${pageUrl }">${pageNumber}</a>
	            </c:otherwise>
	            </c:choose>
	   		</pg:pages>
	        <pg:next><a href="${pageUrl}">&#8250;</a></pg:next>
	        <pg:last><a href="${pageUrl}">&#8250;&#8250;</a></pg:last>
		</pg:pager> 
		</c:if>
		<c:if test="${total<1}">
			没有找到任何跟"关键词"相关的内容。您可以尝试使用其他关键词进行搜索，或返回 首页。
		</c:if>
    </div>
  </div>
  <div class="clearer"></div>
</div>
<!-- /list-wrap -->
<jsp:include page="../../layout/bottom.jsp" flush="true"/>

<script type="text/javascript" src="jsLib/jquery/jquery-1.9.1.min.js"></script>
<script src="jsLib/jquery/jquery.lazyload.min.js" type="text/javascript"></script>
<script src="resources/js/main.js" type="text/javascript"></script>
<script type="text/javascript" src="resources/js/service/list.js"></script>
<script type="text/javascript" src="resources/js/public/category.js"></script>
<script type="text/javascript">
	$("img.lazy").lazyload();
</script>
</body>
</html>