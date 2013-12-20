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
	<link href="resources/css/index.css" rel="stylesheet" type="text/css" />
	<link href="jsLib/jquery.slider/skitter.styles.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="header">
	  	<%@ include file="../../layout/top.jsp"%>
	  	<!-- 导航栏 -->
	  	<jsp:include page="/navigator" />
	</div>
	<!-- /header -->
	<div class="wrap">
	  <div class="column-l">
	  	<!-- 服务分类组件 -->
	    <div class="all-cate-column">
	    	<h3>全部服务分类</h3>
	    	<ul class="mod-cate" id="frist_list">
	    		<span class="cate-loading"></span>
	    	</ul>
	    </div>
	    <!-- 新闻组件 -->
	    <jsp:include page="/newsComponent" />
	    <!-- 服务排行组件 -->
	    <%-- <jsp:include page="/serviceRunklist" /> --%>
	    <!-- 服务机构排行组件 -->
	    <jsp:include page="/topTen" />
	  </div>
	  <div class="column-r">
	  	
	    <jsp:include page="/banner" />
	    
	    <div class="service-panel">
	      <div class="service-nav">
	          <div class="title">服务列表</div>
	          <div class="icons">
	          	<ul class="list">
	            	<li class="current"><a href="#">综合</a></li>
	                <li><a href="#">受申数量</a></li>
	                <li><a href="#">好评度</a></li>
	                <li><a href="#">更新时间</a></li>
	            </ul>
	            <ul class="show-type">
	            	<li class="s1"><a href=""><span>大图</span></a></li>
	                <li class="s2"><a href=""><span>列表</span></a></li>
	            </ul>
	          </div>
	      </div>
	      <div class="service-list">
        <div class="list-style">
          <div class="list-item">
            <a class="apply-service" href="">申请服务</a>
            <div class="pic"><a href=""><img src="resources/images/node/s_list.jpg" width="174" height="150" /></a></div>
            <div class="info">
              <h3 class="service-name"><a href="">宝石鉴定</a></h3>
              <div class="meta-wrap">
                <ul class="meta-list w1">
                  <li><strong class="s1">服务介绍：</strong><div class="meta-value">一句话介绍。最多不超过25个字的简短介绍。</div></li>
                  <li><strong class="s1">提供机构：</strong><div class="meta-value">深圳腾X商务有限公司</div></li>
                  <li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>
                </ul>
                <ul class="meta-list w2">
                  <li><strong class="s1">推出时间：</strong><div class="meta-value">1998-07-01</div></li>
                  <li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>
                  <li><strong class="s1">申请数量：</strong><div class="meta-value">1939次</div></li>
                </ul>
              </div>
            </div>
          </div>
          <!-- /list-item -->
          <div class="list-item">
            <a class="apply-service" href="">申请服务</a>
            <div class="pic"><a href=""><img src="resources/images/node/s_list.jpg" width="174" height="150" /></a></div>
            <div class="info">
              <h3 class="service-name"><a href="">其它服务</a></h3>
              <div class="meta-wrap">
                <ul class="meta-list w1">
                  <li><strong class="s1">服务介绍：</strong><div class="meta-value">一句话介绍。最多不超过25个字的简短介绍。</div></li>
                  <li><strong class="s1">提供机构：</strong><div class="meta-value">深圳腾X商务有限公司</div></li>
                  <li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>
                </ul>
                <ul class="meta-list w2">
                  <li><strong class="s1">推出时间：</strong><div class="meta-value">1998-07-01</div></li>
                  <li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>
                  <li><strong class="s1">申请数量：</strong><div class="meta-value">1939次</div></li>
                </ul>
              </div>
            </div>
          </div>
          <!-- /list-item -->
          <div class="list-item">
            <a class="apply-service" href="">申请服务</a>
            <div class="pic"><a href=""><img src="resources/images/node/s_list.jpg" width="174" height="150" /></a></div>
            <div class="info">
              <h3 class="service-name"><a href="">其它服务</a></h3>
              <div class="meta-wrap">
                <ul class="meta-list w1">
                  <li><strong class="s1">服务介绍：</strong><div class="meta-value">一句话介绍。最多不超过25个字的简短介绍。</div></li>
                  <li><strong class="s1">提供机构：</strong><div class="meta-value">深圳腾X商务有限公司</div></li>
                  <li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>
                </ul>
                <ul class="meta-list w2">
                  <li><strong class="s1">推出时间：</strong><div class="meta-value">1998-07-01</div></li>
                  <li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>
                  <li><strong class="s1">申请数量：</strong><div class="meta-value">1939次</div></li>
                </ul>
              </div>
            </div>
          </div>
          <!-- /list-item -->
          <div class="list-item">
            <a class="apply-service" href="">申请服务</a>
            <div class="pic"><a href=""><img src="resources/images/node/s_list.jpg" width="174" height="150" /></a></div>
            <div class="info">
              <h3 class="service-name"><a href="">其它服务</a></h3>
              <div class="meta-wrap">
                <ul class="meta-list w1">
                  <li><strong class="s1">服务介绍：</strong><div class="meta-value">一句话介绍。最多不超过25个字的简短介绍。</div></li>
                  <li><strong class="s1">提供机构：</strong><div class="meta-value">深圳腾X商务有限公司</div></li>
                  <li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>
                </ul>
                <ul class="meta-list w2">
                  <li><strong class="s1">推出时间：</strong><div class="meta-value">1998-07-01</div></li>
                  <li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>
                  <li><strong class="s1">申请数量：</strong><div class="meta-value">1939次</div></li>
                </ul>
              </div>
            </div>
          </div>
          <!-- /list-item -->
          <div class="list-item">
            <a class="apply-service" href="">申请服务</a>
            <div class="pic"><a href=""><img src="resources/images/node/s_list.jpg" width="174" height="150" /></a></div>
            <div class="info">
              <h3 class="service-name"><a href="">其它服务</a></h3>
              <div class="meta-wrap">
                <ul class="meta-list w1">
                  <li><strong class="s1">服务介绍：</strong><div class="meta-value">一句话介绍。最多不超过25个字的简短介绍。</div></li>
                  <li><strong class="s1">提供机构：</strong><div class="meta-value">深圳腾X商务有限公司</div></li>
                  <li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>
                </ul>
                <ul class="meta-list w2">
                  <li><strong class="s1">推出时间：</strong><div class="meta-value">1998-07-01</div></li>
                  <li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>
                  <li><strong class="s1">申请数量：</strong><div class="meta-value">1939次</div></li>
                </ul>
              </div>
            </div>
          </div>
          <!-- /list-item -->
          <div class="list-item">
            <a class="apply-service" href="">申请服务</a>
            <div class="pic"><a href=""><img src="resources/images/node/s_list.jpg" width="174" height="150" /></a></div>
            <div class="info">
              <h3 class="service-name"><a href="">其它服务</a></h3>
              <div class="meta-wrap">
                <ul class="meta-list w1">
                  <li><strong class="s1">服务介绍：</strong><div class="meta-value">一句话介绍。最多不超过25个字的简短介绍。</div></li>
                  <li><strong class="s1">提供机构：</strong><div class="meta-value">深圳腾X商务有限公司</div></li>
                  <li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>
                </ul>
                <ul class="meta-list w2">
                  <li><strong class="s1">推出时间：</strong><div class="meta-value">1998-07-01</div></li>
                  <li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>
                  <li><strong class="s1">申请数量：</strong><div class="meta-value">1939次</div></li>
                </ul>
              </div>
            </div>
          </div>
          <!-- /list-item -->
        </div>
	      </div>
	      <div class="load-more"><a href="javascript:void(0)">加载更多服务</a></div>
	    </div>
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
	<script src="resources/js/index/index2.js" type="text/javascript"></script>
</body>
</html>