package com.eaglec.win.biz.info;

import java.util.List;

import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.Staff;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.info.SenderMessageRelationship;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONRows;


public interface SenderMessageRelationshipBiz {
	//添加
	public SenderMessageRelationship save(SenderMessageRelationship sendermessagerelationship);
	//查询
	public List<SenderMessageRelationship> find();
	//修改
	public SenderMessageRelationship update(SenderMessageRelationship sendermessagerelationship);
	//删除
	public void delete(SenderMessageRelationship sendermessagerelationship);
	//查找消息根据发送人
	public JSONData<Object> find(Manager manger,Integer type,int start,int limit);
	public JSONRows<Object> find(User user,Integer type,int start,int limit);
	public JSONRows<Object> find(Staff staff,Integer type,int start,int limit);
	//根据发送人，要删除的已发送消息更新成已删除
	public int update(Manager manager ,String ids);
	public int update(User user ,String ids);
	public int update(Staff staff ,String ids);
	
}
