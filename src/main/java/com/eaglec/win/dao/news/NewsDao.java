package com.eaglec.win.dao.news;

import java.util.List;

import com.eaglec.win.dao.BaseDao;
import com.eaglec.win.domain.cms.News;

public interface NewsDao extends BaseDao<News>{
	public News get(int id);

	public News addNews(News news);

	public void deleteNews(News news);

	public void updateNews(News news);

	public List<News> findNewsById(int newsNo);

	// public List<News> getList();
	public List<News> getNewsList(int start, int limit);

	public List<News> getNewsList(String hql, int start, int limit);

	public List<News> getNewsList(String hql);

	public long getCount(String hql);

}
