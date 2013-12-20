<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="ccode" value="${page }"></c:set>
<div class="menu-panel">
	<div class="wraper">
		<ul class="menuNav clearfix">
			<c:forEach var="channel" items="${channelList}" varStatus="status">
				<c:choose>
					<c:when test="${channel.ccode == ccode}">
						<li class="current">
							<a href="${channel.chttp}" target="${channel.linktype}">${channel.cname}</a>
							<c:if test="${channel.children != null}">
								<ul class="sub-nav">
									<c:forEach var="chachild" items="${channel.children }" varStatus="status">
										<c:if test="${chachild.isChannel}">
											<li><a href="${chachild.chttp}${fn:indexOf(chachild.chttp, '?') > 0 ? '&' : '?'}iop=${channel.id}" target="${chachild.linktype}">${chachild.cname}</a></li>
										</c:if>
									</c:forEach>
								</ul>
							</c:if>
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<a href="${channel.chttp}" target="${channel.linktype}">${channel.cname}</a>
							<c:if test="${channel.children != null}">
								<ul class="sub-nav">
									<c:forEach var="chachild" items="${channel.children }" varStatus="status">
										<c:if test="${chachild.isChannel}">
											<li><a href="${chachild.chttp}${fn:indexOf(chachild.chttp, '?') > 0 ? '&' : '?'}iop=${channel.id}" target="${chachild.linktype}">${chachild.cname}</a></li>
										</c:if>
									</c:forEach>
								</ul>
							</c:if>
						</li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</ul>
	</div>
</div>