Ext.define('plat.view.cms.ChannelTab',{
	extend:'Ext.tab.Panel',
	xtype:'channelTab',
    border : false,
    region : 'center',
    title: '频道展示',
    activeTab:0,
    initComponent: function() {
    	this.items = [
    		{
				xtype:'panel',
				title:'首页',
				id:'home',
				html:'<iframe src="./" frameborder="0" width="100%" height="100%"></iframe>'
			}
    	]
        this.callParent();
    }
});

