Ext.define('plat.view.layout.combo.UserCategoryCombo',{
	extend:'Ext.form.field.ComboBox',
	xtype:'usercategorycombo',
	id:'usercategorycombo',
	queryMode: 'local',
    displayField: 'text',
    valueField: 'id',    
    initComponent:function(){
    	this.store = Ext.create('Ext.data.ArrayStore',{
			fields:['id','code','text'],
			proxy:{
				type:'jsonp',				
				url: PlatUrl + 'public/userCategory'
			}
		});
    	this.callParent(arguments);
    }
});