Ext.define('plat.view.cms.NewsGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.newsgrid',
	store : 'cms.NewsStore',
	title : '新闻管理',
	closable : true,
	closeAction : 'hide',
	columnLines : true,
	viewConfig: {
        stripeRows: true,
        enableTextSelection: true
    },
	id : 'xmzxgl',
	initComponent : function () {
		/*//console.log('NewsGrid was initialized!!!');*/
		Ext.apply(this, {
			columns : [new Ext.grid.column.RowNumberer({
					text : '#',
					align : 'center',
					width : 30
				}),
				{header : '标题', dataIndex : 'title', flex : 1, align : 'center'},
				{header : '发布人', dataIndex : 'author', width : 100, align : 'center' },
				{header : '添加时间', dataIndex : 'pubdate', width : 140, align : 'center'},
				{header : '来源', dataIndex : 'source', width : 187, align : 'center'}
				/*{header : '类别', dataIndex : 'cid', align : 'center', renderer : function (value) {
					switch (value) {
						case 1 : 
							value = "展会信息";
							break;
						case 2 : 
							value = "最新推荐";
							break;
						case 3 : 
							value = "政策法规";
							break;
						case 4 : 
							value = "最新公告";
							break;
						case 5 : 
							value = "新闻动态";
					}
					return value;
				}}*/, 
				{
					xtype : 'actioncolumn',
					text : '配图',
					align : 'center',
					sortable : false,
					menuDisabled : true,
					width : 50,
					items : [{
							icon : 'jsLib/extjs/resources/themes/icons/scan.png',
							tooltip : '查看新闻配图',
							handler : function(grid, rowIndex, colIndex, node,
									e, record, rowEl) {
								this.fireEvent('pictureclick', this, grid,
										rowIndex, colIndex, node, e, record,
										rowEl);
							}
					}]
				}, 
				{
			        xtype:'actioncolumn',
			        text:'修改',
			        align:'center',
			        menuDisabled : true,
			        width: 50,
			        items: [{
			            iconCls:'icon-edit',
			            tooltip: '修改',
			            handler: function(grid, rowIndex, colIndex, item, e, record, row) {
					    	var newsWindows = Ext.ComponentQuery.query('newswindow')[0];
					    	if (!newsWindows) {
					    		newsWindows = Ext.widget('newswindow',{
					    			title:'修改新闻'
					    		}).show();
					    	} else {
					    		newsWindows.setTitle('修改新闻');
					    		newsWindows.show();
					    	}
					    	var Eform = Ext.ComponentQuery.query('newsform')[0];
					    	if (Eform.getKindeditor()) {
								Eform.loadRecord(record);
								Eform.getKindeditor().html(record.data.content);
							} else {
								setTimeout(function () {
									Eform.loadRecord(record);
									Eform.getKindeditor().html(record.data.content);
								}, 1000);
							}
					    	
					    	var EoriginalPic = Eform.query('textfield[name=originalPic]')[0];
							if (record.data.picture) {	//当新闻图片存在时，隐藏输入框做标记
								EoriginalPic.setValue(record.data.picture);
							}
					    	newsWindows.down('button[name=modify]').show();
					    	newsWindows.down('button[name=add]').hide();
			            }
			       }]
		       }, {
			        xtype:'actioncolumn',
			        text:'删除',
			        align:'center',
			        menuDisabled : true,
			        width: 50,
			        items: [{
			            iconCls:'icon-remove',
			            tooltip: '删除',
			            handler: function(grid, rowIndex, colIndex) {
			                var record = grid.getStore().getAt(rowIndex);
			                Ext.MessageBox.confirm('警告','确定删除该新闻吗?',function(btn){
					    		if(btn == 'yes'){
					    			grid.getStore().remove(record);
					    		}
					    	});
			            }
			       }]
		       }]
		});
		this.callParent(arguments);
	},
	/*features: [{
		ftype:'grouping',
		groupHeaderTpl: [
		    '{columnName}: ',
		    '<span>{name:this.formatName}</span>',
		    {
		        formatName: function(name) {
		        	switch (name) {
						case 1 : 
							name = "展会信息";
							break;
						case 2 : 
							name = "最新推荐";
							break;
						case 3 : 
							name = "政策法规";
							break;
						case 4 : 
							name = "最新公告";
							break;
						case 5 : 
							name = "新闻动态";
					}
		            return name;
		        }
		    }
		]
	}],*/
	tbar : [{
		text : '新增',
		iconCls : 'icon-add',
		name : 'add'
	}, '-'/*, {
		text : '修改',
		iconCls : 'icon-edit',
		name : 'modify'
	}, '-', {
		text : '删除',
		iconCls : 'icon-remove',
		name : 'delete'
	}, '-'*/, {
		xtype : 'textfield',
		width : 150,
		name : 'title',
		emptyText : '输入标题进行搜索...'
	}, '-', {
		iconCls : 'icon-search',
		text : '查找',
		action : 'search'
	}, '-', '双击新闻可以进行编辑'],
	dockedItems: [{
        xtype: 'pagingtoolbar',
        store: 'cms.NewsStore',   // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true
    }]
});
