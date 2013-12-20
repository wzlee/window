$(function(){
	/************************搜索服务或者机构切换样式*******************************/
	//$('.topNav li').each(function(){
	//	var $ul=$(this).find('.sub-nav');
	//	var lis=$ul.find('li');
	//	if(!lis.length == 0){
	//		$ul.before("<span></span>");
	//	}
	//});
	//选择当前窗口logo
	/* var $lis=$('.drop-window-name').find('li.current');
	var $liAttr=$lis.find('a').attr('data-display');
	$('.logo-in').attr("src","resources/images/logo/"+$liAttr+".png"); */
	//选择项目
	$('.choose-window .drop-arror').hover(function(){
		$(this).addClass('active');
		$('.drop-window-name').show();
	},function(){
		$(this).remove('active');
		$('.choose-window .drop-window-name').hide();
	})
	/*鼠标滑动显示下拉列表*/
	$('.selected').hover(function() {
		$(this).children('.select-list').show();
	}, function() {
		$(this).children('.select-list').hide();
	});
	/*点击效果*/
	$('.select-list li').click(function(event) {
		$('.selected-choose').children('.txt').text($(this).text());
		var display= $(this).data('display');
		var index = $(this).data('index');
		$('#search-input').attr('placeholder', '搜索'+display);
		if ($(this).text() == '机构') {
			$('.search-box > form').attr('action', 'enter/search?search_index=2');
		} else { 
			$('.search-box > form').attr('action', 'service/result');
		}
		$(this).parent().hide();
	});
	
	/* 跨域请求枢纽平台接口,获取子窗口平台信息,并初始化头部超链接*/
	var center_website = $("#center_website").text();    //枢纽平台网址
	$.getJSON(center_website + "/flat/getFlatsByJsonP?jsoncallback=?", function(data){
		var $drop = $(".drop-window-name");
		$.each(data, function(i,item){
			$($drop).append('<li><a href="http://' + item.ip + '" target="_blank">@' + item.flatName + '</a></li>');    
		});
	});
	
	/* 跨域请求枢纽平台接口,获取枢纽平台头部导航栏的数据*/
	$.getJSON(center_website + "/cms/getChannelsByJsonP?jsoncallback=?", function(data){
		var $top = $(".topNav");    //枢纽导航栏
		$.each(data, function(i,item){
			var html;
			if (item.cname == '首页') {
			    item.cname = 'SMEmall首页';
			    if (item.children.length == 0) {
					html = '<li><a href="' + item.chttp + '" target="'+ item.linktype +'" style="color:#660066;font-weight:bold;">' + item.cname + '</a></li>';
				} else {
					html = '<li><a href="' + item.chttp + '" target="'+ item.linktype +'" style="color:#660066;font-weight:bold;">' + item.cname + '</a>';
					var flag = false;
					$.each(item.children, function (j, child) {    //children中是否有频道
						if (child.isChannel) {
							flag = true;
							return false;
						}	
					});
					if (flag) {    //如果children中有频道
						html = html + '&nbsp;<span></span><ul class="sub-nav">';
						$.each(item.children, function (j, child) {
							html = html + '<li><a href="' + child.chttp + '" target="'+ item.linktype +'">' + child.cname + '</a></li>'
						});
						html = html + '</ul>';
					}
					html = html + '</li>';
				}
			} else {
				if (item.children.length == 0) {
					html = '<li><a href="' + item.chttp + '" target="'+ item.linktype +'">' + item.cname + '</a></li>';
				} else {
					html = '<li><a href="' + item.chttp + '" target="'+ item.linktype +'">' + item.cname + '</a>';
					var flag = false;
					$.each(item.children, function (j, child) {    //children中是否有频道
						if (child.isChannel) {
							flag = true;
							return false;
						}	
					});
					if (flag) {    //如果children中有频道
						html = html + '&nbsp;<span></span><ul class="sub-nav">';
						$.each(item.children, function (j, child) {
							html = html + '<li><a href="' + child.chttp + '" target="'+ item.linktype +'">' + child.cname + '</a></li>'
						});
						html = html + '</ul>';
					}
					html = html + '</li>';
				}
			} 
			
			
			$($top).append(html);    
		});
	});
});
	/**
	 * 没有图片及图片找不到时采用默认图片
	 * xuwf 2013-11-13
	 */
	function nofind(){ 
		var img=$('.detailimg');
		$('.detailimg').attr('src','resources/images/service/default_service_pic.gif');
		img.onerror = null;
	} 