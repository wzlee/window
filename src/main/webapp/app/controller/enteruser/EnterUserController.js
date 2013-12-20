//var parentId,parentName,userStatus,enterType;		//主账号ID,主账号用户名,主账号的用户状态
//var staffgrid_g;//全局子账号表格
Ext.define('plat.controller.enteruser.EnterUserController', {
    extend: 'Ext.app.Controller',    
    xtype:'enterusercontroller',
    
    stores: [
				'enteruser.DisableUserStore',
				'enteruser.QEnterUserStore',
				'enteruser.EnterUserStore',
				'enteruser.EnterStaffStore',
				'enteruser.EnterpriseStore',
				'enteruser.OrganRecommendationStore'
			],
    views: [
    			'enteruser.EnterUserGrid',
    			'enteruser.EnterStaffGrid',
    			'enteruser.EnterUserPanel',
    			'enteruser.DisableUserGrid',
    			'enteruser.QEnterUserGrid',
    			'layout.combo.EnterUserTypeCombo',
    			'layout.combo.OrgEnterRoleCombo',
    			'layout.combo.PropertyCombo',
    			'layout.combo.UserTypeCombo',
    			'layout.combo.IndustryTypeCombo',
    			'layout.combo.BusinessPatternCombo',
    			'enteruser.UserEditWindow',
    			'enteruser.StaffAddWindow',
    			'enteruser.StaffEditWindow',
    			'enteruser.StaffDetailWindow',
    			'enteruser.UserDetailWindow',
    			'enteruser.UserEditWindow',
    			'enteruser.UserEditWindow',
    			'enteruser.UserAddWindow',
    			'enteruser.ApplyWindow',
    			'enteruser.ApplyPersonalWindow',
				'public.UploadWin',
    			'public.UploadForm',
    			'enteruser.OrganGrid',
    			'enteruser.OrganWindow',
    			'enteruser.PersonalUserDetailWindow',
    			'enteruser.PersonalUserAddWindow',
    			'enteruser.PersonalUserEditWindow'
    		],    
     refs: [
     		{
     			ref:'qenteruserstore',
     			selector:'qenteruserstore'
     		},
     		{
     			ref:'qenterusergrid',
     			selector:'qenterusergrid'
	     	},
	     	{
	     		ref:'enterstaffgrid',
	     		selector:'enterstaffgrid'
	     	},
	     	{
	     		ref:'staffaddwindow',
	     		selector:'staffaddwindow'
	     	},
	     	{
	     		ref:'staffdetailwindow',
	     		selector:'staffdetailwindow'

	     	},
	     	{
	     		ref:'staffeditwindow',
	     		selector:'staffeditwindow'
	     	},
	     	{
	     		ref:'userdetailwindow',
	     		selector:'userdetailwindow'
	     	},
	     	{
	     		ref:'useraddwindow',
	     		selector:'useraddwindow'
	     	},
	     	{
	     		ref:'usereditwindow',
	     		selector:'usereditwindow'
	     	},
	     	{
	     		ref:'enterusergrid',
	     		selector:'enterusergrid'
	     	},
	     	{
	     		ref:'disableusergrid',
	     		selector:'disableusergrid'
	     	},
	     	{
	     		ref:'applywindow',
	     		selector:'applywindow'
	     	},
	     	{
	     		ref:'organgrid',
	     		selector:'organgrid'
	     	},
	     	{
	     		ref:'personaluserdetailwindow',
	     		selector:'personaluserdetailwindow'
	     	},
	     	{
	     		ref:'personaluseraddwindow',
	     		selector:'personaluseraddwindow'
	     	},
	     	{
	     		ref:'applypersonalwindow',
	     		selector:'applypersonalwindow'
	     	},
	     	{
	     		ref:'personalusereditwindow',
	     		selector:'personalusereditwindow'
	     	}

    ],
  
    init: function() {
        this.control({
        	'enterusergrid':{	//会员列表管理
            	afterrender:function(gridpanel){
            		var uName,eName,enterpriseType;
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {uName:uName,eName:eName,enterpriseType:enterpriseType,userStatus:'1,2'});
            		});
            		store.loadPage(1,{
            			callback:function(r){
            				if(r.length>0){
//			            		gridpanel.getSelectionModel().select(0,true);
//			            		gridpanel.getView().focusRow(0);//获取焦点
            				}
            			}
            		});
            		
            		gridpanel.down('button[action=search]').on('click',function(){
            			uName = gridpanel.down('triggerfield[name=uName]').getValue();
            			eName = gridpanel.down('triggerfield[name=eName]').getValue();
            			enterpriseType = gridpanel.down('combo[name=enterpriseType]').getValue();
            			store.loadPage(1,{params:{uName:uName,eName:eName,enterpriseType:enterpriseType},
            				callback:function(r){//默认选中第一行
//	            				if(r.length>0){
//				            		gridpanel.getSelectionModel().select(0,true);
//				            		gridpanel.getView().focusRow(0);//获取焦点
//	            				}
            				}
            			});            			
					},this);
//					gridpanel.down('triggerfield[name=uName]').on('specialkey',function(field,e){
//        				if (e.getKey() == e.ENTER) {        					
//        					if(!field.getValue()){
//        						store.loadPage(1,{params:{uName:''},
//	        						callback:function(r){//默认选中第一行
//			            				if(r.length>0){
////						            		gridpanel.getSelectionModel().select(0,true);
////						            		gridpanel.getView().focusRow(0);//获取焦点
//			            				}
//		            				}
//        						});
//        					}else {
//        						uName = gridpanel.down('triggerfield[name=uName]').getValue();
//        						store.loadPage(1,{params:{uName:uName},
//        							callback:function(r){//默认选中第一行
//			            				if(r.length>0){
////						            		gridpanel.getSelectionModel().select(0,true);
////						            		gridpanel.getView().focusRow(0);//获取焦点
//			            				}
//            						}
//        						});
//        					}
//                       		
//	                    }
//					},this);
					gridpanel.down('button[action=add]').on('click',function(){
						var window = this.getUseraddwindow();
						if(!window){
	    					window = Ext.widget('useraddwindow',{
	    						 			
	    					});    		
    					}
						window.down('form').getForm().reset();
						window.down('displayfield[name=enterprise.industryType]').setValue(windowId);
						window.show();
					},this);
//					gridpanel.down('button[action=add]').down('[name=enterUser]').on('click',function(){
//						var window = this.getUseraddwindow();
//						if(!window){
//	    					window = Ext.widget('useraddwindow',{
//	    						 			
//	    					});    		
//    					}
//						window.down('form').getForm().reset();
//						window.show();
//					},this);
//					//个人用户添加
//					gridpanel.down('button[action=add]').down('[name=personalUser]').on('click',function(){
//						this.addPersonalUser();					
//					},this);
            	},
            	cellclick:function(gridpanel, td, cellIndex, record, tr, rowIndex, e, eOpts){	//单元格点击事件            		
//            		if(record.data.isPersonal){//个人用户
//            			if(cellIndex == 1){//查看个人用户详情
//            				var window = this.getPersonaluserdetailwindow();
//            				if(!window){
//            					window = Ext.widget('personaluserdetailwindow',{
//									title:'用户详情'+'['+record.data.userName+']'         					
//            					})
//            				}
//            				window.setTitle('用户详情'+'['+record.data.userName+']');
//            				window.down('form').getForm().reset();    					
//	    					window.down('form').getForm().loadRecord(record);
//	    					window.show();	
//            			}
//            		}else{//企业用户
	            		if(cellIndex==3){            			
	            			var window = this.getUserdetailwindow();
	            			if(!window){
		    					window = Ext.widget('userdetailwindow',{
		    						title: '企业信息'			
		    					});    		
	    					}
	    					window.down('form').getForm().reset();    					
	    					window.down('form').getForm().loadRecord(record);
	    					window.show();	
				        }
//            		}
            	}
//            	,	
//            	select:this.showStaff		//行选中事件显示该主账号下的所有子账号信息
            },
            'enterusergrid actioncolumn':{
            	realname:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		this.openApplyWin(record, 1); // 申请实名认证
            	},
            	serviceorg:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		if(record.data['enterprise.type'] == 1){ // 1.普通企业
            			this.openApplyWin(record, 2); // 申请服务机构
            		}else{
            			Ext.Msg.confirm('提示', '该企业已经认证实名,是否继续申请服务机构吗?',function(result){
            				if(result == 'yes'){
		            			Ext.Ajax.request({
		            				url : 'ucenter/apply',
		            				params : {
		            					'user.id' : record.data.id,
		            					'user.userName' : record.data.userName,
		            					'enterprise.id' : record.data['enterprise.id'],
		            					'applyType' : 2,
		            					'approveStatus' : 2
		            				},
		            				success: function(response,result){
		            					var result = eval("("+response.responseText+")");
		    							Ext.Msg.alert('提示', result.message);
								    }
		            			});
            				}
            			});
            		}
            	},
            	personal:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		this.openApplyPersonalWin(record); // 申请个人实名认证
            	},
            	editclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		if(record.data.isPersonal){//个人用户
            			var window = this.getPersonalusereditwindow();
						var record = grid.getStore().getAt(rowIndex);
						if(!window){
	    					window = Ext.widget('personalusereditwindow',{
	    						title:'编辑用户信息'   			
	    					});    		
	    				}					
						window.down('form').form.reset();
						window.show();
						window.down('form').getForm().loadRecord(record);
            		}else{//企业用户
	            		var window = this.getUsereditwindow();
						var record = grid.getStore().getAt(rowIndex);
						if(!window){
	    					window = Ext.widget('usereditwindow',{
	    						title:'编辑会员信息' 			
	    					});    		
	    				}
	    				if( record.data['enterprise.type']>1 ){
	    					window.down("textfield[name=enterprise.name]").setReadOnly(true);
	    				}else {
	    					window.down("textfield[name=enterprise.name]").setReadOnly(false);
	    				}
						window.down('form').form.reset();					
						window.down('form').getForm().loadRecord(record);					
						window.show();
            		}
            		
            	},
            	disableclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			                    Ext.MessageBox.confirm('警告','确定禁用【 '+record.data.userName+' 】吗?',function(btn){
						    		if(btn == 'yes'){								    			
						    			record.set('userStatus',2);
						    			grid.getStore().update({
								    		params:record.data,
								    		callback:function(record,operation){
								    			var result =Ext.JSON.decode(operation.response.responseText);								    						
								    			if(result.success){								    			
								    				Ext.example.msg('','<p align="center">会员【'+operation.params.userName+'】禁用成功</p>');
								    				var disablegrid = Ext.getCmp('jylbgl');		//获取禁用会员grid
								    				if(disablegrid){
								    					disablegrid.store.loadPage(1);
								    				}
								    			}else {
								    				Ext.Msg.alert('提示','会员【'+operation.params.userName+'】禁用失败');
								    			}	
								    		}
								    	});		
									}
								});
            		},
            	deleteclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			                    Ext.MessageBox.confirm('警告','确定删除【 '+record.data.userName+' 】吗?',function(btn){
						    		if(btn == 'yes'){
						    			record.set('userStatus',3);
						    			grid.getStore().update({
								    		params:record.data,
								    		callback:function(record,operation){
								    			var result =Ext.JSON.decode(operation.response.responseText);								    						
								    			if(result.success){								    				
								    				Ext.example.msg('','<p align="center">会员【'+operation.params.userName+'】删除成功</p>');
								    				grid.getStore().remove(record);
								    			}else {
								    				Ext.Msg.alert('提示','会员【'+operation.params.userName+'】删除失败');
								    			}	
								    		}
								    	});			
									}
								});
            	}
            },
            'enterusergrid triggerfield':{            	
            	afterrender:function(triggerfield){
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
            'enterstaffgrid':{
            	afterrender:function(gridpanel){
					gridpanel.down('button[action=add]').on('click',function(){
                		this.addStaff();
					},this);		
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		this.showStaffDetail(record);
            	}
            },
            'enterstaffgrid actioncolumn':{
            	resetpwd:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			        Ext.MessageBox.confirm('警告','确定子账号【'+record.data.userName+'】重置密码吗?',function(btn){
					if(btn == 'yes'){								    			
						    record.set('password','e10adc3949ba59abbe56e057f20f883e');
						    grid.getStore().update({
								params:record.data,
								callback:function(record,operation){
									var result =Ext.JSON.decode(operation.response.responseText);
									if(result.success){
								   	 Ext.example.msg('','<p align="center">子账号【'+operation.params.userName+'】重置密码成功</p>');
									}else {
								   	 Ext.Msg.alert('提示','子账号【'+operation.params.userName+'】重置密码失败');
									}	
								}
							});							    			
						}
					});
            	},
           		editclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var window = this.getStaffeditwindow();
					var record = grid.getStore().getAt(rowIndex);
					if(!window){
    					window = Ext.widget('staffeditwindow',{
    						title:'编辑子账号:【'+record.data.userName+"】"   			
    					});    		
    				};
    				
					window.getComponent('staffeditform').form.reset();
					var staffEditForm = window.getComponent('staffeditform');
					
					staffEditForm.down('combobox[name=staffRole.id]').getStore()
					.setProxy({	
						type:'ajax',
						actionMethods: {  
							read: 'POST'
						},		
						extraParams:{enterpriseType:enterType},
						url:'staff/loadStaffRole'	
					});
					staffEditForm.down('combobox[name=staffRole.id]').getStore().load();

					window.show();
					window.getComponent('staffeditform').getForm().loadRecord(record);
					var managerId = window.down('hiddenfield[name=manager.id]').getValue();
					if(managerId == 0){//分配人不是平台人员
						window.down('displayfield[name=fpname]').setValue(record.get('assigner.userName'));
					}else{
						window.down('displayfield[name=fpname]').setValue(record.get('manager.username'));
					}
            	},
            	disableclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			        Ext.MessageBox.confirm('警告','确定禁用子账号【'+record.data.userName+'】吗?',function(btn){
					if(btn == 'yes'){								    			
						    record.set('staffStatus',2);
//								console.log(record);
						    grid.getStore().update({
								params:record.data,
								callback:function(record,operation){
//									console.log(operation);
									var result =Ext.JSON.decode(operation.response.responseText);
									if(result.success){
								   	 Ext.example.msg('','<p align="center">子账号【'+operation.params.userName+'】禁用成功</p>');
									}else {
								   	 Ext.Msg.alert('提示','子账号【'+operation.params.userName+'】禁用失败');
									}	
								}
							});							    			
						}
					});
            	},
            	recoverclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			        Ext.MessageBox.confirm('警告','确定恢复子账号【'+record.data.userName+'】吗?',function(btn){
					if(btn == 'yes'){								    			
						    record.set('staffStatus',1);
						    grid.getStore().update({
								params:record.data,
								callback:function(record,operation){
									var result =Ext.JSON.decode(operation.response.responseText);
									if(result.success){
								   	 Ext.example.msg('','<p align="center">子账号【'+operation.params.userName+'】恢复成功</p>');
									}else {
								   	 Ext.Msg.alert('提示','子账号【'+operation.params.userName+'】恢复失败');
									}	
								}
							});							    			
						}
					});
            	},
            	deleteclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			        Ext.MessageBox.confirm('警告','确定删除子账号【'+record.data.userName+'】吗?',function(btn){
					if(btn == 'yes'){								    			
						    record.set('staffStatus',3);
						    grid.getStore().update({
								params:record.data,
								callback:function(record,operation){
									var result =Ext.JSON.decode(operation.response.responseText);
									if(result.success){
								   	 Ext.example.msg('','<p align="center">子账号【'+operation.params.userName+'】删除成功</p>');
									}else {
								   	 Ext.Msg.alert('提示','子账号【'+operation.params.userName+'】删除失败');
									}	
								}
							});							    			
						}
					});
            	}
            },
            'disableusergrid':{		//禁用列表管理
            	afterrender:function(gridpanel){
            		var uName,eName,enterpriseType;
            		var store = gridpanel.getStore();
            		store.on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {uName:uName,eName:eName,enterpriseType:enterpriseType,userStatus:'2'});
            		});
            		store.loadPage(1);
            		gridpanel.down('button[action=search]').on('click',function(){
            			uName = gridpanel.down('triggerfield[name=uName]').getValue();
            			eName = gridpanel.down('triggerfield[name=eName]').getValue();
            			enterpriseType = gridpanel.down('combo[name=enterpriseType]').getValue();
            			store.loadPage(1,{params:{uName:uName,eName:eName,enterpriseType:enterpriseType}});      			
					},this);
//					gridpanel.down('triggerfield[name=uName]').on('specialkey',function(field,e){
//        				if (e.getKey() == e.ENTER) {        					
//        					if(!field.getValue()){
//        						store.loadPage(1,{params:{uName:''}});
//        					}else {
//        						uName = gridpanel.down('triggerfield[name=uName]').getValue();
//        						store.loadPage(1,{params:{uName:uName}});
//        					}
//                       		
//	                    }
//					},this);
            	},
            	cellclick:function(gridpanel, td, cellIndex, record, tr, rowIndex, e, eOpts){	//单元格点击事件            		
            		if(cellIndex==3){
            			var window = this.getUserdetailwindow();
            			if(!window){
	    					window = Ext.widget('userdetailwindow',{
	    						title: '会员信息'   			
	    					});    		
    					}
    					window.down('form').getForm().reset();
    					window.down('form').getForm().loadRecord(record);
						window.show();
			        }            	
            	}
            },
            'disableusergrid actioncolumn':{
            	restoreclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			                    Ext.MessageBox.confirm('警告','确定还原【 '+record.data.userName+' 】吗?',function(btn){
						    		if(btn == 'yes'){								    			
						    			record.set('userStatus',1);
						    			grid.getStore().update({
								    		params:record.data,
								    		callback:function(record,operation){
								    			var result =Ext.JSON.decode(operation.response.responseText);								    						
								    			if(result.success){
								    				Ext.example.msg('','<p align="center">会员【'+operation.params.userName+'】还原成功</p>');
								    				var usergrid = Ext.getCmp('enterusergrid');		//获取会员grid
								    				if(usergrid){
								    					usergrid.store.loadPage(1);
								    				}
								    			}else {
								    				Ext.Msg.alert('提示','会员【'+operation.params.userName+'】还原失败');
								    			}	
								    		}
								    	});			
									}
								});
            	},
            	deleteclick:function(column,grid, rowIndex, colIndex, node, e, record, rowEl){
            		var record = grid.getStore().getAt(rowIndex);
			                    Ext.MessageBox.confirm('警告','确定删除【 '+record.data.userName+' 】吗?',function(btn){
						    		if(btn == 'yes'){
						    			record.set('userStatus',3);
						    			grid.getStore().update({
								    		params:record.data,
								    		callback:function(record,operation){
								    			var result =Ext.JSON.decode(operation.response.responseText);								    						
								    			if(result.success){
								    				Ext.example.msg('','<p align="center">会员【'+operation.params.userName+'】删除成功</p>');
								    				grid.getStore().remove(record);
								    			}else {
								    				Ext.Msg.alert('提示','会员【'+operation.params.userName+'】删除失败');
								    			}	
								    		}
								    	});			
									}
								});
            	}
            },
            'disableusergrid triggerfield':{            	
            	afterrender:function(triggerfield){
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
            'qenterusergrid':{
            	afterrender:function(gridpanel){
            		var username = '';
            		var entershort = '';
            		var entername = '';
            		var enterpriseType;
            		var startTime = '';
            		var endTime = '';
            		gridpanel.getStore().on('beforeload',function(store){
            			Ext.apply(store.proxy.extraParams, {//设置全局参数
            				username:username,
            				entershort:entershort,
            				entername:entername,
            				enterpriseType:enterpriseType,
            				startTime:startTime,
            				endTime:endTime,
            				userStatus:'1,2'
            			});
            		});
            		gridpanel.getStore().loadPage(1);	//首先加载全部数据
            		gridpanel.down('button[action=search]').on('click',function(){  
            			//查询事件时给所有条件赋值
            			username = gridpanel.down('triggerfield[name=username]').getValue();
            			entershort = gridpanel.down('triggerfield[name=entershort]').getValue();
	 					entername = gridpanel.down('triggerfield[name=entername]').getValue(); 		 
	 					enterpriseType = gridpanel.down('combo[name=enterpriseType]').getValue();
	 					startTime = gridpanel.down('datefield[name=startdt]').getValue();
	 					endTime = gridpanel.down('datefield[name=enddt]').getValue();
            			gridpanel.getStore().loadPage(1);	
					},this);
					//会员名的回车事件
        			gridpanel.down('triggerfield[name=username]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					if(!field.getValue()){
        						gridpanel.getStore().loadPage(1);
        					}else {
        						gridpanel.getStore().loadPage(1,{params:{username:field.getValue()}});
        					}	
	                    }
					},this);
					//企业简称的回车事件
					gridpanel.down('triggerfield[name=entershort]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					if(!field.getValue()){
        						gridpanel.getStore().load();
        					}else {
        						gridpanel.getStore().load({params:{entershort:field.getValue()}});
        					}
                       		
	                    }
					},this);
					//企业名称的回车事件
					gridpanel.down('triggerfield[name=entername]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
        					if(!field.getValue()){
        						gridpanel.getStore().load();
        					}else {
        						gridpanel.getStore().load({params:{entername:field.getValue()}});
        					}
                       		
	                    }
					},this);
            	},
            	//单元格时间查询企业详情
            	cellclick:function(gridpanel, td, cellIndex, record, tr, rowIndex, e, eOpts){	//单元格点击事件            		
            		if(cellIndex==3){            			
            			var window = this.getUserdetailwindow();
            			if(!window){
	    					window = Ext.widget('userdetailwindow',{
	    						title: '企业信息:'+record.get('enterprise.name')		
	    					});    		
    					}
    						window.down('form').getForm().reset();
    						window.down('form').getForm().loadRecord(record);
    						window.show();
					
			        }
            	}
            },
            'enteruserpanel':{	//会员列表管理
            	afterrender:function(panel){
//            		//console.log(panel.title + '渲染完毕...');            		
            	}
            },
            'usereditwindow':{	//会员编辑窗口
            	afterrender:function(window){
            		var me = this;
            		var store = me.getEnterusergrid().getStore();
            		var act = 'edit';            		
            		window.down('button[action=resetPass]').on('click',function(){
            			window.down('textfield[name=password]').setValue(123456);
            			window.down('hiddenfield[name=isreset]').setValue('true');
            			Ext.Msg.alert('提示','密码重置成功,请提交');
            		});
            		window.down('button[action=submit]').on('click',function(){            			
						submitForm(window,store,act);							    	
            		});
            		window.down('button[action=reset]').on('click',function(){            			
            			window.hide();
            		});
            		/* 点击按钮，打开上传图片窗口*/
            		window.query('button[name=select_logo]')[0].on('click', function () {
            			this.selectPic('上传企业LOGO','editphoto');
            		}, this);
            	}
            },
            'userdetailwindow':{	//企业详细信息窗口
            	afterrender:function(window){
//            		//console.log(window.title + '渲染完毕...');            		
            	}
            },
			'staffaddwindow':{	//添加子账号窗口
            	afterrender:function(window){
            		var me = this;
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=submit]').on('click',function(){
            			mask.show();
            			window.getComponent('staffaddform').form.submit({
				    		clientValidation: true,
						    url: 'staff/save',
						    params: {
						        action: 'save',
						        staffRoleId:window.down('combobox[name=roleName]').getValue()
						    },
						    success: function(form, action) {
						    	mask.hide();
						    	if(action.result.success){
							       window.hide();							       
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							       me.refreshStaff();
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
            		window.down('button[action=cancel]').on('click',function(){
            			window.getComponent('staffaddform').form.reset();
            			window.hide();
            		
            		});            		
            	}
            },
            'staffeditwindow':{	//编辑子账号窗口
            	afterrender:function(window){
            		var me = this;
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=submit]').on('click',function(){
            			window.getComponent('staffeditform').form.submit({
				    		clientValidation: true,
						    url: 'staff/edit',
						    params: {
						        action: 'edit'
//						        roleId:window.down('combobox[name=staffRole.id]').getValue()
						    },
						    success: function(form, action) {
						    	mask.hide();
						    	if(action.result.success){
							       window.hide();							       
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							       me.refreshStaff();
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
            		window.down('button[action=cancel]').on('click',function(){
            			window.getComponent('staffeditform').form.reset();
            			window.hide();
            		
            		});            		
            	}
            },
            'useraddwindow':{	//添加会员窗口
            	afterrender:function(window){
            		var me = this;
            		var store = me.getEnterusergrid().getStore();
            		var act = 'add';            		
            		window.down('button[action=submit]').on('click',function(){
            			var mask = new Ext.LoadMask(window.getEl(), {
							msg : '提交中,请稍等...',
							constrain : true
						});
						mask.show();
            			Ext.data.JsonP.request({
						    url: PlatUrl + 'public/validateUserName ',
						    timeout: 20000,
						    params: {userName : window.down('textfield[name=userName]').getValue()},
						    callbackKey: "callback",
						    success: function(result) {
						    	mask.hide();
						    	if(result.success){						    		
						    		submitForm(window,store,act);
						    	}else {
						    		Ext.Msg.alert('提示',result.message)
						    	}						    							    	
						    },
						    failure: function(result) {
						    	mask.hide();
						        Ext.Msg.alert('提示','网络出现故障，请稍后再试')
						    }
						});
            		});
            		
            		window.down('button[action=reset]').on('click',function(){
            			window.down('form').getForm().reset();
            		});
            		/* 点击按钮，打开上传图片窗口*/
            		window.query('button[name=select_logo]')[0].on('click', function () {
            			this.selectPic('上传企业LOGO','addphoto');
            		}, this);
            	}

            },
            'applywindow':{
            	afterrender:function(window){
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            window.down("button[action=submit]").on('click', function(){
            			var form = window.down('form').getForm();            			
            			if(form.isValid()){
            				var icRegNumber = form.findField('icRegNumber').getValue();
            				var ename = form.findField('name').getValue();
//            				// STEP1 验证营业注册合法
//            				if(!isValidBusCode(icRegNumber)) {
//            					Ext.Msg.alert('提示', '组织机构号不合法！');
//            					return;
//            				}
            				mask.show();
            				if(PlatSwitch){
            					// STEP2 验证营业注册是否存在            				
	            				Ext.data.JsonP.request({
									url: PlatUrl + 'public/validateIcRegNumber',
									timeout: 300000,
									params : {icRegNumber : icRegNumber, eid : form.findField('enterprise.id').getValue()},
									callbackKey: "callback",
									success: function(result) {	
										if(!result.success){
		            						mask.hide();
			    							Ext.Msg.alert('提示', result.message);
			    							return;
		            					}else {
		            						// STEP3  验证企业实名是否存在
		            						Ext.data.JsonP.request({
												url: PlatUrl + 'public/validateEname',
												timeout: 300000,
												params : {name : ename, eid : form.findField('enterprise.id').getValue()},
												callbackKey: "callback",
												success: function(result) {	
													if(!result.success){
		            									mask.hide();
			    										Ext.Msg.alert('提示', result.message);
			    										return;
		            								}else {
		            									// STEP4  提交表单
				            							form.submit({
								                    		url: 'ucenter/apply',
								                    		success: function(fp, o) {
								                    		mask.hide();
								                        	if(o.success){
								                        		Ext.Msg.alert('提示', '提交申请成功！');
								                        		window.close();
								                        	}
								                    	},
								                    	failure : function(form, action){
									                    	mask.hide();
									                    	Ext.Msg.alert('提示', action.result.message);
								                        	window.close();
								                    	}
								                		});
		            								}	            					
												},
												failure: function(result) {
										 			Ext.Msg.alert('提示',"连接枢纽服务器失败，请联系管理员");
												}
											});
		            					}	            					
									},
									failure: function(result) {
										 Ext.Msg.alert('提示',"连接枢纽服务器失败，请联系管理员");
									}
								});
            				}else {
            					// STEP2 验证营业注册是否存在
	            				Ext.Ajax.request({
	            					url : 'ucenter/validateIcRegNumber',
	            					params : {icRegNumber : icRegNumber, eid : form.findField('enterprise.id').getValue()},
		            				success: function(response){
		            					var result = eval("("+response.responseText+")");
		            					if(!result.success){
		            						mask.hide();
			    							Ext.Msg.alert('提示', result.message);
			    							return;
		            					}
		            					// STEP2 验证公司实名是否存在
		            					Ext.Ajax.request({
			            					url : 'ucenter/validateEname',
			            					params : {name : ename, eid : form.findField('enterprise.id').getValue()},
				            				success: function(response){
				            					var result = eval("("+response.responseText+")");
				            					if(!result.success){
				            						mask.hide();
					    							Ext.Msg.alert('提示', result.message);
					    							return;
				            					}
				            					// STEP4  提交表单
				            					form.submit({
								                    url: 'ucenter/apply',
								                    success: function(fp, o) {
								                    	mask.hide();
								                        if(o.success){
								                        	Ext.Msg.alert('提示', '提交申请成功！');
								                        	window.close();
								                        }
								                    },
								                    failure : function(form, action){
								                    	mask.hide();
								                    	Ext.Msg.alert('提示', action.result.message);
							                        	window.close();
								                    }
								                });
										    }
			            				});
								    }
	            				});
            				}
            				
			            }
            		});
            		/* 点击按钮，打开上传图片窗口*/
            		window.query('button[name=select_licenceDuplicate]')[0].on('click', function () {
            			this.selectPic('工商营业执照','licenceDuplicate');
            		}, this);
            		window.query('button[name=select_businessLetter]')[0].on('click', function () {
            			this.selectPic('企业公函','businessLetter');
            		}, this);
            	}
            },
             'applypersonalwindow':{
            	afterrender:function(window){
            		var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
	            	window.down("button[action=submit]").on('click', function(){
	            		var mask = new Ext.LoadMask(window.getEl(), {
							msg : '提交中,请稍等...',
							constrain : true
						});
						mask.show();
						window.down('form').getForm().submit({	//提交表单
							clientValidation: true,
							url: 'ucenter/applyPersonal',
							submitEmptyText: false,							
							success: function(form, action) {
								mask.hide();
								if(action.result.success){
//									if(action.result.message=="该用户名已存在!"){
//										Ext.Msg.alert('','<p align="center">该用户名已存在!</p>');
//									}else {
										window.hide();							       
										Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
//										store.reload();	
//									}
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
	            	/* 点击按钮，打开上传图片窗口*/
            		window.query('button[name=select_personalPhoto]')[0].on('click', function () {
            			this.selectPic('个人近照','personalPhoto');
            		}, this);
            		window.query('button[name=select_idCardPhoto]')[0].on('click', function () {
            			this.selectPic('身份证附件','idCardPhoto');
            		}, this);
            	}
            },            
            "uploadwindow[name=addphoto]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'addphoto');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			"uploadwindow[name=editphoto]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'editphoto');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
            "uploadwindow[name=licenceDuplicate]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'licenceDuplicate');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			"uploadwindow[name=businessLetter]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'businessLetter');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			"uploadwindow[name=personalPhoto]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'personalPhoto');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			"uploadwindow[name=idCardPhoto]" : {
				afterrender : function (window, opts) {
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '上传中,请稍等...',
						constrain : true
					});
					window.down('button[name=submit]').on('click', function () {
            			this.uplodImage(mask, 'idCardPhoto');
            		}, this);
            		window.down('button[name=cancel]').on('click', function () {
            			window.hide();
            		}, this);
				}
			},
			'organgrid':{
				afterrender : function (grid) {
					var me =this;
					grid.getStore().load();
					grid.down('button[action=add]').on('click', function () {
						if(grid.getStore().getCount()<=9){
							me.openAddOrgan('添加top机构',null);
						}else{
							Ext.Msg.alert('提示', '只能添加十条记录！');
						}
            		}, this);
            			
				},
				// 双击显示该类别下的服务
				itemdblclick : function(grid, record) {
					this.openAddOrgan('修改top机构',record);
				}
				
			},
			'organwindow':{
				afterrender:function(window){
					var me = this;
					window.down('combo[name=sort]').on('select',function (){
					var sort = window.down('combo[name=sort]').getValue();
					var store =  Ext.ComponentQuery.query('organgrid')[0].getStore();
						var result=true;
						Ext.each(store.getRange(), function (organ, index, countriesItSelf) {
							if(organ.data.sort==sort){
								Ext.Msg.alert('提示', '该顺序已有相应的服务机构，请选择其它顺序！');
								result = false;
							}
						});	
						if(result ==true){
							window.down('button[action=addOrgan]').setDisabled(false);
						}else{
							window.down('button[action=addOrgan]').setDisabled(true);
						}
					},this);
					window.down('combo[name=enterId]').on('select',function (){
						var id = window.down('combo[name=enterId]').getValue();
						//获取表格一列的值
						window.down('textfield[name=enterprise.id]').setValue(id);
						var store =  Ext.ComponentQuery.query('organgrid')[0].getStore();
						var result=true;
						Ext.each(store.getRange(), function (item, index, countriesItSelf) {
							if(item.data.enterId==id){
								Ext.Msg.alert('提示', '该服务机构已经添加到top10，请选择其它机构！');
								result = false;
							}
						});
						if(result ==true){
							window.down('button[action=addOrgan]').setDisabled(false);
						}else{
							window.down('button[action=addOrgan]').setDisabled(true);
						}
					},this);
					window.down('button[action=addOrgan]').on('click',function (){
						window.getComponent('organform').form.submit({
				    		clientValidation: true,
						    url: 'enter/addOrgan',
						    params: {
						        action: 'save'						        
						    },
						    success: function(form, action) {
						    	if(action.result.success){
							       window.hide();							       
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							        Ext.ComponentQuery.query('organgrid')[0].getStore().load();
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
			},
			'personalusereditwindow':{	//个人用户编辑窗口
            	afterrender:function(window){
            		var me = this;
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});
            		window.down('button[action=resetPass]').on('click',function(){
            			window.down('textfield[name=password]').setValue(123456);
            			window.down('hiddenfield[name=isreset]').setValue('true');
            			Ext.Msg.alert('提示','密码重置成功,请提交');
            		});
            		window.down('button[action=submit]').on('click',function(){            			
            			var userStatus = window.down('hiddenfield[name=userStatus]').getValue();
            			var store = userStatus == 1?me.getEnterusergrid().getStore():me.getDisableusergrid().getStore();
            			mask.show();
            			window.down('form').getForm().submit({
				    		clientValidation: true,
						    url: 'user/edit',
						    params: {
						        action: 'edit'
						    },
						    success: function(form, action) {
						    	mask.hide();
						    	if(action.result.success){
							       window.hide();							       
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');							       
							       store.reload();
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
				    	})
            		});
            		window.down('button[action=reset]').on('click',function(){            			
            			window.hide();
            		});
            	}
            },
			'personaluseraddwindow':{	//添加个人用户窗口
            	afterrender:function(window){
            		var me = this;
					var mask = new Ext.LoadMask(window.getEl(), {
						msg : '提交中,请稍等...',
						constrain : true
					});         		
            		window.down('button[action=submit]').on('click',function(){
            			mask.show();
            			window.down('form').getForm().submit({
				    		clientValidation: true,
						    url: 'user/addPersonal',
						    params: {
						        action: 'add'					        
						    },
						    success: function(form, action) {
						    	mask.hide();
						    	if(action.result.success){
							       window.hide();							       
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							       me.getEnterusergrid().getStore().reload();							       						       
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
				    	})
            		});
            		window.down('button[action=cancel]').on('click',function(){
            			window.down('form').getForm().reset();
            			window.hide();
            		});
            	}

            }
        })    
    },
    openAddOrgan:function(title,record){
    	var organWindow = Ext.ComponentQuery.query('organwindow')[0];
    	if(!organWindow){
			organWindow = Ext.widget('organwindow');    		
		}
		organWindow.setTitle(title);
		organWindow.show();
		var organform = organWindow.getComponent('organform').form;
		organform.reset();
		if(null!=record){
			organform.loadRecord(record);
		}
    },
    openApplyWin : function(record, applyType){
		var window = this.getApplywindow();
		if(!window){
			window = Ext.widget('applywindow');    		
		}
		window.setTitle(applyType == 1 ? '添加实名认证申请' : '添加服务机构申请');
		var form = window.down('form').form; 
		form.reset();
		form.findField('user.id').setValue(record.data.id);
		form.findField('user.userName').setValue(record.data.userName);
		form.findField('enterprise.id').setValue(record.data['enterprise.id']);
		form.findField('enterprise.forShort').setValue(record.data['enterprise.forShort']);
		form.findField('applyType').setValue(applyType);
		window.show();
	},
	openApplyPersonalWin : function(record){
		var window = this.getApplypersonalwindow();
		if(!window){
			window = Ext.widget('applypersonalwindow');    		
		}
		window.setTitle('添加个人实名认证申请');
		var form = window.down('form').form; 
		form.reset();		
		form.findField('user.id').setValue(record.data.id);
		form.findField('user.userName').setValue(record.data.userName);
//		form.findField('enterprise.id').setValue(record.data['enterprise.id']);
//		form.findField('enterprise.forShort').setValue(record.data['enterprise.forShort']);
//		form.findField('applyType').setValue(applyType);
		window.show();
	},
    selectPic : function (title, name) {
		var picWindow = Ext.ComponentQuery.query('uploadwindow[name='+ name +']')[0];
    	if (!picWindow) {
    		Ext.widget('uploadwindow',{
    			title : title,
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
		if (name == 'licenceDuplicate' || name == 'businessLetter') {
			window = this.getApplywindow();
		}else if(name =='addphoto'){
			window = this.getUseraddwindow();
		}else if(name =='editphoto'){
			window = this.getUsereditwindow();
		}else if(name == 'personalPhoto' || name == 'idCardPhoto'){
			window = this.getApplypersonalwindow();
		}
		mask.show();
		Eform.getForm().submit({
			url : 'public/uploadFile',
			clientValidation: true,
		    success: function(form, action) {
		    	if (action.result.success) {
			       var addForm = window.query('form')[0];
			       var Epicture = addForm.down('textfield[name='+name+']');
			       if (name == 'addphoto' || name == 'editphoto') {
			       	 	Epicture = addForm.down('textfield[name=enterprise.photo]');
			       }else if(name == 'personalPhoto'){
			       		Epicture = addForm.down('textfield[name=personalPhoto]');
			       }else if(name == 'idCardPhoto'){
			       		Epicture = addForm.down('textfield[name=idCardPhoto]');
			       }
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
	},
	addPersonalUser:function(){//添加个人用户window
    	var window = this.getPersonaluseraddwindow();
		if(!window){
	    	window = Ext.widget('personaluseraddwindow',{title:'添加个人用户'});    		
    	}
		window.down('form').getForm().reset();
		window.show();
    },	
	refreshStaff:function(){
    	this.getEnterstaffgrid().getStore().reload();
    },
    addStaff:function(record){ 	//添加子账号window
	    var addStaffWindows = this.getStaffaddwindow();
		if(!addStaffWindows){
			addStaffWindows = Ext.widget('staffaddwindow',{
		    title:'添加子账号'    			
		   });    		
		}
		var staffAddForm = addStaffWindows.getComponent('staffaddform');
		staffAddForm.down('combobox[name=roleName]').getStore().setProxy(
		{	type:'ajax',
			actionMethods: {  
				read: 'POST'
			},		
			extraParams:{enterpriseType:enterType},
			url:'staff/loadStaffRole'	
		});
		staffAddForm.down('combobox[name=roleName]').getStore().load();
		staffAddForm.form.reset();
		staffAddForm.down('displayfield[name=parentName]').setValue(parentName);
		staffAddForm.down('hiddenfield[name=parentId]').setValue(parentId);
		staffAddForm.down('hiddenfield[name=staffStatus]').setValue(1);
	
		addStaffWindows.show();		
    },
    showStaff:function(grid,record,index,eOpts){//显示子账号grid
    	userStatus = record.data.userStatus;	//得到主账号用户状态
    	var isPersonal = record.data.isPersonal;	//得到主账号是否为个人户
	    var staffgrid = this.getEnterstaffgrid();
	    staffgrid_g = this.getEnterstaffgrid();	//将全局的子账号表格设置以便主账号状态改变调用
	    staffgrid.setTitle('【'+record.data.userName+'】子账号列表');
	    /*****用户状态是禁用和删除时把子账号的添加按钮禁用*****/
	    if(userStatus == 2){
	    	staffgrid.down('button[action=add]').setDisabled(true);

	    }else if(userStatus == 3){
	    	staffgrid.down('button[action=add]').setDisabled(true);	
	    	
	    }else{
	    	staffgrid.down('button[action=add]').setDisabled(false);
	    	
	    }
	    /************************************/
	    if(isPersonal){//如果是个人用户无法拥有子账号
	    	Ext.example.msg('','<p align="center">账号【'+record.data.userName+'】属于个人账号,无法拥有子账号</p>');
			staffgrid.setVisible(false);
	    }
    	else{//友好企业用户账号
    		staffgrid.setVisible(true);
	    	staffgrid.getStore().setProxy({
	    		type:'ajax',
	    		actionMethods: {  
	        		read: 'POST'
	            },
	    		api:{
	    			read:'staff/query?parentId='+record.data.id,
	    			update:'staff/update'  
	    		},
	    		reader:{
	    			type:'json',
	    			root:'data',
	    			messageProperty:"message"  
	    		},
	    		writer:{  
				    type:"json",  
				    encode:true,  
				    root:"staff",  
				    allowSingle:true  
				}
	    	});
	    	staffgrid.getStore().load();
	    	parentId = record.data.id;
	    	parentName = record.data.userName;
	    	enterType = record.get("enterprise.type");//得到企业类型方面添加子窗体时调用	
    	}
    },
    showStaffDetail:function(record){//显示子账号详情form
    	var staffDetailWindows = this.getStaffdetailwindow();
    	if(!staffDetailWindows){
    		staffDetailWindows = Ext.widget('staffdetailwindow',{
    			title:'子账号详情【'+record.data.userName+'】'
    		});    		
    	}
		staffDetailWindows.show();
		staffDetailWindows.setTitle('子账号详情【'+record.data.userName+'】');
		staffDetailWindows.getComponent('staffdetailform').form.loadRecord(record);
		var managerId = staffDetailWindows.down('hiddenfield[name=manager.id]').getValue();
		if(managerId == 0){//分配人不是平台人员
			staffDetailWindows.down('displayfield[name=fpname]').setValue(record.get('assigner.userName'));
		}else{
			staffDetailWindows.down('displayfield[name=fpname]').setValue(record.get('manager.username'));
		}
    }
});

function submitForm(window,store,act){
	var mask = new Ext.LoadMask(window.getEl(), {
		msg : '提交中,请稍等...',
		constrain : true
	});
	mask.show();
	window.down('form').getForm().submit({	//提交表单
		clientValidation: true,
		url: 'user/'+act,
		submitEmptyText: false,
		params: {
			action: act				        
		},
		success: function(form, action) {
			mask.hide();
			if(action.result.success){
				if(action.result.message=="该用户名已存在!"){
					Ext.Msg.alert('','<p align="center">该用户名已存在!</p>');
				}else {
					window.hide();							       
					Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
					store.reload();	
				}
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
					Ext.Msg.alert('提示', '账号添加失败');
			}
		}
	});
};

/**
 * 验证营业执照是否合法：营业执照长度须为15位数字，前14位为顺序码， 最后一位为根据GB/T 17710 1999(ISO
 * 7064:1993)的混合系统校验位生成算法 计算得出。此方法即是根据此算法来验证最后一位校验位是否政正确。如果
 * 最后一位校验位不正确，则认为此营业执照号不正确(不符合编码规则)。 以下说明来自于网络:
 * 我国现行的营业执照上的注册号都是15位的，不存在13位的，从07年开始国 家进行了全面的注册号升级就全部都是15位的了，如果你看见的是13位的注
 * 册号那肯定是假的。 15位数字的含义，代码结构工商注册号由14位数字本体码和1位数字校验码
 * 组成，其中本体码从左至右依次为：6位首次登记机关码、8位顺序码。 一、前六位代表的是工商行政管理机关的代码，国家工商行政管理总局用
 * “100000”表示，省级、地市级、区县级登记机关代码分别使用6位行 政区划代码表示。设立在经济技术开发区、高新技术开发区和保税区
 * 的工商行政管理机关（县级或县级以上）或者各类专业分局应由批准 设立的上级机关统一赋予工商行政管理机关代码，并报国家工商行政 管理总局信息化管理部门备案。
 * 二、顺序码是7-14位，顺序码指工商行政管理机关在其管辖范围内按照先 后次序为申请登记注册的市场主体所分配的顺序号。为了便于管理和
 * 赋码，8位顺序码中的第1位（自左至右）采用以下分配规则： 1）内资各类企业使用“0”、“1”、“2”、“3”； 2）外资企业使用“4”、“5”；
 * 3）个体工商户使用“6”、“7”、“8”、“9”。 顺序码是系统根据企业性质情况自动生成的。 三、校验码是最后一位，校验码用于检验本体码的正确性
 */
function isValidBusCode(busCode) {
	var ret = false;
	var sum = 0;
	var s = [];
	var p = [];
	var a = [];
	var m = 10;
	p[0] = m;
	for (var i = 0; i < busCode.length; i++) {
		a[i] = parseInt(busCode.substring(i, i + 1), m);
		s[i] = (p[i] % (m + 1)) + a[i];
		if (0 == s[i] % m) {
			p[i + 1] = 10 * 2;
		} else {
			p[i + 1] = (s[i] % m) * 2;
		}
	}
	if (1 == (s[14] % m)) {
		//组织机构号正确!
		ret = true;
	} else {
		//组织机构号错误!
		ret = false;
	}
	return ret;
}