Ext.define('plat.view.enteruser.UserDetailWindow', {
    extend: 'Ext.window.Window',
	xtype:'userdetailwindow',    
	//height: 560,
    width: 585,    
    layout:'fit',
    modal:true,
    closeAction : 'hide',
    title: '会员详细信息',
    
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',                    
                    bodyPadding: 10,
                    defaults:{
		            	xtype: 'fieldset',
		            	width: 550,
		            	margin: 2,
		            	fixed: true,
						layout: 'column'
						
					},
                    items: [
                        {                            
                            //height: 330,                                                             
		                    defaults:{
		                    	xtype: 'displayfield',
								labelWidth:80,
								labelAlign:'right',
								width: 250,
								margin:'2'								
							},
                            title: '基本信息',
                            items: [                            	
                                {
                                    fieldLabel: '会员名',
                                    name:'userName'
                                },                                
                                {
                                    fieldLabel: '注册时间',                                    
                                    name:'regTime'
                                },                                
                                {
                                    fieldLabel: '电子邮箱',
                                    name:'enterprise.email'
                                },                                
                                {
                                    fieldLabel: '单位性质',
                                    name:'enterprise.enterpriseProperty',                                    
                                    renderer:function(value){                                    	
                                    	if(!value){                                    	
                                    		value ='';
                                    	}else {
                                    		value = PlatMap.Enterprises.enterpriseProperty[value];
                                    	}
				                		return value;                                    	
                                    }
                                },
                                {
                                    fieldLabel: '所属窗口',                                    
                                    name:'enterprise.industryType',
                                    renderer:function(value){
                                    	if(value==''){                                    	
                                    		value ='';
                                    	}else{
                                    		value = PlatMap.Enterprises.industryType[value];
                                    	}
				                		return value;
                                    }
                                },
                                {
                                    fieldLabel: '用户类型',                                    
                                    name:'enterprise.type',
                                    renderer:function(value){
                                    	if(value==''){                                    	
                                    		value ='';
                                    	}else {
                                    		value = PlatMap.Enterprises.type[value];
                                    	}
				                		return value;
                                    }
                                  
                                },
                                {
                                    fieldLabel: '企业全称',    
                                    name:'enterprise.name'
                                },
                                {
                                    fieldLabel: '企业简称',                                    
                                    name:'enterprise.forShort'
                                },
                                {
                                    fieldLabel: '企业logo',
                                    name:'enterprise.photo',
                                    renderer : function(value){
						    			if(value == ''){
						    				return '暂无';
						    			}else if(value == 'enterprise_logo.jpg'){
					    					return "<a href='resources/images/ucenter/enterprise_logo.jpg' class='fancybox'>点击查看</a>";
						    			}else {
						    				return "<a href='upload/" + value + "' class='fancybox'>点击查看</a>";
						    			}
					    			}
                                },
                                
                                {
                                    fieldLabel: '经营模式',
                                    name:'enterprise.businessPattern',
                                    renderer:function(value){                                    	
                                    	if(value==''){                                    	
                                    		value ='';
                                    	}else {
                                    		value = PlatMap.Enterprises.businessPattern[value];
                                    	}
				                		return value;                                    	
                                    }
                                },
                                {
                                    fieldLabel: '员工人数',
                                    name:'enterprise.staffNumber'
                                },
                                {
                                    fieldLabel: '年营业额(万)',                                    
                                    name:'enterprise.salesRevenue'
                                },
                                {
                                    fieldLabel: '营业执照',
                                    name:'enterprise.licenceDuplicate',
                                    renderer : function(value){
						    			if(value == '') return '暂无';
						    			return "<a href='upload/" + value + "' class='fancybox'>点击查看</a>";
					    			}
                                },
                                {
                                    fieldLabel: '组织机构号',                                    
                                    name:'enterprise.icRegNumber'
                                },
                                {
                                    fieldLabel: '法人姓名',
                                    name:'enterprise.legalPerson'
                                },
                                {
                                    fieldLabel: '企业公函',                                    
                                    name:'enterprise.businessLetter',                                    
                                    renderer : function(value){
						    			if(value == '') return '暂无';
						    			return "<a href='upload/" + value + "' class='fancybox'>点击查看</a>";
					    			}
                                },
                                {
                                    fieldLabel: '联系人',                                    
                                    name:'enterprise.linkman'
                                },
                                {
                                    fieldLabel: '联系电话',                                    
                                    name:'enterprise.tel'
                                },                                
                                {
                                    fieldLabel: '成立日期',                                    
                                    name:'enterprise.openedTime'
                                },                                
                                
                                {
                                    fieldLabel: '联系地址',
                                    width:520,	                                
                                    name:'enterprise.address'
                                }
                            ]
                        },
                        {
                            //height: 160,
                            title: '其他信息',
                            defaults:{
		                    	xtype: 'textarea',
								labelWidth:80,
								labelAlign:'right',
								width: 520,							
								readOnly:true,
                            	listeners: {
							        'focus' : function(text){
									　　　　text.blur();
									　}
						    	},
								margin:'2'
							},
                            items: [
                                { 
                                    fieldLabel: '主要产品',                                    
                                    name:'enterprise.mainRemark'
                                },
                                {
                                	//height: 100,                                	
                                    fieldLabel: '备注',                                    
                                    name:'remark'
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});