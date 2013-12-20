Ext.define('plat.view.enteruser.EnterUserGrid',{
	extend:'Ext.grid.Panel',
	xtype:'enterusergrid',
	id:'enterusergrid',
	//title:'主列表管理',
	height: 250,
    width: 400,
//    collapsible:true,
    animCollapse: false,
    columnLines:true,
    emptyText:'没有找到相关帐号,换个关键词再试试吧',
    store:'enteruser.EnterUserStore',   
    tbar :['-',
    			{xtype: 'triggerfield',name:'uName',width:200,triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
    				display:false,	//自定义变量判断triggercls是否display
    				emptyText: '请输入会员名...',    			
    				onTriggerClick:function(){
    					this.reset();
    					this.display = false;
	    				this.triggerCell.setDisplayed(false);
	    				this.setWidth(200);
    				}
    			},
    			{xtype: 'triggerfield',name:'eName',width:200,triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
    				display:false,	//自定义变量判断triggercls是否display
    				emptyText: '请输入企业全称...',    			
    				onTriggerClick:function(){
    					this.reset();
    					this.display = false;
	    				this.triggerCell.setDisplayed(false);
	    				this.setWidth(200);
    				}
    			},   
    			{
    				xtype: 'combo',
    				name:'enterpriseType',
    				width:200,
    				emptyText: '请选择用户类型...',
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
				{iconCls:'icon-search',text:'查找',action:'search'},'-',
				{iconCls:'icon-add',text:'添加',action:'add'}
//				{iconCls:'icon-add',text:'添加',action:'add',menu: [
//			        {text: '企业用户',name:'enterUser'},
//			        {text: '个人用户',name:'personalUser'}
//				]}
//				,
//				'-','选中主账号可以查看该用户的子账号'

	],
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            columns: [
            	new Ext.grid.column.RowNumberer({text:'#',align:'center',width:30}),                
                { text: '会员名',align:'center',width:80, dataIndex: 'userName',
                	renderer:function(value,metaData,record){
                		if(record.data.isPersonal) return '<a href="javascript:void(0);">'+value+'</a>';
                		return value;
                	}
                },
                { text: '企业简称',align:'center',width:80, dataIndex: 'enterprise.forShort'},
                { text: '企业全称',align:'center',width:80, dataIndex: 'enterprise.name',flex:1,renderer:function(value,metaData,record){   
                	if(record.data.isPersonal) return '暂无';
                	return '<a href="javascript:void(0);">' + value + '</a>';                	
                }},
                { text: '用户类型',align:'center',width:80, dataIndex: 'enterprise.type',renderer:function(value,metaData,record){
				    if(record.data.isPersonal) return '个人';
				    return PlatMap.Enterprises.type[value];
                }},                
                { text: '电子邮箱',align:'center',width:100, dataIndex: 'enterprise.email',flex:1,
	                renderer:function(value,metaData ,record){	                	
	                	if(value){
	                		return record.data.isApproved?value+'<img src="resources/images/drop-yes.gif" title="邮箱已验证" >':value+'<img title="邮箱未验证" src="resources/images/exclamation.png">';
	                	}
	                }
                },
                { text: '注册时间',align:'center',width:80, dataIndex: 'regTime'},
                //{ text: '关注领域',align:'center',width:80, dataIndex: 'attentionField',flex:1},
                { text: '数据来源',align:'center',width:80, dataIndex: 'sourceId',
                	renderer:function(value){                		
                		return PlatMap.User.sourceId[value];
                	}
                },
//            	{ text: '个人用户',align:'center',width:80, dataIndex: 'isPersonal',
//            		renderer:function(value,metaData ,record){     	
//	                	return record.data.isPersonal?'是':'否';
//	                }
//            	},                
                { text: '帐号状态',align:'center',width:80, dataIndex: 'userStatus',
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
                		}
                		return value;
                		//return PlatMap.Enterprises.userStatus[value];
	                }
	            },
	            {
		            xtype:'actioncolumn',
		            text:'认证申请',		            
		            align:'center',
		            sortable:false,	
		            width:70,
//		            width:90,
		            items: [		            	
		            		{
			                icon:'resources/images/applyForRealname.png',			                
			                tooltip: '申请实名',
			                hidden:true,
			                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl){
			                	
			                	this.fireEvent('realname', this, grid, rowIndex, colIndex, node, e, record, rowEl);	
			                },
			                isDisabled:function(grid, rowIndex, colIndex,item,record){			                	
			                	if(grid.getStore().getAt(rowIndex).data.userStatus != "01"){
			                		return true;
			                	}else if(grid.getStore().getAt(rowIndex).data.isPersonal){
			                		return true;			                			                		
			                	}else {
			                		return record.data['enterprise.type']!='1';		
			                	}
			                }			                
	    				},{
	    				},
	    				{
			                icon:'resources/images/applyForServiceOrg.png',			                
			                tooltip: '申请服务机构',			                
			                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl){
			                	this.fireEvent('serviceorg', this, grid, rowIndex, colIndex, node, e, record, rowEl);	
			                },
			                isDisabled:function(grid, rowIndex, colIndex,item,record){
			                	if(grid.getStore().getAt(rowIndex).data.userStatus != "01"){
			                		return true;
			                	}else if(grid.getStore().getAt(rowIndex).data.isPersonal){		   
			                		return true;
			                	}else {
			                		return (record.data['enterprise.type'] == 3)||(record.data['enterprise.type'] == 4);
			                	}
			                }
	    				}
//	    				,{
//	    				},
//	    				{
//			                icon:'resources/images/applyForPersonal.gif',			                
//			                tooltip: '申请个人实名',			                
//			                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl){
//			                	this.fireEvent('personal', this, grid, rowIndex, colIndex, node, e, record, rowEl);	
//			                },
//			                isDisabled:function(grid, rowIndex, colIndex,item,record){
//			                	if(grid.getStore().getAt(rowIndex).data.userStatus != "01" && grid.getStore().getAt(rowIndex).data.isPersonal){
//			                		return true;	                		
//			                	}else if(!grid.getStore().getAt(rowIndex).data.isPersonal){
//			                		return true;			                		
//			                	}else if(grid.getStore().getAt(rowIndex).data.certSign == 1){			                		
//			                		return true;
//			                	};
//			                }
//	    				}		 
			        ]
		        },		        
                {
		            xtype:'actioncolumn',
		            text:'编辑',		            
		            align:'center',
		            sortable:false,				            
		            width:50,
		            items: [
		            	{
			                icon:'resources/images/edit.png',
			                tooltip: '编辑用户',			                
			                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl){
			                	this.fireEvent('editclick', this, grid, rowIndex, colIndex, node, e, record, rowEl);	
			                },
			                isDisabled:function(grid, rowIndex, colIndex,item,record){
			                	//true disable 默认 false
			                	if(grid.getStore().getAt(rowIndex).data.userStatus != "01"){
			                		return true;
			                	}
//			                	else {
//			                		return record.data['enterprise.type']!='1';			                		
//			                	};
			                	
			                	
			                }
	    				}
			        ]
		        },
		        {
		            xtype:'actioncolumn',
		            text:'禁用',
		            align:'center',
		            sortable:false,				            
		            width:50,
		            items: [
		            	{
			                icon:'resources/images/drop-no.gif',
			                tooltip: '禁用用户',
			                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl) {
			                	this.fireEvent('disableclick', this, grid, rowIndex, colIndex, node, e, record, rowEl);			                    
			                },
			                isDisabled:function(grid, rowIndex, colIndex){
			                	return grid.getStore().getAt(rowIndex).data.userStatus != "01";
			                }
		            	}
			        ]
		        },
		        {
		            xtype:'actioncolumn',
		            text:'删除',
		            align:'center',
		            sortable:false,				            
		            width:50,
		            items: [
		            	{
			                iconCls:'icon-remove',
			                tooltip: '删除用户',
			                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl) {
			                	this.fireEvent('deleteclick', this, grid, rowIndex, colIndex, node, e, record, rowEl);			                    
			                },
			                isDisabled:function(grid, rowIndex, colIndex){
			                	return grid.getStore().getAt(rowIndex).data.userStatus == "03";
			                }
		            	}
			        ]
		        }
            ],
            
            dockedItems :[{
					        xtype: 'pagingtoolbar',
					        store: 'enteruser.EnterUserStore',
					        dock: 'bottom',
					        displayInfo: true
					    }]
       
        });
        
          //me.callParent(arguments);
        me.callParent();
    }
    		
});

