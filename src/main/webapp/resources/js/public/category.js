$(function(){
	/*$.ajax({
		url: '/category/findAllCategoryAndService',
		type: 'post',
		dataType: 'json',
		async: false,
		success: function(data){
			$('.all-cate-column').html(data.message);
			 左侧菜单 
			$('.mod-cate li').hover(function(){
			var me = $(this);
			me.children('.name-wrap').children('h4').addClass('on');
					$(this).children('.mod-sub-cate').show();
				}, function(){
					$(this).children('.name-wrap').children('h4').removeClass('on');
					$(this).children('.mod-sub-cate').hide();
				}
　　　　　　);
		},
		failure: function(data){
			//console.log(data.message);
		}
	});*/
	
	$.ajax({
		url: 'category/findAllCategoryAndService',
		type: 'post',
		dataType: 'json',
//		async: true,
		success: function(data){
			$('.mod-cate').html(data.message);
			/* 左侧菜单 */
			$('.mod-cate li').hover(function(){
				$(this).children('.name-wrap').children('.name-column').addClass('on');
				$(this).children('.mod-sub-cate').show();
			},function(){
				$(this).children('.name-wrap').children('.name-column').removeClass('on');
				$(this).children('.mod-sub-cate').hide();
			});
		},
		failure: function(data){
			//console.log(data.message);
		}
	});
	
	
	
	/*var center_website = $("#center_website").text();    //枢纽平台网址
	$.getJSON(center_website + "/category/getAllCategoryByJsonP?jsoncallback=?", function(data){
		$('.mod-cate').html(data);
		 左侧菜单 
		$('.mod-cate li').hover(function(){
			$(this).children('.name-wrap').children('.name-column').addClass('on');
			$(this).children('.mod-sub-cate').show();
		},function(){
			$(this).children('.name-wrap').children('.name-column').removeClass('on');
			$(this).children('.mod-sub-cate').hide();
		});   
	});*/
}); 


