package com.eaglec.win.biz.business;

import com.eaglec.win.domain.business.GoodsOrder;
import com.eaglec.win.view.JSONData;
/**
 * 订单Service
 * @author xuwf
 * @since 2013-9-6
 * 
 */
public interface GoodsOrderBiz {
	
	/**
	 * 添加服务订单
	 * @author xuwf
	 * @since 2013-9-6
	 * @param goodsOrder
	 */
	public void saveGoodsOrder(GoodsOrder goodsOrder);
	
	/**
	 * 更新服务订单
	 * @author xuwf
	 * @since 2013-9-14
	 * @param goodsOrder
	 */
	public void updateGoodsOrder(GoodsOrder goodsOrder);
	
	/**
	 * 根据订单id查询订单
	 * @author xuwf
	 * @since 2013-9-14
	 * 
	 * @param orderId
	 * @return	订单
	 */
	public GoodsOrder findById(Integer orderId);
	
	/**
	 * 混合条件查询订单(支撑运营平台-订单管理)
	 * @author xuwf
	 * @since 2013-9-24
	 * @param orderNumber	订单编号
	 * @param orderStatus	订单状态
	 * @param startTime		下单时间
	 * @param endTime		结束时间
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONData<GoodsOrder> queryOrder(String orderNumber,String serviceName,String orderStatus,
			String startTime,String endTime,int start,int limit);
}
