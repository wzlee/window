Ext.define('plat.view.enteruser.OrganGrid', {
    extend: 'Ext.grid.Panel',
    xtype: 'organgrid',
    height: 250,
    width: 400,
    title: '机构top10配置',
    id:'toporg',
	closeAction:'hide',
	closable:true,
	columnLines:true,
	store:'enteruser.OrganRecommendationStore',
    tbar :[
    		'-',
			{iconCls:'icon-add',text:'添加top机构',action:'add',name:'add'},'-'
	],
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            columns: [
            	{xtype:'rownumberer',text:'序号',align:'center',width:50},
    			{ text: 'id',align:'center', dataIndex: 'id',hidden:true},
    			{ text: '机构名称',align:'center', dataIndex: 'enterprise.name',width:300},
    			{ text: '机构id',align:'center', dataIndex: 'enterprise.id',width:300,hidden:true},
    			{ text: '排列顺序',align:'center', dataIndex: 'sort',width:200},
    			{ text: '添加时间',align:'center', dataIndex: 'addTime',width:300},
    			{
					text : '删除',
					width : 80,
					name:'deleteServiceAd',
					menuDisabled : true,
					xtype : 'actioncolumn',
					tooltip : '删除推荐服务',
					align : 'center',
					icon : 'resources/images/delete.png',
					handler : function(grid, rowIndex, colIndex) {
						var record = grid.getStore().getAt(rowIndex);
						Ext.MessageBox.confirm('警告', '删除后数据无法恢复,确定删除吗?',
							function(btn) {
								if (btn == 'yes') {
									grid.getStore().destroy({
										params : record.data,
										callback:function(record,operation){
			    						var result =Ext.JSON.decode(operation.response.responseText);								    						
			    						if(result.success){
											grid.getStore().load();
			    						}else {
			    							Ext.Msg.alert('提示','服务【'+operation.params.serviceName+'】申请下架失败');
			    						}
					    			}
								});
							}
						});
					}
				}
            ]
        });
        me.callParent(arguments);
    }
});