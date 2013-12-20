package com.eaglec.win.biz.user;

import java.util.List;

import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.view.JSONData;

/**
 * 子账号Biz<br/>
 * 封装对子账号的相关操作
 * 
 * @author xuwf
 * @since 2013-8-23
 * 
 */
public interface StaffBiz {
	
	/**
	 * 新增子账号
	 * @author xuwf
	 * @since 2013-8-23
	 * 
	 * @param staff
	 */
	public abstract void save(Staff staff);
	
	/**
	 * 修改子账号
	 * @author xuwf
	 * @since 2013-8-23
	 * 
	 * @param staff
	 */
	public abstract void update(Staff staff);
	
	/**
	 * 根据用户名查找子账号对象(判断子账号用户名是否重复)
	 * @author xuwf
	 * @since 2013-8-24
	 * 
	 * @param userName	用户名
	 * @param parentId	主账号ID
	 * @return
	 */
	public abstract Staff findByUserName(String userName,Integer parentId);
	
	/**
	 * 根据主账号id分页查询子账号列表
	 * @author xuwf
	 * @author 2013-8-23
	 * 
	 * @param parentId	主账号id
	 * @param start
	 * @param limit
	 * @returns
	 */
	public abstract JSONData<Staff> findStaff(Integer parentId,int start,int limit);
	
	/**
	 * 查询某个主账号下的所有子账号
	 * @author xuwf
	 * @since 2013-9-9
	 * @param user	主账号
	 * @return	对应子账号集合
	 */
	public List<Staff> findByParent(User user);
	
	/**
	 * 计算某个主账号下面的子账号的数量(限制20)
	 * @param parentId
	 * @author xuwf
	 * @since 2013-8-28
	 * 
	 * @return
	 */
	public abstract Long getTotal(Integer parentId);
	
	public Staff findByStid(String stid);
	
	/**
	 * 根据用户名查找子账号对象(判断子账号用户名是否重复)
	 * @author xuwf
	 * @since 2013-8-24
	 * 
	 * @param userName	用户名
	 * @param parentId	主账号ID
	 * @return
	 */
	public abstract Staff findByUserName(String userName);
	/**
	 * 查找所有子账号
	 */
	public List<Staff> find();
}
