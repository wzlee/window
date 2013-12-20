<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="column-news">
	<div class="hd">
    <h3>行业新闻</h3>
    <span class="more"><a href="news/getNewsList?start=0">更多>></a></span>
    </div>
    <ul class="list">
     	<c:forEach items="${newsList}" var="news" varStatus="status">
     		<c:choose>
     			<c:when test="${fn:length(fn:trim(news.title)) > 16}">
     				<li class="s${status.count}"><a href="news/getOneNewsDetails?id=${news.id}">${fn:substring(news.title, 0, 16)}</a></li>	
     			</c:when>
     			<c:otherwise>
     				<li class="s${status.count}"><a href="news/getOneNewsDetails?id=${news.id}">${fn:trim(news.title)}</a></li>		
     			</c:otherwise>
     		</c:choose>
		</c:forEach> 
    </ul>
</div>