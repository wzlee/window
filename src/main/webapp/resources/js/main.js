$(function(){
	/*鼠标滑动显示下拉列表*/
	$('.selected').hover(function() {
		$(this).children('.select-list').show();
	}, function() {
		$(this).children('.select-list').hide();
	});
	/*导航滑动*/
	$('.top-nav > ul > li').hover(function() {
		$(this).addClass('active');
		$(this).children('.sub-nav').show();
	}, function() {
		$(this).removeClass('active');
		$(this).children('.sub-nav').hide();
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
	
	/******************子窗口详情js**********************/
	$("img.lazy").lazyload({
		placeholder : "img/grey.gif",    
		effect : "fadeIn"  
	});
	
	/**********************************************/
})