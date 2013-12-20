Ext.define('plat.view.service.AddForUserWindow', {
    extend: 'Ext.window.Window',
	xtype:'addforuserwindow',
	
    width: 678,
    height: 500,
	layout:'fit',
	modal:true,
	buttonAlign:'center',
	closeAction : 'hide',
	kindeitor : null,
	getKindeditor : function () {
    	return this.kindeditor;
    },
    setKindeditor : function (kindeditor) {
    	this.kindeditor = kindeditor;
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
		            {	
		            	xtype:'form',
		            	id:'addforuserform',
		            	layout:'column',					    
					    defaults:{
					        labelWidth:61,
					        labelAlign:'right',
					        msgTarget: 'side',
					        margin:'2'
					    },
					    bodyPadding:'10',
					    items:[					    		
					    		{
				                    xtype: 'enterprisecombo',
				                    fieldLabel: '服务机构',
				                    width: 350,
				                    allowBlank:false,
				                    afterLabelTextTpl: required,
				                    editable:false,
				                    emptyText:'请选择服务机构...',
				                    name:'enterprise.id'
				                    
				                },
				                {
				                    xtype: 'textfield',
				                    name:'serviceName',
				                    width: 350,
				                    allowBlank:false,
				                    afterLabelTextTpl: required,
				                    minLength:5,
				                    minLengthText:'服务名称最少为5个字符',
				                    maxLength:50,
				                    maxLengthText:'服务名称限制在 50 个字符以内',
				                    fieldLabel: '服务名称'
				                },				                
//				                {
//				                    xtype: 'radiogroup',
//				                    width: 350,
//				                    fieldLabel: '服务来源',
//				                    afterLabelTextTpl: required,
//				                    allowBlank:false,
//				                    items: [
//				                        {
//				                            xtype: 'radiofield',
//				                            boxLabel: '运营平台',
//				                            inputValue: 1,
//				                            columnWidth:50,
//				                            checked: true,
//				                            name: 'serviceSource'
//				                            
//				                            
//				                        },
//				                        {
//				                            xtype: 'radiofield',
//				                            boxLabel: '服务机构',
//				                            inputValue: 2,
//				                            columnWidth:50,
//				                            name: 'serviceSource'
//				                            
//				                        }
//				                    ]
//				                },
				                {
				                    xtype: 'hiddenfield',
				                    submitValue:true,
				                    name:'serviceSource',
				                    value:'2'
				                },
//				                {
//				                    xtype: 'hiddenfield',
//				                    submitValue:true,
//				                    name:'enterprise.icRegNumber'
//				                },
//				                {
//				                    xtype: 'hiddenfield',
//				                    submitValue:true,
//				                    name:'organizeId'
//				                },
//				                {
//				                    xtype: 'serviceorgcombo',
//				                    name:'organizeName',
//				                    width: 259,
//				                    afterLabelTextTpl: required,
//				                    allowBlank:false,
//				                    editable:false,
//				                    emptyText:'请选择服务机构...',
//				                    fieldLabel: '服务机构',
//				                    listeners:{
//				                    	select:function(combo,records){
//				                    		this.ownerCt.down('hiddenfield[name=organizeId]').setValue(records[0].data.id);
//				                    	}
//				                    }
//				                },
				                {
				                    xtype: 'hiddenfield',
				                    submitValue:true,
				                    name:'category.text'
				                },
				                {
				                    xtype: 'hiddenfield',
				                    submitValue:true,
				                    name:'category.code'
				                },
				                {
				                    xtype: 'treecombo',
				                    name:'category.id',
				                    afterLabelTextTpl: required,
				                    allowBlank:false,
				                    width: 350,
				                    store: Ext.data.StoreManager.lookup('serviceCategoryStore') ? 
				                    		Ext.data.StoreManager.lookup('serviceCategoryStore'):
				                    		Ext .create( 'plat.store.service.ServiceCategoryStore',
													{
														storeId : 'serviceCategoryStore'
													}),
						            displayField:'text',
									valueField:'id',
									nameTarget:'hiddenfield[name="category.text"]',
									codeTarget:'hiddenfield[name="category.code"]',
									canSelectFolders:false,
				                    rootVisible:false,
				                    editable:false,
				                    emptyText:'请选择服务类别...',
				                    fieldLabel: '服务类别'
				                },
				                {
				                    xtype: 'smethodcombo',
				                    fieldLabel: '服务方式',
				                    width: 550,
				                    name:'serviceMethod',
				                    afterLabelTextTpl: required,
				                    allowBlank:false,
				                    editable:false,
				                    emptyText:'请选择服务方式...',
				                    multiSelect:true
				                },
				                {
				                    xtype: 'textfield',
				                    width: 300,
				                    name:'linkMan',
				                    afterLabelTextTpl: required,
				                    allowBlank:false,
				                    fieldLabel: '联系人'
				                },
				                {
				                    xtype: 'textfield',
				                    name:'tel',
				                    width: 300,
				                    afterLabelTextTpl: required,
				                    allowBlank:false,
				                    regex:/^(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
									regexText:'联系电话输入不正确',
				                    fieldLabel: '联系电话'
				                },				                
				                {
				                    xtype: 'textfield',
				                    width: 350,
				                    name:'email',
				                    fieldLabel: '邮箱',
				                    afterLabelTextTpl: required,
				                    allowBlank:false,
				                    vtype:'email'
				                },
							    {				                		
					            	xtype: 'numberfield',
					            	width: 300,
					            	name:'costPrice',
					           		afterLabelTextTpl: required,
					            	allowBlank:false,
					            	allowDecimals:false,
					            	minValue: 0,
					           		value: 0,
					             	labelWidth:61,
					              	fieldLabel: '服务价格'
					           	},
//					            {
//					             	xtype: 'textfield',
//					              	width: 325,					              	
//					             	name:'chargeMethod',
//					              	afterLabelTextTpl: required,
//					             	allowBlank:false,
//					             	labelWidth:80,
//					           		fieldLabel: '收费模式'					                
//				                },				                
				                {
				                	xtype: 'container',
							        anchor: '100%',
							        layout: 'hbox',
							        items : [{
							        	xtype:'textfield',
						                fieldLabel: '图片',
						                name: 'picture',
						                readOnly : true,
						                labelWidth : 61,
						                labelAlign:'right',
						                afterLabelTextTpl: required,
				                    	allowBlank:false,
						                flex : 1
							        }, {
							        	xtype : 'button',
							        	name : 'select',
							        	icon : 'jsLib/extjs/resources/themes/icons/add1.png'
							        }]
				                },
				                {
				                    xtype: 'htmleditor',
				                    plugins: [new Ext.create('Ext.ux.form.HtmlEditor.InsertImage', {})],
				                    height: 200,
				                    width: 550,
				                    name:'serviceProcedure',				                    
				                    fieldLabel: '服务描述'
				                }
//				                {
//							    	xtype: 'textarea',
//							        name: 'serviceProcedure',
//							        id : 'serviceProcedure2',
//							        height: 180,
//							        anchor: '100%',
//							        grow : true,
//							        style: 'background-color: white;',
//							        listeners :　{
//							        	render : function (t, opts)　{
//							        		setTimeout(function(){  
//							                    var editor = KindEditor.create('#serviceProcedure2', {
//							                    	uploadJson : 'public/uploadByKindedior',
//							                    	themeType : 'simple',
//							                    	resizeType : 0,
//							                    	items : ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
//												        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
//												        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
//												        'superscript', 'clearhtml', 'quickformat', 'selectall', '|', '/',
//												        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
//												        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
//												        'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
//												        'anchor', 'link', 'unlink', '|', 'fullscreen', 'about'
//													]
//							                    }); 
//							                    t.findParentByType('addforuserwindow').setKindeditor(editor);
//							                },1000); 
//							        	}
//							        }
//							    }
					    	]
		            	}
            		],
			buttons:[
				{
					text:'提交',
//					iconCls:'icon-submit',
					action:'submit'
				},
				{
					text:'清空',
//					iconCls:'icon-reset',
					action:'reset'
				}
			]
        });
		
        me.callParent(arguments);
    }

});