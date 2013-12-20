package com.eaglec.win.sync;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.eaglec.win.utils.Dao;

@Component
@Lazy(false)
public class BeforeSync implements InitializingBean{
	
	@Autowired
	private Dao dao;

	@Override
	public void afterPropertiesSet() throws Exception {
		DataSync.setDao(dao);
	}
	
}
