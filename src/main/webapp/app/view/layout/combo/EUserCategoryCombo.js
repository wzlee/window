Ext.define('plat.view.layout.combo.EUserCategoryCombo',{
	extend:'Ext.form.field.ComboBox',
	xtype:'eusercategorycombo',
	id:'eusercategorycombo',
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