Ext.define('plat.view.layout.combo.UserTypeCombo',{
	extend:'Ext.form.field.ComboBox',
	xtype:'usertypecombo',
	displayField: 'text',
	valueField: 'id',
	store:new Ext.data.SimpleStore({
		fields:['id','text'],
		data:[
			[1,'普通企业'],
			[2,'认证企业'],
			[3,'服务机构'],
			[4,'政府机构']						
			]		
	})
});