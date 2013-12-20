function nofind(){ 
	var img=event.srcElement; 
	img.src='resources/images/default.png'; 
	img.onerror=null;
} 

/****************************
 *申请服务窗口js	xuwf 2013-9-2
 ****************************
 */
//邮箱正则表达式 
var emailReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
var telReg = /^[0-9]{3,4}-[0-9]{7,8}$/;
var mobileReg = /^[1][0-9]{10}$/;
var patt_name = /^\S{2,10}$/i;
 function serviceApply(){
	$('.apply-btn').click(function(){
		var $btn = $(this);
		if($('#yselogin').length>0){
			if($('#yselogin').length>0){
				$('.apply-form input[name=sid]').val($btn.data('uuid'));
		 		$('.apply-form .sname').html($btn.data('sname'));
		 		$('.apply-form .sprice').html($btn.data('sprice'));
		 		$('.apply-form .link-tel').val($btn.data('tel'));
		 		$('.apply-form .link-mail').val($btn.data('email'));

		 	if(isApply == 0){
				art.dialog({
				    title: '提示',
				    width:450,
				    lock:true,
				    content: '<span style="font-size:16px;">没有申请服务的权限！</span>',
				    button:[{
				    	value:'确定',
				    	callback:function(){
				    		return true;
				    	}
				    }]
				});
				return;	
			}
			if(userId == $('#serviceEnterId').val()){//登录企业
				art.dialog({
				    title: '提示',
				    width:450,
				    lock:true,
				    content: '<span style="font-size:16px;">不能购买自己的服务！</span>',
				    button:[{
				    	value:'确定',
				    	callback:function(){
				    		return true;
				    	}
				    }]
				});
				return;
			}
			if(enterType == 1){//实名认证才能申请服务
				art.dialog({
				    title: '提示',
				    width:450,
				    lock:true,
				    content: '<span style="font-size:16px;">实名企业才能申请服务,请先认证！</span>',
				    button:[{
				    	value:'立即认证',
				    	callback:function(){
				    		window.location.href = "/ucenter/auth?op=2";
				    	}
				    }]
				});
				return;
			}
				var applyDialog = art.dialog.get('apply-dialog');//服务申请窗口
				if(applyDialog == null){
					applyDialog = art.dialog({
						id:'apply-dialog',
						width:450,
						lock:true,
						title:'【'+$btn.data('sname')+'】服务申请',
						content:$('.apply-form').html(),
					 	initialize: function () {
					 		$('.link-name,.link-tel,.link-mail').blur(function(e){
					 			if(!patt_name.test($('.link-name').val())){
					 				$('.apply-name-info').html('2-10个中文或者英文字符!');
					 			}else{
					 				$('.apply-name-info').html('');
					 			}
					 			
					 			if($('.link-tel').val()==''){
						 			$('.apply-tel-info').html('联系电话不能为空!');
						 			
					 			}else{
					 				if(!mobileReg.test($('.link-tel').val()) && !telReg.test($('.link-tel').val())){
						 				$('.apply-tel-info').html('联系电话格式不正确,格式为固话或手机号码!');
						 			}else{
						 				$('.apply-tel-info').html('');
						 			}
					 			}
					 			if($('.link-mail').val()==''){
									$('.apply-email-info').html('邮箱地址不能为空!');
					 			}else{
					 				if(!emailReg.test($('.link-mail').val())){
										$('.apply-email-info').html('请输入有效的电子邮箱地址!');
							 		}else{
							 			$('.apply-email-info').html('');
							 		}
					 			}
					 		});
					    },
						button:[
					        	{
									value :'确认提交',
									callback : function(){
										if(!patt_name.test($('.link-name').val())){
											$('.apply-name-info').html('2-10个中文或者英文字符!');
											return false;
										}else if(!mobileReg.test($('.link-tel').val()) && !telReg.test($('.link-tel').val())){
											$('.apply-tel-info').html('联系电话格式不正确,格式为固话或手机号码!');
											return false;
										}else if(!emailReg.test($('.link-mail').val())){
											$('.apply-email-info').html('请输入有效的电子邮箱地址!');
											return false;
										}else{
											var center_website = $('#center_website').text();
											//跨域请求枢纽的申请服务接口
											$.getJSON(center_website + "/order/applyServiceByJsonP?jsoncallback=?",
												{	//请求接口带的参数
													sid : $('.apply-sid').val(),
													linkMan : $('.link-name').val(),
													tel : $('.link-tel').val(),
													email : $('.link-mail').val(),
													remark : $('.buyer-remark').val(),
													sm_login : getCookie('SM_LOGIN')
												},
												function(data){
													if (data.success) {
														applyDialog.hidden();
														
														$('.apply-btn').addClass('hide');
														$('.enter-btn').removeClass('hide');
														showSubmitDia($btn);
													}else{
														art.alert(data.message);
													}
//											  		art.dialog({	//提示返回值
//													    title: '提示',
//													    width:450,
//													    lock:true,
//													    content: '<span style="font-size:16px;">' + data.message + '</span>',
//													    button:[{
//													    	value:'确定',
//													    	callback:function(){
//													    		return true;
//													    	}
//													    }]
//													});
												}
											);
										}
									},
									focus : true
								},
								{
									value :'取消',
									callback:function(){
										$('.apply-name-info').html('');
										$('.apply-tel-info').html('');
										$('.apply-email-info').html('');
										applyDialog.visible();
									}
								}
				        	]
					});
				}else{
					applyDialog.visible();
				}
			}else{
				art.dialog({
				    title: '提示',
				    width:450,
				    lock:true,
				    content: '<span style="font-size:16px;">申请服务需要登录,请先登录！</span>',
				    button:[{
				    	value:'立即登录',
				    	callback:function(){
				    		window.location.href = "/login";
				    	}
				    }]
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
			    	callback:function(){
			    		window.location.href = "/login";
			    	}
			    }]})
		}
	});
};

/**
 * 显示确认提交完毕窗口 
 * @param 
 */
function showSubmitDia(btn){
	var submitDialog = art.dialog.get('submit_dialog');
	if(null == submitDialog){
		submitDialog = art.dialog({
			id:'submit_dialog',
			title:'【'+btn.data('sname')+'】申请',
			content:$('.submit-form').html(),
			lock:true
			
		});
	}
	submitDialog.time(3000);	//3秒后自动关闭
}
/**
 * 联系电话可以修改
 */
function updateTel(){
	var $btn = $(this);
	$('.tel').remove();
	$('.link-tel').attr('type','text');
	$('.updateTel').html('');
};
/**
 * Email联系电话可以修改	
 */
function updateEmail(){
	var $btn = $(this);
	$('.email').remove();
	$('.link-mail').attr('type','text');
	$('.updateEmail').html('');
};
