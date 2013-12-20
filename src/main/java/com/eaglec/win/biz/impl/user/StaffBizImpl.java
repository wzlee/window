package com.eaglec.win.biz.impl.user;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.dao.user.StaffDao;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.view.JSONData;
@Service
public class StaffBizImpl implements StaffBiz {
	
	@Autowired
	private StaffDao staffDao;
	
	@Override
	public void save(Staff staff) {
		staffDao.save(staff);
	}

	@Override
	public void update(Staff staff) {
		staffDao.update(staff);
	}

	@Override
	public JSONData<Staff> findStaff(Integer parentId, int start, int limit) {
		String hql = "from Staff s where 1=1";
		if(null != parentId){
			hql += " and s.parent.id = "+ parentId ;
		}
		hql += " order by s.assignTime desc";
		return staffDao.outJSONData(hql, start, limit);
	}
	
	public List<Staff> findByParent(User user){
		String hql = "from Staff s where s.parent.id =:id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", user.getId());
		return staffDao.findList(hql, params);
	}

	@Override
	public Staff findByUserName(String userName,Integer parentId) {
		String hql = "from Staff s where s.userName = '"+ userName+"'";
		if(null != parentId){
			hql += " and s.parent.id = "+ parentId + " order by s.assignTime desc";
		}
		return staffDao.get(hql);
	}
	
	@Override
	public Long getTotal(Integer parentId){
		String hql = "select count(s.id) from Staff s where s.parent.id = "+ parentId;
		return (Long)staffDao.uniqueResult(hql);
	}

	@Override
	public Staff findByStid(String stid) {
		String hql = "from Staff s where s.stid =:stid";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stid", stid);
		List<Staff> staffs = staffDao.findList(hql, params);
		return staffs.size() == 0?null:staffs.get(0);
	}

	@Override
	public Staff findByUserName(String userName) {
		String hql = "from Staff s where s.userName = '"+ userName+"'";
//		if(null != parentId){
//			hql += " and s.parent.id = "+ parentId + " order by s.assignTime desc";
//		}
		return staffDao.get(hql);
	}
	
	@Override
	public List<Staff> find() {
		// TODO Auto-generated method stub
		return staffDao.findList("from Staff");
	}
}
