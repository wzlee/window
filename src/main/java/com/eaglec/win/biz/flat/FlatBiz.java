package com.eaglec.win.biz.flat;

import com.eaglec.win.domain.base.Flat;
import com.eaglec.win.view.JSONData;


public interface FlatBiz {
	
	public void addOrUpdate(Flat flat);
	
	public JSONData<Flat> queryByFlatName(String flatName);
	
}
