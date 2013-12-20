/**
 * xwf qservice服务综合查询
 */
Ext.Loader.setConfig({enabled:true});
Ext.Loader.setPath({
	'Ext.ux':'jsLib/extjs/ux'
});
Ext.require([
			]);
var plat = new Ext.application({
    name: 'plat',
//    appFolder:'resource/app',
    controllers: ['service.QServiceController'],
    launch: function() {
		Ext.create('plat.view.service.QServicePanel', {
			width:'100%',
			height:'100%',
			renderTo:Ext.getBody()
    	});
        this.controllers.addListener('add',this.initController,this);
    },
    initController:function(index, controller, token){
    	controller.init();
    	//console.log(token+'初始化完毕...');
  	}
});
