$(function(){
	var $swtich = $(".switch-control").find("a");
	
	/* 点击大图显示形式*/
	$($swtich[0]).on('click', function () { 
		var name = $(".current").text();
		$("input[name='name']").attr("value", name);
		$("input[name='type']").attr("value", 0);
		$(".search-box").find("form").submit();
	});
	
	/* 点击列表显示形式*/
	$($swtich[1]).on('click', function () { 
		var name = $(".current").text();
		$("input[name='name']").attr("value", name);
		$("input[name='type']").attr("value", 1);
		$(".search-box").find("form").submit();
	});
	
	/*服务结果按时间,申请数量,排序*/
	$(".nav-pills li a").bind("click", function () {
		var order = null;
		var clazz = $(this).attr("class");
		switch (clazz) {
			case "serviceNum-order" : 
				order = "serviceNum";
				break;
			case "time-order" : 
				order = "registerTime";
				break;
			case "price-order" :
				order = "costPrice";
				break;
			default :
				order = null;
		}
		var name = $(".current").text();
		$("input[name='name']").attr("value", name);
		$("input[name='order']").attr("value", order);
		$(".search-box").find("form").submit();
	});
})