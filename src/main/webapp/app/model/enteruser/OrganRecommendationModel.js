Ext.define('plat.model.enteruser.OrganRecommendationModel', {
    extend: 'Ext.data.Model',
    fields: [
    		'id',
    		 {name:'enterId' ,mapping:'enterprise.id'},
    	     'enterprise.name',
    	     'enterprise.id',
    	     'sort',
    	     'addTime'
    	]
});
