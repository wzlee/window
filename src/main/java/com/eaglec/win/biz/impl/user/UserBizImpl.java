package com.eaglec.win.biz.impl.user;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.dao.user.UserDao;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.view.JSONData;

@Service
public class UserBizImpl implements UserBiz {
	@Autowired
	private UserDao userDao;

	@Override
	public JSONData<User> queryMultiple(String username, String entershort,
			String entername,Integer enterpriseType,String startTime,
			String endTime, int start, int limit)  {
		String hql = "from User u  where enterprise.industryType = "+Common.windowId;
		if(!StringUtils.isEmpty(username)){
			hql += " and u.userName like '%"+ username + "%'";
		}
		if(!StringUtils.isEmpty(entershort)){
			hql += " and u.enterprise.forShort like '%"+ entershort +"%'";
		}
		if(!StringUtils.isEmpty(entername)){
			hql += " and u.enterprise.name like '%"+ entername +"%'";
		}
		if(!StringUtils.isEmpty(enterpriseType)){
			hql += " and u.enterprise.type = "+enterpriseType;
		}
		if(!StringUtils.isEmpty(startTime)){
			startTime = startTime.substring(0, startTime.indexOf("T"));
			hql += " and u.regTime >= '"+ startTime+" 00:00:00'";	
		}
		if(!StringUtils.isEmpty(endTime)){
			endTime = endTime.substring(0, endTime.indexOf("T"));
			hql += " and u.regTime <= '"+ endTime+" 23:59:59'";
		}
		hql += " order by u.regTime desc";
		return userDao.outJSONData(hql, start, limit);
	}

	/**
	 * @author pangyf
	 * @since 2013-08-22
	 */
	public JSONData<User> findAll(String uName,String eName,Integer enterpriseType,String userStatus,int start, int limit) {		
		
		String hql = "from User where enterprise.industryType = "+Common.windowId
				+" and userStatus in (" + userStatus +") ";
		if(!StringUtils.isEmpty(uName)){
			hql += " and userName like '%" + uName + "%'";
		}
		if(!StringUtils.isEmpty(eName)){
			hql += " and enterprise.name like '%" + eName + "%')";
		}
		if(!StringUtils.isEmpty(enterpriseType)){
			hql += " and enterprise.type = "+enterpriseType;
		}
		hql +="order by regTime desc";
		return userDao.outJSONData(hql,start, limit);
	}

	@Override
	public User update(User user) {
		return userDao.update(user);
	}
	
	@Override
	public User add(User user) {
		return userDao.saveOrUpdate(user);
	}
	
	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		return userDao.get(id);
	}

	@Override
	public List<User> findUserByName(String name) {
		String hql="from User u where u.userName='"+name+"'";
		return userDao.findList(hql);
	}
	
	/**
	 * @author pangyf
	 * @since 2013-08-22
	 */
	@Override
	public List<User> findUserListByName(String userName) {
		String hql = "from User where userStatus != "+Constant.DELETED+" and userName = '" + userName + "'";	
		return userDao.findList(hql);		
	}

	@Override
	public List<User> findUserByEmail(String email) {
		String hql = "from User where email = '" + email + "'";	
		return userDao.findList(hql);
	}

	@Override
	public User findUserByUid(String uid) {
		String hql = "from User where uid = '" + uid + "'";	
		return userDao.get(hql);
	}

	@Override
	public User findUserByEid(Integer eid) {
		String hql = "from User where enterprise.id = " + eid;	
		return userDao.get(hql);
	}
	
	@Override
	public List<User> find() {
		// TODO Auto-generated method stub
		return userDao.findList("from User");
	}

}
