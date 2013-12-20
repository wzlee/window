/**
 * xuwf statistics服务数据统计
 */
Ext.define('plat.controller.service.StatisticsController', {
    extend: 'Ext.app.Controller',
    
    alias:'widget.statisticscontroller',
    stores: [
				'service.SerStore'
			],
    views: [
    			'service.StatisticsGrid',
    			'service.StatisticsPanel',
    			'layout.combo.CategoryCombo'
    		],
    refs: [
    		{
    			ref:'statisticspanel',
    			selector:'statisticspanel'
    		},
    		{
    			ref:'statisticsgrid',
    			selector:'statisticsgrid'
    		},{
    			ref:'categorycombo',
    			selector:'categorycombo'
    		}
    ],
    init: function() {
        this.control({
        	'statisticspanel':{
            	afterrender:function(panel){
            		//console.log(panel.title + '渲染完毕...');
            	},
            	hide:function(){
            		//console.log('我被隐藏！');
            	}
            },
            'statisticsgrid':{
            	afterrender:function(gridpanel){
            		//console.log(gridpanel.title + '渲染完毕...');
            		gridpanel.getStore().on('beforeload',function(store,options){
        				Ext.apply(store.proxy.extraParams, {cid:gridpanel.down('combo[cid=cid]').getValue()});
                	},this);
            		gridpanel.down('button[action=search]').on('click',function(){
                		gridpanel.getStore().load();
					},this);
            	}
            },
            'categorycombo':{
            	afterrender:function(combo){
            		//console.log(combo.name+'渲染完毕...');
            	}
            }
        });
    },
    loadService:function(cid){
    	this.getServicegrid().getStore().load({params:{cid:cid}});
    }
});