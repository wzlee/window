Ext.define('plat.view.enteruser.ApplyWindow', {
	extend : 'Ext.window.Window',
	xtype : 'applywindow',

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
						id : 'applyform',
						layout : {
							type : 'column'
						},
						defaults : {
							labelWidth : 100,
							labelAlign : 'right',
							margin : '1'
						},
						bodyPadding : '10',
						items : [{
									xtype : 'hiddenfield',
									name : 'user.id'
								}, {
									xtype : 'hiddenfield',
									name : 'user.userName'
								}, {
									xtype : 'hiddenfield',
									name : 'enterprise.id'
								}, {
									xtype : 'hiddenfield',
									name : 'applyType',
									value : 1 // 默认 实名认证
								},{
									xtype : 'hiddenfield',
									name : 'approveStatus',
									value : 2 // 默认 未处理
								}, {
									xtype : 'displayfield',
									name : 'enterprise.forShort',
									fieldLabel : '企业简称'
								}, {
									xtype : 'textfield',
									name : 'icRegNumber',
									width : 300,
									fieldLabel : '组织机构号',
									allowBlank : false,
									regex : /^\S{1,20}$/,
									regexText : '长度须1~20'
								}, {
									xtype : 'textfield',
									name : 'name',
									width : 300,
									fieldLabel : '企业实名',
									allowBlank : false,
									regex : /^\S{2,50}$/,
									regexText : '长度须2~50'
								}, {
				                	xtype: 'container',
							        anchor: '100%',
							        layout: 'hbox',
							        items : [{
							        	xtype:'textfield',
						                fieldLabel: '工商营业执照',
						                name: 'licenceDuplicate',
						                readOnly : true,
						                labelWidth : 100,
						                labelAlign:'right',
						                allowBlank : false,
						                flex : 1
							        }, {
							        	xtype : 'button',
							        	name : 'select_licenceDuplicate',
							        	icon : 'jsLib/extjs/resources/themes/icons/add1.png'
							        }]
				                },{
				                	xtype: 'container',
							        anchor: '100%',
							        layout: 'hbox',
							        items : [{
							        	xtype:'textfield',
						                fieldLabel: '企业公函',
						                name: 'businessLetter',
						                readOnly : true,
						                labelWidth : 100,
						                labelAlign:'right',
						                allowBlank : false,
						                flex : 1
							        }, {
							        	xtype : 'button',
							        	name : 'select_businessLetter',
							        	icon : 'jsLib/extjs/resources/themes/icons/add1.png'
							        }]
				                },
				                {
									xtype: 'panel',
									width : 350,
									html :'<div style="font-size:8pt; margin-left: 45px;"><div style="margin-bottom: 5px;margin-top: 5px;">请下载<a target="_blank" href="upload/files/qysmrzsqgh.doc">《企业实名认证申请公函》</a>，加盖企业公章</div>' +
											'<div style="margin-bottom: 5px;">(合同章、财务章等无效)后扫描或者拍照上传</div>'+
											'<div style="margin-top: 10px;">支持jpg,png,gif格式，大小不超过2M/张</div></div>'								
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