Ext.define('plat.view.enteruser.OrganWindow', {
    extend: 'Ext.window.Window',
	xtype:'organwindow',
	
    width: 300,
    height: 200,
	layout:'fit',
	closeAction : 'hide',
	buttonAlign:'center',
	modal : true,
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
		            {	
		            	xtype:'form',
		            	id:'organform',
		            	layout: {
					        type: 'column'
					    },
					    defaults:{
					        labelWidth:61,
					        labelAlign:'right',
					        msgTarget: 'side',
					        margin:'5'
					    },
					    bodyPadding:'10',
					    items:[{
				                    xtype: 'textfield',
				                    width: 300,
				                    name:'id',
				                    hidden:true,
				                    submitValue:true
				                },
				                {
				                    xtype: 'textfield',
				                    width: 300,
				                    name:'enterprise.id',
				                    hidden:true,
				                    submitValue:true
				                },{
									xtype : 'combo',
									name : 'sort',
									allowBlank : false,
									afterLabelTextTpl : required,
									width : 150,
									store : [[1, '1'], [2, '2'], [3, '3'],
											[4, '4'], [5, '5'], [6, '6'],
											[7, '7'], [8, '8'], [9, '9'],
											[10, '10']],
									rootVisible : false,
									editable : false,
									emptyText : '请选择显示顺序...',
									fieldLabel : '显示顺序'
								},{
									xtype : 'combo',
									name : 'enterId',
									fieldLabel : '对应机构',
									afterLabelTextTpl: required,
									queryMode: 'local',
									store : Ext.create('Ext.data.JsonStore', {
										fields : ['id','name'],
										autoLoad : true,
										proxy : {
											type : 'ajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'enter/findTopEnter',
											reader : {
												type : 'json',
												root : 'data',
												successProperty : 'success'
											}
										}
									}),
									displayField:'name',
									valueField:'id',
									typeAhead : false,
									emptyText:'请选择机构...',
									width: 250,
									anchor : '100%',
									listConfig : {
										loadingText : 'Searching...'
								}
							}]
		            	}
            		],
			buttons:[
				{
					text:'确定',
					action:'addOrgan'
				},
				{
					text:'取消',
					scope: this,
	                handler: this.close
				}
			]
        });
        me.callParent(arguments);
    }
});