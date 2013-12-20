package com.eaglec.win.biz.business;


import com.eaglec.win.domain.business.ResponseBidding;
import com.eaglec.win.view.JSONData;



/**
 * 应标Biz
 * 
 * @author Xiadi
 * @since 2013-9-29
 */
public interface ResponseBiddingBiz {
	/**
	 * 根据id查找ResponseBidding对象
	 * @author PYF
	 * @since 2013-9-29
	 * @param id
	 * @return ResponseBidding对象
	 */
	public abstract ResponseBidding getResponseBiddingById(int id);
	
	/**
	 * 应标申请
	 * @author pangyf
	 * @since 2013-9-29
	 * @param rb
	 * @return ResponseBidding.id
	 */
	public abstract int apply(ResponseBidding rb);
	
	public abstract ResponseBidding addOrModify(ResponseBidding rb);
	
	/**
	 * 根据招标方和应标状态得到某张招标单的应标信息
	 * @author xuwf
	 * @since 2013-10-09
	 * 
	 * @param bidServiceId	招标单id
	 * @param status		应标状态
	 * @return
	 */
	public abstract JSONData<ResponseBidding> getByBidService(Integer bidServiceId,Integer status,int start,int limit);
	
	/**
	 * 统计当前该招标的应标人数
	 * @author pangyf
	 * @since 2013-10-12
	 * @param bid
	 * @return
	 */
	public abstract long countResponseBiddingByBid(int bid);

}
