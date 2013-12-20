package com.eaglec.win.biz.info;

import java.util.List;

import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.info.ReceiverMessageRelationship;
import com.eaglec.win.view.JSONRows;



public interface ReceiverMessageRelationshipBiz {
	//添加
	public ReceiverMessageRelationship save(ReceiverMessageRelationship receivermessagerelationship);
	//查询
	public List<ReceiverMessageRelationship> find();
	//修改
	public ReceiverMessageRelationship update(ReceiverMessageRelationship receivermessagerelationship);
	//删除
	public void delete(ReceiverMessageRelationship receivermessagerelationship);
	//查找消息根据接收人
//	public JSONData<Object> find(Manager manger,Integer type,int start,int limit);
	public JSONRows<Object> find(User user,Integer type,int start,int limit);
	public JSONRows<Object> find(Staff staff,Integer type,int start,int limit);
	public JSONRows<Object> findtype(User user,Integer type,int start,int limit);
	public JSONRows<Object> findtype(Staff staff,Integer type,int start,int limit);
	//根据接收人，要删除的已发送消息更新成已删除
//	public int update(Manager manager ,String ids);
	public int update(User user ,String ids);
	public int update(Staff staff ,String ids);
	//设置为已读
	public int updateyetRead(User user,Integer id);
	public int updateyetRead(Staff staff,Integer id);
	
}
