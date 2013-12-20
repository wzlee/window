Ext.define('plat.view.enteruser.ApplyPersonalWindow', {
	extend : 'Ext.window.Window',
	xtype : 'applypersonalwindow',

	width : 350,
	layout : 'fit',
	closeAction : 'hide',
	buttonAlign : 'center',
	modal : true,
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			items : [{
						xtype : 'form',
						id : 'applypersonalform',
						layout :'column',
						defaults : {
							labelWidth : 100,
							labelAlign : 'right',
							margin : '1'
						},
						bodyPadding : '10',
						items : [{
									xtype : 'hiddenfield',
									name : 'user.id'
								},{
									xtype : 'hiddenfield',
									name : 'applyType',
									value:1
								},{
									xtype : 'hiddenfield',
									name : 'approveStatus',
									value:2
								},{
									xtype : 'displayfield',
									name : 'user.userName',
									fieldLabel : '用户名'
								},
								{
									xtype : 'textfield',
									name : 'name',
									width : 300,
									fieldLabel : '真实姓名',
									allowBlank : false
									
								},{
				                	xtype: 'container',
							        anchor: '100%',
							        layout: 'hbox',
							        items : [{
							        	xtype:'textfield',
						                fieldLabel: '个人近照图片',
						                name: 'personalPhoto',
						                readOnly : true,
						                labelWidth : 100,
						                labelAlign:'right',
						                allowBlank : false,
						                flex : 1
							        }, {
							        	xtype : 'button',
							        	name : 'select_personalPhoto',
							        	icon : 'jsLib/extjs/resources/themes/icons/add1.png'
							        }]
				                },{
				                	xtype: 'container',
							        anchor: '100%',
							        layout: 'hbox',
							        items : [{
							        	xtype:'textfield',
						                fieldLabel: '身份证附件图片',
						                name: 'idCardPhoto',
						                readOnly : true,
						                labelWidth : 100,
						                labelAlign:'right',
						                allowBlank : false,
						                flex : 1
							        }, {
							        	xtype : 'button',
							        	name : 'select_idCardPhoto',
							        	icon : 'jsLib/extjs/resources/themes/icons/add1.png'
							        }]
				                }				                
				                ]
					}],
			buttons : [{
						text : '提交',
						action : 'submit'
					}, {
						text : '取消',
						action : 'esc',
						handler : function() {
							this.up('window').close();
						}
					}]
		});
		me.callParent(arguments);
	}
});