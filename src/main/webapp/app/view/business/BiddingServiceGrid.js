/**
 * 标单管理 xuwf 2013-10-08
 */
Ext.apply(Ext.form.field.VTypes, {
        daterange1: function(val, field) {
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
            return true;
        }        
});
Ext.define('plat.view.business.BiddingServiceGrid',{
	extend:'Ext.grid.Panel',
	xtype:'biddingservicegrid',  
	title:'标单管理',
	id:'bdgl',
	closable:true,
    closeAction:'hide',
	columnLines:true,
	emptyText:'没有找到相关的数据,请检查输出条件是否存在......',
    store:'business.BiddingServiceStore',
    cls:'font-size:14px',
    tbar:[{
        xtype: 'buttongroup',
        columns: 3,
        title: '',
        items: [
        	{
    				xtype:'triggerfield',
    				width:200,
    				name:'bidNo',
    				fieldLabel:'招标单号',
    				emptyText:'请输入招标单号...',
    				labelWidth:60,
    				labelStyle:'font-size:13',
    				triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
    				onTriggerClick:function(){
						this.reset();
					}
    		},
        	{
				xtype:'triggerfield',
				triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
				width:200,
				name:'name',
				fieldLabel:'服务名称',
				labelWidth:60,
				emptyText:'请输入服务名称...',
				labelStyle:'font-size:13',
    			onTriggerClick:function(){
					this.reset();
				}
			},
			{
    				xtype:'biddingservicestatus',
    				width:200,
    				fieldLabel:'状态',
    				name:'status',
    				labelWidth:30,
    				forceSelection:true,
    				labelStyle:'font-size:13',
    				multiSelect:true,
    				emptyText:'请选择标单状态...'
    		},
			{
//				dateRange:{begin:'beginDate1',end:'endDate1'}, //用于VType类型dateRange
				xtype:'datefield',
				name:'startdt',
				width:200,
				labelWidth:60,
				emptyText:'请选中起始时间...',
				fieldLabel:'下单时间',
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
//	features: [{
//		ftype:'grouping',
//		name:'group',
//		id:'group',
//		groupByText:'按属性分组',
//		showGroupsText:'显示分组',
//		startCollapsed:false,
//		groupHeaderTpl: '<font color="green">{name}</font> : 共[ <font color="green">{rows.length}</font> ]个招标单'
//	}],
    initComponent: function() {
      	var me = this;
        Ext.apply(this, {
			columns: [
						{xtype:'rownumberer',text:'#',align:'center',width:30},
				        { text: '招标单ID',align:'center', dataIndex: 'id',hidden:true},
				        { text: '招标单号',align:'center',width:140, dataIndex: 'bidNo',
				        	renderer:function(value,metaData,record){                	
                				return '<a href="javascript:void(0);">' + value + '</a>';              
				        	}
				        },			    
				        { text: '服务类别ID',align:'center', dataIndex: 'category.id',hidden:true},
				        { text: '服务类别',align:'center',width:130, dataIndex: 'category.text'}, 
				       	{ text: '服务名称',align:'center',width:150, dataIndex: 'name'},
				       	{ text: '价格',align:'center',width:100, dataIndex: 'minPrice',
				       		renderer:function(value,metaData,record){
				       			if(record.data.minPrice && record.data.maxPrice){
				       				return record.data.minPrice+'-'+record.data.maxPrice;
				       			}else if(!record.data.minPrice){
				       				return record.data.maxPrice;
				       			}else if(!record.data.maxPrice){
				       				return record.data.minPrice;
				       			}else if(!record.data.minPrice && !record.data.maxPrice){
				       				return 0;
				       			}
				       		}
				       	}, { text: '创建时间',align:'center',width:128, dataIndex: 'createTime'},
				        {
				        	xtype:'actioncolumn',
				        	text:'操作',
				        	align:'center',
				            sortable:false,
				            width:50,
				            items:[
				            	{
					               	iconCls:'icon-up',
					                tooltip: '取消招标',
					                handler: function(grid, rowIndex, colIndex, node, e, record, rowEl){
					                	var status = grid.getStore().getAt(rowIndex).data.status;
					                	//'待发布，平台退回，招标中，交易进行中，等待招标方关闭，等待应标放关闭'进入其他处理窗口
					                	if(status == 1 || status == 3 || status == 4){
					                		this.fireEvent('otherclick', this, grid, rowIndex, colIndex, node, e, record, rowEl);	
					                	}
			                		},
					                isDisabled:function(grid, rowIndex, colIndex){
					                	var status = grid.getStore().getAt(rowIndex).data.status;
					                	if(status == 1 || status == 3 || status == 4){
					                		return false;
					                	}
					                	return true;
					                }
					            }
					    	]
				        },
				       	{ text: '附件',align:'center',width:120,dataIndex: 'attachment',
				       		renderer:function(value,metaData,record){    
				       			if(value){
				       				var fnames = value.split(',');
									value = [];
									for(var i=0;i<fnames.length;i++){
										 var aname = '附件' + (i+1);
										 value[i] = '<a target="_blank" href="upload/files/'+fnames[i]+'">' + aname + '</a>';
									}
									value = value.join(' ');
				       			}
                				return value;              
				        	}
				       	},
//				       	{ text: '描述',align:'center',width:100,dataIndex: 'description'},
				       	{ text: '联系人',align:'center',width:80,dataIndex: 'linkMan'},
				        { text: '状态',align:'center',width:93, dataIndex: 'status',
				        	renderer:function(v){
				        		return PlatMap.BiddingService.status[v];
				        	}
				        }
		    		],
		    		
	    	dockedItems :[{
					        xtype: 'pagingtoolbar',
					        store: 'business.BiddingServiceStore',  
					        dock: 'bottom',
					        displayInfo: true,
					        displayMsg:"显示第{0}条到第{1}条,共{2}条数据",      
       						emptyMsg:"查找不到任何数据"
					    }]
        });
        this.callParent();
    }
});