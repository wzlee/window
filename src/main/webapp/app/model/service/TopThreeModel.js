/**
 * Servive Model
 */
Ext.define('plat.model.service.TopThreeModel', {
    extend: 'Ext.data.Model',
    fields: [
    		'id',
    		'topthree',
    		{name:'serviceName',mapping:'service.serviceName'},
    		{name:'serviceSource',mapping:'service.serviceSource'},
    		{name:'serviceNo',mapping:'service.serviceNo'},
    		{name:'registerTime',mapping:'service.registerTime'},
    		{name:'serviceid',mapping:'service.id'}
    		]
});
