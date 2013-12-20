function delHtmlTag(str){
  return str.replace(/<[^>]+>/g, "");//去掉所有的html标记
} 

$(function(){
	/* 全局ajax默认设置*/
	$.ajaxSetup({
		timeout : 15000
	});

	/* 行业机构TOP10 */
	$('.column-org .list li:even').addClass('on');
	
	//幻灯片
	$('.box_skitter').skitter({
		theme: 'default',
		numbers_align: 'right',
		progressbar: false, 
		dots: true, 
		preview: false,
		hideTools: true,
		label: false
	});
	//服务列表导航
	$('.service-nav .icons .list li').hover(function(){
		$(this).addClass('on');
	},function(){
		$(this).removeClass('on');
	});
	
	/* 服务列表初始加载*/
	var start = 0;
	var limit = 15;
	var start_list = 0;
	var limit_list = 6;
	var order = "id";    //排序
	loadService("'已上架'", "id", start, limit, 0);
	start = start + limit;
	/* 加载更多服务*/
	$(".load-more a").click( function () {
		var $ul = $(".thumb-style");	//次标签只有大图模式下才有,所以用来作为标识,区分服务大图模式与服务列表模式
		if ($ul.length) {
			loadService("'已上架'", "id", start, limit, 0); 
			start = start + limit;
		} else {
			loadService("'已上架'", order, start_list, limit_list, 1); 
			start_list = start_list + limit_list;
		}
	});
	//服务列表显示方式切换
	$('.show-type li').click(function() {
		$(this).addClass('active');
		$(this).siblings().removeClass('active');
	});
	/* 切换服务视图(列表)*/
	$(".show-type").find("a").bind("click", function () {
		var $ul = $(".thumb-style");	//次标签只有大图模式下才有,所以用来作为标识,区分服务大图模式与服务列表模式
		var clazz = $(this).attr("class");
		
		if (clazz == 's1') {	//点击的是大图模式
			if ($ul.length) {
				return;
			}
			start_list = 0;
			loadService("'已上架'", "id", start, limit, 0);
			start = start + limit;
		} else {	//点击的是列表模式
			if ($ul.length == 0) {
				return;
			}
			start = 0;
			order = "id";
			loadService("'已上架'", order, start_list, limit_list, 1);
			start_list = start_list + limit_list;
		}
	});
	
	/* 服务排序*/
	$(".list li a").bind("click", function () {
		var clazz = $(this).attr("class");
		/* 调整样式*/
		var $parent = $(this).parent();
		var $current = $($parent).siblings(".current");
		$($current).removeClass("current");
		$($parent).addClass("current");
		
		switch (clazz) {
			case "order-serviceNum" : 
				order = "serviceNum";
				break;
			case "order-registerTime" : 
				order = "registerTime";
				break;
//			case "order-mix" :
//				order = "id";
//				break;
			default :
				order = "id";
				break;
		}
		start = 0;
		start_list = 0;
		loadService("'已上架'", order, start_list, limit_list, 1);
		start_list = start_list + limit_list;
	});
	
	//当前已加载的img进行lazyload,不包含ajax请求后加载进来的img
	$("img[class=lazy]").lazyload({
		placeholder : "img/grey.gif",    
		effect : "fadeIn"  
	});
	
	
	
	//------------------------------------------------以下是方法------------------------------------------------------
	/**
	 * @description ajax加载服务数据
	 * @param queryStatus 服务状态,如:'已上架'
	 * @param column 按某一列降序,如:'id'
	 * @param start 起始数字,如:0
	 * @param limit 分页限制,如:15
	 * @param tyep 服务列表显示形式(0:大图型, 1:列表型)
	 * */
	function loadService (queryStatus, column, start, limit, type) {
		var $loading = $('.load-more a');
		$loading.empty();
		$loading.append('加载中...');
//		$loading.append('<img src="resources/images/service/default_service_pic.gif" style="position:relative;top:9px" />');
		$.post("service/getServiceData", {    //加载服务数据		
				"start" : start,
				"limit" : limit,
				"order" : column
			},
			function(data){
				var $loadMore = $(".load-more a");
				if (data.length == 0) {
					$loadMore.empty();
					$loadMore.append("已加载全部服务");
					return;
				}
				var html;
				if (type == 0) {
					html = '<ul class="thumb-style">';	
					$.each(topthree, function(i, n){
						html = html + '<li>' +
										'<a href="service/detail?id='+n.id+'" target="_blank">'+
										'<img alt="'+n.serviceName+'" title="'+n.serviceName+'" src="upload/' + n.picture +'" width="232" height="200" onerror="this.src=\'resources/images/service/default_service_pic.gif\'"/>' +
										'</a>'+
											'<h3 class="name">'+n.serviceName+
											'</h3>' +
											'<h3 class="apply">' +
												'<a class="apply-button" href="service/detail?id='+n.id+'" target="_blank">查看详情</a>' +
											'</h3>' +
										'</li>';
					});
					$.each(data, function(i, n){
						html = html + '<li>' +
										'<a href="service/detail?id='+n.id+'" target="_blank">'+
										'<img alt="'+n.serviceName+'" title="'+n.serviceName+'" src="upload/' + n.picture +'" width="232" height="200" onerror="this.src=\'resources/images/service/default_service_pic.gif\'"/>' +
										'</a>'+
											'<h3 class="name">'+n.serviceName+
											'</h3>' +
											'<h3 class="apply">' +
												'<a class="apply-button" href="service/detail?id='+n.id+'" target="_blank">查看详情</a>' +
											'</h3>' +
										'</li>';
					});
					html = html + '</ul>';
					$lastLine = $("")
				} else {
					html = '<div class="list-style">';
					$.each(data, function(i, n){
						n.serviceProcedure = delHtmlTag(n.serviceProcedure).replace("&nbsp;", "");    //去掉html标签和空格
						if (n.serviceProcedure.length > 25) {    //控制服务介绍显示长度为不大于25个字符
							n.serviceProcedure = n.serviceProcedure.substr(0, 25);
						} else if (n.serviceProcedure.length == 0){
							n.serviceProcedure = '暂无描述';
						} 
						n.registerTime = n.registerTime.substr(0, 10);   //时间字符串截取成 "yyyy-MM-dd"格式
						html = html + '<div class="list-item">' +
							            '<a class="apply-service" href="service/detail?id='+n.id+'" target="_blank">查看详情</a>' +
							            '<div class="pic"><a href="service/detail?id='+n.id+'" target="_blank">' +
							            	'<img alt="'+n.serviceName+'" title="'+n.serviceName+'" src="upload/' + n.picture +'" width="174" height="150" onerror="this.src=\'resources/images/service/default_service_pic.gif\'"/>' +
							            '</a></div>' +
							            '<div class="info">' + 
							              '<h3 class="service-name"><a href="service/detail?id='+n.id+'" target="_blank">' + n.serviceName + '</a></h3>' +
							              '<div class="meta-wrap">' +
							                '<ul class="meta-list w1">' +
							                  '<li><strong class="s1">服务介绍：</strong><div class="meta-value">' + n.serviceProcedure + '</div></li>' + 
							                  '<li><strong class="s1">提供机构：</strong><div class="meta-value">' + n.enterpriseName +'</div></li>' +
							                  '<li><strong class="s2">评级：</strong><div class="meta-star"><img src="resources/images/node/sprites_star.png" /></div></li>' +
							                '</ul>' +
							                '<ul class="meta-list w2">' +
							                  '<li><strong class="s1">推出时间：</strong><div class="meta-value">' + n.registerTime + '</div></li>' +
							                 /* '<li><strong class="s1">更新时间：</strong><div class="meta-value">2013-06-20</div></li>' +*/
							                  '<li><strong class="s1">申请数量：</strong><div class="meta-value">' + n.serviceNum + '次</div></li>' +
							                '</ul>' +
							              '</div>' +
							            '</div>' +
							          '</div>';
					});
					html = html + '</div>';
				}
				if (start == 0) {
					$(".service-list").empty();
				}
				$(".service-list").append(html);
//				serviceApply();
				
				//服务列表申请
				if (type == 0) {
					$('.service-list ul li').hover(function(){
						$(this).children('h3.name').hide();
						$(this).children('h3.apply').show();
					}, function(){
						$(this).children('h3.name').show();
						$(this).children('h3.apply').hide();
					});
				} else {
					$('.list-item').hover(function() {
						$(this).addClass('on');
						$(this).children('.apply-service').show();
						$(this).children('.service-name').addClass('on');
					}, function() {
						$(this).removeClass('on');
						$(this).children('.apply-service').hide();
						$(this).children('.service-name').removeClass('on');
					});
				}
				
				if (data.length < limit) {
					$loadMore.empty();
					$loadMore.append("已加载全部服务");
				} else {
					$loading.empty();
					$loading.append('加载更多服务');
				}
				$(".service-list").children().last().find("img[class=lazy]").lazyload({
					placeholder : "img/grey.gif",    
					effect : "fadeIn"  
				});
		}, "json");
	}
});