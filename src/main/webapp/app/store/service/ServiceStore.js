Ext.define('plat.store.service.ServiceStore', {
    extend: 'Ext.data.Store',
    model:'plat.model.service.ServiceModel',
    
//    storeId:'servicestore',
//    groupField: 'cid',
    proxy: {
        type: 'ajax',
     	actionMethods: {  
        	read: 'POST'
            },
        api:{  
		    read:'service/query',  
		    create:'service/add',  
		    destroy:'service/delete',  
		    update:'service/update'  
      		},  
		reader:{  
      		type: 'json',
			root: 'data',
        	messageProperty:"message"  
      		}, 
		writer:{  
		    type:"json",  
		    encode:true,  
		    root:"services",  
		    allowSingle:false  
		}
    },
    autoSync:true,
    folderSort: true,
    sorters: [{property: 'service_id', direction: 'ASC'}]
});
