Ext.define('plat.store.service.TopThreeStore', {
    extend: 'Ext.data.Store',
    model:'plat.model.service.TopThreeModel',
    storeId:'topthreestore',
    proxy: {
        type: 'ajax',
     	actionMethods: {  
        	read: 'POST'
            },
        api:{  
		    read:'service/findTopthree',  
		    update:'service/updateTopthree'  
      		},  
		reader:{  
      		type: 'json',
			root: 'data',
        	messageProperty:"message"  
      		}, 
		writer:{  
		    type:"json",  
		    encode:true,  
		    root:"",  
		    allowSingle:false  
		}
    },
    folderSort: true,
    sorters: [{property: 'regTime', direction: 'DESC'}]
});