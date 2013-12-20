package com.eaglec.win.biz.impl.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eaglec.win.biz.business.GoodsOrderBiz;
import com.eaglec.win.dao.business.BiddingServiceDao;
import com.eaglec.win.dao.business.GoodsOrderDao;
import com.eaglec.win.dao.business.ResponseBiddingDao;
import com.eaglec.win.dao.user.UserDao;
import com.eaglec.win.domain.business.GoodsOrder;
import com.eaglec.win.view.JSONData;

@Service
public class GoodsOrderBizImpl implements GoodsOrderBiz{
	@Autowired
	private GoodsOrderDao goodsOrderDao;
	@Autowired 
	private UserDao userDao;
	@Autowired
	private BiddingServiceDao biddingServiceDao;
	@Autowired
	private ResponseBiddingDao responseBiddingDao;
	
	@Override
	public void saveGoodsOrder(GoodsOrder goodsOrder) {
		goodsOrderDao.save(goodsOrder);
	}

	@Override
	public GoodsOrder findById(Integer orderId) {		
		return goodsOrderDao.get(orderId);
	}

	@Override
	public void updateGoodsOrder(GoodsOrder goodsOrder) {
		goodsOrderDao.update(goodsOrder);
	}

	@Override
	public JSONData<GoodsOrder> queryOrder(String orderNumber,String serviceName,
			String orderStatus, String startTime, String endTime, int start,
			int limit) {
		String hql = "from GoodsOrder o where 1=1";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(orderNumber)){
			hql += " and o.orderNumber like :orderNumber";
			params.put("orderNumber", "%"+orderNumber+"%");
		}
		if(!StringUtils.isEmpty(serviceName)){
			hql += " and o.serviceName like :serviceName";
			params.put("serviceName", "%"+serviceName+"%");
		}
		if(!StringUtils.isEmpty(orderStatus)){
			hql += " and o.orderStatus in (" + orderStatus + ")";
		}
		if(!StringUtils.isEmpty(startTime)){
			startTime = startTime.substring(0, startTime.indexOf("T"));
			hql += " and o.createTime >= :startTime";
			params.put("startTime", startTime+ " 00:00:00");
		}
		if(!StringUtils.isEmpty(endTime)){
			endTime = endTime.substring(0, endTime.indexOf("T"));
			hql += " and o.createTime <= :endTime";
			params.put("endTime", endTime+ " 23:59:59");
		}
		hql += " order by o.createTime desc";
		return goodsOrderDao.outJSONData(hql, params, start, limit);
	}
}
