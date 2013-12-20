package com.eaglec.win.biz.impl.flat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eaglec.win.biz.flat.FlatBiz;
import com.eaglec.win.dao.flat.FlatDao;
import com.eaglec.win.domain.base.Flat;
import com.eaglec.win.view.JSONData;

@Service
public class FlatBizImpl implements FlatBiz {

	@Autowired
	private FlatDao flatDao;
	
	/**
	 * 添加或者修改平台信息
	 * @author liuliping
	 * @since 2013-10-12
	 * @param flat 平台信息对象
	 * @return JSONResult 返回json格式的结果信息
	 * */
	public void addOrUpdate(Flat flat) {
		flatDao.saveOrUpdate(flat);
	}

	/**
	 * 通过平台名称查询平台信息
	 * @author liuliping
	 * @since 2013-10-12
	 * @param flatName 平台名称
	 * @return JSONData 返回json格式数据
	 * */
	public JSONData<Flat> queryByFlatName(String flatName) {
		StringBuilder sb = new StringBuilder();
		sb.append("FROM Flat WHERE 1=1 ");
		if (!StringUtils.isEmpty(flatName)) {
			sb.append(" AND flatName = '").append(flatName).append("'");
		}
		return flatDao.outJSONData(sb.toString(), 0, 16);    //平台数量为1+15
	}
	
}
