<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ page session="false" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<title>新闻列表-深圳市中小企业公共服务平台</title>
<meta charset="UTF-8">
<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
<link type="text/css" rel="stylesheet" href="resources/css/main/news.css" />

<script type="text/javascript" src="jsLib/jquery/jquery-1.9.1.min.js"></script>
</head>

<body>
	<div class="header">
	  	<%@ include file="../../layout/top.jsp"%>
	  	<jsp:include page="/navigator" />
	</div>
	<div class="wrap">
  		<div class="crumb-nav">首页 ><span class="current">新闻</span></div>
  		<div class="news-container">
    		<div class="column">
    			<c:forEach var="newlists" items="${newsLists}" varStatus="status">
    				<div class="news-row">
        				<div class="news-row-h">
        					<h2><a href="news/getOneNewsDetails?id=${newlists.id}" target="_blank">${newlists.title}</a></h2>
        					<span class="date">${newlists.pubdate}</span>
        				</div>
        				<div class="news-row-c">
        					<c:choose>
        						<c:when test="${newlists.picture != ''}">
        							<div class="news-thumb">
		          						<a href="news/getOneNewsDetails?id=${newlists.id}" target="_blank">
		          							<c:if test="${newlists.picture.contains('http')}">
		          								<img width="160" height="120" src="${newlists.picture}" onerror="this.src='resources/images/service/default_service_pic.gif'"/>
		          							</c:if>
		          							<c:if test="${!newlists.picture.contains('http')}">
		          								<img width="160" height="120" src="upload/${newlists.picture}" onerror="this.src='resources/images/service/default_service_pic.gif'"/>
		          							</c:if>
		          						</a>
		          					</div>
        						</c:when>
        					</c:choose>
          					<div class="news-des">
          						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${fn:substring(newlists.content, 0, 300)}</p>
          						<p class="more"><a href="news/getOneNewsDetails?id=${newlists.id}" target="_blank">阅读全文</a></p>
          					</div>
          				</div>
          			</div>
    			</c:forEach>
	      		
	      		<div class="plist">
					<pg:pager scope="request" maxItems="9" maxIndexPages="10" index="center" maxPageItems="9"
						 url="news/getNewsList" items="${newsTotal}" export="currentPageNumber=pageNumber">
				    	<pg:first>
				    		<c:if test="${pageNumber != currentPageNumber }">
				    			<a href="${pageUrl}">&#8249;&#8249;</a>
				    		</c:if>	
				    	</pg:first>
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
				        <pg:last>
				        	<c:if test="${pageNumber != currentPageNumber }">
				        		<a href="${pageUrl}">&#8250;&#8250;</a>
				        	</c:if>	
				        </pg:last>
					</pg:pager> 
			    </div>
	      		
	    		</div>
			    <div class="aside">
			      <div class="column-box">
			        <h3 class="column-box-head">新闻热点</h3>
			             <c:forEach var="item" items="${hotNews }" begin="0" end="0">
				        	<div class="box-thumb">
				        	<a href="news/getOneNewsDetails?id=${item.id }" title="${item.title }">
				        	<c:if test="${item.picture.contains('http')}">
				        		<img src="${item.picture}" alt="" width="190" height="80" onerror="this.src='resources/images/service/default_service_pic.gif'"/>
				        	</c:if>
				        	<c:if test="${!item.picture.contains('http')}">
				        		<img src="upload/${item.picture}" alt="" width="190" height="80" onerror="this.src='resources/images/service/default_service_pic.gif'"/>
				        	</c:if>				        	
				        	<h4>${fn:substring(item.title, 0, 13) }</h4></a>
				        	</div>
				          </c:forEach>
			        <ul class="box-list">
			          <c:forEach var="item" items="${hotNews }" begin="1" end="9">
			           <li><a href="news/getOneNewsDetails?id=${item.id }" title="${item.title }">•${fn:substring(item.title, 0, 13) }</a></li>
			          </c:forEach>
			        </ul>
			      </div>
			      <div class="column-box mt16">
			        <h3 class="column-box-head">新闻推荐</h3>
			        <ul class="box-list">
			          <c:forEach var="item" items="${pushNews }">
			           <li><a href="news/getOneNewsDetails?id=${item.id }" title="${item.title }">•${fn:substring(item.title, 0, 13) }</a></li>
			          </c:forEach>
			        </ul>
			      </div>
			      <div class="mt16">
			        <a href=""><img src="resources/images/main/ad1.jpg" /></a>
			      </div>
			      <div class="mt16">
			        <a href=""><img src="resources/images/main/ad2.jpg" /></a>
			      </div>
			    </div>
	     	<div class="clearer"></div>
	  	</div>
	</div>
	<jsp:include page="../../layout/bottom.jsp" flush="true"/>
</body>
</html>