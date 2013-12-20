/**
 * 企业用户搜索 xuwf	2013-8-22
 */

Ext.apply(Ext.form.field.VTypes, {
        daterange: function(val, field) {
            var date = field.parseDate(val);
            if (!date) {
                return false;
            }
            if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
                var start = field.up('buttongroup').down('#' + field.startDateField);
                start.setMaxValue(date);
                start.validate();
                this.dateRangeMax = date;
            }
            else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
                var end = field.up('buttongroup').down('#' + field.endDateField);
                end.setMinValue(date);
                end.validate();
                this.dateRangeMin = date;
            }
            /*
             * Always return true since we're only using this vtype to set the
             * min/max allowed values (these are tested for after the vtype test)
             */
            return true;
        }        
});
Ext.define('plat.view.enteruser.QEnterUserGrid',{
	extend:'Ext.grid.Panel',
	xtype:'qenterusergrid',
	title:'用户列表',
	id:'yhssgl',
	closable:true,
	closeAction:'hide',
	columnLines:true,
	emptyText:'没有找到相关的数据,请检查输出条件是否存在......',
	store:'enteruser.QEnterUserStore',
	tbar:[{
        xtype: 'buttongroup',
        columns: 4,
        title: '',
        items: [
        	{
				xtype:'triggerfield',
				triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
				width:200,
				name:'username',
				fieldLabel:'会员名',
				labelWidth:60,
				emptyText:'输入会员名...',
				labelStyle:'font-size:13',
				onTriggerClick:function(){
					this.reset();
				}
			},{
				xtype:'triggerfield',
				triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
				width:200,	
				name:'entershort',
				fieldLabel:'企业简称',
				labelWidth:60,
				emptyText:'输入企业简称...',
				labelStyle:'font-size:13',
				onTriggerClick:function(){
					this.reset();
				}
			},{
				xtype:'triggerfield',
				triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
				width:200,
				name:'entername',
				fieldLabel:'企业全称',
				labelWidth:60,
				emptyText:'输入企业全称...',
				labelStyle:'font-size:13',
				onTriggerClick:function(){
					this.reset();
				}
			},
    		{
    				xtype: 'combo',
    				name:'enterpriseType',
    				width:200,
    				labelWidth:60,
    				emptyText: '请选择用户类型...',
    				fieldLabel:'用户类型',
    				queryMode: 'local',
				    displayField: 'text',
				    valueField: 'value',
				    editable : false,
				    store:  Ext.create('Ext.data.Store', {
				    fields: ['value', 'text'],
				    	data : [
					        {"value" : "", "text" : "全部"},
					        {"value" : 1, "text" : "普通企业"},
					        {"value" : 2, "text" : "认证企业"},
					        {"value" : 3, "text" : "服务机构"},
					        {"value" : 4, "text" : "政府机构"}
				 	]})
    		},    
			{
//				dateRange:{begin:'beginDate1',end:'endDate1'}, //用于VType类型dateRange
				xtype:'datefield',
				name:'startdt',
				width:200,
				labelWidth:60,
				emptyText:'请选中起始时间...',
				fieldLabel:'注册时间',
				labelStyle:'font-size:13',
				itemId: 'startdt',
		        vtype: 'daterange',
		        endDateField: 'enddt' // id of the end date field
			},
			{
				xtype:'datefield',
				name:'enddt',
				width:200,
				labelWidth:60,
				emptyText:'请选中结束时间...',
				fieldLabel:'结束时间',
				labelStyle:'font-size:13',
				itemId: 'enddt',
		        vtype: 'daterange',
		        startDateField: 'startdt' // id of the start date field
			},
        	{iconCls:'icon-search',text:'查找',action:'search'}
        ]
    }],
	initComponent: function() {
      	var me = this;    	
        Ext.apply(this, {	
			columns: [
						{xtype:'rownumberer',text:'#',align:'center',width:30},
				        { text: '企业ID',align:'center', dataIndex: 'id',hidden:true},
				        { text: '会员名',align:'center',width:80, dataIndex: 'userName'},
				        { text: '企业简称',align:'center',width:80, dataIndex: 'enterprise.forShort'},
				        { text: '企业全称',align:'center',width:130, dataIndex: 'enterprise.name',
				        	renderer:function(v,metaData,record){
					        	return '<a href="javascript:void(0)">' + v + '</a>';
				        	}
				        },
				        { text: '用户类型',align:'center',width:100, dataIndex: 'enterprise.type',
				        	renderer:function(v){
				        		return PlatMap.Enterprises.type[v];
				        	}
				        },
				        { text: '密码',align:'center',width:120, dataIndex: 'password', flex: 1,hidden:true},
					    { text: '电子邮箱',align:'center',width:170, dataIndex: 'enterprise.email',
					     	renderer:function(value,metaData ,record){
				               	if(record.get('enterprise.email')){
				                	return record.data.isApproved?value+'<img src="resources/images/drop-yes.gif" title="邮箱已验证">':value+'<img title="邮箱未验证" src="resources/images/exclamation.png">';
				                }
				            }
					    },
				        { text: '注册时间',align:'center',width:150, dataIndex: 'regTime'},
				        { text: '个人用户',align:'center',width:80, dataIndex: 'isPersonal',hidden:true,
				        	renderer:function(value,metaData,record){
				        		return record.data.isPersonal ? '是':'否';
				        	}
				        },
				      	{ text: '个人邮箱',align:'center', width:120,dataIndex: 'email',hidden:true},
				      	{ text: '数据来源',align:'center',width:100, dataIndex: 'sourceId',
				        	renderer:function(v){
					    		return v==1?'录入用户':'注册用户';
					    }},
				        { text: '账号状态',align:'center',width:80, dataIndex: 'userStatus',
				        	renderer:function(value){
	                			switch(value){
		                			case 1:
		                				value = '<font color="green">有效</font>';
		                				break;
		                			case 2:
		                				value = '<font color="gray">禁用</font>';
		                				break;
		                			case 3:
		                				value = '<font color="red">删除</font>';
		                				break;
		                			case 0:
		                				value = '<font color="purple">未设置</font>'
		                				break;
	                			}
                				return value;
	              			 }
				        },
					    { text: '邮箱是否已验证',align:'center',width:60, dataIndex: 'isApproved',hidden:true},
				        { text: '备注',align:'center',width:80, dataIndex: '备注',hidden:true}
				       
		    		],
		    		
	    	dockedItems :[{
					        xtype: 'pagingtoolbar',
					        store: 'enteruser.QEnterUserStore',  
					        dock: 'bottom',
					        displayInfo: true,
					        displayMsg:"显示第{0}条到第{1}条,共{2}条数据",      
       						emptyMsg:"查找不到任何数据"
					    }]
        });
        this.callParent();
    }
});