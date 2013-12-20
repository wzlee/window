package com.eaglec.win.biz.impl.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.cms.ModuleBiz;
import com.eaglec.win.dao.cms.ChannelDao;
import com.eaglec.win.dao.cms.ModuelDao;
import com.eaglec.win.domain.cms.Channel;
import com.eaglec.win.domain.cms.Module;

@Service
public class ModuleBizImpl implements ModuleBiz {
	
	@Autowired
	private ModuelDao moduelDao;
	
	@Autowired
	private ChannelDao channelDao;

	@Override
	public List<Module> findAll(String mchannel) {
		return moduelDao.findList("from Module where mchannel='"+ mchannel +"' order by mposition");
	}

	@Override
	public List<?> findAllTemplate() {
		moduelDao.findList("");
		return null;
	}

	@Override
	public int add(Module m) {
		return moduelDao.add(m);
	}

	@Override
	public void delete(Module m) {
		moduelDao.delete(m);
		List<Channel> channel = channelDao.findList("from Channel where ccode='"+ m.getMchannel() +"'");
		if (channel.size() > 0) {
			Channel cMap = (Channel)channel.get(0);
			Integer mnumber = cMap.getMnumber();
			if (mnumber > 0) {
				cMap.setMnumber(mnumber - 1);
				channelDao.update(cMap);
			}
		}
		//给大于自己的其他模块的索引值减一
		List<Module> mList = moduelDao.findList("from Module where mchannel='"+ m.getMchannel() +"' and mposition>'"+ m.getMposition() +"'");
		for (int i = 0; i < mList.size(); i++) {
			Module module = mList.get(i);
			module.setMposition(module.getMposition() - 1);
			moduelDao.update(module);
		}
	}

	@Override
	public void update(Module m) {
		moduelDao.update(m);
	}

}
