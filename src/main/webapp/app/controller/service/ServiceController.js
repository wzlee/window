Ext.define('plat.controller.service.ServiceController', {
    extend: 'Ext.app.Controller',
    
    alias:'widget.servicecontroller',
    
    stores: [
				'service.ServiceStore',
				'service.UServiceStore',
				'service.DServiceStore',
				'service.RServiceStore',
				'service.AServiceStore',
				'service.NServiceStore',
				'service.QServiceStore',
				'service.TopThreeStore'
			],
    views: [
    			'service.ServicePanel',
    			'layout.combo.CategoryCombo',
    			'layout.combo.SMethodCombo',
    			'layout.combo.SObjectCombo',
    			'layout.combo.SOrgCombo',
    			'layout.combo.ServiceOrgCombo',
    			'layout.combo.EnterpriseCombo',
    			'layout.combo.UserCategoryCombo',
    			'layout.combo.EUserCategoryCombo',
    			'service.ServiceGrid',
    			'service.NServiceGrid',
    			'service.UServiceGrid',
    			'service.DServiceGrid',
    			'service.RServiceGrid',
    			'service.AServiceGrid',
    			'service.ServiceAddWindow',
    			'service.AddForUserWindow',
    			'service.ServiceEditWindow',
    			'service.ServiceDetailWindow',
    			'service.UServiceWindow',
    			'service.QServiceGrid',
    			'service.QServicePanel',
    			'layout.combo.ServiceStatus',
    			'public.UploadWin',
    			'public.UploadForm',
    			'service.ApprServiceWindow',
    			'service.TopThreeGrid',
    			'service.TopThreeWindow'
//    			'layout.combo.CategoryTreePicker'
    		],
    refs: [
    		{
    			ref:'servicepanel',
    			selector:'servicepanel'
    		},
    		{
    			ref:'categorygrid',
    			selector:'categorygrid'
    		},
    		{
    			ref:'servicegrid',
    			selector:'servicegrid'
    		},
    		{
    			ref:'uservicestore',
    			selector:'uservicestore'
    		},
    		{
    			ref:'dservicestore',
    			selector:'dservicestore'
    		},
    		{
    			ref:'nservicestore',
    			selector:'nservicestore'
    		},
    		{
    			ref:'aservicestore',
    			selector:'aservicestore'
    		},
    		{
    			ref:'qservicestore',
    			selector:'qservicestore'
    		},
    		{
    			ref:'nservicegrid',
    			selector:'nservicegrid'
    		},
    		{
    			ref:'uservicegrid',
    			selector:'uservicegrid'
    		},
    		{
    			ref:'dservicegrid',
    			selector:'dservicegrid'
    		},
    		{
    			ref:'rservicegrid',
    			selector:'rservicegrid'
    		},
    		{
    			ref:'serviceaddwindow',
    			selector:'serviceaddwindow'
    		},
    		{
    			ref:'addforuserwindow',
    			selector:'addforuserwindow'
    		},
    		{
    			ref:'serviceeditwindow',
    			selector:'serviceeditwindow'
    		},
    		{
    			ref:'servicedetailwindow',
    			selector:'servicedetailwindow'
    		},
    		{
    			ref:'uservicewindow',
    			selector:'uservicewindow'
    		},
    		{
    			ref:'servicestatus',
    			selector:'servicestatus'
    		},
    		{
    			ref:'apprservicewindow',
    			selector:'apprservicewindow'
    		},
	     	{
	     		ref:'topthreegrid',
	     		selector:'topthreegrid'
	     	},
	     	{
	     		ref:'topthreewindow',
	     		selector:'topthreewindow'
	     	}
    ],
    
    init: function() {
        this.control({
        	'servicepanel':{
            	afterrender:function(panel){
            		////console.log(panel.title + '渲染完毕...');
            	}
            },
            'nservicegrid':{
            	afterrender:function(gridpanel){
            		////console.log(gridpanel.title + '渲染完毕...');
            		var serviceName = '';
            		var enterprisename = '';
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {serviceName:serviceName,enterprisename:enterprisename});
            		});
            		store.loadPage(1);            		
            		gridpanel.down('button[action=search]').on('click',function(){
            			serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
            			enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
            			store.loadPage(1,{params:{serviceName:serviceName,enterprisename:enterprisename}});            			
					},this);
					gridpanel.down('button[name=add]').on('click',function(){						
						this.addServiceForUser();
					},this);					
        			gridpanel.down('textfield[name=serviceName]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{serviceName:serviceName}});
        					}
                       		
	                    }
					},this);
					gridpanel.down('textfield[name=enterprisename]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{enterprisename:enterprisename}});
        					}
                       		
	                    }
					},this);
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		if(record.data.locked){
            			this.showServiceDetail(record);
            		}else{
            			this.editServiceData(record);
            		}
            	}
            },
            'nservicegrid triggerfield':{            	
            	afterrender:function(triggerfield){
            		////console.log(triggerfield.getName()+'渲染完毕...');
            		triggerfield.triggerCell.setDisplayed(false);
            	},
            	dirtychange:function(triggerfield){
            		
            		if(triggerfield.triggerCell!=null){
            			
            			if(triggerfield.getValue!=null&&!triggerfield.display){	//当值不为空，dispalyed为假时，显示清除按钮
            				triggerfield.display=true;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);            				
            			}else if((triggerfield.getValue!=null)&&(triggerfield.display)){	//当值不为空，displayed为真时，隐藏清除按钮
            				triggerfield.display=false;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);
            			}            			
            		}
            		
            	}        
            },
            'uservicegrid':{
            	afterrender:function(gridpanel){
            		////console.log(gridpanel.title + '渲染完毕...');
            		var serviceName = '';
            		var enterprisename = '';
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {serviceName:serviceName,enterprisename:enterprisename});
            		});
            		store.loadPage(1);            		
            		gridpanel.down('button[action=search]').on('click',function(){
            			serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
            			enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
            			store.loadPage(1,{params:{serviceName:serviceName,enterprisename:enterprisename}});            			
					},this);
        			gridpanel.down('textfield[name=serviceName]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{serviceName:serviceName}});
        					}
                       		
	                    }
					},this);
					gridpanel.down('textfield[name=enterprisename]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{enterprisename:enterprisename}});
        					}
                       		
	                    }
					},this);
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            			this.showServiceDetail(record);
            	}
            },
            'uservicegrid triggerfield':{            	
            	afterrender:function(triggerfield){
            		////console.log(triggerfield.getName()+'渲染完毕...');
            		triggerfield.triggerCell.setDisplayed(false);
            	},
            	dirtychange:function(triggerfield){
            		
            		if(triggerfield.triggerCell!=null){
            			
            			if(triggerfield.getValue!=null&&!triggerfield.display){	//当值不为空，dispalyed为假时，显示清除按钮
            				triggerfield.display=true;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);            				
            			}else if((triggerfield.getValue!=null)&&(triggerfield.display)){	//当值不为空，displayed为真时，隐藏清除按钮
            				triggerfield.display=false;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);
            			}            			
            		}
            		
            	}        
            },
             'dservicegrid':{
            	afterrender:function(gridpanel){
            		////console.log(gridpanel.title + '渲染完毕...');
            		var serviceName = '';
            		var enterprisename = '';
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {serviceName:serviceName,enterprisename:enterprisename});
            		});
            		store.loadPage(1);            		
            		gridpanel.down('button[action=search]').on('click',function(){
            			serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
            			enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
            			store.loadPage(1,{params:{serviceName:serviceName,enterprisename:enterprisename}});            			
					},this);
        			gridpanel.down('textfield[name=serviceName]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{serviceName:serviceName}});
        					}
                       		
	                    }
					},this);	
					gridpanel.down('textfield[name=enterprisename]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{enterprisename:enterprisename}});
        					}
                       		
	                    }
					},this);	
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		if(record.data.locked){
            			this.showServiceDetail(record);
            		}else{
            			this.editServiceData(record);
            		}
            	}
            },
            'dservicegrid triggerfield':{            	
            	afterrender:function(triggerfield){
            		////console.log(triggerfield.getName()+'渲染完毕...');
            		triggerfield.triggerCell.setDisplayed(false);
            	},
            	dirtychange:function(triggerfield){
            		
            		if(triggerfield.triggerCell!=null){
            			
            			if(triggerfield.getValue!=null&&!triggerfield.display){	//当值不为空，dispalyed为假时，显示清除按钮
            				triggerfield.display=true;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);            				
            			}else if((triggerfield.getValue!=null)&&(triggerfield.display)){	//当值不为空，displayed为真时，隐藏清除按钮
            				triggerfield.display=false;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);
            			}            			
            		}
            		
            	}        
            },
            'rservicegrid':{	//已删除服务管理表
            	afterrender:function(gridpanel){
            		////console.log(gridpanel.title + '渲染完毕...');
            		var serviceName = '';
            		var enterprisename = '';
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {serviceName:serviceName,enterprisename:enterprisename});
            		});
            		store.loadPage(1);            		
            		gridpanel.down('button[action=search]').on('click',function(){
            			serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
            			enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
            			store.loadPage(1,{params:{serviceName:serviceName}});            			
					},this);            		
        			gridpanel.down('textfield[name=serviceName]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					serviceName = gridpanel.down('textfield[name=serviceName]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{serviceName:serviceName}});
        					}
                       		
	                    }
					},this);
					gridpanel.down('textfield[name=enterprisename]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
        					if(!field.getValue()){
        						store.loadPage(1);
        					}else {
        						store.loadPage(1,{params:{enterprisename:enterprisename}});
        					}
                       		
	                    }
					},this);
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		this.showServiceDetail(record);
            	}
            },
            'rservicegrid triggerfield':{            	
            	afterrender:function(triggerfield){
            		////console.log(triggerfield.getName()+'渲染完毕...');
            		triggerfield.triggerCell.setDisplayed(false);
            	},
            	dirtychange:function(triggerfield){
            		
            		if(triggerfield.triggerCell!=null){
            			
            			if(triggerfield.getValue!=null&&!triggerfield.display){	//当值不为空，dispalyed为假时，显示清除按钮
            				triggerfield.display=true;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);            				
            			}else if((triggerfield.getValue!=null)&&(triggerfield.display)){	//当值不为空，displayed为真时，隐藏清除按钮
            				triggerfield.display=false;
            				triggerfield.triggerCell.setDisplayed(triggerfield.display);
            				triggerfield.setWidth(200);
            			}            			
            		}
            		
            	}        
            },
            'aservicegrid' : {	//审核服务表格
            	afterrender : function (gridpanel) {
            		////console.log(gridpanel.title + '渲染完毕...');
            		var serviceName = '',status = '2,4,7';
            		var enterprisename = '';
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {queryStatus : status,serviceName:serviceName,enterprisename:enterprisename});
            		});
            		store.loadPage(1);
            		
            		gridpanel.down('button[action=search]').on('click', function () {
                		value = gridpanel.down('combo').getValue();
            			serviceName = gridpanel.down('textfield[name=sname]').getValue();
            			enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
            			if (value) {
            				status = value;
            			}else {
            				status = '2,4,7';
            			};            			
                		store.loadPage(1);
					}, this);
					
        			gridpanel.down('textfield[name=sname]').on('specialkey', function(field,e){
        				if (e.getKey() == e.ENTER) {
                       		value = gridpanel.down('combo').getValue();
	            			serviceName = gridpanel.down('textfield[name=sname]').getValue();
	            			if (value) {
	            				status = value;
	            			}else {
	            				status = '2,4,7';
	            			};
	                		store.loadPage(1);
		                    }
					}, this);
					gridpanel.down('textfield[name=enterprisename]').on('specialkey', function(field,e){
        				if (e.getKey() == e.ENTER) {
                       		value = gridpanel.down('combo').getValue();
	            			enterprisename = gridpanel.down('textfield[name=enterprisename]').getValue();
	            			if (value) {
	            				status = value;
	            			}else {
	            				status = '2,4,7';
	            			};
	                		store.loadPage(1);
		                    }
					}, this);
            	}, 
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		this.showServiceDetail(record);
            	}
            },
            'qservicegrid':{   	//服务综合查询表格
            	afterrender:function(gridpanel){
            		////console.log(gridpanel.title + '渲染完毕...');	
            		var queryStatus;
            		var sname = '';
            		var enterprisename = '';
            		gridpanel.getStore().on('beforeload',function(store){        
            			Ext.apply(store.proxy.extraParams, {//设置全局参数
            				sname:sname,
            				status:queryStatus,
            				enterprisename:enterprisename
            			});
            		});
            		gridpanel.getStore().loadPage(1);	//首先加载全部数据
            		gridpanel.down('button[action=search]').on('click',function(){  
            			//多选下拉框默认传过来是数组
//            			queryStatus = gridpanel.down('combo[name=status]').getValue().join("','");
						queryStatus = gridpanel.down('combo[name=status]').getValue();
            			sname = gridpanel.down('triggerfield[name=sname]').getValue().trim();
            			enterprisename = gridpanel.down('triggerfield[name=enterprisename]').getValue().trim();
            			////console.log('status:'+queryStatus);
            			gridpanel.view.getFeature('group').expandAll();//展开全部
            			gridpanel.getStore().loadPage(1);
					},this);
					gridpanel.down('triggerfield[name=sname]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					if(!field.getValue()){
        						gridpanel.getStore().loadPage(1);
        					}else {
        						gridpanel.getStore().loadPage(1,{params:{sname:field.getValue()}});
        					}	
	                    }
					},this);
					gridpanel.down('triggerfield[name=enterprisename]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					if(!field.getValue()){
        						gridpanel.getStore().loadPage(1);
        					}else {
        						gridpanel.getStore().loadPage(1,{params:{enterprisename:field.getValue()}});
        					}	
	                    }
					},this);
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		this.showServiceDetail(record);
            	}
            },
            'serviceeditwindow':{
            	afterrender:function(window){
            		var me = this;
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=submit]').on('click',function(){
            			var oldcost = window.getComponent('serviceeditform').form.getRecord().get('costPrice');
            			var newcost = window.down('numberfield[name=costPrice]').getValue();
            			if((oldcost*0.9)>newcost|(oldcost*1.1)<newcost){
            				Ext.Msg.alert('提示','服务价格只能在原来的价格上浮动10%');
            			}else {
            				mask.show();
            				window.down('form').getForm().findField('serviceProcedure').	 //同步kindeditor内容到textarea
            						setValue(window.getKindeditor().html());
            				window.getComponent('serviceeditform').form.submit({
					    		clientValidation: true,
							    url: 'service/edit',
							    params: {
							        action: 'edit'						        
							    },
							    success: function(form, action) {
							    	mask.hide();
							    	if(action.result.success){
								       window.hide();
								       //var result = action.result.message.split('|');
								       //me.refreshService(result[0]);
								       //Ext.example.msg('','<p align="center">'+result[1]+'</p>');
								       Ext.getCmp("indexpage").up().focus().activeTab.getStore().reload();
								       Ext.example.msg('','<p align="center">服务编辑成功</p>');	
							    	}else{
							    		//Ext.Msg.alert('','<p align="center">'+result[1]+'</p>');
							    		Ext.Msg.alert('','<p align="center">服务编辑失败</p>');
							    	}
							    },
							    failure: function(form, action) {
							    	mask.hide();
							        switch (action.failureType) {
							            case Ext.form.action.Action.CLIENT_INVALID:
							                Ext.Msg.alert('提示', '提交表单中包含非法字符(或必填项为空)！');
							                break;
							            case Ext.form.action.Action.CONNECT_FAILURE:
							                Ext.Msg.alert('提示', 'Ajax请求失败');
							                break;
							            case Ext.form.action.Action.SERVER_INVALID:
							               Ext.Msg.alert('提示', action.result.msg);
							       }
							    }
					    	});
            			}
            			
            			
            		});   
            		window.query('button[name=select]')[0].on('click', function () {
            			this.selectPic('editservice');
            		}, this);
            	}
            },
            'serviceaddwindow':{	//添加运营服务窗口
            	afterrender:function(window){
            		var me = this;
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=submit]').on('click',function(){
            			mask.show();
            			window.down('form').getForm().findField('serviceProcedure').	 //同步kindeditor内容到textarea
            						setValue(window.getKindeditor().html());
            			window.getComponent('serviceaddform').form.submit({
				    		clientValidation: true,
						    url: 'service/save',
						    params: {
						        action: 'save'						        
						    },
						    success: function(form, action) {
						    	mask.hide();
						    	if(action.result.success){
							       window.hide();							       
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							       me.refreshNService();
						    	}else{
						    		Ext.Msg.alert('','<p align="center">'+action.result.message+'</p>');
						    	}
						    },
						    failure: function(form, action) {
						    	mask.hide();
						        switch (action.failureType) {
						            case Ext.form.action.Action.CLIENT_INVALID:
						                Ext.Msg.alert('提示', '提交表单中包含非法字符(或必填项为空)！');
						                break;
						            case Ext.form.action.Action.CONNECT_FAILURE:
						                Ext.Msg.alert('提示', 'Ajax请求失败');
						                break;
						            case Ext.form.action.Action.SERVER_INVALID:
						               Ext.Msg.alert('提示', action.result.message);
						       }
						    }
				    	});
            		});
            		window.down('button[action=reset]').on('click',function(){            			
            			window.down('treecombo[name="category.id"]').setValue(',');
            			window.getComponent('serviceaddform').form.reset();
            			window.getKindeditor().html('');  
            		});   
            		window.query('button[name=select]')[0].on('click', function () {
            			this.selectPic('service');
            		}, this);
            	}
            },
            'addforuserwindow':{	//添加机构服务窗口
            	afterrender:function(window){
            		var me = this;
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=submit]').on('click',function(){
            			mask.show();
            			window.down('form').getForm().findField('serviceProcedure').	 //同步kindeditor内容到textarea
            						setValue(window.getKindeditor().html());
            			window.getComponent('addforuserform').form.submit({
				    		clientValidation: true,
						    url: 'service/saveforuser',
						    params: {
						        action: 'saveforuser'						        
						    },
						    success: function(form, action) {
						    	mask.hide();						    	
						    	if(action.result.success){
							       window.hide();							       
							       //Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							       Ext.example.msg('','<p align="center">服务添加成功</p>');
							       me.refreshNService();
						    	}else{
						    		//Ext.Msg.alert('','<p align="center">'+action.result.message+'</p>');
						    		Ext.Msg.alert('','<p align="center">服务添加失败</p>');
						    	}
						    },
						    failure: function(form, action) {
						    	mask.hide();
						        switch (action.failureType) {
						            case Ext.form.action.Action.CLIENT_INVALID:
						                Ext.Msg.alert('提示', '提交表单中包含非法字符(或必填项为空)！');
						                break;
						            case Ext.form.action.Action.CONNECT_FAILURE:
						                Ext.Msg.alert('提示', 'Ajax请求失败');
						                break;
						            case Ext.form.action.Action.SERVER_INVALID:
						               Ext.Msg.alert('提示', action.result.message);
						       }
						    }
				    	});            		});
            		window.down('button[action=reset]').on('click',function(){
            			//Ext.getCmp('usercategorycombo').store.removeAll();
            			window.down('treecombo[name="category.id"]').setValue(',');
            			window.getComponent('addforuserform').form.reset();            			
            		});   
            		window.query('button[name=select]')[0].on('click', function () {
            			this.selectPic('service');
            		}, this);
            	}
            },
//            'addforuserwindow enterprisecombo':{	//添加机构服务窗口
//            	afterrender:function(window){            		
//            	},
//            	select:function(combo, records, eOpts){
//					
//            		var icRegNumber = combo.store.getById(combo.value).data.icRegNumber;
//            		var categoryCombo = Ext.getCmp('usercategorycombo');
//            		var mask = new Ext.LoadMask(categoryCombo.up("form"),{
//						msg : '加载服务类别中,请稍等...'
//					});
//					mask.show();
//            		categoryCombo.reset();
//            		var store = categoryCombo.store;
//            		store.removeAll();
//            		store.on('beforeload',function(store){
//            			Ext.apply(store.proxy.extraParams, {icRegNumber:icRegNumber});
//            		});
//            		store.load({params: { icRegNumber: icRegNumber}});
//            		store.on('load',function(store,records,successful,eOpts){
//            			console.log(store);
//            			console.log(records);
//            			console.log(successful);
//            			console.log(eOpts);            			
//            			if(successful){
//            				if(store)
//            				mask.hide();
//            			}else {
//            				mask.hide();
//            				Ext.Msg.alert('提示','服务类别加载失败');
//            			}
//            		});
//            	}
//            },
            'uservicewindow':{
            	afterrender:function(window){
            		var me = this;
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=submit]').on('click',function(){
            			mask.show();
            			window.down('form').getForm().findField('serviceProcedure').	 //同步kindeditor内容到textarea
            						setValue(window.getKindeditor().html());
            			var record = window.down('form').form.getRecord();
            			window.down('form').form.updateRecord(record);            			
            			window.getComponent('userviceform').form.submit({
				    		clientValidation: true,
						    url: 'service/updateComments',
						    params: record.data,
						    success: function(form, action) {
						    	mask.hide();
						    	if(action.result.success){
							       Ext.example.msg('提示','<p align="center">'+action.result.message+'</p>');
							       window.hide();
							       me.refreshUService();
						    	}else{
						    		Ext.Msg.alert('提示','<p align="center">'+action.result.message+'</p>');
						    	}
						    },
						    failure: function(form, action) {
						    	mask.hide();
						        switch (action.failureType) {
						            case Ext.form.action.Action.CLIENT_INVALID:
						                Ext.Msg.alert('提示', '提交表单中包含非法字符(或必填项为空)！');
						                break;
						            case Ext.form.action.Action.CONNECT_FAILURE:
						                Ext.Msg.alert('提示', 'Ajax请求失败');
						                break;
						            case Ext.form.action.Action.SERVER_INVALID:
						               Ext.Msg.alert('提示', action.result.msg);
						       }
						    }
				    	});
            		});
            		//关闭按钮
            		window.down('button[action=close]').on('click',function(){
            			window.close();
            		});
            		
            		/* 点击按钮，打开上传图片窗口*/
            		window.query('button[name=select]')[0].on('click', function () {
            			this.selectPic('uservice');
            		}, this);
          		  }
            }, 
            "uploadwindow[name=service]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'service');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			"uploadwindow[name=editservice]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'editservice');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			"uploadwindow[name=uservice]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'uservice');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			'apprservicewindow' : {
            	afterrender:function(window){
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=enter]').on('click',function(){
	            		var grid = Ext.getCmp('fwsjtjcx');
	            		var store = grid.getStore();
	            		var record = store.getAt(window.down('hiddenfield[name=rowIndex]').getValue());            				
						record.data.access = window.title.indexOf('批准') > -1 ? 0 :1;;
						record.data.context = window.down('textarea[name=context]').getValue();
						mask.show();
						store.update({
							params:record.data,
							callback:function(r,operation){
								var result =Ext.JSON.decode(operation.response.responseText);
								mask.hide();
								window.hide();
								if(result.success){
									if(record.data.access == 0){
										Ext.Msg.alert('提示','服务【'+operation.params.serviceName+'】批准成功');
									}else {
										Ext.Msg.alert('提示','服务【'+operation.params.serviceName+'】打回成功');
									}
									grid.getStore().remove(record);
								}else {
									if(record.data.access == 0){
										Ext.Msg.alert('提示','服务【'+operation.params.serviceName+'】批准失败');
									}else {
										Ext.Msg.alert('提示','服务【'+operation.params.serviceName+'】打回失败');
									}
									
								}
							}
						});
            		},this);
   					window.down('button[action=cancle]').on('click',function(){            			
            			window.hide();
            			window.getComponent('apprserviceform').form.reset();
   					});
            }
         },
        'topthreegrid':{//top3服务配置
			afterrender : function (grid) {
				var me =this;
				grid.getStore().load();
				grid.down('button[action=add]').on('click', function () {
					if(grid.getStore().getCount()<=2){
						me.opentopthreewindow('添加top服务',null);
					}else{
						Ext.Msg.alert('提示', '只能添加三条记录！');
					}
        		}, this);
        			
			},
			// 双击显示该类别下的服务
			itemdblclick : function(grid, record) {
				this.opentopthreewindow('修改top服务',record);
			}
        },
        'topthreewindow':{//top3服务的增加和修改
			afterrender:function(window){
				var me = this;
				window.down('combo[name=topthree]').on('select',function (){
					var topthree = window.down('combo[name=topthree]').getValue();
					var store =  Ext.ComponentQuery.query('topthreegrid')[0].getStore();
					var result=true;
					Ext.each(store.getRange(), function (service, index, countriesItSelf) {
						if(service.data.topthree==topthree){
							Ext.Msg.alert('提示', '该顺序已有相应的服务，请选择其它顺序！');
							result = false;
							window.down('combo[name=topthree]').setValue('');
						}
					});	
					if(result ==true){
						window.down('button[action=add]').setDisabled(false);
					}else{
						window.down('button[action=add]').setDisabled(true);
					}
				},this);
				window.down('combo[valueField=id]').on('select',function (){
					var id = window.down('combo[valueField=id]').getValue();
					//获取表格一列的值
					window.down('textfield[name=serviceid]').setValue(id);
					var store =  Ext.ComponentQuery.query('topthreegrid')[0].getStore();
					var result=true;
					Ext.each(store.getRange(), function (item, index, countriesItSelf) {
						if(item.data.serviceid==id){
							Ext.Msg.alert('提示', '该服务已经添加到top3，请选择其它服务！');
							result = false;
							window.down('textfield[valueField=id]').setValue('');
						}
					});
					if(result ==true){
						window.down('button[action=add]').setDisabled(false);
					}else{
						window.down('button[action=add]').setDisabled(true);
					}
				},this);
				window.down('button[action=add]').on('click',function (){
					window.getComponent('topform').form.submit({
			    		clientValidation: true,
					    url: 'service/updatetop',
					    success: function(form, action) {
					    	if(action.result.success){
						       window.hide();							       
						       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
						        Ext.ComponentQuery.query('topthreegrid')[0].getStore().load();
					    	}else{
					    		Ext.Msg.alert('','<p align="center">'+action.result.message+'</p>');
					    	}
					    },
					    failure: function(form, action) {
					        switch (action.failureType) {
					            case Ext.form.action.Action.CLIENT_INVALID:
					                Ext.Msg.alert('提示', '提交表单中包含非法字符(或必填项为空)！');
					                break;
					            case Ext.form.action.Action.CONNECT_FAILURE:
					                Ext.Msg.alert('提示', 'Ajax请求失败');
					                break;
					            case Ext.form.action.Action.SERVER_INVALID:
					               Ext.Msg.alert('提示', action.result.message);
					       }
					    }
			    	});
				},this);
			}
        }
        });
    },
    opentopthreewindow:function(title,record){
		var topthreewindow = Ext.ComponentQuery.query('topthreewindow')[0];
		if(!topthreewindow){
			topthreewindow = Ext.widget('topthreewindow');    		
		}
		topthreewindow.setTitle(title);
		topthreewindow.show();
		var topform = topthreewindow.getComponent('topform');
		topform.form.reset();
		if(null!=record){
			topform.loadRecord(record);
			topform.down('combo[valueField=id]').setValue(record.data.serviceid);
		}
    },
    extraParams : function (store, param) {
    	Ext.apply(store.proxy.extraParams, {
			queryStatus : param,
			sname : this.getServiceGrid().down('textfield[name=sname]').getValue()
		});
    },
    
    loadService:function(cid){
    	this.getServicegrid().getStore().load({params:{cid:cid}});
    },
    refreshService:function(value){
    	if('已下架'==value){
    		this.getDservicegrid().getStore().reload();
    	}else{
    		this.getNservicegrid().getStore().reload();
    	}
    },
    refreshUService:function(){
    	this.getUservicegrid().getStore().reload();
    },
    refreshNService:function(){
    	this.getNservicegrid().getStore().reload();
    },    
    addServiceForUser:function(record){
    	var icRegNumber;
    	var addForUserWindows = this.getAddforuserwindow();
        if(!addForUserWindows){
			addForUserWindows = Ext.widget('addforuserwindow',{
			title:'添加机构新服务'    			
			});    		
		} 
		addForUserWindows.down('treecombo[name="category.id"]').setValue(',');
		addForUserWindows.getComponent('addforuserform').form.reset();
		addForUserWindows.down('hiddenfield[name=serviceSource]').setValue(2);
		addForUserWindows.show();
		if (addForUserWindows.getKindeditor()) {
			addForUserWindows.getKindeditor().html('');
		} else {
			setTimeout(function () {
				addForUserWindows.getKindeditor().html('');
			}, 1000);
		}
    },
    addService:function(record){ 
    	var addServiceWindows = this.getServiceaddwindow();
    	if(!addServiceWindows){
			addServiceWindows = Ext.widget('serviceaddwindow',{
		    	title:'添加运营新服务'    			
		    });    		
		}
		addServiceWindows.down('treecombo[name="category.id"]').setValue(','); 
		addServiceWindows.getComponent('serviceaddform').form.reset();
		//addServiceWindows.down('hiddenfield[name=enterprise.icRegNumber]').setValue(1234567890);		
		addServiceWindows.down('hiddenfield[name=serviceSource]').setValue(3);
		addServiceWindows.show();
    	if (addServiceWindows.getKindeditor()) {
			addServiceWindows.getKindeditor().html('');
		} else {
			setTimeout(function () {
				addServiceWindows.getKindeditor().html('');
			}, 1000);
		}
    },
    editServiceData:function(record){
    	var serviceEditWindows = this.getServiceeditwindow();
    	if(!serviceEditWindows){
    		serviceEditWindows = Ext.widget('serviceeditwindow',{
    			title:'编辑服务['+record.data.serviceName+']'
    		});    		
    	}
		serviceEditWindows.down('treecombo[name="category.id"]').setValue(',');
		serviceEditWindows.getComponent('serviceeditform').form.reset();
		//Ext.getCmp("eusercategorycombo").store.removeAll();
    	//Ext.getCmp("eusercategorycombo").store.load({params: { icRegNumber: record.get("enterprise.icRegNumber")}});	
		serviceEditWindows.setTitle('编辑服务['+record.data.serviceName+']');
		serviceEditWindows.getComponent('serviceeditform').form.loadRecord(record);	
		//Ext.getCmp("eusercategorycombo").setValue(record.raw.category.id*1);
		serviceEditWindows.show();
		if (serviceEditWindows.getKindeditor()) {
			serviceEditWindows.getKindeditor().html(record.data.serviceProcedure);
		} else {
			setTimeout(function () {
				serviceEditWindows.getKindeditor().html(record.data.serviceProcedure);
			}, 1000);
		}
    },
    showServiceDetail:function(record){
    	var serviceDetailWindows = this.getServicedetailwindow();
    	if(!serviceDetailWindows){
    		serviceDetailWindows = Ext.widget('servicedetailwindow',{
    			title:'服务详情['+record.data.serviceName+']'
    		});    		
    	}
		serviceDetailWindows.show();
		serviceDetailWindows.setTitle('服务详情['+record.data.serviceName+']');
		serviceDetailWindows.getComponent('servicedetailform').form.loadRecord(record);
		if (serviceDetailWindows.getKindeditor()) {
			serviceDetailWindows.getKindeditor().html(record.data.serviceProcedure);
		} else {
			setTimeout(function () {
				serviceDetailWindows.getKindeditor().html(record.data.serviceProcedure);
			}, 1000);
		}
    },
    updateService : function (record) {
    	var serviceWindow;
    	var serviceWindows = Ext.ComponentQuery.query('servicewindow');
    	if(serviceWindows.length == 0){
    		serviceWindow = Ext.widget('servicewindow',{
    			title:'修改服务',
    			id:'servicewindow'
    		}).show();
    		serviceWindow.getComponent('serviceform').form.loadRecord(record);
    	}else{
    		serviceWindows[0].show();
    		serviceWindows[0].getComponent('serviceform').form.reset();
    	}
    },
    selectPic : function (name) {
		var picWindow = Ext.ComponentQuery.query('uploadwindow[name='+ name +']')[0];
    	if (!picWindow) {
    		Ext.widget('uploadwindow',{
    			title : '上传',
    			name : name
    		}).show();
    	} else {
    		picWindow.show();
    	}
	},
	uplodImage : function (mask, name) {
		var Ewindow = Ext.ComponentQuery.query('uploadwindow[name='+ name +']')[0];
		var Eform = Ewindow.query('uploadform')[0];
		var window;
		if (name == 'service') {
			window = this.getAddforuserwindow();
		} else if (name == 'editservice') {
			window = this.getServiceeditwindow();
		} else if (name == 'uservice') {
			window = this.getUservicewindow();
		}
		mask.show();
		Eform.getForm().submit({
			url : 'public/uploadFile',
			clientValidation: true,
		    success: function(form, action) {
		    	if (action.result.success) {
			       var addForm = window.query('form')[0];
			       var Epicture = addForm.down('textfield[name=picture]');
			       Epicture.setValue(action.result.message);
			       form.reset();
			       Ewindow.hide();
		    	} else {
		    		Ext.Msg.alert('提示','<p align="center">'+action.result.message+'</p>');
		    	}
		    	mask.hide();
		    	Ewindow.hide();
		    },
		    failure: function(form, action) {
		        switch (action.failureType) {
		            case Ext.form.action.Action.CLIENT_INVALID:
		                break;
		            case Ext.form.action.Action.CONNECT_FAILURE:
		                Ext.Msg.alert('提示', '<p align="center">Ajax请求失败</p>');
		                break;
		            case Ext.form.action.Action.SERVER_INVALID:
		               Ext.Msg.alert('提示', '<p align="center">' + 
		               	action.result.msg + '</p>');
		       }
		       mask.hide();
		       Ewindow.hide();
		    }
		});
	}
});
//服务审核专用
function openApprServiceWindow(title, rowIndex){
	var apprservicewindow = null;
	var windows = Ext.ComponentQuery.query('apprservicewindow');
	if(windows.length > 0 ){
		apprservicewindow = windows[0];
		apprservicewindow.setTitle(title);
		apprservicewindow.show();
	} else {
		apprservicewindow = Ext.widget('apprservicewindow',{title:title}).show();
	}
	var form = apprservicewindow.getComponent('apprserviceform').form;
	form.reset();
	apprservicewindow.down('hiddenfield[name=rowIndex]').setValue(rowIndex);
}