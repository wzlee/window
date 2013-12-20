package com.eaglec.win.dao.publik;

import com.eaglec.win.dao.BaseDao;
import com.eaglec.win.domain.base.FileManager;

public interface FileManagerDao extends BaseDao<FileManager>{
	public FileManager get(int id);
	
	public FileManager findOne(String fileName);
}
