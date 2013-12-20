Ext.define('plat.view.service.TopThreeGrid', {
    extend: 'Ext.grid.Panel',
    xtype: 'topthreegrid',
    height: 250,
    width: 400,
    title: '服务top3配置',
    id:'topthree',
	closeAction:'hide',
	closable:true,
	columnLines:true,
	store:'service.TopThreeStore',
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
    			{ text: 'serviceid',align:'center', dataIndex: 'serviceid',hidden:true},
    			{ text: '服务名称',align:'center', dataIndex: 'serviceName',width:300},
    			{ text: '排列顺序',align:'center', dataIndex: 'topthree',width:200},
    			{ text: '添加时间',align:'center', dataIndex: 'registerTime',width:300},
    			{
					text : '取消热点服务',
					width : 80,
					name:'deleteService',
					menuDisabled : true,
					xtype : 'actioncolumn',
					tooltip : '取消热点服务',
					align : 'center',
					icon : 'resources/images/delete.png',
					handler : function(grid, rowIndex, colIndex) {
						var record = grid.getStore().getAt(rowIndex);
						Ext.MessageBox.confirm('警告', '确定要取消该服务为热点服务么？',
							function(btn) {
								if (btn == 'yes') {
									Ext.Ajax.request({
									    type:'POST',
									    url: 'service/deletetop',
									    params:{'id':record.data.id},
									    success: function(response) {
									    	var result = Ext.decode(response.responseText);
										    	if(result.success){
										    		grid.getStore().load();
										    	}
									    },
									    failure: function(form, action) {
									    	Ext.example.msg('取消失败！',result.messgae);
									    }
									});
								}
							}
						);
					}
				}
            ]
        });
        me.callParent(arguments);
    }
});