package com.eaglec.win.biz.auth;

import java.util.List;

import com.eaglec.win.domain.auth.Menu;


/**
 * @author cs
 *权限的crud
 */
public interface MenuBiz {
	//添加权限
	public Menu saveMenu(Menu menu);
	//查询
	public List<Menu> findAll();
	//根据主键ID列表查询
	public List<Menu> findMenus(String menuids);
	//删除
	public void deleteRights(Menu rights);
	

}
