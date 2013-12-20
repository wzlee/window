package com.eaglec.win.biz.business;

import java.util.List;

import com.eaglec.win.domain.business.OrderInfo;
import com.eaglec.win.view.JSONData;

/**
 * 订单交易详细信息Biz
 * @author xuwf
 * @since 2013-9-11
 *
 */
public interface OrderInfoBiz {
	
	/**
	 * 添加订单交易详细信息
	 * @author xuwf
	 * @since 2013-9-14
	 * @param orderInfo
	 */
	public void saveOrderInfo(OrderInfo orderInfo);
	
	/**
	 * 根据订单id查询该订单的订单详情
	 * @author xuwf
	 * @since 2013-9-17
	 * 
	 * @param orderId
	 * @return
	 */
	public List<OrderInfo> findList(Integer orderId);
	
	/**
	 * 根据订单id查询该订单的订单详情(暂不使用)
	 * @author xuwf
	 * @since 2013-9-29
	 * 
	 * @param orderId
	 * @return
	 */
	public JSONData<OrderInfo> findOrderInfo(Integer orderId,int start,int limit);
	
}
