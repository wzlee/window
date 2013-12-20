package com.eaglec.win.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eaglec.win.aop.NeedSession;
import com.eaglec.win.aop.SessionType;
import com.eaglec.win.biz.business.AppealBiz;
import com.eaglec.win.biz.business.BiddingServiceBiz;
import com.eaglec.win.biz.business.BiddingServiceDetailBiz;
import com.eaglec.win.biz.business.GoodsOrderBiz;
import com.eaglec.win.biz.business.ResponseBiddingBiz;
import com.eaglec.win.biz.publik.CategoryBiz;
import com.eaglec.win.biz.publik.FileManagerBiz;
import com.eaglec.win.biz.user.StaffBiz;
import com.eaglec.win.biz.user.UserBiz;
import com.eaglec.win.domain.auth.Manager;
import com.eaglec.win.domain.business.Appeal;
import com.eaglec.win.domain.business.BiddingService;
import com.eaglec.win.domain.business.BiddingServiceDetail;
import com.eaglec.win.domain.business.ResponseBidding;
import com.eaglec.win.utils.Constant;
import com.eaglec.win.utils.StrUtils;
import com.eaglec.win.view.JSONData;
import com.eaglec.win.view.JSONResult;

/**
 * 招标/应标 控制层
 * 
 * @author Xiadi
 * @since 2013-9-29
 */
@Controller
@RequestMapping(value="/bidding")
public class BiddingServiceController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BiddingServiceController.class);

	@Autowired
	private BiddingServiceBiz biddingServiceBiz;
	@Autowired
	private ResponseBiddingBiz responseBiddingBiz;
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private BiddingServiceDetailBiz biddingServiceDetailBiz;
	@Autowired
	private AppealBiz appealBiz;
	@Autowired
	private GoodsOrderBiz orderBiz;
	@Autowired
	private FileManagerBiz fileManagerBiz;
	@Autowired
	private CategoryBiz categoryBiz;
	@Autowired
	private StaffBiz staffBiz;
	
	/**
	 * 混合条件查询招标单(支撑运营平台-招标管理)
	 * @author xuwf
	 * @since 2013-10-08
	 * 
	 * @param name			服务名称
	 * @param status		招标状态
	 * @param startTime		创建时间
	 * @param endTime		结束时间
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/query")
	public void find(@RequestParam(value="bidNo",required=false)String bidNo,
			@RequestParam(value="name",required=false)String name,
			@RequestParam(value="status",required=false)String status,
			@RequestParam(value="startTime",required=false)String startTime,
			@RequestParam(value="endTime",required=false)String endTime,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="20", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[ status:"+status+",bidNo:"+bidNo
				+",startTime:"+startTime+",endTime:"+endTime+"]");
		if(null == status){
			status = "";
		}
		JSONData<BiddingService> jdbs = biddingServiceBiz.queryBidding(bidNo, name,status, 
				startTime, endTime, start, limit);
		this.outJson(response, jdbs);
	}
	

	/**
	 * 审核处理 - 支撑运营平台
	 *@author xuwf
	 *@since 2013-10-08
	 *
	 *@param 
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/biddingAudit")
	public void biddingAudit(Integer id,Integer status,
			String remark,HttpServletRequest request,HttpServletResponse response) {
			logger.info("remark:"+remark+"status:"+status+",id:"+id);
			//得到招标单
			BiddingService biddingService = biddingServiceBiz.findBiddingServiceById(id);
			
			//平台人员处理招标单
			Manager manager = (Manager) request.getSession().getAttribute("manager");
			BiddingServiceDetail biddingservicedetail = null;
			try{
				if(status == Constant.BIDDING_DOING){//通过审核状态为： 4招标中
					biddingService.setStatus(status);
					biddingServiceBiz.addOrModify(biddingService);
					
					biddingservicedetail = new BiddingServiceDetail(biddingService,status, remark, manager,
						StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),Constant.ACTION_BIDDING_AUDIT_SUCCESS);
				}else if(status == Constant.BIDDING_PLAT_REJECT){//驳回审核状态为：3平台退回
					biddingService.setStatus(status);
					biddingServiceBiz.addOrModify(biddingService);
					
					biddingservicedetail = new BiddingServiceDetail(biddingService,status, remark, manager,
						StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),Constant.ACTION_BIDDING_AUDIT_FAILED);
				}
				biddingServiceDetailBiz.add(biddingservicedetail);
				logger.info("[ "+biddingService.getBidNo()+" ]审核成功!");
				this.outJson(response,new JSONResult(true,"招标单[" + biddingService.getBidNo() + "]审核成功"));
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"招标单[" + biddingService.getBidNo() + "]审核失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("招标单审核失败!异常信息:"+e.getLocalizedMessage());
			}
	}
	
	/**
	 * 取消招标 - 支撑运营平台
	 *@author xuwf
	 *@since 2013-10-09
	 *
	 *@param 
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/handlerBidding")
	public void handlerBidding(Integer id,Integer status,
			String remark,HttpServletRequest request,HttpServletResponse response) {
			logger.info("remark:"+remark+"status:"+status+",id:"+id);
			//得到招标单
			BiddingService biddingService = biddingServiceBiz.findBiddingServiceById(id);
			
			//平台人员处理招标单
			Manager manager = (Manager) request.getSession().getAttribute("manager");
			BiddingServiceDetail biddingservicedetail = null;
			try{
				biddingService.setStatus(status);
				biddingServiceBiz.addOrModify(biddingService);
					
				biddingservicedetail = new BiddingServiceDetail(biddingService,status, remark, manager,
					StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),Constant.ACTION_PLAT_CANCEL_BIDDING);
				biddingServiceDetailBiz.add(biddingservicedetail);
					
				logger.info("[ "+biddingService.getBidNo()+" ]取消招标成功!");
				this.outJson(response,new JSONResult(true,"招标单[" + biddingService.getBidNo() + "]取消招标成功"));
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"招标单[" + biddingService.getBidNo() + "]处理失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("招标单处理失败!异常信息:"+e.getLocalizedMessage());
			}
	}
	
	/**
	 * 申诉处理招标单 - 支撑运营平台
	 *@author xuwf
	 *@since 2013-10-12
	 *
	 *@param 
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/appealHandlerBidding")
	public void appealHandlerBidding(Integer id,Integer status,
			String remark,HttpServletRequest request,HttpServletResponse response) {
			logger.info("remark:"+remark+"status:"+status+",id:"+id);
			//得到招标单
			BiddingService biddingService = biddingServiceBiz.findBiddingServiceById(id);
			//得到申诉单
			Appeal appeal = appealBiz.queryByBidId(id);
			//平台人员处理招标单
			Manager manager = (Manager) request.getSession().getAttribute("manager");
			BiddingServiceDetail biddingservicedetail = null;
			try{
				if(status == Constant.BIDDING_TRADE_END){//关闭交易后状态为： 10交易结束				
					biddingService.setStatus(status);
					biddingServiceBiz.addOrModify(biddingService);
					
					biddingservicedetail = new BiddingServiceDetail(biddingService,status, remark, manager,
						StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),Constant.ACTION_CLOSE_APPEAL_BIDDING);
					biddingServiceDetailBiz.add(biddingservicedetail);
					
					//完善申诉单信息
					appeal.setAppealStatus(Constant.APPEAL_STATUS＿CLOSE_DEAL);
					appeal.setManager(manager);
					appeal.setProcessMode(Constant.HANDLER_MODE_CLOSE);
					appeal.setProcessTime(StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()));
					appeal.setHandlerRemark(remark);
					appealBiz.update(appeal);
					
					logger.info("[ "+biddingService.getBidNo()+" ]关闭交易成功!");
					this.outJson(response,new JSONResult(true,"招标单[" + biddingService.getBidNo() + "]关闭交易成功"));
				}else if(status == Constant.BIDDING_CANCEL){//取消招标后状态为：11招标取消
					biddingService.setStatus(status);
					biddingServiceBiz.addOrModify(biddingService);
					
					biddingservicedetail = new BiddingServiceDetail(biddingService,status, remark, manager,
						StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()),Constant.ACTION_CANCEL_APPEAL_BIDDING);
					biddingServiceDetailBiz.add(biddingservicedetail);
					
					//完善申诉单信息
					appeal.setAppealStatus(Constant.APPEAL_STATUS_ORDER_CANCEL);
					appeal.setManager(manager);
					appeal.setProcessMode(Constant.HANDLER_MODE_CANCEL);
					appeal.setProcessTime(StrUtils.formateDate("yyyy-MM-dd HH:mm:ss", new Date()));
					appeal.setHandlerRemark(remark);
					appealBiz.update(appeal);
					
					logger.info("[ "+biddingService.getBidNo()+" ]取消招标成功!");
					this.outJson(response,new JSONResult(true,"招标单[" + biddingService.getBidNo() + "]取消招标成功"));
				}
			} catch (Exception e) {
				this.outJson(response,new JSONResult(false,"招标单[" + biddingService.getBidNo() + "]处理失败!异常信息:"+e.getLocalizedMessage()));
				logger.info("招标单处理失败!异常信息:"+e.getLocalizedMessage());
			}
	}
	
	/**
	 * 根据招单id查看该招单的应标信息-支撑平台
	 * @author xuwf
	 * @since 2013-10-09
	 * 
	 * @param bidId		招单id
	 * @param request		
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findResponseBidding")
	public void findResponseBidding(
			@RequestParam(value="bidId",required=false)Integer bidId,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="100", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[ bidId:"+bidId+",status:"+status+"]");
		
		JSONData<ResponseBidding> jdrb = responseBiddingBiz.getByBidService(bidId, status,start,limit);
		this.outJson(response, jdrb);
	}
	
	/**
	 * 根据招单id查看该招单的申诉信息-支撑平台
	 * @author xuwf
	 * @since 2013-10-12
	 * 
	 * @param bidId		招单id
	 * @param request		
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findBiddingAppeal")
	public void findBiddingAppeal(
			@RequestParam(value="bidId",required=false)Integer bidId,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="100", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[ bidId:"+bidId+"]");
		
		JSONData<Appeal> jdappeal = appealBiz.queryByBidId(bidId, start, limit);
		this.outJson(response, jdappeal);
	}
	
	/**
	 * 根据招单id查看该招单的流水信息-支撑平台
	 * @author xuwf
	 * @since 2013-10-09
	 * 
	 * @param bidId		招单id
	 * @param request		
	 * @param response
	 */
	@NeedSession(SessionType.MANAGER)
	@RequestMapping(value = "/findBiddingServiceDetail")
	public void findBiddingServiceDetail(
			@RequestParam(value="bidId",required=false)Integer bidId,
			@RequestParam(value="start", defaultValue="0", required=false)int start,
			@RequestParam(value="limit", defaultValue="100", required=false)int limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("queryParams[ bidId:"+bidId+"]");
		
		JSONData<BiddingServiceDetail> jdbsd = biddingServiceDetailBiz.getByBidId(bidId, start, limit);
		this.outJson(response, jdbsd);
	}
}
