<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="column-org">
     <h3>行业机构TOP10</h3>
      <ul class="list">
      	<c:forEach var="enterprise" items="${enterpriseList }">	
	        <li>
	          <div class="pic">
	            <a href="javascript:void(0);">
	            	<c:if test="${enterprise.photo.contains('http')}">
	            		<img class="lazy" src="resources/images/node/20130913160622.gif" 
	            	data-original="${enterprise.photo}"
	            	width="36" height="36" />
	            	</c:if>
	            	<c:if test="${!enterprise.photo.contains('http')}">
	            		<img class="lazy" src="resources/images/node/20130913160622.gif" 
	            	data-original="upload/${enterprise.photo}"
	            	width="36" height="36" />
	            	</c:if>
	            	
	            </a>
	          </div>
	          <div class="intro">
	            <h5><a href="">${enterprise.name }</a></h5>
	            <p>
	            	<%-- <c:choose>
     				<c:when test="${fn:length(fn:trim(enterprise.profile)) > 16}">
     					${fn:substring(enterprise.profile, 0, 16)}
     				</c:when>
     				<c:otherwise>
     					${fn:trim(enterprise.profile)}
     				</c:otherwise>
     			</c:choose> --%>
	            </p>
	          </div>
	        </li>
      	</c:forEach>
      </ul>
   </div>