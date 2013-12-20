package com.eaglec.win.biz.cms;

import java.util.List;

import com.eaglec.win.domain.cms.Channel;

public interface ChannelBiz {
	
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：获取所有的频道
	 */
	public List<Channel> findAll();
	
	/**
	 * @date: 2013-8-24
	 * @author：lwch
	 * @description：根据频道类别，获取该类别下所有的频道
	 */
	public List<Channel> findChnnelByCtype(Object id);
	
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：添加频道(ctypev：类别编码，数据库中未存)
	 */
	public int add(Channel c);
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：删除频道
	 */
	public void delete(Channel c);
	/**
	 * @date: 2013-8-14
	 * @author：lwch
	 * @description：修改频道
	 */
	public void update(Channel c);

	/**
	 * @date: 2013-8-23
	 * @author：lwch
	 * @description：频道排序
	 */
	public void updateSeqencing(int cid, int cindex, int oid, int oindex);
}
