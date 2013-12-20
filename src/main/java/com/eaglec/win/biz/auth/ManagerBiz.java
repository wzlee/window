package com.eaglec.win.biz.auth;

import java.util.List;

import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.view.JSONData;


/**
 * @author cs
 *用户的CRUD
 */
public interface ManagerBiz {
	//添加
	public Manager saveManager(Manager user);
	//查询
	public List<Manager> findUser();
	//根据用户名查询
	public Manager findUserByUsername(String username);
	//根据用户Id
	public Manager findUserById(Integer id);
	//根据roleid 查询用户
	public List<Manager> findUserByRoleId(Integer roleid);
	//删除
	public void deleteUser(Manager user);
	//模糊查询
	public JSONData<Manager> queryManagerByName(String username,int start,int limit);

}
