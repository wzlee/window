Ext.define('plat.view.service.TopThreeWindow', {
    extend: 'Ext.window.Window',
	xtype:'topthreewindow',
	
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
		            	id:'topform',
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
				                    name:'serviceid',
				                    hidden:true,
				                    submitValue:true
				                },
				                {
									xtype : 'combo',
									name : 'topthree',
									allowBlank : false,
									afterLabelTextTpl : required,
									width : 150,
									store : [[1, '1'], [2, '2'], [3, '3']],
									rootVisible : false,
									editable : false,
									emptyText : '请选择显示顺序...',
									fieldLabel : '显示顺序'
								},{
									xtype : 'combo',
									fieldLabel : '服务名称',
									afterLabelTextTpl: required,
									queryMode: 'local',
									store : Ext.create('Ext.data.JsonStore', {
										fields : ['id','serviceName'],
										autoLoad : true,
										proxy : {
											type : 'ajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'service/queryTopthree',
											reader : {
												type : 'json',
												root : 'data',
												successProperty : 'success'
											}
										}
									}),
									displayField:'serviceName',
									valueField:'id',
									typeAhead : false,
									emptyText:'请选择服务...',
									width: 250,
									anchor : '100%',
									listConfig : {
										loadingText : 'Searching...'
								},
								validator:function(){
									if(this.getValue()!=""&&null!=this.getValue()){
										if(this.getStore().find( 'id', this.getValue()) != -1){
											return true;
										}else{
											return "请填写存在的服务名称！";
										}
									}else{
										return "该项为必填项，请选择服务名称！";
									}
								}
							}]
		            	}
            		],
			buttons:[
				{
					text:'确定',
					action:'add'
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