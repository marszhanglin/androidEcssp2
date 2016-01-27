/*------------------------------------------------------------------------------
 总线-令牌
-------------------------------------------------------------------------------*/
CREATE TABLE T_Esb_Token(                
id             VARCHAR2(64),             /*主键*/
pkey           VARCHAR2(16)   NOT NULL,  /*密钥*/
value          VARCHAR2(500)  NOT NULL,  /*令牌值*/
allowedIps     VARCHAR2(500),            /*允许的ip值 多个时用";"分割 为空时不验证*/
CONSTRAINT pk_Esb_Token_id PRIMARY KEY (id));

/*------------------------------------------------------------------------------
 总线-访问日志
-------------------------------------------------------------------------------*/
CREATE TABLE T_Esb_Log(                  
id             VARCHAR2(64),             /*主键*/
createTime     DATE,                     /*创建时间*/
tokenId        VARCHAR2(64),             /*总线令牌Id*/
tokenValue     VARCHAR2(500),            /*总线令牌值*/
ip             VARCHAR2(20),             /*ip*/
requestUri     VARCHAR2(300),            /*请求路径*/
errMsg         VARCHAR2(4000),           /*异常信息*/
CONSTRAINT pk_Esb_Log_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 移动应用-用户授权
-------------------------------------------------------------------------------*/
CREATE TABLE T_Mobile_UserGrant(         
id             VARCHAR2(64),             /*主键*/
createTime     DATE,                     /*创建时间*/
expireTime     DATE,                     /*到期时间*/
userId         VARCHAR2(64)   NOT NULL,  /*用户Id*/
imei           VARCHAR2(50),             /*IMEI号*/
grantedCode    NUMBER(10,0)   NOT NULL,  /*授权码*/
pkey           VARCHAR2(16)   NOT NULL,  /*密钥*/
CONSTRAINT pk_Mobile_UserGrant_id PRIMARY KEY (id),
CONSTRAINT fk_Mobile_UserGrant_userId FOREIGN KEY (userId) REFERENCES Org_User(id) ON DELETE CASCADE);

/*------------------------------------------------------------------------------
 移动应用-服务配置
-------------------------------------------------------------------------------*/
CREATE TABLE T_Mobile_ServiceConfig(     
id             VARCHAR2(64),             /*主键*/
createTime     DATE,                     /*创建时间*/
cls            VARCHAR2(200)   NOT NULL, /*类名*/
mthd           VARCHAR2(200)   NOT NULL, /*方法名*/
code           VARCHAR2(200)   NOT NULL, /*服务编码*/
name           VARCHAR2(400)   NOT NULL, /*服务名称*/
summary        VARCHAR2(500),            /*服务说明*/
memo           VARCHAR2(500),            /*备注*/
disabled       VARCHAR2(1)     NOT NULL, /*是否禁用。'0'：可用，'1'：禁用*/
CONSTRAINT pk_Mobile_ServiceConfig_id PRIMARY KEY (id));

/*------------------------------------------------------------------------------
 移动应用-访问日志
-------------------------------------------------------------------------------*/
CREATE TABLE T_Mobile_Log(               
id             VARCHAR2(64),             /*主键*/
createTime     DATE,                     /*创建时间*/
svcId          VARCHAR2(64),             /*服务Id*/
svcCls         VARCHAR2(200),            /*服务类名*/
svcMthd        VARCHAR2(200),            /*服务方法名*/
svcCode        VARCHAR2(200),            /*服务编码*/
svcName        VARCHAR2(400),            /*服务名称*/
ip             VARCHAR2(20),             /*ip*/
grantedId      VARCHAR2(64),             /*用户授权Id*/
userId         VARCHAR2(64),             /*用户Id*/
loginName      VARCHAR2(32),             /*登录名*/
userName       VARCHAR2(32),             /*用户姓名*/
imei           VARCHAR2(50),             /*IMEI号*/
grantedCode    NUMBER(10,0),             /*授权码*/
errMsg         VARCHAR2(4000),           /*异常信息*/
CONSTRAINT pk_Mobile_Log_id PRIMARY KEY (id));

/*------------------------------------------------------------------------------
 应急指挥处置－业务功能信息表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Incident_Function(					
id              VARCHAR2(64)   NOT NULL, /*主键*/
functionCode    VARCHAR2(64)   NOT NULL, /*功能唯一ID*/
functionName    VARCHAR2(100)  NOT NULL, /*功能名称*/
functionSuper   VARCHAR2(64),			 /*上级代码*/
functionUrl     VARCHAR2(300),			 /*功能URL*/
functionPic     VARCHAR2(50),			 /*功能图标*/
functionOrder   NUMBER(5,2)    NOT NULL, /*排列顺序*/
functionView    VARCHAR2(30),			 /*控制方式 1:独立打开 2:调用脚本函数*/
attr            VARCHAR2(50),			 /*备用字段*/
createTime      DATE,					 /*新建时间*/
updateTime      DATE,					 /*更新时间*/
createBy        VARCHAR2(64),			 /*创建人*/	
CONSTRAINT pk_Indt_Func_id PRIMARY KEY (id),
CONSTRAINT uk_Indt_Func_functionCode UNIQUE (functionCode),
CONSTRAINT fk_Indt_Func_functionSuper FOREIGN KEY (functionSuper) REFERENCES T_Incident_Function(id));

/*------------------------------------------------------------------------------
应急指挥处置-业务功能-事件类型配置表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Incident_Function_Config(			
id              VARCHAR2(64)   NOT NULL, /*主键*/
eventType       VARCHAR2(64)   NOT NULL, /*事件类型*/
departId        VARCHAR2(64)   NOT NULL, /*用户机构ID*/
functionCode    VARCHAR2(64)   NOT NULL, /*功能唯一ID*/
functionName    VARCHAR2(100)  NOT NULL, /*功能名称*/
functionSuper   VARCHAR2(64),			 /*上级代码*/
functionOrder   NUMBER(5,2)    NOT NULL, /*排列顺序*/
attr            VARCHAR2(50),			 /*备用字段*/
createTime      DATE,					 /*新建时间*/
updateTime      DATE,					 /*更新时间*/
createBy        VARCHAR2(64),			 /*创建人*/
CONSTRAINT pk_Indt_Func_Cfg_id PRIMARY KEY (id),
CONSTRAINT fk_Indt_Func_Cfg_eventType FOREIGN KEY (eventType) REFERENCES T_Event_EventType(id),
CONSTRAINT fk_Indt_Func_Cfg_departId FOREIGN KEY (departId) REFERENCES Org_Organization(id),
CONSTRAINT fk_Indt_Func_Cfg_functionSuper FOREIGN KEY (functionSuper) REFERENCES T_Incident_Function_Config(id));

/*------------------------------------------------------------------------------
应急指挥处置-业务功能-事件实例表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Incident_Function_Event  (			
id              VARCHAR2(64)  NOT NULL, /*主键*/
eventId         VARCHAR2(64)  NOT NULL, /*事件ID*/
eventType       VARCHAR2(64)  NOT NULL,	/*事件类型*/
functionCode    VARCHAR2(64)  NOT NULL,	/*功能唯一ID*/
functionName    VARCHAR2(100) NOT NULL,	/*功能名称*/
functionType    VARCHAR2(30),			/*功能类别*/
functionUrl     VARCHAR2(500),			/*功能URL*/
functionPic		VARCHAR2(50),			/*功能图标*/
functionSuper   VARCHAR2(64),			/*上级代码*/
functionOrder   NUMBER(5,2)   NOT NULL,	/*排列顺序*/
functionView    VARCHAR2(30),			/*控制方式 1:独立打开 2:调用脚本函数*/
attr            VARCHAR2(50),			/*备用字段*/
createTime      DATE,					/*新建时间*/
updateTime      DATE,					/*更新时间*/
createBy        VARCHAR2(64),			/*创建人*/
CONSTRAINT pk_Indt_Func_Evt_id PRIMARY KEY (id),
CONSTRAINT fk_Indt_Func_Evt_eventId FOREIGN KEY (eventId) REFERENCES T_Event_Info(id) ON DELETE CASCADE,
CONSTRAINT fk_Indt_Func_Evt_functionSuper FOREIGN KEY (functionSuper) REFERENCES T_Incident_Function_Event(id));

/*------------------------------------------------------------------------------
应急指挥处置-事件信息接报-危化品表
-------------------------------------------------------------------------------*/
create table T_Event_Dangeritem	(
id         		VARCHAR2(36) 	NOT NULL, /*主键*/
xh         		NUMBER(36),				  /*序号*/
zwm        		VARCHAR2(300),			  /*中文名*/
ywm        		VARCHAR2(300),			  /*英文名*/
fzz        		VARCHAR2(300),            /*分子式*/
fzl        		VARCHAR2(300),            /*分子量*/
cash       		VARCHAR2(300),            /*CAS号*/
rtecsh     		VARCHAR2(300),            /*RTECS号*/
unbh       		VARCHAR2(300),            /*UN编号*/
wxhwbh     		VARCHAR2(300),            /*危险货物编号*/
imdggzym   		VARCHAR2(300),            /*IMDG规则页码*/
wgyxz      		VARCHAR2(500),            /*外观与性状*/
zyyt       		VARCHAR2(4000),           /*主要用途*/
rd         		VARCHAR2(300),            /*熔点*/
fd         		VARCHAR2(300),            /*沸点*/
xdmds      		VARCHAR2(300),            /*相对密度水*/
xdmdkq     		VARCHAR2(300),            /*相对密度空气*/
bhzqy      		VARCHAR2(300),            /*饱和蒸汽压*/
rjx        		VARCHAR2(500),            /*溶解性*/
rsx        		VARCHAR2(300),            /*燃烧性*/
jghxfj     		VARCHAR2(300),            /*建规火险分级*/
sd         		VARCHAR2(300),            /*闪点*/
zrwd       		VARCHAR2(300),            /*自燃温度*/
bzxx       		VARCHAR2(300),            /*爆炸下限*/
bzsx       		VARCHAR2(300),            /*爆炸上限*/
wxtx       		VARCHAR2(4000),           /*危险特性*/
rsfjcw     		VARCHAR2(500),            /*燃烧分解产物*/
wdx        		VARCHAR2(300),            /*稳定性*/
jhwh       		VARCHAR2(300),            /*聚合危害*/
jjw        		VARCHAR2(500),            /*禁忌物*/
mdff       		VARCHAR2(4000),           /*灭火方法*/
wxxlb     		VARCHAR2(500),            /*危险性类别*/
wxhwbzbz   		VARCHAR2(300),            /*危险货物包装标志*/
bzlb       		VARCHAR2(300),            /*包装类别*/
cyzysx     		VARCHAR2(4000),           /*储运注意事项*/
jcxz       		VARCHAR2(500),            /*接触限值*/
qrtj       		VARCHAR2(500),            /*侵入途径*/
dx         		VARCHAR2(600),            /*毒性*/
jkwh       		VARCHAR2(4000),           /*健康危害*/
pfjc       		VARCHAR2(600),            /*皮肤接触*/
yjjc       		VARCHAR2(4000),           /*眼睛接触*/
xr         		VARCHAR2(4000),           /*吸入*/
sr         		VARCHAR2(4000),           /*食入*/
gckz       		VARCHAR2(4000),           /*工程控制*/
hxxtfh    		VARCHAR2(4000),           /*呼吸系统防护*/
yjfh       		VARCHAR2(600),            /*眼睛防护*/
fhf        		VARCHAR2(600),            /*防护服*/
shf        		VARCHAR2(600),            /*手防护*/
ljwd       		VARCHAR2(300),            /*临界温度*/
ljyl       		VARCHAR2(300),            /*临界压力*/
rsr        		VARCHAR2(300),            /*燃烧热*/
bmjcdtj    		VARCHAR2(500),            /*避免接触的条件*/
xlcz       		VARCHAR2(4000),           /*泄漏处置*/
qt         		VARCHAR2(4000),           /*其他*/
slxlglbj   		VARCHAR2(300),            /*少量泄漏隔离半径*/
slxlbtssbj 		VARCHAR2(300),		      /*少量泄漏白天疏散半径*/
slxlyjssbj 		VARCHAR2(300),            /*少量泄漏夜间疏散半径*/
dlxlglbj   		VARCHAR2(300),			  /*大量泄漏隔离半径*/
dlxlbtssbj 		VARCHAR2(300),            /*大量泄漏白天疏散半径*/
dlxlyjssbj 		VARCHAR2(300),            /*大量泄漏夜间疏散半径*/
zcljl      		FLOAT,                    /*zcljl*/
scljl      		FLOAT,					  /*scljl*/
ljl        		VARCHAR2(10),             /*危险化学品相对应的临界量*/
xzxs       		VARCHAR2(10),             /*危险化学品相对应的校正系数*/
showSign   		VARCHAR2(10),             /*显示标识*/
eventId         VARCHAR2(64),             /*关联事件ID*/
CONSTRAINT pk_Event_DangerItem_id PRIMARY KEY (id));
/*------------------------------------------------------------------------------
 企业基本信息-周边信息表
-------------------------------------------------------------------------------*/
create table T_UNITS_AROUND
(
  id             VARCHAR2(64) NOT NULL, /*主键*/
  unitsid        VARCHAR2(64) NOT NULL, /*企业信息ID*/
  aroundname     VARCHAR2(200),         /*周边信息名称*/
  address        VARCHAR2(300),         /*详细地址*/
  gisx           VARCHAR2(20),          /*经度*/
  gisy           VARCHAR2(20),          /*纬度*/
  pic360         VARCHAR2(512),         /*周边图片附件ID*/
  attr1          VARCHAR2(50),          /*备用字段1*/
  attr2          VARCHAR2(50),          /*备用字段2*/
  remark         VARCHAR2(200),         /*备注*/
  curstatus      VARCHAR2(32),          /*状态*/
  createtime     DATE,                  /*创建时间*/
  createby       VARCHAR2(32),          /*创建人*/
  updatetime     DATE,                  /*修改时间*/
  updateby       VARCHAR2(32),          /*修改人*/
  dataflg        VARCHAR2(10)           /*数据标志*/
CONSTRAINT pk_Units_Around_id PRIMARY KEY (id),
CONSTRAINT fk_Units_Around_unitsId FOREIGN KEY (unitsId) REFERENCES T_Base_Units(id) ON DELETE CASCADE);
/*------------------------------------------------------------------------------
应急预案-事件响应-应急资源保障详细表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Plan_Event_Resource_Item  (
   id                 VARCHAR2(64) NOT NULL,    /*主键*/
   planId             VARCHAR2(64) NOT NULL,	/*应急预案ID*/
   eventId            VARCHAR2(64) NOT NULL,	/*事件ID*/
   taskId             VARCHAR2(10),			    /*任务ID*/
   resourceId         VARCHAR2(64),			    /*资源编号*/
   resourceType       VARCHAR2(32),			    /*资源类型*/
   resourceName       VARCHAR2(100),			/*资源名称*/
   fatherId           VARCHAR2(64) NOT NULL,	/*父类ID*/
   resourceNum        NUMBER(8),				/*资源数量*/
   gisY               VARCHAR2(32),			    /*纬度*/
   gisX               VARCHAR2(32),			    /*经度*/
   state              VARCHAR2(32),			    /*状态*/
   remark             VARCHAR2(100),			/*备注*/
   dataflg            VARCHAR2(10),			    /*数据标志*/
   createBy           VARCHAR2(32),			    /*创建者*/
   createTime         DATE,					    /*创建时间*/
   updateBy           VARCHAR2(32),			    /*修改者*/
   updateTime    	  DATE,					    /*修改时间*/
   resourcedeptid     VARCHAR2(64),				/*所属机构id*/
   resourcpersonid    VARCHAR2(64),				/*办理人id*/
   resourcepersonname VARCHAR2(64),				/*办理人*/
   resourcedeptname   VARCHAR2(64),				/*所属机构*/
   CONSTRAINT pk_PlanEveResItem_id PRIMARY KEY (id));
   
/*------------------------------------------------------------------------------
事件接报-企业信息-重大危险源
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_Danger(
   id                        VARCHAR2(64)   NOT NULL, /*主键*/
   unitId                    VARCHAR2(64)   NOT NULL, /*企业ID*/
   unitName                  VARCHAR2(100)  NOT NULL, /*企业名称*/
   principalName             VARCHAR2(100)  NOT NULL, /*负责人姓名*/
   phoneNumber               VARCHAR2(15)   NOT NULL, /*联系电话*/
   DangerFountainheadType    VARCHAR2(50)   NOT NULL, /*危险源类型*/
   DangerFountainheadName    VARCHAR2(100)  NOT NULL, /*危险源名称*/
   unitType                  VARCHAR2(15)   NOT NULL, /*单位类型*/
   declarantLevel            VARCHAR2(15)   NOT NULL, /*申报等级*/
   
   CONSTRAINT pk_BaseUnitlDanger_id PRIMARY KEY (id),
   CONSTRAINT fk_Base_Danger_unitsId FOREIGN KEY (unitId) REFERENCES T_Base_Units(id) ON DELETE CASCADE);
);

/*-------------------------------------------------------------------------------
应急指挥处置-企业信息-企业资质证书
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_QlfctCtfct (
id                     VARCHAR2(64)   NOT NULL, /*主键*/
unitsId                VARCHAR2(64),            /*企业ID*/
qymc                   VARCHAR2(100),           /*企业名称*/
credentialsZzbh        VARCHAR2(50),            /*资质编号*/    
credentialsZzmc        VARCHAR2(100),           /*资质名称*/
credentialsZzlx        VARCHAR2(50),            /*资质类型*/
credentialsFzsj        VARCHAR2(50),            /*发证时间*/
credentialsYxq         VARCHAR2(50),            /*有效期*/
credentialsFzdw        VARCHAR2(100),           /*发证单位*/
credentialsBz          VARCHAR2(200),           /*备注*/
flg                    VARCHAR2(20),            /*状态*/
createBy               VARCHAR2(64),            /*创建人*/
createDt               VARCHAR2(64),            /*创建时间*/
updateBy               VARCHAR2(64),            /*修改人*/
updateDt               VARCHAR2(64),            /*修改时间*/
attachmentIds          VARCHAR2(1024),          /*附件ID*/     
attachmentNames        VARCHAR2(2048),          /*附件名称*/
attachurl              VARCHAR2(2048 CHAR),   
CONSTRAINT pk_Base_Unit_QlfctCtfct_id PRIMARY KEY (id),
CONSTRAINT fk_Unit_QlfctCtfct_unitsId FOREIGN KEY (unitsId) REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*------------------------------------------------------------------------------
应急指挥处置-企业信息-企业产品设备
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_ProdEquip(
id                     VARCHAR2(64) NOT NULL,   /*主键*/
unitsId                VARCHAR2(64),			/*企业ID*/
qymc                   VARCHAR2(100),        	/*企业名称*/
prodCpmc               VARCHAR2(100),          	/*产品名称*/    
prodNcl                VARCHAR2(100),          	/*年产量*/
prodNcz                NUMBER(12),           	/*年产值*/
prodNxssr              NUMBER(12),            	/*年销售收入*/
prodBz                 VARCHAR2(200),        	/*备注*/
attr1                  VARCHAR2(50),           	/*备用字段1*/
attr2                  VARCHAR2(50),           	/*备用字段2*/
flg                    VARCHAR2(20),           	/*状态*/
createBy               VARCHAR2(64),           	/*创建人*/
createDt               VARCHAR2(64),           	/*创建时间*/
updateBy               VARCHAR2(64),           	/*修改人*/
updateDt               VARCHAR2(64),           	/*修改时间*/  
CONSTRAINT pk_Base_Unit_ProdEquip_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_ProdEquip_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
应急指挥处置-企业信息-特种设备
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_Special(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
unitsId                VARCHAR2(64),            /*企业ID*/
qymc                   VARCHAR2(100),           /*企业名称*/
equipName              VARCHAR2(100),           /*设备名称*/    
equipCode              VARCHAR2(100),           /*设备编号*/
equipLevel             VARCHAR2(100),          	/*设备级别*/
madeIn                 VARCHAR2(100),           /*制造单位*/
instalDate             VARCHAR2(32),            /*安装时间*/
enableDate             VARCHAR2(32),            /*启用日期*/
checkDate              VARCHAR2(32),            /*检测时间*/
lastDate               VARCHAR2(32),            /*到期日期*/
attachUrl          	   VARCHAR2(200),           /*附件*/
CONSTRAINT pk_Base_Unit_Special_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_Special_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*------------------------------------------------------------------------------
事件接报-企业信息-安全设施设备情况
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_SafeEquip(
id                    	VARCHAR2(64)   NOT NULL,  	/*主键*/
unitsId                	VARCHAR2(64),  				/*企业ID*/
equipName         		VARCHAR2(100), 				/*设备名称*/
equipNumber       		NUMBER(8), 					/*设备数量*/
normalNumber        	NUMBER(8), 					/*正常设备数量*/
breakNumber     		NUMBER(8), 					/*损坏设备数量*/
stopNumber      		NUMBER(8), 					/*停用设备数量*/
attachUrl          	    VARCHAR2(200),           	/*附件*/
CONSTRAINT pk_Base_Unit_SafeEquip_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_SafeEquip_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE);
);
/*-------------------------------------------------------------------------------
应急指挥处置-企业信息-储罐区基本信息
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_TankArea(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
unitsId                VARCHAR2(64),            /*企业ID*/
qymc                   VARCHAR2(100),           /*企业名称*/
tankName               VARCHAR2(100),           /*储罐区名称*/    
tankCode               VARCHAR2(100),           /*储罐区编号*/
tankTotal              VARCHAR2(100),          	/*容积总量*/
tankCount              VARCHAR2(100),           /*储罐个数*/
tankDike               VARCHAR2(200),           /*有无防护堤防*/
tankPlace              VARCHAR2(200),           /*位置*/
attachUrl          	   VARCHAR2(200),           /*附件*/
CONSTRAINT pk_Base_Unit_TankArea_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_TankArea_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
应急指挥处置-企业信息-储罐区基本信息--储罐区危险物质信息
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_TankAreaSub(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
tankId                 VARCHAR2(64),            /*储罐区ID*/
objectName             VARCHAR2(100),           /*物质名称*/
capacity               VARCHAR2(100),           /*容量*/
maxQuality             VARCHAR2(100),           /*最大存储物资质量*/    
realQuality            VARCHAR2(100),           /*实际存储物资质量*/
CONSTRAINT pk_Unit_TankAreaSub_id PRIMARY KEY (id),
CONSTRAINT fk_Unit_TankAreaSub_tankId FOREIGN KEY (tankId) 
  REFERENCES T_Base_Unit_TankArea(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
应急指挥处置-企业信息-库区基本信息
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_Reservoir(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
unitsId                VARCHAR2(64),            /*企业ID*/
qymc                   VARCHAR2(100),           /*企业名称*/
reservoirName          VARCHAR2(100),           /*库区名称*/    
reservoirCount         VARCHAR2(100),           /*库区数量*/
reservoirArea          VARCHAR2(100),          	/*库区面积*/
reservoirFun           VARCHAR2(100),           /*所处功能区*/
reservoirTel           VARCHAR2(32),            /*联系电话*/
attachUrl          	   VARCHAR2(200),           /*附件*/
CONSTRAINT pk_Base_Unit_Reservoir_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_Reservoir_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
应急指挥处置-企业信息-危险生产装置
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_DangerProd(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
unitsId                VARCHAR2(64),            /*企业ID*/
qymc                   VARCHAR2(100),           /*企业名称*/
dangerName         VARCHAR2(100),           /*生产装置名称*/    
dangerFun          VARCHAR2(100),           /*所处功能区*/
dangerArea         VARCHAR2(100),          	/*占地面积*/
dangerValue        VARCHAR2(100),           /*固定资产总值*/
dangerTel          VARCHAR2(64),            /*联系电话*/
attachUrl          	   VARCHAR2(200),           /*附件*/
CONSTRAINT pk_Base_DangerProd_id PRIMARY KEY (id),
CONSTRAINT fk_Base_DangerProd_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);

/*-------------------------------------------------------------------------------
应急资源-资源类型
-------------------------------------------------------------------------------*/
CREATE TABLE T_Resource_Type(
id                     	 VARCHAR2(64)   NOT NULL, /*主键*/
valueName                VARCHAR2(128),            /*类型名称*/
isleaf                  VARCHAR2(64),           /*是否最子节点0、1*/
parentId    			VARCHAR2(64),           /*父节点id*/    
type          			VARCHAR2(128),           /*八大类型*/ 
icon 					VARCHAR2(128),			/*图标*/
CONSTRAINT pk_Resource_Type_id PRIMARY KEY (id)
);


create table T_Mobile_Reg_User
(
  id         VARCHAR2(32) not null,/*涓婚敭*/
  orgid      VARCHAR2(32),         /*鏈烘瀯id*/
  loginname  VARCHAR2(32) not null,/*鐧诲綍璐﹀彿 */
  pwd        VARCHAR2(128) not null,/*瀵嗙爜 */ 
  ifaudit      CHAR(1) default 0 not null ,/* 鏄惁瀹℃牳閫氳繃锛堥粯璁や负涓嶉�氳繃锛?/
  createtime VARCHAR2(32),   /*鍒涘缓鏃堕棿*/
  updatetime VARCHAR2(32),   /*鏇存柊鏃堕棿*/
  creatname  VARCHAR2(32),    /*鍒涘缓浜?*/
  
  CONSTRAINT pk_T_Mobile_User_id PRIMARY KEY (id)

);

/*-------------------------------------------------------------------------------
值班值守-通知公告
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_Notice(
id                     	 VARCHAR2(64)   NOT NULL,  /*主键*/
orgId                    VARCHAR2(64)   NOT NULL,  /*所属组织*/
rcvOrgIds                VARCHAR2(1000) NOT NULL,  /*接收组织Ids*/
rcvOrgNames              VARCHAR2(1000) NOT NULL,  /*接收组织*/
title                    VARCHAR2(200)  NOT NULL,  /*标题*/
summary                  VARCHAR2(1000) NOT NULL,  /*摘要*/
typeId                   VARCHAR2(128)  NOT NULL,  /*类型*/
checked                  VARCHAR2(1)    NOT NULL,  /*是否审核*/
content         	     CLOB,                     /*内容*/    
atchIds                  VARCHAR2(1000),           /*附件内容*/
createTime               DATE,					   /*创建时间*/
updateTime    	         DATE,				       /*修改时间*/
creatorId                VARCHAR2(64),             /*创建人Id*/
creator                  VARCHAR2(400),            /*创建人*/
CONSTRAINT pk_Duty_Notice_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_Notice_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));

/*-------------------------------------------------------------------------------
值班值守-通知公告通知人员表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_NoticePerson(
id                     	 VARCHAR2(64)   NOT NULL,  /*主键*/
fatherId                 VARCHAR2(64)   NOT NULL,  /*所属通知公告Id*/
personId                 VARCHAR2(64)   NOT NULL,  /*人员Id*/
seqc                     NUMBER(10,0)   NOT NULL,  /*序号*/
CONSTRAINT pk_Duty_NoticePsn_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_NoticePsn_fatherId FOREIGN KEY (fatherId) REFERENCES T_Duty_Notice(id) ON DELETE CASCADE,
CONSTRAINT fk_Duty_NoticePsn_personId FOREIGN KEY (personId) REFERENCES T_Duty_Address(id));

/*-------------------------------------------------------------------------------
值班值守-待办事项
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_UndisposedMatter(
id                       VARCHAR2(64)    NOT NULL,  /*主键*/
orgId                    VARCHAR2(64)    NOT NULL,  /*所属组织*/
title                    VARCHAR2(256)   NOT NULL,  /*待办事项标题*/
content                  VARCHAR2(1000)  NOT NULL,  /*待办事项内容*/
status                   VARCHAR2(128),             /*待办事项状态*/
currentTime              DATE,                      /*待办事项时间*/
startTime                DATE,                      /*待办事项开始时间*/
endTime                  DATE,                      /*待办事项结束时间*/
remark                   VARCHAR2(1000),            /*备注*/
createBy                 VARCHAR2(64),              /*创建人*/
createTime               DATE,                      /*创建时间*/
updateBy                 VARCHAR2(64),              /*修改人*/
updateTime               DATE,                      /*修改时间*/ 
CONSTRAINT pk_Duty_UndisposedMatter_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_UndisposedMatter_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));

/*-------------------------------------------------------------------------------
值班值守-特别提醒事项
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_SpecialNotice(
id                       VARCHAR2(64)    NOT NULL,  /*主键*/
orgId                    VARCHAR2(64)    NOT NULL,  /*所属组织*/
title                    VARCHAR2(256)   NOT NULL,  /*标题*/
content                  VARCHAR2(1000)  NOT NULL,  /*内容*/
leve					 VARCHAR2(128)   NOT NULL,  /*等级*/
noticeTime               DATE,                      /*当天时间*/
startTime                DATE,                      /*开始时间*/
endTime                  DATE,                      /*结束时间*/
remark                   VARCHAR2(1000),            /*备注*/
createBy                 VARCHAR2(64),              /*创建人*/
createTime               DATE,                      /*创建时间*/
updateBy                 VARCHAR2(64),              /*修改人*/
updateTime               DATE,                      /*修改时间*/ 
CONSTRAINT pk_Duty_SpecialNotice_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_SpecialNotice_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));


/*-------------------------------------------------------------------------------
发布手段类型
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_IssueType(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
isn                    VARCHAR2(2000) NOT NULL, /*内码*/
code                   VARCHAR2(200),           /*编码*/
value				   VARCHAR2(200), 			/*值*/ 
name                   VARCHAR2(400)  NOT NULL, /*名称*/
isDir                  VARCHAR2(1)    NOT NULL, /*是否目录。'0'：否，'1'：是*/
isLeaf                 VARCHAR2(1)    NOT NULL, /*是否最细级。'0'：否，'1'：是*/
rank                   NUMBER(2,0)    NOT NULL, /*级数*/
parentId    		   VARCHAR2(64),            /*父节点id*/    
CONSTRAINT pk_Infoissue_issueType_id PRIMARY KEY (id),
CONSTRAINT uk_Infoissue_issueType_code UNIQUE (code),
CONSTRAINT fk_Infoissue_Type_parentId FOREIGN KEY (parentId) REFERENCES T_Infoissue_issueType(id));

/*-------------------------------------------------------------------------------
信息发布-发布手段
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_Way(
id 						VARCHAR2(64)	 NOT NULL,	/*主键*/
typeId 					VARCHAR2(128)	 NOT NULL,	/*发布手段ID*/
explain 				VARCHAR2(1024),				/*说明*/
seqc    			    NUMBER(10,0)       NOT NULL,  /*排序*/
createTime  			DATE,			            /*创建时间*/
updateTime  			DATE,			            /*更新时间*/
creatorId   			VARCHAR2(64),               /*创建者ID*/
createor     			VARCHAR2(64),		        /*创建者*/
CONSTRAINT pk_Infoissue_way_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
信息发布-发布手段明细
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_WayItem(
id 						VARCHAR2(64)	 NOT NULL,	/*主键*/
fatherId  				VARCHAR2(64)	 NOT NULL,	/*父亲ID*/
name 					VARCHAR2(128)	 NOT NULL,	/*名称*/
orgId 					VARCHAR2(64)	 NOT NULL,	/*部门单位*/
lgnacc					VARCHAR2(128),				/*用户名*/
pwd						VARCHAR2(128),				/*密码*/
ip						VARCHAR2(128),				/*IP*/
port					VARCHAR2(128),				/*端口*/
channelNo				VARCHAR2(128),				/*频道*/
factoryId 				VARCHAR2(128),				/*厂家ID*/
deviceNo	 			VARCHAR2(128),				/*设备编号*/
createTime  			DATE,			            /*创建时间*/
updateTime  			DATE,			            /*更新时间*/
creatorId   			VARCHAR2(64),               /*创建者ID*/
createor     			VARCHAR2(64),		        /*创建者*/
CONSTRAINT pk_Infoissue_wayitem_id PRIMARY KEY (id),
CONSTRAINT fk_Infoissue_wayitem_fatid FOREIGN KEY (fatherId) REFERENCES T_Infoissue_Way(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
信息发布-策略
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_Strategy(
id          			VARCHAR2(64)   NOT NULL,   /*主键*/
orgid       			VARCHAR2(64)   NOT NULL,   /*所属组织*/
name       				VARCHAR2(200)  NOT NULL,   /*标题*/
typeid      			VARCHAR2(128)  NOT NULL,   /*信息类型*/
levels     				VARCHAR2(128)  NOT NULL,   /*信息细类*/
process					VARCHAR2(128)  NOT NULL,   /*流程*/
createtime  			DATE,                      /*创建时间*/
updatetime  			DATE,                      /*更新时间*/
creatorid   			VARCHAR2(64),              /*创建者ID*/
creator     			VARCHAR2(400),             /*创建者*/
CONSTRAINT pk_Infoissue_Strategy_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
信息发布-策略-发布手段
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_StrategyWay(
id 						VARCHAR2(64)   NOT NULL,   /*主键*/
strategyId 				VARCHAR2(64)   NOT NULL,   /*策略ID*/
wayId					VARCHAR2(64)   NOT NULL,   /*手段ID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*序号*/
CONSTRAINT pk_Infoissue_StrategyWay_id PRIMARY KEY(id),
CONSTRAINT fk_Infoissue_SWay_strategyId FOREIGN KEY(strategyId) REFERENCES T_Infoissue_Strategy(id) ON DELETE CASCADE,
CONSTRAINT fk_Infoissue_StrategyWay_wayId FOREIGN KEY (wayId) REFERENCES T_Infoissue_Way(id));

/*-------------------------------------------------------------------------------
信息发布-策略-发布手段明细
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_StrategyWayItem(
id 						VARCHAR2(64)   NOT NULL,   /*主键*/
strategyWayId			VARCHAR2(64)   NOT NULL,   /*策略发布手段ID*/
wayItemId 				VARCHAR2(64)   NOT NULL,   /*发布手段明细ID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*序号*/
CONSTRAINT pk_Infoissue_SWayItem_id PRIMARY KEY(id),
CONSTRAINT fk_Infoissue_SWItem_SWayId FOREIGN KEY(strategyWayId) REFERENCES T_Infoissue_StrategyWay(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
信息发布
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_Info(
id             			 VARCHAR2(64)   NOT NULL,  /*主键*/
title         			 VARCHAR2(128),	           /*标题*/
summary     			 VARCHAR2(1000),  		   /*摘要*/
typeId  				 VARCHAR2(64),			   /*信息类型*/
eventId					 VARCHAR2(64),			   /*事件ID*/
levels      			 VARCHAR2(64),			   /*信息细类*/
state       			 VARCHAR2(16),			   /*事件状态*/
InfluenceRange        	 VARCHAR2(2048),		   /*影响范围*/
startTime			     DATE,					   /*预测发生时间*/
endTime    			     DATE,			           /*预测结束时间*/
orgId         			 VARCHAR2(64),		       /*发布单位*/
recorgIds   			 VARCHAR2(1000) NOT NULL,  /*接收组织IDS*/
recorgNames 			 VARCHAR2(1000) NOT NULL,  /*接收组织名称*/
auth 					 VARCHAR2(64),			   /*发布人*/
contact				     VARCHAR2(128),			   /*联系方式*/
content      			 CLOB,					   /*内容*/
createTime  			 DATE,			           /*创建时间*/
updateTime  			 DATE,			           /*更新时间*/
atchids       			 VARCHAR2(1000),		   /*附件*/
origin      			 VARCHAR2(1000),		   /*来源*/
releaseDesc 			 VARCHAR2(2048),		   /*解除描述*/
release     			 CHAR(1),				   /*解除状态*/
creatorId   			 VARCHAR2(64),             /*创建者ID*/
createor     			 VARCHAR2(64),		       /*创建者*/
CONSTRAINT pk_Info_id PRIMARY KEY (id),
CONSTRAINT fk_Info_eventId FOREIGN KEY(eventId) REFERENCES T_Event_Info(id));


/*-------------------------------------------------------------------------------
信息发布-信息发布手段
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_InfoWay(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
infoId  				VARCHAR2(64)	NOT NULL,  /*T_Infoissue_Info中的ID*/
wayId 					VARCHAR2(64)	NOT NULL,  /*发布手段方法ID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*序号*/
content					VARCHAR2(1024),			   /*内容*/
other					VARCHAR2(1024),			   /*额外发布对象*/
CONSTRAINT pk_InfoWay_id PRIMARY KEY (id),
CONSTRAINT fk_InfoWay_fatherId FOREIGN KEY(infoId) REFERENCES T_Infoissue_Info(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
信息发布-信息发布手段明细
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_InfoWayItem(
id 						VARCHAR2(64)   NOT NULL,   /*主键*/
infoWayId				VARCHAR2(64)   NOT NULL,   /*信息发布手段ID*/
wayItemId 				VARCHAR2(64)   NOT NULL,   /*发布手段明细ID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*序号*/
CONSTRAINT pk_InfoWayItem_id PRIMARY KEY(id),
CONSTRAINT fk_InfoWayItem_infoWayId FOREIGN KEY(infoWayId) REFERENCES T_Infoissue_InfoWay(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
通讯录
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_Contacts(
id            			VARCHAR2(64)  NOT NULL, /*系统id*/
name          			VARCHAR2(300) NOT NULL, /*联系人名称*/
sex						VARCHAR2(32)  NOT NULL, /*性别 男:1,女:2*/
alias					VARCHAR2(300), 			/*联系人别名*/
address         		VARCHAR2(300),			/*地址*/
orgId         			VARCHAR2(64)  NOT NULL, /*单位id*/
orgAlias				VARCHAR2(300),			/*组织别名*/
belongArea				VARCHAR2(32),			/*所属区域*/
quickSearch       		VARCHAR2(400) NOT NULL, /*快速查找（包含用户姓名，电话，邮箱等信息）*/
firstChar       		VARCHAR2(1)   NOT NULL, /*联系人名称首字母（大写）*/
callNos					VARCHAR2(1000),       	/*通讯方式*/
gisX          			NUMBER(20,10),          /*地图x坐标*/
gisY          			NUMBER(20,10),          /*地图y坐标*/
userId					VARCHAR2(64), 			/*用户id*/
remark          		VARCHAR2(1000),       	/*备注*/
creatorId       		VARCHAR2(64)  NOT NULL, /*创建者ID*/
creator         		VARCHAR2(64)  NOT NULL, /*创建者*/
createTime        		DATE      	NOT NULL, 	/*创建时间*/
updateTime        		DATE,           		/*更新时间*/
updateBy        		VARCHAR2(64),       	/*更新人*/
code                    VARCHAR2(200),          /*编号*/
CONSTRAINT pk_Duty_Contacts_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_Contacts_userId FOREIGN KEY(userId) REFERENCES Org_User(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
通讯录明细
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_ContactsItem(
id            			VARCHAR2(64)  NOT NULL, /*系统id*/
fatherId        		VARCHAR2(64)  NOT NULL, /*通讯录id*/
typeId          		VARCHAR2(64)  NOT NULL, /*通讯类型id，手机、固话等*/
callNo          		VARCHAR2(64)  NOT NULL, /*通讯号*/
seqc          			NUMBER(10)    NOT NULL, /*排序号*/
CONSTRAINT pk_Duty_Contacts_Item_id PRIMARY KEY (id),
CONSTRAINT fk_Contacts_Item_fatherId FOREIGN KEY (fatherId) REFERENCES T_Duty_Contacts(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
通讯录分组
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_Group(
id            			VARCHAR2(64)	NOT NULL, 	/*系统id*/
name          			VARCHAR2(400)	NOT NULL,	/*名称*/
ownerId					VARCHAR2(64), 				/*所属人id，如果为空表示系统组*/
maintainOrgId			VARCHAR2(64), 				/*维护单位id*/
maintainOrgIds			VARCHAR2(1000),			 	/*维护单位及其上级维护单位id字符串，以逗号分隔*/
isPublic				VARCHAR2(1)		NOT NULL,	/*是否公开。'0'：否，'1'：是 (倒序)*/
defCommuTypeId			VARCHAR2(128)	NOT NULL, 	/*默认通讯方式*/
code          			NUMBER(10,0)	NOT NULL, 	/*序号 (升序)*/
isn           			VARCHAR2(2000)  NOT NULL,	/*内码*/
isDir         			VARCHAR2(1)		NOT NULL,	/*是否目录。'0'：否，'1'：是*/
isLeaf          		VARCHAR2(1)		NOT NULL,   /*是否最细级。'0'：否，'1'：是*/
rank          			NUMBER(2,0)		NOT NULL,   /*级数*/
parentId        		VARCHAR2(64),				/*父节点id*/    
creatorId       		VARCHAR2(64),				/*创建者ID*/
createor        		VARCHAR2(64),				/*创建者*/
createTime        		DATE,						/*创建时间*/
updateBy        		VARCHAR2(64),				/*更新人id*/
updateTime        		DATE,						/*更新时间*/
CONSTRAINT pk_Duty_Group_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_Group_parentId FOREIGN KEY (parentId) REFERENCES T_Duty_Group(id));

/*-------------------------------------------------------------------------------
通讯录分组关系表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Contacts_Group_Relation(
id            			VARCHAR2(64)	NOT NULL,	/*系统id*/
groupId					VARCHAR2(64)	NOT NULL,	/*分组id*/
contactsId				VARCHAR2(64)	NOT NULL,	/*通讯录id*/
code					NUMBER(10),					/*排序号*/
CONSTRAINT pk_Group_Relation_id PRIMARY KEY (id),
CONSTRAINT fk_Group_Relation_groupId FOREIGN KEY (groupId) REFERENCES T_Duty_Group(id) ON DELETE CASCADE,
CONSTRAINT fk_Group_Relation_contactsId FOREIGN KEY (contactsId) REFERENCES T_Duty_Contacts(id) ON DELETE CASCADE);

/*-------------------------------------------------------------------------------
组织通讯设备管理
-------------------------------------------------------------------------------*/
CREATE TABLE T_Sys_OrganCummEquip(
id						VARCHAR2(64)	NOT NULL,	/*系统id*/
name					VARCHAR2(400)	NOT NULL,	/*名称*/
company					VARCHAR2(400),				/*单位名称*/ 
address					VARCHAR2(400),				/*地址*/
orgId					VARCHAR2(64)	NOT NULL,	/*所属组织*/
typeId					VARCHAR2(128)	NOT NULL,	/*监控类别*/
gisX					NUMBER(20,10),				/*地图x坐标*/
gisY					NUMBER(20,10),				/*地图y坐标*/
linker					VARCHAR2(50),				/*联系人*/
tel						VARCHAR2(50),				/*联系电话*/
factoryTypeId			VARCHAR2(128),				/*厂家类别*/
ip						VARCHAR2(50),				/*ip*/
port					VARCHAR2(50),				/*端口*/
lgnAcc					VARCHAR2(50),				/*登录账户*/
pwd						VARCHAR2(50),				/*密码*/
channelNo				VARCHAR2(50),				/*通道号*/ 
creatorId				VARCHAR2(64)	NOT NULL,	/*创建者ID*/
creator					VARCHAR2(64)	NOT NULL,	/*创建者*/
createTime				DATE,						/*创建时间*/
updateBy				VARCHAR2(64),				/*更新人id*/
updateTime				DATE,						/*更新时间*/
code                    VARCHAR2(200),              /*编码*/
uniqueNo                VARCHAR2(200),              /*唯一号*/
CONSTRAINT pk_Sys_OrganCummEquip_id PRIMARY KEY (id),
CONSTRAINT fk_Sys_OrganCummEquip_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));


/*------------------------------------------------------------------------------
 融合通讯-呼叫通讯日志
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_CalledLog(                  
id						VARCHAR2(64)	NOT NULL,	/*主键*/
createTime				DATE,						/*通话开始时间*/
endTime					DATE,						/*通话结束时间*/
commuId					VARCHAR2(64),				/*通讯服务器记录ID*/
orgId					VARCHAR2(64)	NOT NULL,	/*组织id*/
userId					VARCHAR2(64)	NOT NULL,	/*发起人（主叫）用户ID*/
userName				VARCHAR2(400),				/*发起人（主叫）用户*/
members					VARCHAR2(1000),				/*参与成员姓名用"."分割*/
caller					VARCHAR2(100),				/*主叫号码*/
callee					VARCHAR2(1000),				/*成员号码*/
type					VARCHAR2(30),				/*通讯类型 单呼、组呼等*/
isRecord				VARCHAR2(1),				/*是否有录音 0:没有 1:有*/
CONSTRAINT pk_Comm_CallidLog_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 融合通讯-通知音录音日志
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_NotifiedLog(                  
id						VARCHAR2(64)	NOT NULL,	/*主键*/
createTime				DATE			NOT NULL,	/*录音开始时间*/
endTime					DATE,						/*录音结束时间*/
commuId					VARCHAR2(64)	NOT NULL,	/*通讯服务器记录ID（title）*/
orgId					VARCHAR2(64)	NOT NULL,	/*组织id*/
userId					VARCHAR2(64)	NOT NULL,	/*发起人（主叫）用户ID*/
userName				VARCHAR2(400),				/*发起人（主叫）用户*/
caller					VARCHAR2(100),				/*主叫号码*/
len						NUMBER(10),                 /*录音长度*/
CONSTRAINT pk_Comm_NotifiedLog_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 融合通讯-语音广播发送日志
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_NotifiedItemLog(  
id						VARCHAR2(64)	NOT NULL,	/*系统ID*/
fatherId				VARCHAR2(64)	NOT NULL,	/*语音广播录音ID*/
orgId					VARCHAR2(64)	NOT NULL,	/*组织ID*/
userId					VARCHAR2(64)	NOT NULL,	/*发起人（主叫）用户ID*/
userName				VARCHAR2(400),				/*发起人（主叫）用户*/
members					VARCHAR2(1000),				/*参与成员姓名用"."分割*/
caller					VARCHAR2(100),				/*主叫号码*/
callee					VARCHAR2(1000),				/*成员号码，用"."分割*/
createTime				DATE,						/*发送通知时间*/
CONSTRAINT pk_Comm_NotifiedItem_id PRIMARY KEY (id),
CONSTRAINT fk_Comm_NotifiedItem_fatherId FOREIGN KEY (fatherId) REFERENCES T_Comm_NotifiedLog(id) ON DELETE CASCADE);


/*------------------------------------------------------------------------------
 融合通讯-短信发送日志
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_MsgLog(                  
id						VARCHAR2(64)	NOT NULL,	/*主键*/
createTime				DATE,						/*通话开始时间*/
orgId					VARCHAR2(64)	NOT NULL,	/*组织id*/
userId					VARCHAR2(64)	NOT NULL,	/*发起人（主叫）用户ID*/
userName				VARCHAR2(400),				/*发起人（主叫）用户*/
members					VARCHAR2(1000),				/*参与成员姓名用","分割*/
caller					VARCHAR2(100),				/*发送号码*/
callee					VARCHAR2(1000),				/*接收号码*/
content					VARCHAR2(500),				/*短信内容*/
timeschedule			DATE,						/*定时发送*/
timesend				DATE,						/*发送时间*/
notifytype				VARCHAR2(1),				/*通知类型,0:普通;1:通知无回执;2:通知有回执。默认0。*/
status					VARCHAR2(1),				/*状态，0：等待发送；1：发送中；2：发送成功；3：发送失败；4：发送超时。*/
batchglag				VARCHAR2(1),				/*批量标志，是否批量 0:否 1:是*/
batchinfo				VARCHAR2(1000),				/*批量发送时，各个号码的状态*/
receiptdesc				VARCHAR2(1000),				/*短信内容*/
CONSTRAINT pk_Comm_MsgLog_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 融合通讯-短信分组
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_MsgGroup(                  
id						VARCHAR2(64)	NOT NULL,	/*主键*/
createTime				DATE,						/*通话开始时间*/
orgId					VARCHAR2(64)	NOT NULL,	/*组织id*/
userId					VARCHAR2(64)	NOT NULL,	/*发起人（主叫）用户ID*/
userName				VARCHAR2(400),				/*发起人（主叫）用户*/
caller					VARCHAR2(100),				/*主叫号码*/
callee					VARCHAR2(1000),				/*成员号码，用"."分割*/
members					VARCHAR2(1000),				/*参与成员姓名用","分割*/
CONSTRAINT pk_Comm_MsgGroup_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 修改短信模板分组表，添加所属用户id，并设置级联删除
-------------------------------------------------------------------------------*/
ALTER TABLE T_Duty_Smsnotice_Group ADD(ownerId VARCHAR(64));
ALTER TABLE T_Duty_Smsnotice_Group ADD CONSTRAINT fk_Duty_SGroup_ownerId FOREIGN KEY(ownerId) REFERENCES Org_User(id) ON DELETE CASCADE;


/*-------------------------------------------------------------------------------
事件评估表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventEval(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
name  				    VARCHAR2(400)	NOT NULL,  /*名称*/
eventId  				VARCHAR2(64)	NOT NULL,  /*事件Id*/
eventName  				VARCHAR2(200)	NOT NULL,  /*事件名称*/
eventType				VARCHAR2(32)	NOT NULL,  /*事件类别*/
eventArea               VARCHAR2(32)	NOT NULL,  /*所属区域*/
happenDate  			DATE,			           /*事发时间*/
happenAddress   		VARCHAR2(100),             /*事发详细地址*/
scope        		    VARCHAR2(200),             /*范围*/
population     		    VARCHAR2(200),             /*受灾人口*/
injAndDeaths            VARCHAR2(200),             /*人员伤亡*/
propertyLoss            VARCHAR2(200),             /*财产损失*/
eventReason        		VARCHAR2(2000),            /*事故原因*/
effect           		VARCHAR2(2000),            /*处置效果*/
summary        		    VARCHAR2(2000),            /*评估说明*/
memo        		    VARCHAR2(2000),            /*备注*/
atchIds        		    VARCHAR2(1000),            /*附件Ids*/
statusId                VARCHAR2(128)   NOT NULL,  /*状态Id（拟稿、已审）*/
evalTime     			DATE            NOT NULL,  /*评估时间*/
createTime  			DATE,			           /*创建时间*/
updateTime  			DATE,			           /*更新时间*/
creatorId   			VARCHAR2(64),              /*创建者(评估人)Id*/
createor     			VARCHAR2(400),		       /*创建者(评估人)*/
CONSTRAINT pk_Eval_EventEval_id PRIMARY KEY (id)),
CONSTRAINT fk_Eval_EventEval_eventId FOREIGN KEY (eventId) REFERENCES T_Event_Info(id);

/*-------------------------------------------------------------------------------
事件评估指标表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventEvalInid(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
fatherId  				VARCHAR2(64)	NOT NULL,  /*事件评估Id*/
indiCode       			VARCHAR2(64),              /*指标代码*/
indiName       			VARCHAR2(50),              /*指标名称*/
indiValue      			VARCHAR2(100),             /*监测指标值*/
seqc                    NUMBER(10,0)    NOT NULL,  /*序号*/
CONSTRAINT pk_Eval_EventEvalI_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_EventEvalI_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_EventEval(id) ON DELETE CASCADE);

/*-------------------------------------------------------------------------------
评估指标表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EvalInid(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
typeId                  VARCHAR2(128)   NOT NULL,  /*评估类型Id（事前、事中、事后）*/
eventTypeId				VARCHAR2(64)	NOT NULL,  /*事件类别*/
code       			    NUMBER(10,0)    NOT NULL,  /*指标编号*/
level1       	        VARCHAR2(400)   NOT NULL,  /*一级指标*/
level2       	        VARCHAR2(400)   NOT NULL,  /*二级指标*/
level3       	        VARCHAR2(400)   NOT NULL,  /*三级指标*/
weightValue      		NUMBER(10,2)    NOT NULL,  /*分值权重*/
formula                 VARCHAR2(400),             /*计算公式*/
summary        		    VARCHAR2(2000),            /*指标说明*/
CONSTRAINT pk_Eval_EvalInid_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
评估项目配置表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProjectCfg(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
eventTypeId				VARCHAR2(64)	NOT NULL,  /*事件类别*/
orgId 					VARCHAR2(64)	NOT NULL,  /*所属组织Id*/
code       			    NUMBER(10,0)    NOT NULL,  /*编号*/
name         	        VARCHAR2(400)   NOT NULL,  /*名称*/
formula                 VARCHAR2(400)   NOT NULL,  /*结论公式*/
createTime  			DATE,			           /*创建时间*/
updateTime  			DATE,			           /*更新时间*/
creatorId   			VARCHAR2(64),              /*创建者ID*/
createor     			VARCHAR2(64),		       /*创建者*/
CONSTRAINT pk_Eval_PrjCfg_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_PrjCfg_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));

/*-------------------------------------------------------------------------------
评估项目配置明细表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProjectCfgItem(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
fatherId  				VARCHAR2(64)	NOT NULL,  /*评估项目配置Id*/
inidId       			VARCHAR2(64)	NOT NULL,  /*指标Id*/
weightValue      		NUMBER(10,2)    NOT NULL,  /*分值权重*/
formula                 VARCHAR2(400),             /*计算公式*/
seqc       			    NUMBER(10,0)    NOT NULL,  /*序号*/
CONSTRAINT pk_Eval_PrjCfgI_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_PrjCfgI_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_ProjectCfg(id) ON DELETE CASCADE,
CONSTRAINT fk_Eval_PrjCfgI_inidId FOREIGN KEY (inidId) REFERENCES T_Eval_EvalInid(id));

/*-------------------------------------------------------------------------------
应急能力评估表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventGrade(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
orgId 					VARCHAR2(64)	NOT NULL,  /*所属组织Id*/
typeId  				VARCHAR2(64)	NOT NULL,  /*记录类型Id*/
eventId 				VARCHAR2(64)	NOT NULL,  /*事件Id*/
prjCfgId 				VARCHAR2(64)	NOT NULL,  /*评估项目配置Id*/
name         	        VARCHAR2(400)   NOT NULL,  /*评估名称*/
evalTime     			DATE            NOT NULL,  /*评估时间*/
evalValue       	    NUMBER(20,5)    NOT NULL,  /*评估值*/
evalResult         	    VARCHAR2(2000),            /*评估结论*/
evalSummary        	    VARCHAR2(2000),            /*评估说明*/
memo            	    VARCHAR2(2000),            /*备注*/
formula                 VARCHAR2(400)   NOT NULL,  /*结论公式*/
createTime  			DATE,			           /*创建时间*/
updateTime  			DATE,			           /*更新时间*/
creatorId   			VARCHAR2(64),              /*创建者(评估人)ID*/
createor     			VARCHAR2(64),		       /*创建者(评估人)*/
CONSTRAINT pk_Eval_EvtGde_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_EvtGde_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id),
CONSTRAINT fk_Eval_EvtGde_eventId FOREIGN KEY (eventId) REFERENCES T_Event_Info(id),
CONSTRAINT fk_Eval_EvtGde_prjCfgId FOREIGN KEY (prjCfgId) REFERENCES T_Eval_ProjectCfg(id));

/*-------------------------------------------------------------------------------
应急能力评估明细表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventGradeItem(
id 						VARCHAR2(64)	NOT NULL,  /*主键*/
fatherId  				VARCHAR2(64)	NOT NULL,  /*应急能力评估Id*/
inidId       			VARCHAR2(64)	NOT NULL,  /*指标Id*/
weightValue      		NUMBER(10,2)    NOT NULL,  /*分值权重*/
formula                 VARCHAR2(400),             /*计算公式*/
evalValue       	    NUMBER(20,5)    NOT NULL,  /*评估值*/
seqc       			    NUMBER(10,0)    NOT NULL,  /*序号*/
CONSTRAINT pk_Eval_EvtGdeI_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_EvtGdeI_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_EventGrade(id) ON DELETE CASCADE,
CONSTRAINT fk_Eval_EvtGdeI_inidId FOREIGN KEY (inidId) REFERENCES T_Eval_EvalInid(id));

/*-------------------------------------------------------------------------------
过程再现表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProcessEmersion(
id                      VARCHAR2(64)     NOT NULL,  /*主键*/
eventId                 VARCHAR2(64)     NOT NULL,  /*事件Id*/
eventType               VARCHAR2(32)     NOT NULL,  /*事件类别*/
eventName               VARCHAR2(200)    NOT NULL,  /*事件名称*/
happenDate              DATE,                       /*事发时间*/
happenAddress           VARCHAR2(100),              /*事发详细地址*/
eventLever              VARCHAR2(32),               /*事件级别*/
CONSTRAINT pk_Eval_ProcessEmersion_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
过程再现明细表
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProcessEmersionItem(
id                      VARCHAR2(64) 	NOT NULL,  /*主键*/
fatherId                VARCHAR2(64)    NOT NULL,  /*过程再现id*/
bizId                   VARCHAR2(64),              /*实体id*/
bizTable                VARCHAR2(100),             /*表名*/
processType             VARCHAR2(128)   NOT NULL,  /*过程类型*/
processContent          VARCHAR2(1000),            /*过程详细信息*/
createTime              DATE,                      /*创建时间*/
creatorId   			VARCHAR2(64),              /*创建者Id*/
createor     			VARCHAR2(400),		       /*创建者*/
CONSTRAINT pk_Eval_ProcessEmersionItem_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_ProcessEmersionItem_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_ProcessEmersion(id) ON DELETE CASCADE);



/*-------------------------------------------------------------------------------
视频监控
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Monitor(
id                     VARCHAR2(64)   NOT NULL, /*主键*/
typeId                 VARCHAR2(64)   NOT NULL, /*监控类别*/
name                   VARCHAR2(400)  NOT NULL, /*名称*/
company                VARCHAR2(400),           /*单位名称*/ 
gisX                   NUMBER(20,10),           /*地图x坐标*/
gisY                   NUMBER(20,10),           /*地图y坐标*/
linker                 VARCHAR2(50),            /*联系人*/
tel                    VARCHAR2(50),            /*联系电话*/
code	               VARCHAR2(50),            /*编号*/ 
factoryTypeId          VARCHAR2(128),           /*厂家类别*/
ip                     VARCHAR2(50),            /*ip*/
port				   VARCHAR2(50),            /*端口*/
lgnAcc                 VARCHAR2(50),            /*登录账户*/
pwd                    VARCHAR2(50),            /*密码*/
channelNo              VARCHAR2(50),            /*通道号*/ 
disabled               VARCHAR2(1),             /*地图展示状态*/
addr                   VARCHAR2(400),           /*地址*/
orgId              	   VARCHAR2(64),            /*所属组织*/
CONSTRAINT pk_Base_Monitor_id PRIMARY KEY (id));>>>>>>> .r32432
