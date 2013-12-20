package com.eaglec.win.biz.impl.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.auth.ManagerBiz;
import com.eaglec.win.dao.auth.ManagerDao;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.view.JSONData;

@Service
public class ManagerBizImpl implements ManagerBiz {
	
	@Autowired
	private ManagerDao managerDao;
	
	//添加
	public Manager saveManager(Manager manager){
		return managerDao.saveOrUpdate(manager);
	}
	//查询
	public List<Manager> findUser(){
		return managerDao.findList("from Manager");
	}
	//删除
	public void deleteUser(Manager user){
		managerDao.delete(user);
	}
	@Override
	public Manager findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return managerDao.get("from Manager u where u.username='"+username+"'");
	}
	@Override
	public List<Manager> findUserByRoleId(Integer roleid) {
		// TODO Auto-generated method stub
		return managerDao.findList("from Manager u where u.role.id="+roleid);
	}
	@Override
	public Manager findUserById(Integer id) {
		// TODO Auto-generated method stub
		return managerDao.get(id);
	}
	@Override
	public JSONData<Manager> queryManagerByName(String username, int start,int limit) {
		String hql = "from Manager where 1=1";
		if(username != null && !username.isEmpty()){
			hql += " and username like '%" + username + "%'";
		}
		hql += " order by registerTime";
		return managerDao.outJSONData(hql, start, limit);
	}
	

}
