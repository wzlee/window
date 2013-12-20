/**
 * xuwf statistics服务数据统计
 */
Ext.define('plat.controller.service.QServiceController', {
    extend: 'Ext.app.Controller',
    
    alias:'widget.statisticscontroller',
    stores: [
				'service.QServiceStore'
			],
    views: [
    			'service.QServiceGrid',
    			'service.QServicePanel',
    			'service.ServiceWindow',
    			'layout.combo.ServiceStatus',
    			'layout.combo.ServiceClass'
    		],
    refs: [
    		{
    			ref:'qservicepanel',
    			selector:'qservicepanel'
    		},
    		{
    			ref:'servicewindow',
    			selector:'servicewindow'
    		},
    		{
    			ref:'qservicegrid',
    			selector:'qservicegrid'
    		},{
    			ref:'servicestatus',
    			selector:'servicestatus'
    		},{
    			ref:'serviceclass',
    			selector:'serviceclass'
    		}
    ],
    init: function() {
        this.control({
        	'qservicepanel':{
            	afterrender:function(panel){
            		//console.log(panel.title + '渲染完毕...');
            	},
            	hide:function(){
            		//console.log('我被隐藏！');
            	}
            },
           'qservicegrid':{   	
            	afterrender:function(gridpanel){
            		//console.log(gridpanel.title + '渲染完毕...');
            		gridpanel.getStore().on('beforeload',function(store,options){	
//        				//console.log('status的值为:'+gridpanel.down('combo[name=status]').getValue()==null ? "":gridpanel.down('combo[name=status]').getValue());
//        				//console.log('serviceClass的值为:'+gridpanel.down('combo[name=serviceClass]').getValue()===null ? 0:gridpanel.down('combo[name=serviceClass]').getValue());
                	},this);
            		gridpanel.down('button[action=search]').on('click',function(){
//						gridpanel.getStore().load({params:{status:gridpanel.down('combo[name=status]').getValue(),cid:gridpanel.down('combo[name=status]').getValue()}});
            			var Estore = gridpanel.getStore();		
            			Estore.getProxy().extraParams =
            				{
            					status:gridpanel.down('combo[name=status]').getValue()
            				}
            			Estore.load();
//            			gridpanel.getStore().load({params:{status:gridpanel.down('combo[name=status]').getValue()}});
					},this);
            	},
            	itemdblclick:function(grid, record, item, index, e, eOpts){
            		this.serviceDetail(record);
            	}
            },
            'servicewindow':{
            	afterrender:function(window){
            		var me = this;
            		window.down('button[action=submit]').on('click',function(){
            			window.getComponent('serviceform').form.submit({
				    		clientValidation: true,
						    url: 'service/add',
						    params: {
						        action: 'add'
						    },
						    success: function(form, action) {
						    	if(action.result.success){
							       Ext.example.msg('','<p align="center">'+action.result.message+'</p>');
							       window.hide();
							       me.refreshService();
						    	}else{
						    		Ext.Msg.alert('提示','<p align="center">'+action.result.message+'</p>');
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
						               Ext.Msg.alert('提示', action.result.msg);
						       }
						    }
				    	});
            		});
            	}
            }
        });
    },
    
    serviceDetail:function(record){
    	var serviceWindow;
    	var serviceWindows = Ext.ComponentQuery.query('servicewindow');
    	if(serviceWindows.length == 0){
    		serviceWindow = Ext.widget('servicewindow',{
    			title:'服务详情',
    			id:'servicewindow'
    		}).show();
    		serviceWindow.getComponent('serviceform').form.loadRecord(record);
    	}else{
    		serviceWindows[0].show();
    		serviceWindow.getComponent('serviceform').form.reset();
    	}
    }
});



