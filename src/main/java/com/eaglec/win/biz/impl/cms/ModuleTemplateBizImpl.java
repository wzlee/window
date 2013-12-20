package com.eaglec.win.biz.impl.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaglec.win.biz.cms.ModuleTemplateBiz;
import com.eaglec.win.dao.cms.ModuleTemplateDao;
import com.eaglec.win.domain.cms.ModuleTemplate;

@Service
public class ModuleTemplateBizImpl implements ModuleTemplateBiz {
	
	@Autowired
	private ModuleTemplateDao moduleTemplateDao;

	@Override
	public List<ModuleTemplate> findAll() {
		return moduleTemplateDao.findList("from ModuleTemplate");
	}

	@Override
	public void add(ModuleTemplate m) {
		moduleTemplateDao.save(m);
	}

	@Override
	public void delete(ModuleTemplate m) {
		moduleTemplateDao.delete(m);
	}

	@Override
	public void update(ModuleTemplate m) {
		moduleTemplateDao.update(m);
	}

}
