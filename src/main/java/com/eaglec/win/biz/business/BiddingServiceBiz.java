package com.eaglec.win.biz.business;

import com.eaglec.win.domain.business.BiddingService;
import com.eaglec.win.view.JSONData;

/**
 * 招标biz
 * 
 * @author Xiadi
 * @since 2013-9-29
 */
public interface BiddingServiceBiz {

	/**
	 * 新增或修改招标
	 * @author Xiadi
	 * @since 2013-9-29 
	 *
	 * @param biddingService
	 */
	public abstract BiddingService addOrModify(BiddingService biddingService);
	
	/**
	 * 根据Id查找招标
	 * @author Xiadi
	 * @since 2013-9-29 
	 *
	 * @param id
	 * @return
	 */
	public abstract BiddingService findBiddingServiceById(Integer id);

	/**
	 * 混合条件查询招标订单(支撑平台-标单管理)
	 * @author  xuwf
	 * @since 2013-10-8
	 * 
	 * @param name				服务名称
	 * @param status			招标单状态		
	 * @param startTime			创建时间
	 * @param endTime			结束时间
	 * @param start
	 * @param limit
	 * @return
	 */
	public abstract JSONData<BiddingService> queryBidding(String bidNo,String name,String status,
			String startTime,String endTime,int start,int limit);
}
