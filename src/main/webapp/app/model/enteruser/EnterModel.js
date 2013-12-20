/**
 * 企业用户实体模型 xuwf 2013-8-22  
 */
Ext.define('plat.model.enteruser.EnterModel', {
    extend: 'Ext.data.Model',
    fields: [
    		{name:'id',type:'int'},
    		{name:'userName',type:'String'},
    		{name:'sex',type:'int'},
    		{name:'checkcode',type:'String'},
    		{name:'userStatus',type:'int'},
    		{name:'regTime',type:'String'},
    		{name:'isPersonal',type:'boolean'},
    		{name:'email',type:'String'},
    		{name:'isApproved',type:'boolean'},
    		{name:'sourceId',type:'int'},    		
    		{name:'remark',type:'String',defaultValue:'暂无'},   
    		//个人用户信息
    		{name:'nickName',type:'String'},
    		{name:'headPortraint',type:'String'},
    		{name:'personalPhoto',type:'String'},
    		{name:'idCardPhoto',type:'String'},
    		{name:'realName',type:'String'},
    		{name:'mobile',type:'String'},
    		{name:'address',type:'String'},
    		{name:'certSign',type:'Integer'},
    		//所属企业信息
    		{name:'enterprise.id',type:'int'},
    		{name:'enterprise.enterpriseProperty',type:'int'},
    		{name:'enterprise.enterpriseCode',type:'String'},
    		{name:'enterprise.name',type:'String'},    		
    		{name:'enterprise.forShort',type:'String'},
    		{name:'enterprise.type',type:'int'},
    		{name:'enterprise.legalPerson',type:'String'},
    		{name:'enterprise.linkman',type:'String'},
    		{name:'enterprise.tel',type:'String'},
    		{name:'enterprise.email',type:'String'},
    		{name:'enterprise.address',type:'String'},
    		{name:'enterprise.industryType',type:'int'},
    		{name:'enterprise.mainRemark',type:'String'},
    		{name:'enterprise.openedTime',type:'String'},
    		{name:'enterprise.photo',type:'String'},
    		{name:'enterprise.licenceDuplicate',type:'String'},
    		{name:'enterprise.businessLetter',type:'String'},
    		{name:'enterprise.attachment',type:'String'},
    		{name:'enterprise.isApproved',type:'boolean'},
    		{name:'enterprise.icRegNumber',type:'String'},
    		{name:'enterprise.profile',type:'String'},
    		{name:'enterprise.businessPattern',type:'String'},
    		{name:'enterprise.staffNumber',type:'int'},
    		{name:'enterprise.salesRevenue',type:'double',defaultValue:0}
    		
    	]
});
