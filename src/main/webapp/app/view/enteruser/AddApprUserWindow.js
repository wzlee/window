Ext.define('plat.view.enteruser.AddApprUserWindow', {
    extend: 'Ext.window.Window',
	xtype:'addappruserwindow',
	
    width: 300,
    height: 160,
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
		            	id:'appruserform',
		            	layout: {
					        type: 'column'
					    },
					    defaultType: 'textfield',
					    bodyPadding:'10',
					    items:[{
			                	fieldLabel : '会员名',	
			                	name : 'user.userName'
			                },{
			                	fieldLabel : '密码',	
			                	name : 'user.password'
			                },{
			                	fieldLabel : '密码',	
			                	name : 'user.password'
			                }]
		            	}
            		],
			buttons:[
				{
					text:'确定',
					action:'submit'
				},
				{
					text:'取消',
					action:'esc'
				}
			]
        });
        me.callParent(arguments);
    }
});