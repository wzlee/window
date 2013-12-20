Ext.define('plat.view.cms.ModelConfigPanel',{
	extend:'Ext.panel.Panel',
	alias:'widget.modelConfigPanel',
	width: 520,
	height: 390,
	frame: false,
    border : false,
    layout : 'border',
    initComponent: function() {
        this.callParent();
    },
    tbar: [{
    	xtype : 'label',
    	text : '请选择模版：',
        forId: 'myFieldId',
        margin: '0 10',
    	scope : this
    }, {
    	xtype: 'combobox',
        width: 135,
    	hideLabel: true,
    	valueField: 'id',
        store: Ext.create('Ext.data.JsonStore', {
            fields: ['id', 'text'],
            data : [
            	{id : 's_category', text : '服务类别'},
				{id : 's_organ', text : '服务机构'}
			]
        }),
        value: 's_category',
        displayField: 'text',
        typeAhead: true,
        queryMode: 'local',
        triggerAction: 'all',
        emptyText:'请选择模版……',
        selectOnFocus:true
    }],
    items: [{
    	xtype: 'categoryServeTree'
    },{
    	xtype: 'modelForm'
    }]
});