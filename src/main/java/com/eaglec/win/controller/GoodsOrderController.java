package com.eaglec.win.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eaglec.win.aop.NeedSession;
import com.eaglec.win.aop.SessionType;
import com.eaglec.win.biz.business.AppealBiz;
import com.eaglec.win.biz.business.BiddingServiceBiz;
import com.eaglec.win.biz.business.BiddingServiceDetailBiz;
import com.eaglec.win.biz.business.GoodsOrderBiz;
import com.eaglec.win.biz.business.OrderEvaluationBiz;
import com.eaglec.win.biz.business.OrderInfoBiz;
import com.eaglec.win.biz.business.OrderOperateLogBiz;
import com.eaglec.win.biz.business.ResponseBiddingBiz;
import com.eaglec.win.biz.service.ServiceBiz;
import com.eaglec.win.biz.user.EnterpriseBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.base.User;
import com.eaglec.win.domain.business.Appeal;
import com.eaglec.win.domain.business.BiddingService;
import com.eaglec.win.domain.business.BiddingServiceDetail;
import com.eaglec.win.domain.business.GoodsOrder;
import com.eaglec.win.domain.business.OrderEvaluation;
import com.eaglec.win.domain.business.OrderInfo;
import com.eaglec.win.domain.business.OrderOperateLog;
import com.eaglec.win.domain.service.Service;
import com.eaglec.win.sync.DataSync;
import com.eaglec.win.utils.Common;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.StrUtils;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;
import com.eaglec.win.view.OrderAndBiddingAppeal;

/**
 * 服务订单控制层
 * @author xuwf
 * @since 2013-9-11
 *
 */
@Controller
@RequestMapping(value="/order")
public class GoodsOrderController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(GoodsOrderController.class);
	
	@Autowired
	private GoodsOrderBiz orderBiz;
	@Autowired
	private ServiceBiz serviceBiz; 
	@Autowired
	private OrderInfoBiz orderInfoBiz;
	@Autowired
	private OrderOperateLogBiz orderOperLogBiz;
	@Autowired
	private OrderEvaluationBiz orderEvaluationBiz;
	@Autowired
	private AppealBiz appealBiz;
	@Autowired
	private BiddingServiceBiz biddingServiceBiz;
	@Autowired 
	private BiddingServiceDetailBiz biddingServiceDetailBiz;
	@Autowired
	private ResponseBiddingBiz responseBiddingBiz;
	@Autowired
	private EnterpriseBiz enterpriseBiz;
	
	/**
	 * 服务申请功能
	 * @author xuwf
	 * @since 2013-9-10
	 * 
	 * @param id		服务id
	 * @param linkMan	联系人
	 * @param tel		电话
	 * @param email		邮箱
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.USER)
	@RequestMapping(value="applyService")
	public void applyService(Integer id,String linkMan,String tel,String email,String remark,
			HttpServletRequest request,HttpServletResponse response){
		try {
			Service service = serviceBiz.findServiceById(id);	//申请的服务
			GoodsOrder order = null;					//申请服务产生一张订单
			OrderInfo orderInfo = null;					//订单流水
			OrderOperateLog orderOperateLog = null;		//订单操作日志
			//订单编号的生成规则
			String orderNumber = "S"+StrUtils.formateDate("YYYYMMdd", new Date())+Common.random();
			//当前时间作为订单下单时间
			String currentTime = StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date());
			//登录用户(主账号登录即操作者)
			if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
				User user = (User)request.getSession().getAttribute("user");
				
				order = new GoodsOrder(orderNumber, Constant.WAIT_SELLER_CONFIRM, 
					service.getCostPrice(), linkMan, tel, email, remark, currentTime, user, 
					user.getEnterprise().getId(), service.getEnterprise().getId(),
					service.getServiceName(), service, Constant.ORDER_SOURCE_S);
				//添加订单详细信息
				orderInfo = new OrderInfo(order, order.getOrderStatus(), 
						remark,user,currentTime,Constant.ACTION_BUYER_ORDER_SUBMIT);
				//添加订单操作日志
				orderOperateLog = new OrderOperateLog(order.getOrderNumber(),
						user.getUserName(),Constant.WAIT_SELLER_CONFIRM_STR,
						StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
			}else{//子账号登录
//				Staff staff = (Staff) request.getSession().getAttribute("user");
//				if(staff.getStaffRole().isApply()){//可以申请服务
//					order = new GoodsOrder(orderNumber, Constant.WAIT_SELLER_CONFIRM, 
//							service.getCostPrice(), linkMan, tel, email, remark, currentTime, staff, 
//							staff.getParent().getEnterprise().getId(), service.getEnterprise().getId(),
//							service.getServiceName(), service, Constant.ORDER_SOURCE_S);
//					//添加订单详细信息
//					orderInfo = new OrderInfo(order, order.getOrderStatus(), 
//							remark,staff,currentTime,Constant.ACTION_BUYER_ORDER_SUBMIT);
//					//添加订单操作日志
//					orderOperateLog = new OrderOperateLog(order.getOrderNumber(),
//							staff.getUserName(),Constant.WAIT_SELLER_CONFIRM_STR,
//							StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
//				}else{
//					response.sendRedirect("/error/nosession?type=zizhanghaowuquanxian");
//				}
			}

			orderBiz.saveGoodsOrder(order);
			orderInfoBiz.saveOrderInfo(orderInfo);
			logger.info("添加订单详细信息");		
			orderOperLogBiz.saveOrderOperLog(orderOperateLog);
			logger.info("添加订单操作日志");
				
			this.outJson(response,new JSONResult(true,"申请服务成功!"));
			logger.info("[ "+order.getOrderNumber()+" ]添加成功!");
		} catch (Exception e) {
			this.outJson( response, new JSONResult(false, 
					"申请服务失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("申请服务失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 关闭招单交易
	 * @author xuwf
	 * @since 2013-10-11
	 * 
	 * @param flag	买家0，卖家1
	 * @param bidId
	 * @param satisfaction
	 * @param remark
	 * @param request
	 * @param response
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/closeBidding")
	public void closeBidding(Integer flag,Integer bidId,Integer satisfaction,
			String remark,HttpServletRequest request,HttpServletResponse response) {
		try {	
			//当前时间
			String currentTime = StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date());
			//登录用户(即操作者)
			User user = (User)request.getSession().getAttribute("user");
			
			BiddingService biddingService = biddingServiceBiz.findBiddingServiceById(bidId);
			//添加订单评价
			OrderEvaluation orderEvaluation = new OrderEvaluation(remark, currentTime, user,
					biddingService, satisfaction);
			orderEvaluationBiz.saveOrderEvaluation(orderEvaluation);
			logger.info("添加订单评价");
			
			BiddingServiceDetail biddingServiceDetail = null;//招单流水信息
			
			if(biddingService.getStatus() == Constant.BIDDING_TRADE_ING){//招单状态是交易进行中，证明买家和卖家都没有关闭
				if(flag == Constant.FLAG_SELLER){//卖家登录
					//订单关闭交易后状态改为"等待买家关闭"
					biddingService.setStatus(Constant.BIDDING_BUYER_CLOSING);
					biddingServiceBiz.addOrModify(biddingService); //更新招单状态
					
					biddingServiceDetail = new BiddingServiceDetail(biddingService, 
							biddingService.getStatus(), remark, user, currentTime, Constant.ACTION_SELLER_CLOSE_BIDDING);
					
				}else if(flag == Constant.FLAG_BUYER){//买家登录
					//订单关闭交易后状态改为"等待卖家关闭"
					biddingService.setStatus(Constant.BIDDING_SELLER_CLOSING);
					biddingServiceBiz.addOrModify(biddingService); //更新招单状态
					
					biddingServiceDetail = new BiddingServiceDetail(biddingService, 
							biddingService.getStatus(), remark, user, currentTime, Constant.ACTION_BUYER_CLOSE_BIDDING);
				}
			}else{	//订单状态为等待买家或卖家关闭时,下一步都是交易结束
				//订单关闭交易后状态改为"交易结束"
				biddingService.setStatus(Constant.BIDDING_TRADE_END);
				biddingServiceBiz.addOrModify(biddingService); //更新招单状态
				
				if(flag == Constant.FLAG_SELLER){//卖家登录
					biddingServiceDetail = new BiddingServiceDetail(biddingService, 
							biddingService.getStatus(), remark, user, currentTime,
							Constant.ACTION_SELLER_CLOSE_BIDDING);
				}else if(flag == Constant.FLAG_BUYER){//买家登录
					biddingServiceDetail = new BiddingServiceDetail(biddingService, 
							biddingService.getStatus(), remark, user, currentTime,
							Constant.ACTION_BUYER_CLOSE_BIDDING);
				}
			}
			
			biddingServiceDetailBiz.add(biddingServiceDetail);
			logger.info("添加招单流水信息");

			this.outJson(response,new JSONResult(true,"订单【"+biddingService.getBidNo()+"】关闭交易成功!"));
			logger.info("订单[ "+biddingService.getBidNo()+" ]关闭交易成功!");
		} catch (Exception e) {
			this.outJson(response,new JSONResult(false,"交易关闭失败!异常信息:"+e.getLocalizedMessage()));
			logger.info("交易关闭失败!异常信息:"+e.getLocalizedMessage());
		}	
	}
	 */
	
	/**
	 * 订单申诉
	 * @author xuwf
	 * @since 2013-9-16
	 * @param flag			买家0,卖家1
	 * @param orderId		订单id
	 * @param attachment	附件路径
	 * @param reason		申诉原因
	 * @param remark		申诉备注
	 * @param request
	 * @param response
	@NeedSession(SessionType.USER)
	@RequestMapping(value = "/orderappeal")
	public void orderAppeal(int flag,Integer orderId,String attachment,Integer reason,
			String remark,HttpServletRequest request,HttpServletResponse response){
		try {
			GoodsOrder goodsOrder = orderBiz.findById(orderId);	
			
			//订单提交申诉后状态改为"申诉处理中"
			goodsOrder.setOrderStatus(Constant.APPEAL_PROCESSING);
			orderBiz.updateGoodsOrder(goodsOrder);//更新订单状态
			
			//提交申诉后添加申诉单
			Appeal appeal = null;
			OrderInfo orderInfo = null;	//订单详细信息
			if(flag == Constant.FLAG_BUYER){//买家申诉
				//主账号登录
				if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
					User user = (User)request.getSession().getAttribute("user");
					appeal = new Appeal(goodsOrder, attachment, remark, reason,Constant.APPEAL_TYPE_BUYER,
						user,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
				}else{//子账号登录
					Staff staff = (Staff)request.getSession().getAttribute("user");
					appeal = new Appeal(goodsOrder, attachment, remark, reason,Constant.APPEAL_TYPE_BUYER,
							staff,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
				}
				if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_B){//来源招单服务
					//修改招单服务的状态和插入招标的流水----
					BiddingService biddingService = goodsOrder.getBiddingService();
					biddingService.setStatus(Constant.BIDDING_APPEAL_DEALING);
					biddingServiceBiz.addOrModify(biddingService);
					logger.info("修改招单服务状态");
					BiddingServiceDetail biddingServiceDetail =null;
					//主账号登录
					if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
						User user = (User)request.getSession().getAttribute("user");
						biddingServiceDetail= new BiddingServiceDetail(biddingService, 
								biddingService.getStatus(), remark, user, StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()), Constant.ACTION_BUYER_APPEAL_BIDDING);
					}else{//子账号登录
						Staff staff = (Staff)request.getSession().getAttribute("user");
						biddingServiceDetail= new BiddingServiceDetail(biddingService, 
								biddingService.getStatus(), remark, staff, StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()), Constant.ACTION_BUYER_APPEAL_BIDDING);
					}
					biddingServiceDetailBiz.add(biddingServiceDetail);
					logger.info("新增招单流水");
				}
				//主账号登录
				if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
					User user = (User)request.getSession().getAttribute("user");
					orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), 
						remark,user,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()),
						Constant.ACTION_BUYER_APPEAL);
				}else{//子账号登录
					Staff staff = (Staff)request.getSession().getAttribute("user");
					orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), 
						remark,staff,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()),
						Constant.ACTION_BUYER_APPEAL);
				}
			}else if(flag == Constant.FLAG_SELLER){//卖家申诉
				//主账号登录
				if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
					User user = (User)request.getSession().getAttribute("user");
					appeal = new Appeal(goodsOrder, attachment, remark, reason,Constant.APPEAL_TYPE_SELLER,
						user,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
				}else{//子账号登录
					Staff staff = (Staff)request.getSession().getAttribute("user");
					appeal = new Appeal(goodsOrder, attachment, remark, reason,Constant.APPEAL_TYPE_SELLER,
						staff,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
				}
				
				if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_B){//来源招单服务
					//修改招单服务的状态和插入招标的流水----
					BiddingService biddingService = goodsOrder.getBiddingService();
					biddingService.setStatus(Constant.BIDDING_APPEAL_DEALING);
					biddingServiceBiz.addOrModify(biddingService);
					logger.info("修改招单服务状态");
					BiddingServiceDetail biddingServiceDetail = null;
					//主账号登录
					if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
						User user = (User)request.getSession().getAttribute("user");
						biddingServiceDetail = new BiddingServiceDetail(biddingService, 
							biddingService.getStatus(), remark, user, StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()), Constant.ACTION_SELLER_APPEAL_BIDDING);
					}else{//子账号登录
						Staff staff = (Staff)request.getSession().getAttribute("user");
						biddingServiceDetail = new BiddingServiceDetail(biddingService, 
							biddingService.getStatus(), remark, staff, StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()), Constant.ACTION_SELLER_APPEAL_BIDDING);
					}
					
					biddingServiceDetailBiz.add(biddingServiceDetail);
					logger.info("新增招单流水");
				}
				//主账号登录
				if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
					User user = (User)request.getSession().getAttribute("user");
					orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), 
						remark,user,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()),
						Constant.ACTION_SELLER_APPEAL);
				}else{//子账号登录
					Staff staff = (Staff)request.getSession().getAttribute("user");
					orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), 
						remark,staff,StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()),
						Constant.ACTION_SELLER_APPEAL);
				}
			}
			appealBiz.saveAppeal(appeal);
			logger.info("添加申诉信息");
			orderInfoBiz.saveOrderInfo(orderInfo);
			logger.info("添加订单详细信息");

			//添加订单操作日志
			OrderOperateLog orderOperateLog = null;
			//主账号登录
			if(request.getSession().getAttribute("usertype").equals(Constant.LOGIN_USER)){
				User user = (User)request.getSession().getAttribute("user");
				orderOperateLog = new OrderOperateLog(goodsOrder.getOrderNumber(),
						user.getUserName(),Constant.APPEAL_PROCESSING_STR,
						StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
			}else{//子账号登录
				Staff staff = (Staff)request.getSession().getAttribute("user");
				orderOperateLog = new OrderOperateLog(goodsOrder.getOrderNumber(),
						staff.getUserName(),Constant.APPEAL_PROCESSING_STR,
						StrUtils.formateDate("YYYY-MM-dd HH:mm:ss", new Date()));
			}
			orderOperLogBiz.saveOrderOperLog(orderOperateLog);
			logger.info("添加订单操作日志");
				
			this.outJson(response,new JSONResult(true,"订单【"+goodsOrder.getOrderNumber()+"】提交成功!"));
			logger.info("[ "+goodsOrder.getOrderNumber()+" ]提交申诉成功!");

		} catch (Exception e) {
			this.outJson(response, new JSONResult(false, 
					"提交申诉失败!异常信息:" + e.getLocalizedMessage()));
			logger.info("提交申诉失败!异常信息:" + e.getLocalizedMessage());
		}
	}
	*/
	
	/**
	 * 查询订单，按状态、订单状态、下单时间（模糊查询）查询(支撑运营平台)
	 * 
	 * @author xuwf
	 * @since 2013-9-24
	 * 
	 * @param status		服务状态
	 * @param start			起始值
	 * @param limit			每页条数
	 * @param request	
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/find")
	public void find(@RequestParam(value="orderStatus",required=false)String orderStatus,
			@RequestParam(value="orderNumber",required=false)String orderNumber,
			@RequestParam(value="serviceName",required=false)String serviceName,
			@RequestParam(value="startTime",required=false)String startTime,
			@RequestParam(value="endTime",required=false)String endTime,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="20", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		if(null == orderStatus){
			orderStatus = "";
		}
		JSONData<GoodsOrder> orders = orderBiz.queryOrder(orderNumber, serviceName,orderStatus, startTime, endTime, start, limit);
		this.outJson(response,orders);
	}
	

	/**
	 * 订单处理 - 支撑运营平台
	 *@author xuwf
	 *@since 2013-9-24
	 *
	 *@param goodsOrder 
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/edit")
	public void update(Integer appealId,Integer orderId,Integer handlerMode,
			String handlerRemark,HttpServletRequest request,HttpServletResponse response) {
			//得到订单
			GoodsOrder goodsOrder = orderBiz.findById(orderId);
			
			//平台人员处理订单
			Manager manager = (Manager) request.getSession().getAttribute("manager");
			//申诉单
			Appeal appeal;
			try {
				if(handlerMode == Constant.HANDLER_MODE_CLOSE){//处理方式：关闭订单
					OrderInfo orderInfo;
					//添加订单操作
					OrderOperateLog orderOperateLog = new OrderOperateLog(goodsOrder.getOrderNumber(), 
							manager.getUsername(),Constant.TRANSACTIONS_END_STR,  StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()));
					orderOperLogBiz.saveOrderOperLog(orderOperateLog);
					
					if(!StringUtils.isEmpty(appealId) || goodsOrder.getOrderStatus() == Constant.APPEAL_PROCESSING){//申诉处理对订单的操作
						//添加订单详情:申诉处理，关闭订单
						//修改订单状态并保存
						goodsOrder.setOrderStatus(Constant.TRANSACTIONS_END);
						orderBiz.updateGoodsOrder(goodsOrder);
						
						orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), handlerRemark,
								manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),
								Constant.ACTION_ORDER_APPEAL_CLOSE);
						orderInfoBiz.saveOrderInfo(orderInfo);
						
						if(StringUtils.isEmpty(appealId)){
							appeal = appealBiz.queryByOrderId(orderId);
						}else{
							appeal = appealBiz.findById(appealId);
						}
						//为申诉单完善资料
						appeal.setAppealStatus(Constant.APPEAL_STATUS＿CLOSE_DEAL);
						appeal.setManager(manager);
						appeal.setProcessMode(Constant.HANDLER_MODE_CLOSE);
						appeal.setProcessTime(StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()));
						appeal.setHandlerRemark(handlerRemark);
						appealBiz.update(appeal);
						
						//同步申诉到枢纽平台
						DataSync.createDataSync("appeal", "aid", appeal.getAid(), "false");
						
						if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_B){//来源招单服务
							//修改招单服务的状态和插入招标的流水----
							BiddingService biddingService = goodsOrder.getBiddingService();
							biddingService.setStatus(Constant.BIDDING_TRADE_END);
							biddingServiceBiz.addOrModify(biddingService);
							logger.info("修改招单服务状态");
							BiddingServiceDetail biddingServiceDetail = new BiddingServiceDetail(biddingService, 
									biddingService.getStatus(), handlerRemark, manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()), Constant.ACTION_CLOSE_APPEAL_BIDDING);
							biddingServiceDetailBiz.add(biddingServiceDetail);
							logger.info("新增招单流水");
						}
					}else{//管理员关闭订单操作
						//修改订单状态并保存
						goodsOrder.setOrderStatus(Constant.TRANSACTIONS_END);
						orderBiz.updateGoodsOrder(goodsOrder);
						
						//添加订单详情
						orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), handlerRemark,
								manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),
								Constant.ACTION_CLOSE_DEAL);
						orderInfoBiz.saveOrderInfo(orderInfo);

						if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_B){//来源招单服务
							//修改招单服务的状态和插入招标的流水----
							BiddingService biddingService = goodsOrder.getBiddingService();
							biddingService.setStatus(Constant.BIDDING_TRADE_END);
							biddingServiceBiz.addOrModify(biddingService);
							logger.info("修改招单服务状态");
							BiddingServiceDetail biddingServiceDetail = new BiddingServiceDetail(biddingService, 
									biddingService.getStatus(), handlerRemark, manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()), Constant.ACTION_PLAT_CLOSE_BIDDING);
							biddingServiceDetailBiz.add(biddingServiceDetail);
							logger.info("新增招单流水");
						}
					}	
				
					//普通服务才有服务评价(管理员正常关闭或者申诉关闭都默认服务满意度5分)
					if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_S){
						//添加服务的评价交易信息
						Service service = goodsOrder.getService();
						//得到买家的满意度评价,如果为空，默认5分
						Integer buyerSat = (Integer) orderEvaluationBiz.getBuyerSatisfaction(goodsOrder);
						if(StringUtils.isEmpty(buyerSat)){
							buyerSat = Constant.SATISFACTION_VERY;
						}
						//总分=之前的总分+买家满意度
						service.setTotalScore(service.getTotalScore()+buyerSat);
						//服务次数+1
						service.setServiceNum(service.getServiceNum()+1);
						serviceBiz.update(service);
						//服务评价=总分/服务次数
						service.setEvaluateScore(Common.parseDouble(service.getTotalScore()/service.getServiceNum()));
						serviceBiz.update(service);
						
						//同步服务到枢纽平台
						DataSync.createDataSync("service", "sid", service.getSid(), "false");
					}	
			/***************************买家卖家信誉信息****************************
					//得到买家企业
					Enterprise buyEnterprise = enterpriseBiz.findByEid(goodsOrder.getPurchaser_id());
					
					//得到买家企业的信誉表
					EnterpriseCredit ec = enterpriseCreditBiz.queryByEnterprise(buyEnterprise.getId());
					Integer buyCount = 0;	//购买次数
					Integer buyScore = 0;	//购买总评分
					double buyCredit = 0;	//买家信誉	= 购买总评分/购买次数
					if(!StringUtils.isEmpty(ec)){//之前存在该企业信誉信息
						if(!StringUtils.isEmpty(ec.getBuyCount())){//购买服务次数
							buyCount = ec.getBuyCount()+ 1;
						}else{
							buyCount = 1;
						}
						if(!StringUtils.isEmpty(ec.getBuyScore())){//购买总评分
							buyScore = ec.getBuyScore() + Constant.SATISFACTION_VERY;
						}else{
							buyScore = Constant.SATISFACTION_VERY;
						}
						buyCredit = (double)buyScore/buyCount;
					}else{//不存在该企业的企业信誉信息
						buyCount = 1;
						buyScore = Constant.SATISFACTION_VERY;
						buyCredit = (double)buyScore/buyCount;
						//添加买家企业信誉
						ec = new EnterpriseCredit();
					}
					ec.setBuyCount(buyCount);
					ec.setBuyScore(buyScore);
					ec.setBuyCredit(Common.parseDouble(buyCredit));
					ec.setEnterprise(buyEnterprise);
					enterpriseCreditBiz.saveEnterpriseCredit(ec);
					//得到卖家企业
					Enterprise sellEnterprise = enterpriseBiz.findByEid(goodsOrder.getSeller_id());
					//得到卖家企业的信誉表
					EnterpriseCredit sellEc = enterpriseCreditBiz.queryByEnterprise(sellEnterprise.getId());
					Integer sellCount = 0;	//卖出次数
					Integer sellScore = 0;	//卖出总评分
					double sellCredit = 0;	//卖家信誉
					if(!StringUtils.isEmpty(sellEc)){//之前存在该企业信誉信息
						if(!StringUtils.isEmpty(sellEc.getSellCount())){//购买服务次数
							sellCount = sellEc.getSellCount()+ 1;
						}else{
							sellCount = 1;
						}
						if(!StringUtils.isEmpty(sellEc.getSellScore())){//购买总评分
							sellScore = sellEc.getSellScore() + Constant.SATISFACTION_VERY;
						}else{
							sellScore = Constant.SATISFACTION_VERY;
						}
						sellCredit = (double)sellScore/sellCount;
					}else{//不存在该企业的企业信誉信息
						sellCount = 1;
						sellScore = Constant.SATISFACTION_VERY;
						sellCredit = (double)sellScore/sellCount;
						//添加买家企业信誉
						sellEc = new EnterpriseCredit();
					}
					sellEc.setSellCount(sellCount);
					sellEc.setSellScore(sellScore);
					sellEc.setSellCredit(Common.parseDouble(sellCredit));
					sellEc.setEnterprise(sellEnterprise);
					enterpriseCreditBiz.saveEnterpriseCredit(sellEc);
			 	**************************买家卖家信誉信息*****************************/
					
					//同步订单至枢纽
					DataSync.createDataSync("goodsorder", "oid", goodsOrder.getOid(), "false");
					DataSync.createDataSync("orderinfo", "oiid", orderInfo.getOiid(), "false");
					
				}else if(handlerMode == Constant.HANDLER_MODE_CANCEL){//处理方式：取消订单
					OrderInfo orderInfo;//订单流水
					
					//添加订单操作
					OrderOperateLog orderOperateLog = new OrderOperateLog(goodsOrder.getOrderNumber(), 
							manager.getUsername(),Constant.ORDER_CANCEL_STR,  StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()));
					orderOperLogBiz.saveOrderOperLog(orderOperateLog);
					
					if(!StringUtils.isEmpty(appealId) || goodsOrder.getOrderStatus() == Constant.APPEAL_PROCESSING){//申诉处理对订单的操作
						//修改订单状态保存
						goodsOrder.setOrderStatus(Constant.ORDER_CANCEL);
						orderBiz.updateGoodsOrder(goodsOrder);
						//添加订单详情
						orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), handlerRemark,
								manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),
								Constant.ACTION_ORDER_APPEAL_CANCEL);
						orderInfoBiz.saveOrderInfo(orderInfo);
						
						if(StringUtils.isEmpty(appealId)){
							appeal = appealBiz.queryByOrderId(orderId);
						}else{
							appeal = appealBiz.findById(appealId);
						}
						//为申诉单完善资料
						appeal.setAppealStatus(Constant.APPEAL_STATUS_ORDER_CANCEL);
						appeal.setManager(manager);
						appeal.setProcessMode(Constant.HANDLER_MODE_CANCEL);
						appeal.setProcessTime(StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()));
						appeal.setHandlerRemark(handlerRemark);
						appealBiz.update(appeal);
						
						//同步申诉到枢纽
						DataSync.createDataSync("appeal", "aid", appeal.getAid(), "false");
						
						if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_B){//来源招单服务
							//修改招单服务的状态和插入招标的流水----
							BiddingService biddingService = goodsOrder.getBiddingService();
							biddingService.setStatus(Constant.BIDDING_ORDER_CANCEL);
							biddingServiceBiz.addOrModify(biddingService);
							logger.info("修改招单服务状态");
							BiddingServiceDetail biddingServiceDetail = new BiddingServiceDetail(biddingService, 
									biddingService.getStatus(), handlerRemark, manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()), Constant.ACTION_CANCEL_APPEAL_BIDDING);
							biddingServiceDetailBiz.add(biddingServiceDetail);
							logger.info("新增招单流水");
						}
					}else{//管理员取消订单操作
						//修改订单状态并保存
						goodsOrder.setOrderStatus(Constant.ORDER_CANCEL);
						orderBiz.updateGoodsOrder(goodsOrder);
						//添加订单详情
						orderInfo = new OrderInfo(goodsOrder, goodsOrder.getOrderStatus(), handlerRemark,
								manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),
								Constant.ACTION_MANAGER_ORDER_CANCEL);
						orderInfoBiz.saveOrderInfo(orderInfo);
						
						if(goodsOrder.getOrderSource() == Constant.ORDER_SOURCE_B){//来源招单服务
							//修改招单服务的状态和插入招标的流水----
							BiddingService biddingService = goodsOrder.getBiddingService();
							biddingService.setStatus(Constant.BIDDING_ORDER_CANCEL);
							biddingServiceBiz.addOrModify(biddingService);
							logger.info("修改招单服务状态");
							BiddingServiceDetail biddingServiceDetail = new BiddingServiceDetail(biddingService, 
									biddingService.getStatus(), handlerRemark, manager, StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()), Constant.ACTION_PLAT_CANCEL_BIDDING_ORDER);
							biddingServiceDetailBiz.add(biddingServiceDetail);
							logger.info("新增招单流水");
						}
					}
					
					//同步订单至枢纽
					DataSync.createDataSync("goodsorder", "oid", goodsOrder.getOid(), "false");
					DataSync.createDataSync("orderinfo", "oiid", orderInfo.getOiid(), "false");
				}
				logger.info("[ "+goodsOrder.getOrderNumber()+" ]处理成功!");
				this.outJson(response,new JSONResult(true,"订单[" + goodsOrder.getOrderNumber() + "]处理成功"));
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"订单[" + goodsOrder.getOrderNumber() + "]处理失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("订单处理失败!异常信息:"+e.getLocalizedMessage());
			}
	}
	
	/**
	 * 查询申诉处理中的订单	支撑平台
	 * @author xuwf
	 * @since 2013-9-25
	 * 
	 * @param orderStatus		订单状态
	 * @param appealType		申诉类型1.买家 申诉2.卖家申诉
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findAppeal")
	public void findAppeal(@RequestParam(value="orderStatus",required=false)Integer orderStatus,
			@RequestParam(value="orderNumber",required=false)String orderNumber,
			@RequestParam(value="appealType",required=false)String appealType,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="20", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONData<Appeal> appeals = appealBiz.findByStatus(orderStatus,orderNumber,appealType, start, limit);
		this.outJson(response,appeals);
	}
	
	/**
	 * 申诉查询		支撑平台
	 * @author xuwf
	 * @since 2013-9-26
	 * 
	 * @param orderNum		订单编号
	 * @param appealType	申诉类型1.买家申诉 2.卖家申诉
	 * @param startTime		处理开始时间
	 * @param endTime		结束时间
	 * @param buyer			买家
	 * @param seller		卖家
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/qAppealList")
	public void queryIntegrated(@RequestParam(value="orderNumber",required=false)String orderNum,
			@RequestParam(value="appealType",required=false)String appealType,
			@RequestParam(value="startTime",required=false)String startTime,
			@RequestParam(value="endTime",required=false)String endTime,
			@RequestParam(value="buyer",required=false)String buyer,
			@RequestParam(value="seller",required=false)String seller,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="20", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		JSONData<OrderAndBiddingAppeal> appeals = appealBiz.queryIntegrated(orderNum, appealType, startTime,
				endTime, buyer, seller, start, limit);
		this.outJson(response,appeals);
	}
	
	/**
	 * 根据订单id查看该订单的流水信息
	 * @param orderId		订单id
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findOrderInfo")
	public void findOrderInfo(
			@RequestParam(value="orderId",required=false)Integer orderId,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="100", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONData<OrderInfo> jdInfo = orderInfoBiz.findOrderInfo(orderId, start, limit);
		this.outJson(response, jdInfo);
	}
	
	/**
	 * 根据订单id查看该订单的评价信息
	 * @param orderId		订单id
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findEval")
	public void findEval(
			@RequestParam(value="orderId",required=false)Integer orderId,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="100", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONData<OrderEvaluation> jdEval = orderEvaluationBiz.findEval(orderId, start, limit);
		this.outJson(response, jdEval);
	}
}
