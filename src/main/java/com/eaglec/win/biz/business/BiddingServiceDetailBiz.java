package com.eaglec.win.biz.business;

import com.eaglec.win.domain.business.BiddingServiceDetail;
import com.eaglec.win.view.JSONData;

/**
 * 招标流水biz
 * 
 * @author xuwf
 * @since 2013-10-08
 */
public interface BiddingServiceDetailBiz {

	/**
	 * 新增招标流水信息
	 * @author Xiadi
	 * @since 2013-10-08
	 *
	 * @param biddingService
	 */
	public abstract void add(BiddingServiceDetail biddingServiceDetail);

	/**
	 * 根据找单id查询该招单的所有流水信息
	 * @author xuwf
	 * @since 2013-10-09
	 * 
	 * @param bidServiceId	找单id
	 * @param start
	 * @param limit
	 * @return
	 */
	public abstract JSONData<BiddingServiceDetail> getByBidId(Integer bidServiceId,int start,int limit);
	
}
