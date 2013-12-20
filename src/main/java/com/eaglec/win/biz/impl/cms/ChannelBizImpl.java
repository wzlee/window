package com.eaglec.win.biz.impl.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.eaglec.win.biz.cms.ChannelBiz;
import com.eaglec.win.dao.cms.ChannelDao;
import com.eaglec.win.domain.cms.Channel;

@Service
public class ChannelBizImpl implements ChannelBiz {
	
	@Autowired
	private ChannelDao channelDao; 

	@Override
	public List<Channel> findAll() {
		return channelDao.findList("from Channel where pid = null order by cindex");
	}
	
	/**
	 * @date: 2013-8-24
	 * @author：lwch
	 * @description：根据频道类别，获取该类别下所有的频道
	 */
	@Override
	public List<Channel> findChnnelByCtype(Object id){
		if (id != null) {
			id = "'"+ id +"'"; 
		}
		return channelDao.findList("from Channel where pid = "+ id +" and isChannel = 1 order by cindex");
	}

	@Override
	public int add(Channel c) {
		int cindex = 0;
		if (c.getPid() == 0) {
			cindex = channelDao.getMaxCindex(" is null") + 1;
			c.setPid(null);
			c.setLeaf(true);
		} else {
			cindex = channelDao.getMaxCindex(" = '"+c.getPid().toString()+"'") + 1;
			c.setCcode(c.getCcode()+ "-" + cindex);
			if(cindex == 1){
				channelDao.executeSql("update channel set leaf = 0 where id = '"+ c.getPid() +"'");
			}
		}
		c.setCindex(cindex);
		return channelDao.add(c);
	}

	@Override
	public void delete(Channel c) {
		channelDao.delete(c);
		if (c.getLeaf()) {
			channelDao.executeSql("update channel set leaf = 1 where id = '"+ c.getPid() +"'");
		}
	}

	@Override
	public void update(Channel c) {
		if (c.getPid() == 0) {
			c.setPid(null);
		}
		channelDao.update(c);
	}
	
	@Override
	public void updateSeqencing(int cid, int cindex, int oid, int oindex){
		channelDao.updateSeqencing(cid, cindex, oid, oindex);
	}

}
