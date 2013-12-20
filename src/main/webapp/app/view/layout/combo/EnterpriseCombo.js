Ext.define('plat.view.layout.combo.EnterpriseCombo',{
	extend:'Ext.form.field.ComboBox',
	xtype:'enterprisecombo',
	
    displayField: 'name',	
    valueField: 'id',
    
    initComponent:function(){
    	this.store = Ext.create('Ext.data.ArrayStore',{
    		fields:['id','name','icRegNumber'],
    		proxy:{
    			type:'ajax',    			
    			url:'enter/loadenter'
    		}
    	});
    	this.callParent(arguments);
    }
});