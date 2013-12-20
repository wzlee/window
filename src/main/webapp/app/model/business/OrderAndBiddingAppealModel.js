Ext.define('plat.model.business.OrderAndBiddingAppealModel',{
 	extend: 'Ext.data.Model',
    fields: [
    			'id',
    			'oaNum',
    			'serviceId',
    			'serviceName',
    			'categoryName',
    			'price',
    			'tranPrice',
    			'buyer',
    			'seller',
    			'appealType',
    			'handlerRemark',
    			'processorTime',
    			//订单
    			{name:'goodsOrder.id',type:'int'},
    			{name:'goodsOrder.orderNumber',type:'String'},
    			{name:'goodsOrder.orderStatus',type:'Integer'},
    			{name:'goodsOrder.transactionPrice',type:'Integer'},
    			{name:'goodsOrder.userName',type:'String'},
    			{name:'goodsOrder.phone',type:'String'},
    			{name:'goodsOrder.email',type:'String'},
    			{name:'goodsOrder.remark',type:'String'},
    			{name:'goodsOrder.createTime',type:'String'},
    			{name:'goodsOrder.serviceName',type:'String'},
    			//买家信息
    			{name:'goodsOrder.buyer.id',type:'int'},
    			{name:'goodsOrder.buyer.enterprise.name',type:'String'},
    			//卖家
    			{name:'goodsOrder.service.enterprise.name',type:'String'},
    			//服务商品
    			{name:'goodsOrder.service.id',type:'int'},
    			{name:'goodsOrder.service.serviceNo',type:'String'},
    			{name:'goodsOrder.service.serviceSource',type:'Integer'},
    			{name:'goodsOrder.service.serviceName',type:'String'},
    			{name:'goodsOrder.service.category.id',type:'int'},
    			{name:'goodsOrder.service.category.text',type:'String'},
    			{name:'goodsOrder.service.registerTime',type:'String'},
    			{name:'goodsOrder.service.serviceNum',type:'Integer'},
    			{name:'goodsOrder.service.linkMan',type:'String'},
    			{name:'goodsOrder.service.tel',type:'String'},
    			{name:'goodsOrder.service.email',type:'String'},
    			{name:'goodsOrder.service.serviceMethod',type:'String'},
    			{name:'goodsOrder.service.serviceProcedure',type:'String'},
    			{name:'goodsOrder.service.chargeMethod',type:'String'},
    			{name:'goodsOrder.service.costPrice',type:'Integer'},
    			{name:'goodsOrder.service.currentStatus',type:'Integer'},
    			{name:'goodsOrder.service.lastStatus',type:'Integer'},
    			{name:'goodsOrder.service.picture',type:'String'},
    			{name:'goodsOrder.service.totalScore',type:'double'},
    			{name:'goodsOrder.service.evaluateScore',type:'double'},
    			//申诉招单
    			{name:'goodsOrder.biddingService.id',type:'int'},
    			{name:'goodsOrder.biddingService.bidNo',type:'String'},
    			{name:'goodsOrder.biddingService.createTime',type:'String'},
    			{name:'goodsOrder.biddingService.name',type:'String'},
    			{name:'goodsOrder.biddingService.minPrice',type:'Integer'},
    			{name:'goodsOrder.biddingService.maxPrice',type:'Integer'},
    			{name:'goodsOrder.biddingService.attachment',type:'String'},
    			{name:'goodsOrder.biddingService.description',type:'String'},
    			{name:'goodsOrder.biddingService.linkMan',type:'String'},
    			{name:'goodsOrder.biddingService.tel',type:'String'},
    			{name:'goodsOrder.biddingService.email',type:'String'},
    			{name:'goodsOrder.biddingService.status',type:'Integer'},
    			{name:'goodsOrder.biddingService.category.id',type:'int'},
    			{name:'goodsOrder.biddingService.category.text',type:'String'},
    			{name:'goodsOrder.biddingService.user.id',type:'int'},
    			{name:'goodsOrder.biddingService.user.userName',type:'String'},
    			{name:'goodsOrder.biddingService.user.enterprise.name',type:'String'},
    			{name:'goodsOrder.biddingServie.rid',type:'Integer'},
    			{name:'goodsOrder.biddingService.rname',type:'String'}
    		
    ]
	
});