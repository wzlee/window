<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="slider">
	<div class="box_skitter">
  		<ul>
	  		<c:forEach var="banner" items="${bannerList}" varStatus="status">
	  			<li>
					<a href="${banner.mindex}" target="${banner.linktype}">
						<c:if test="${banner.micon.contains('http')}">
							<img src="${banner.micon}" class="${banner.mclass}" width="716" height="260" alt="${banner.mname}" title="${banner.mname}" />
						</c:if>
	  					<c:if test="${!banner.micon.contains('http')}">
	  						<img src="upload/${banner.micon}" class="${banner.mclass}" width="716" height="260" alt="${banner.mname}" title="${banner.mname}" />
	  					</c:if>
	  				</a>
	  			</li>
	  		</c:forEach>
      	</ul>
    </div>
</div>