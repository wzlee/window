Ext.Loader.setConfig({enabled:true});
Ext.Loader.setPath({
	'Ext.ux':'jsLib/extjs/ux'
});
Ext.require([
			]);
var plat = new Ext.application({
    name: 'plat',
//    appFolder:'resource/app',
    controllers: ['service.ServiceController'],
    launch: function() {
		Ext.create('plat.view.service.ServicePanel', {
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

