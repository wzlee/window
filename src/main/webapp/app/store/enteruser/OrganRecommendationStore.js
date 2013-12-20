Ext.define('plat.store.enteruser.OrganRecommendationStore', {
    extend: 'Ext.data.Store',
    model:'plat.model.enteruser.OrganRecommendationModel',
    storeId:'organrecommendationstore',
    proxy: {
        type: 'ajax',
     	actionMethods: {  
        	read: 'POST'
            },
        api:{  
		    read:'enter/findAllOrgan',  
		    create:'enter/addOrgan',  
		    destroy:'enter/deleteOrgan',  
		    update:'enter/updateOrgan'  
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