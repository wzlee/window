package com.eaglec.win.biz.impl.publik;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.dao.publik.CategoryDao;
import com.eaglec.win.domain.base.Category;
import com.eaglec.win.utils.Constant;

@Service
public class CategoryBizImpl implements CategoryBiz {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public List<Category> findAll() {
		String hql = "from Category where pid is null";
		return categoryDao.findList(hql);
	}
	
	@Override
	public List<Category> findAllCategory() {
		String hql = "from Category where pid =1 and clazz='SERVICE'";
		return categoryDao.findList(hql);
	}
	
	/**
	 * @date: 2013-8-24
	 * @author：lwch
	 * @description：查询所有服务类别的父级别
	 */
	@Override
	public List<Category> findCategoryParent() {
		String sql = "select * from category where pid in (select id from category where pid is null and clazz='SERVICE')";
		return categoryDao.executeSqlQueryEntityList(sql);
	}
	
	/**
	 * @date: 2013-8-24
	 * @author：lwch
	 * @description：根据父类别ID，查找子类别
	 */
	@Override
	public List<Category> findCategoryChildren(int id){
		return categoryDao.findList("from Category where pid = '"+ id +"'");
	}

	@Override
	public List<Category> findCategoryByClazzAndPid(String clazz, int pid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clazz", clazz);
		map.put("pid", pid);
		return categoryDao.findList("from Category where clazz=:clazz and pid=:pid", map);
	}

	@Override
	public Category addOrUpdate(Category category) {
		return categoryDao.saveOrUpdate(category);
	}

	@Override
	public void delete(Category category) {
		categoryDao.delete(category);
	}

	@Override
	public void deleteById(Integer id) {
		categoryDao.delete(id);
	}

	@Override
	public Category findById(String clazz, Integer id) {
		String hql = "from Category where clazz=:clazz and id=:id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clazz", clazz);
		params.put("id", id);
		return categoryDao.get(hql, params);
	}
	
	/**
	 * @date: 2013-10-18
	 * @author：lwch
	 * @description：根据类别ID去查询服务表里面对应的服务总数量
	 */
	public int loadCategoryToServerCount(int cid){
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from Service where category.id = ").append(cid)
		  .append(" and currentStatus in (")
		  .append(Constant.SERVICE_STATUS_ADDED).append(",")
		  .append(Constant.SERVICE_STATUS_CHANGE_AUDIT).append(",")
		  .append(Constant.SERVICE_STATUS_DOWN_AUDIT).append(")")
		  .append(" and enterprise.status = ").append(Constant.ENTERPRISE_STATUS_EFFECTIVE);
//		String sql = "select count(*) from service where category_id = "+ cid + " and currentStatus = " + Constant.SERVICE_STATUS_ADDED;
		Long count = categoryDao.count(sb.toString());
		return count.intValue();
	}

	@Override
	public Category findRootByClazz(String clazz) {
		// TODO Auto-generated method stub
		String hql = "from Category where pid is null and clazz=:clazz ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clazz", clazz);
		return categoryDao.get(hql, params);
	}

}
