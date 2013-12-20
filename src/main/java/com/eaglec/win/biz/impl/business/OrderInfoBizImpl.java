package com.eaglec.win.biz.impl.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.business.OrderInfoBiz;
import com.eaglec.win.dao.business.OrderInfoDao;
import com.eaglec.win.domain.business.OrderInfo;
import com.eaglec.win.view.JSONData;

@Service
public class OrderInfoBizImpl implements OrderInfoBiz {
	@Autowired
	private OrderInfoDao orderInfoDao;
	
	@Override
	public void saveOrderInfo(OrderInfo orderInfo) {	
		orderInfoDao.save(orderInfo);
	}

	@Override
	public List<OrderInfo> findList(Integer orderId) {
		String hql = "from OrderInfo i where i.goodsOrder.id = :id order by i.processTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", orderId);
		return orderInfoDao.findList(hql, params);
	}
	
	@Override
	public JSONData<OrderInfo> findOrderInfo(Integer orderId,int start,int limit) {
		String hql = "from OrderInfo i where i.goodsOrder.id = :id order by i.processTime";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", orderId);
		return orderInfoDao.outJSONData(hql, params, start, limit);
	}

}
