package com.eaglec.win.biz.info;

import java.util.List;

import com.eaglec.win.domain.info.Message;


public interface MessageBiz {
	//添加
	public Message save(Message message);
	//查询
	public List<Message> find();
	//修改
	public Message update(Message message);
	//删除
	public void delete(Message message);
	
}
