package com.eaglec.win.biz.business;

import com.eaglec.win.domain.business.Appeal;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONRows;
import com.eaglec.win.view.OrderAndBiddingAppeal;

/**
 * 服务申诉Biz
 * @author xuwf
 * @since 2013-9-11
 * 
 */
public interface AppealBiz {
	
	/**
	 * 保存申诉信息
	 * @author xuwf
	 * @since 2013-9-14
	 * 
	 * @param appeal
	 */
	public void saveAppeal(Appeal appeal);
	
	/**
	 * 根据订单状态查询"申诉处理中"的申诉单
	 * @author xuwf
	 * @since 2013-9-25
	 * 
	 * @param orderStatus	订单状态
	 * @param orderNumber	订单编号 
	 * @param appealType	申诉类型
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONData<Appeal> findByStatus(Integer orderStatus,String orderNumber,String appealType,int start,int limit);
	
	/**
	 * 综合查询申诉	支撑平台
	 * @author xuwf
	 * @since 2013-9-26
	 * 
	 * @param orderNum		订单编号
	 * @param appealType	申诉类型(1、买家申诉 2、卖家申诉)
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @param buyer			买家公司全称
	 * @param seller		卖家公司全称
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONData<OrderAndBiddingAppeal> queryIntegrated(String orderNum,String appealType,String startTime,
			String endTime,String buyer,String seller,int start,int limit);
	
	/**
	 * 根据id查询申诉单
	 * @author xuwf
	 * @since 2013-9-26
	 * 
	 * @param id	申诉单id
	 * @return
	 */
	public Appeal findById(Integer id);
	
	/**
	 * 更新申诉单
	 * @author xuwf
	 * @since 2013-9-26
	 * 
	 * @param appeal
	 */
	public void update(Appeal appeal);
	
	/**
	 * 根据招单id查看该招单的申诉信息
	 * @author xuwf
	 * @since 2013-10-12
	 * 
	 * @param bidId		招单id
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONData<Appeal> queryByBidId(Integer bidId,int start,int limit);
	
	/**
	 * 根据招单id查看该招单的申诉信息
	 * @author xuwf
	 * @since 2013-10-12
	 * 
	 * @param bidId		招单id
	 * @param start
	 * @param limit
	 * @return
	 */
	public Appeal queryByBidId(Integer bidId);
	
	/**
	 * 根据订单id查询申诉单
	 * @author xuwf
	 * @since 2013-10-16
	 * @param orderId
	 * @return
	 */
	public abstract Appeal queryByOrderId(Integer orderId);
}
