package com.eaglec.win.biz.impl.user;

import org.springframework.stereotype.Service;

import com.eaglec.win.dao.impl.BaseDaoImpl;
import com.eaglec.win.dao.user.UserApproveDao;
import com.eaglec.win.domain.base.UserApprovedDetail;

@Service
public class UserApproveDaoImpl extends BaseDaoImpl<UserApprovedDetail> implements UserApproveDao  {
}
