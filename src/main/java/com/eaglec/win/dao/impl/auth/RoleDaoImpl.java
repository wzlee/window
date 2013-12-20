package com.eaglec.win.dao.impl.auth;

import org.springframework.stereotype.Repository;

import com.eaglec.win.dao.auth.RoleDao;
import com.eaglec.win.dao.impl.BaseDaoImpl;
import com.eaglec.win.domain.auth.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

}
