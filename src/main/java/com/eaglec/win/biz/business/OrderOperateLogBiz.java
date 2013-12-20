package com.eaglec.win.biz.business;

import com.eaglec.win.domain.business.OrderOperateLog;

/**
 * 订单操作日志Biz
 * @author xuwf
 * @since 2013-9-11
 *
 */
public interface OrderOperateLogBiz {
	
	/**
	 * 保存订单操作日志
	 * @author xuwf
	 * @since 2013-9-14
	 * @param orderOperateLog
	 */
	public void saveOrderOperLog(OrderOperateLog orderOperateLog);
}
