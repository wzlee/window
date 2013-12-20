package com.eaglec.win.biz.impl.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.auth.MenuBiz;
import com.eaglec.win.dao.auth.MenuDao;
import com.eaglec.win.domain.auth.Menu;

@Service
public class MenuBizImpl implements MenuBiz {
	
	@Autowired
	private MenuDao menuDao;

	@Override
	public Menu saveMenu(Menu rights) {
		// TODO Auto-generated method stub
		return menuDao.save(rights);
	}

	@Override
	public List<Menu> findAll() {
		// TODO Auto-generated method stub
			return menuDao.findList("from Menu");
	}

	@Override
	public void deleteRights(Menu rights) {
		// TODO Auto-generated method stub
		menuDao.delete(rights);
	}

	@Override
	public List<Menu> findMenus(String menuids) {
		// TODO Auto-generated method stub
		return menuDao.executeSqlQuerys("select * from menu where id in("+menuids+")");
	}
	
	

}
