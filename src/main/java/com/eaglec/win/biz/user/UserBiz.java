package com.eaglec.win.biz.user;
import java.util.List;

import com.eaglec.win.domain.base.User;
import com.eaglec.win.view.JSONData;

/**
 * 注册用户Service<br/>
 * 封装对注册用户的相关操作
 * 
 * @author Xiadi
 * @since 2013-8-22
 * 
 */
public interface UserBiz {
	
	/**
	 * 根据条件分页查找企业用户
	 * @author xuwf
	 * @since 2013-8-22
	 * 
	 * @param username	会员名
	 * @param entershort 企业简称
	 * @param entername  企业全称
	 * @param enterpriseType 企业类型
	 * @param startTime 起始时间
	 * @param lastTime 截止时间
	 * @param start	
	 * @param limit
	 * @return	JSONData<User> user列表 JSON格式
	 */
	public abstract JSONData<User> queryMultiple(String username,String entershort,
			String entername,Integer enterpriseType,String startTime,String endTime,
			int start,int limit);
	
	/**
	 * 查询所有的企业用户
	 * @author pangyf
	 * @since 2013-08-22
	 */
	public abstract JSONData<User> findAll(String uName,String eName,Integer enterpriseType,String userStatus,int start, int limit);
	
	/**
	 * 更新企业用户
	 * @author pangyf
	 * @since 2013-08-22
	 */
	public abstract User update(User user);
	
	/**
	 * 添加企业用户
	 * @author pangyf
	 * @since 2013-08-22
	 */
	public abstract User add(User user);
	/**
	 * 根据ID查询用户
	 * @author pangyf
	 * @since 2013-08-22
	 */
	public User findUserById(Integer id);
	
	/**
	 * 根据用户名查询企业用户
	 * @author pangyf
	 * @since 2013-08-27
	 */
	public abstract List<User> findUserListByName(String userName);

	/**
	 * 根据用户名返回用户
	 * @param name
	 * @return
	 */
	public List<User> findUserByName(String name);
	
	/**
	 * 根据邮箱返回用户
	 * @param name
	 * @return
	 */
	public List<User> findUserByEmail(String email);

	/**
	 * 根据uid返回用户
	 * @param name
	 * @return
	 */
	public User findUserByUid(String uid);
	
	public User findUserByEid(Integer eid);
	
	/**
	 * 查询所有user
	 */
	public List<User> find();
	
	/**
	 * 根据企业id查找企业主账号
	 * @author cs
	 * @since 2013-12-24
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public abstract User findUserByEnterprise(Integer enterpriseId);
}
