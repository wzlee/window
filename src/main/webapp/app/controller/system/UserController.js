Ext.define('plat.controller.system.UserController', {
	extend: 'Ext.app.Controller',
	alias:'widget.user',
	
	stores:['system.UserStore'],
    views: [
    		'system.UserGridView',
    		'layout.combo.SexCombo',
    		'layout.combo.GroupCombo',
    		'layout.combo.UserStatus'
    		],//声明该控制层要用到的view
    refs: [//相当于一个映射,这样就可以在控制层方便的通过geter取得相应的对象了
	        {
	            ref: 'usergrid',
	            selector: 'usergrid'
	        }
    ],
    init:function(){
    	this.control({
    		'usergrid': {
                afterrender:function(gridpanel){
                	//console.log(gridpanel.title + '渲染完毕...');
                	Ext.getBody().unmask();
        			gridpanel.getStore().on('beforeload',function(store,options){
//        				//console.log(gridpanel.getDockedItems());
                		Ext.apply(store.proxy.extraParams, gridpanel.down('textfield[name=nickname]').getSubmitData());
                	},this);
//                	gridpanel.getStore().on('groupchange',function(store,groupers){
//        				var grouped = store.isGrouped(),
//	                    groupBy = groupers.items[0] ? groupers.items[0].property : '',
//	                    toggleMenuItems, len, i = 0;
//		                // Clear grouping button only valid if the store is grouped
//		                gridpanel.down('[action=ungroup]').setDisabled(!grouped);
//		                // Sync state of group toggle checkboxes
////		                if (grouped && groupBy === 'groupname') {
////		                    toggleMenuItems = gridpanel.down('button[text=Toggle groups...]').menu.items.items;
////		                    for (len = toggleMenuItems.length; i < len; i++) {
////		                        toggleMenuItems[i].setChecked(groupingFeature.isExpanded(toggleMenuItems[i].text));
////		                    }
////		                    gridpanel.down('[text=Toggle groups...]').enable();
////		                } else {
////		                    gridpanel.down('[text=Toggle groups...]').disable();
////		                }
//                	},this);
                	gridpanel.getSelectionModel().on('selectionchange', function(selModel, selections){
				        gridpanel.down('button[action=delete]').setDisabled(selections.length === 0);
				    });
        			gridpanel.down('textfield[name=nickname]').on('specialkey',function(field,e){
        				if (e.getKey() == e.ENTER) {
                       		gridpanel.getStore().load();
	                    }
					},this);
                	gridpanel.down('button[action=search]').on('click',function(){
                		gridpanel.getStore().load();
					},this);
					gridpanel.down('button[action=add]').on('click',function(){
//						gridpanel.getStore().suspendAutoSync();
//						gridpanel.getStore().resumeAutoSync();
						gridpanel.getStore().insert(0,{username:'user',password:'123456',realname:'用户',sex:'男',registerDate:Ext.Date.format(new Date(),'Y-m-d H:i:s'),groupname:'普通用户组',online:false,status:'未激活'});
						gridpanel.getPlugin('cellEditing').startEditByPosition({row:0,column:1});
					},this);
					gridpanel.down('button[action=delete]').on('click',function(){
						gridpanel.getStore().remove(gridpanel.getSelectionModel().getSelection());
//						gridpanel.getStore().sync();
					},this);
					gridpanel.down('button[action=sync]').on('click',function(){
						var newRecords = gridpanel.getStore().getNewRecords().length;
                		var updateRecords = gridpanel.getStore().getUpdatedRecords().length;
                		var removeRecords = gridpanel.getStore().getRemovedRecords().length;
                		if(newRecords > 0 || updateRecords > 0 || removeRecords > 0){
							gridpanel.getEl().mask("数据同步中,请稍候...");
							gridpanel.getStore().sync({
								success:function(batch,options){
									gridpanel.getEl().unmask();
									Ext.example.msg('','数据同步成功!');
								},
								failure:function(batch,options){
									gridpanel.getEl().unmask();
									Ext.example.msg("","同步失败,请稍后重新同步!");
								}
							});
                		}else{
                			Ext.example.msg("","没有数据被修改或是删除,同步取消!");
                		}
					},this);
//					gridpanel.down('button[action=import]').on('click',function(){
//                		Ext.example.msg("","功能建设中,敬请期待...");
//					},this);
//					gridpanel.down('button[action=export]').on('click',function(){
//                		Ext.example.msg("","功能建设中,敬请期待...");
//					},this);
                },
                beforeclose:function(panel){
					var newRecords = panel.getStore().getNewRecords().length;
                	var updateRecords = panel.getStore().getUpdatedRecords().length;
                	if(newRecords > 0 || updateRecords > 0){
                		Ext.example.msg('','<font color="red">有修改的数据未处理,请同步数据或取消编辑!</font>');
	                	return false;               
                	}
                },
                beforehide:function(gridpanel){
            		//console.log(gridpanel.title + "将会隐藏!");
            	},
            	destroy:function(grid){
            		//console.log(grid.title + "已被销毁...");
            	}
            }
    	});
    },
    loadCookies:function(loginform){
    	if(Ext.util.Cookies.get("user")){
    		loginform.down('textfield[name=user.username]').setValue(Ext.util.Cookies.get("user").split(',')[0]);
			loginform.down('textfield[name=user.password]').setValue(Ext.util.Cookies.get("user").split(',')[1]);
    	}else if(Ext.util.Cookies.get("plat_username")){
    		loginform.down('textfield[name=user.username]').setValue(Ext.util.Cookies.get("plat_username"));
    	}else{
    		return;
    	}
    }
});