package com.eaglec.win;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.eaglec.win.biz.auth.ManagerBiz;
import com.eaglec.win.biz.auth.MenuBiz;
import com.eaglec.win.biz.auth.RoleBiz;
import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.biz.service.ServiceBiz;
import com.eaglec.win.domain.auth.Menu;
import com.eaglec.win.utils.MD5;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={
//		"file:src/main/resources/shiro.xml", 
        "file:src/main/webapp/WEB-INF/spring/*.xml", 
        "file:src/main/webapp/WEB-INF/spring/appServlet/*.xml"})
public class HibernateTest {

	@Test
	public void test() {
		System.out.println(MD5.toMD5("chens"));
	}

//	@Autowired
//	HibernateTemplate hibernateTemplate;
	
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private ManagerBiz userBiz;
	@Autowired
	private RoleBiz roleBiz;
	@Autowired
	private MenuBiz rightsBiz;
	@Autowired
	private CategoryBiz categoryBiz;
	@Autowired
	private ServiceBiz serviceBiz;
	
	@Test
	public void hibernamteTest(){
//		System.out.println(userBiz.saveUser(new Manager(null,"ctest1", MD5.toMD5("ctest1"),null, null,"测试没有外键1",null,2,null)));
		List<Integer> menuids = new ArrayList<Integer>();
//		menuids.add(1);
//		menuids.add(2);
//		menuids.add(3);
		for(Menu menu:rightsBiz.findAll()){
			menuids.add(menu.getId());
		}
//		System.out.println(rightsBiz.findMenus(menuids.toString().substring(1, menuids.toString().length()-1).trim()));
		System.out.println(roleBiz.queryRoleById(15).getMenuIds().toString());
		/*Session s = sessionFactory.openSession();
		s.beginTransaction();
//		List<Menu> list=s.createQuery("Select menu from Role r where r.id=1").list();
		List<Role> list=s.createQuery("from Role u where u.rolename='管理员'").list();
		System.out.println(list.get(0).getMenu());
		s.getTransaction().commit();*/
		
	}
	@Test
	public void Json(){
		List<Menu> list = new ArrayList<Menu>();
		Menu r1 = new Menu();
		r1.setId(1);
		r1.setLeaf(false);
		r1.setCreateTime(new Date());
		r1.setText("权限1");
		r1.setRemark("菜单");
		r1.setPid(0);
		list.add(r1);
		Menu r2 = new Menu();
		r2.setId(2);
		r2.setLeaf(true);
		r2.setCreateTime(new Date());
		r2.setText("权限2");
		r2.setRemark("菜单");
		r2.setPid(1);
		/*List<Menu> l = new ArrayList<Menu>();
		l.add(r2);
		r1.setChildren(l);*/
		list.add(r1);
		list.add(r2);
		
		List<Menu> xxx = new ArrayList<Menu>();
		for(int i = 0 ;i<list.size();i++){
			for(int t =i+1;t<list.size();t++){
				if(list.get(i).getId()==list.get(t).getPid()){
					list.get(i).setLeaf(false);
					if(list.get(i).getChildren()==null){
						List<Menu> tt = new ArrayList<Menu>();
						tt.add(list.get(t));
						list.get(i).setChildren(tt);
					}else{
						list.get(i).getChildren().add(list.get(t));
					}
					xxx.add(list.get(i));
				}
			}
		}
		
		System.out.println(JSON.toJSONString(xxx.toArray()));
	}
	

	@Test
	public void authTest(){
//		Manager u = new Manager();
//		u.setManagername("chens");
//		u.setPassword(MD5.toMD5("chens"));
//		System.out.println(userBiz.findManagerByRoleId(1));
//		System.out.println(userBiz.findManagerByManagername("chens"));
//		System.out.println(userBiz.findManager().get(0).getManagername());
//		System.out.println(roleBiz.queryRole().get(1).getMenu().get(0).getText());
//		System.out.println(JSON.toJSONString(roleBiz.queryRole().toArray(),SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.PrettyFormat));
		//System.out.println(getResult(rightsBiz.queryMenu()).size());
		//System.out.println(JSON.toJSONString(getResult(rightsBiz.queryMenu()),SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.PrettyFormat));
		System.out.println(roleBiz.queryRoleByName("管理员").getRolename());
	}
	
	@Test
	public void serviceBizTest(){
//		List<Service> list = serviceBiz.queryServiceByStatus("新服务");
//		for(Service service:list){
//			System.out.println(service.toString());
//		}
//		System.out.println(serviceBiz.queryServiceByStatus("新服务").size());
//		serviceBiz.add(new Service("101002",1,null,"中小企业发展规划优惠政策2",new Date(),0,"法律政策资讯2","胡惊涛","010-3838438","hjt@gov.cn","7","上门服务","米撒旦法斯蒂芬2","免费","新服务",null));
	}

	public static List<Menu> getResult(List<Menu> src) {
		List<Menu> parents = new ArrayList<Menu>();
//		for (Menu ent : src) {
//			if (!ent.isLeaf()) {
//				Menu result = ent;
//				result.setChildren(new ArrayList<Menu>());
//				parents.add(result);
//			}
//		}
		List<Menu> last = new ArrayList<Menu>();
		for (Menu ent : src) {
			if (ent.getPid()==0||ent.getPid()==1000||ent.getPid()==1001||ent.getPid()==1002||ent.getPid()==1003) {
				ent.setChildren(new ArrayList<Menu>());
				parents.add(ent);
			}else{
				last.add(ent);
			}
		}
		buildTree(parents, last);
		return parents;
	}

	private static void buildTree(List<Menu> parents, List<Menu> others) {
		List<Menu> record = new ArrayList<Menu>();
		for (Iterator<Menu> it = parents.iterator(); it.hasNext();) {
			Menu vi = it.next();
			if (vi.getId() != null) {
				for (Iterator<Menu> otherIt = others.iterator(); otherIt
						.hasNext();) {
					Menu inVi = otherIt.next();
					if (vi.getId().equals(inVi.getPid())) {
						if (null == vi.getChildren()) {
							vi.setChildren(new ArrayList<Menu>());
						}
						vi.getChildren().add(inVi);
						record.add(inVi);
						otherIt.remove();
					}
				}
			}
		}
		if (others.size() == 0) {
			return;
		} else {
			buildTree(record, others);
		}
	}
}
