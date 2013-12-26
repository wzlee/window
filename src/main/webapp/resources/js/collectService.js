
/**
 * 收藏服务
 * @author liuliping
 * @param id 服务id
 * @param obj html对象
 * */
function collect(obj, id) {
	if ($(obj).text() == '已收藏') {
		art.dialog({
			id : "d_service_collect",
		    title: '提示',
		    ok : function () {},
		    okValue : '关闭',
		    content: "已经收藏过此服务,去看看<a href='service/result' style='color:#0000FF;' target='_blank'>其他服务</a>",
		    fixed : true
		});
		return;
	}
	
	art.dialog({
			id : "d_service_collect",
		    title: '提示',
		    ok : function () {
		    	$.ajax({
					type : 'POST',
					url : 'myFavorites/add',
					async : false,
					data: {
						serviceId : id
					},
					dataType : 'json',
					beforeSend:function(){					
					},
					success : function(data) {
						if (data.success) {
							art.dialog({
								id : "d_service_collect2",
							    title: '提示',
							    ok : function () {},
							    okValue : '关闭',
							    content: data.message,
							    fixed : true
							});
							$(obj).text('已收藏');
							$(obj).addClass("active");
						}
					},
					failure : function(response) {
						art.dialog({
							id : "d_service_collect2",
						    title: '提示',
						    ok : function () {},
						    okValue : '关闭',
						    content: '服务器正忙,请稍后再试',
						    fixed : true,
						    lock : true
						});
			//			$.messager.alert('提示','服务器正忙,请稍后再试');
					}
				});
		    },
		    cancel : function () {},
		    okValue : '确定',
		    cancelValue : '取消',
		    content: "确定收藏该服务吗",
		    fixed : true,
		    lock : true
		});
	

}

/**
 * 分享 
 */
function share() {
	art.dialog({
		id : "d_service_collect",
	    title: '提示',
	    ok : function () {},
	    okValue : '关闭',
	    content: "暂未开放",
	    fixed : true
	});
}

$(function(){
	var $fa = $('.follow a');
	var beattentionid = $('#beattentionid').val();
	var center_website = $('#center_website').text();
	
	if(isLogin){
		//做一个双方是已经关注的跨域验证
		$.getJSON(center_website + "/service/valdateAddattentionJsonP?jsoncallback=?",
			{	
				sm_login : getCookie('_SM_LOCAL_LOGIN'),
				'beattentionid':$('#beattentionid').val()
			},
			function(data){
				if(data.success){
					$fa.html('已关注');
					$fa.addClass("attentionservice");
					$('.attentionservice').first().hover(
						function(){
							$(this).html('取消关注');
				  		},
				  		function () {
							$(this).html('已关注');
				  		}
					);
				}else{
					$fa.html('关注该机构');
					$fa.removeClass("attentionservice");
				}
			}
		);
	}else{
		$fa.html('关注该机构');
		$fa.removeClass("attentionservice");
	}
	
	$fa.click(function(){
		if($('#yselogin').length>0){
			if ($fa.text() == '取消关注'){
				art.dialog({
					id : "attentionservice",
				    title: '提示',
				    ok : function () {
				    	//跨域请求枢纽的取消关注接口
						$.getJSON(center_website + "/service/cancelAddattentionJsonP?jsoncallback=?",
							{	
								sm_login : getCookie('_SM_LOCAL_LOGIN'),
								'beattentionid':$('#beattentionid').val()
							},
							function(data){
								art.dialog({
								    title: '提示',
								    beforeunload:function(){
								    	if(data.success)
								    		location.reload();
								    },
								    ok : function () {
								    },
								    okValue : '关闭',
								    content: data.message,
								    fixed : true
								});
							}
						);
				    	
				    	/*$.ajax({
							type : 'POST',
							url : 'service/cancelattention',
							async : false,
							data: {
								'attentionid':$('#attentionid').val(),
								'beattentionid':$('#beattentionid').val()
							},
							dataType : 'json',
							beforeSend:function(){					
							},
							success : function(data) {
								art.dialog({
								    title: '提示',
								    ok : function () {
								    	if(data.success)
								    		location.reload();
								    },
								    okValue : '关闭',
								    content: data.message,
								    fixed : true
								});
							},
							failure : function(response) {
								art.dialog({
								    title: '提示',
								    ok : function () {},
								    okValue : '关闭',
								    content: '服务器正忙,请稍后再试',
								    fixed : true,
								    lock : true
								});
							}
						});*/
				    },
				    cancel : function () {},
				    okValue : '确定',
				    cancelValue : '取消',
				    content: "确定取消关注该服务机构吗",
				    fixed : true,
				    lock : true
				});
				return;
			}else{
				art.dialog({
					id : "attentionservice",
				    title: '提示',
				    ok : function () {
						//跨域请求枢纽的添加关注接口
						$.getJSON(center_website + "/service/addattentionJsonP?jsoncallback=?",
							{	
								sm_login : getCookie('_SM_LOCAL_LOGIN'),
								'beattentionid':$('#beattentionid').val()
							},
							function(data){
								art.dialog({
								    title: '提示',
								    beforeunload:function(){
								    	if(data.success)
								    		location.reload();
								    },
								    ok : function () {},
								    okValue : '关闭',
								    content: data.message,
								    fixed : true
								});
							}
						);
										
				    	
				    	
				    	
				    	/*
				    	$.ajax({
							type : 'POST',
							url : 'service/addattention',
							async : false,
							data: {
								'attentionid':$('#attentionid').val(),
								'beattentionid':$('#beattentionid').val()
							},
							dataType : 'json',
							beforeSend:function(){					
							},
							success : function(data) {
								art.dialog({
								    title: '提示',
								    ok : function () {
								    	if(data.success)
								    		location.reload();
								    },
								    okValue : '关闭',
								    content: data.message,
								    fixed : true
								});
							},
							failure : function(response) {
								art.dialog({
								    title: '提示',
								    ok : function () {},
								    okValue : '关闭',
								    content: '服务器正忙,请稍后再试',
								    fixed : true,
								    lock : true
								});
							}
						});
				    */
				    },
				    cancel : function () {},
				    okValue : '确定',
				    cancelValue : '取消',
				    content: "确定关注该服务机构吗",
				    fixed : true,
				    lock : true
				});
			}
		}else{
			art.dialog({
			    title: '提示',
			    width:450,
			    lock:true,
			    content: '<span style="font-size:16px;">申请服务需要登录,请先登录！</span>',
			    button:[{
			    	value:'立即登录',
			    	callback:function(){window.location.href = "/login";}
			    }]
		    })
		}
	});
})

/**
 * 微信端收藏服务
 * @author liuliping
 * @param id 服务id
 * @param obj html对象
 * */
function collect_wx(obj, id) {
	$.ajax({
		type : 'POST',
		url : 'myFavorites/addFromWX',
		async : false,
		data: {
			serviceId : id
		},
		dataType : 'json',
		beforeSend:function(){					
		},
		success : function(data) {
			if (data.success) {
				window.location.href = data.message;
			}
		},
		failure : function(response) {
//			art.dialog({
//				id : "d_service_collect2",
//			    title: '提示',
//			    ok : function () {},
//			    okValue : '关闭',
//			    content: '服务器正忙,请稍后再试',
//			    fixed : true,
//			    lock : true
//			});
//			$.messager.alert('提示','服务器正忙,请稍后再试');
			window.location.href = '500';
		}
	});
}



