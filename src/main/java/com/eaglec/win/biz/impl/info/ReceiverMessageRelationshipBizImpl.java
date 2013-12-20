package com.eaglec.win.biz.impl.info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.info.ReceiverMessageRelationshipBiz;
import com.eaglec.win.dao.info.ReceiverMessageRelationshipDao;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.info.ReceiverMessageRelationship;
import com.eaglec.win.view.JSONRows;


@Service
public class ReceiverMessageRelationshipBizImpl implements ReceiverMessageRelationshipBiz{
	
	@Autowired
	private ReceiverMessageRelationshipDao receiverMessageRelationshipDao;
	
	//添加
	public ReceiverMessageRelationship save(ReceiverMessageRelationship receivermessagerelationship){
		return receiverMessageRelationshipDao.save(receivermessagerelationship);
	}
	//查询
	public List<ReceiverMessageRelationship> find(){
		return receiverMessageRelationshipDao.findList("from ReceiverMessageRelationship r"+" order by r.message.sendTime desc");
	}
	//修改
	public ReceiverMessageRelationship update(ReceiverMessageRelationship receivermessagerelationship){
		return receiverMessageRelationshipDao.update(receivermessagerelationship);
	}
	//删除
	public void delete(ReceiverMessageRelationship receivermessagerelationship){
		receiverMessageRelationshipDao.delete(receivermessagerelationship);
	}
	@Override
	public JSONRows<Object> find(User user, Integer type, int start, int limit) {
		return receiverMessageRelationshipDao.outJSONRowsO("select r.message from ReceiverMessageRelationship r where r.receiverUserId="+user.getId()+" and r.deleteSign="+type+" order by r.message.sendTime desc",start,limit);
	}
	@Override
	public JSONRows<Object> find(Staff staff, Integer type, int start, int limit) {
		return receiverMessageRelationshipDao.outJSONRowsO("select r.message from ReceiverMessageRelationship r where r.receiverStaffId="+staff.getId()+" and r.deleteSign="+type+" order by r.message.sendTime desc",start,limit);
	}
	@Override
	public JSONRows<Object> findtype(User user, Integer type, int start, int limit) {
		return receiverMessageRelationshipDao.outJSONRowsO("select r.message,r.readSign from ReceiverMessageRelationship r where r.receiverUserId="+user.getId()+" and r.deleteSign="+type+" order by r.message.sendTime desc",start,limit);
	}
	@Override
	public JSONRows<Object> findtype(Staff staff, Integer type, int start, int limit) {
		return receiverMessageRelationshipDao.outJSONRowsO("select r.message,r.readSign from ReceiverMessageRelationship r where r.receiverStaffId="+staff.getId()+" and r.deleteSign="+type+" order by r.message.sendTime desc",start,limit);
	}
	
	//设置已删除
	@Override
	public int update(User user, String ids) {
		// TODO Auto-generated method stub
		return receiverMessageRelationshipDao.executeHql("update ReceiverMessageRelationship s set s.deleteSign = 1 where s.receiverUserId="+user.getId()+" and s.message.id in("+ids+")",null);
	}
	@Override
	public int update(Staff staff, String ids) {
		// TODO Auto-generated method stub
		return receiverMessageRelationshipDao.executeHql("update ReceiverMessageRelationship s set s.deleteSign = 1 where s.receiverStaffId="+staff.getId()+" and s.message.id in("+ids+")",null);
	}
	
	//设置已读
	@Override
	public int updateyetRead(User user, Integer id) {
		return receiverMessageRelationshipDao.executeHql("update ReceiverMessageRelationship s set s.readSign = 1 where s.receiverUserId="+user.getId()+" and s.message.id ="+id+")",null);
	}
	@Override
	public int updateyetRead(Staff staff, Integer id) {
		return receiverMessageRelationshipDao.executeHql("update ReceiverMessageRelationship s set s.readSign = 1 where s.receiverStaffId="+staff.getId()+" and s.message.id ="+id+")",null);
	}
	
}
