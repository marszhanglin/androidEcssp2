/*------------------------------------------------------------------------------
 ����-����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Esb_Token(                
id             VARCHAR2(64),             /*����*/
pkey           VARCHAR2(16)   NOT NULL,  /*��Կ*/
value          VARCHAR2(500)  NOT NULL,  /*����ֵ*/
allowedIps     VARCHAR2(500),            /*�����ipֵ ���ʱ��";"�ָ� Ϊ��ʱ����֤*/
CONSTRAINT pk_Esb_Token_id PRIMARY KEY (id));

/*------------------------------------------------------------------------------
 ����-������־
-------------------------------------------------------------------------------*/
CREATE TABLE T_Esb_Log(                  
id             VARCHAR2(64),             /*����*/
createTime     DATE,                     /*����ʱ��*/
tokenId        VARCHAR2(64),             /*��������Id*/
tokenValue     VARCHAR2(500),            /*��������ֵ*/
ip             VARCHAR2(20),             /*ip*/
requestUri     VARCHAR2(300),            /*����·��*/
errMsg         VARCHAR2(4000),           /*�쳣��Ϣ*/
CONSTRAINT pk_Esb_Log_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 �ƶ�Ӧ��-�û���Ȩ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Mobile_UserGrant(         
id             VARCHAR2(64),             /*����*/
createTime     DATE,                     /*����ʱ��*/
expireTime     DATE,                     /*����ʱ��*/
userId         VARCHAR2(64)   NOT NULL,  /*�û�Id*/
imei           VARCHAR2(50),             /*IMEI��*/
grantedCode    NUMBER(10,0)   NOT NULL,  /*��Ȩ��*/
pkey           VARCHAR2(16)   NOT NULL,  /*��Կ*/
CONSTRAINT pk_Mobile_UserGrant_id PRIMARY KEY (id),
CONSTRAINT fk_Mobile_UserGrant_userId FOREIGN KEY (userId) REFERENCES Org_User(id) ON DELETE CASCADE);

/*------------------------------------------------------------------------------
 �ƶ�Ӧ��-��������
-------------------------------------------------------------------------------*/
CREATE TABLE T_Mobile_ServiceConfig(     
id             VARCHAR2(64),             /*����*/
createTime     DATE,                     /*����ʱ��*/
cls            VARCHAR2(200)   NOT NULL, /*����*/
mthd           VARCHAR2(200)   NOT NULL, /*������*/
code           VARCHAR2(200)   NOT NULL, /*�������*/
name           VARCHAR2(400)   NOT NULL, /*��������*/
summary        VARCHAR2(500),            /*����˵��*/
memo           VARCHAR2(500),            /*��ע*/
disabled       VARCHAR2(1)     NOT NULL, /*�Ƿ���á�'0'�����ã�'1'������*/
CONSTRAINT pk_Mobile_ServiceConfig_id PRIMARY KEY (id));

/*------------------------------------------------------------------------------
 �ƶ�Ӧ��-������־
-------------------------------------------------------------------------------*/
CREATE TABLE T_Mobile_Log(               
id             VARCHAR2(64),             /*����*/
createTime     DATE,                     /*����ʱ��*/
svcId          VARCHAR2(64),             /*����Id*/
svcCls         VARCHAR2(200),            /*��������*/
svcMthd        VARCHAR2(200),            /*���񷽷���*/
svcCode        VARCHAR2(200),            /*�������*/
svcName        VARCHAR2(400),            /*��������*/
ip             VARCHAR2(20),             /*ip*/
grantedId      VARCHAR2(64),             /*�û���ȨId*/
userId         VARCHAR2(64),             /*�û�Id*/
loginName      VARCHAR2(32),             /*��¼��*/
userName       VARCHAR2(32),             /*�û�����*/
imei           VARCHAR2(50),             /*IMEI��*/
grantedCode    NUMBER(10,0),             /*��Ȩ��*/
errMsg         VARCHAR2(4000),           /*�쳣��Ϣ*/
CONSTRAINT pk_Mobile_Log_id PRIMARY KEY (id));

/*------------------------------------------------------------------------------
 Ӧ��ָ�Ӵ��ã�ҵ������Ϣ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Incident_Function(					
id              VARCHAR2(64)   NOT NULL, /*����*/
functionCode    VARCHAR2(64)   NOT NULL, /*����ΨһID*/
functionName    VARCHAR2(100)  NOT NULL, /*��������*/
functionSuper   VARCHAR2(64),			 /*�ϼ�����*/
functionUrl     VARCHAR2(300),			 /*����URL*/
functionPic     VARCHAR2(50),			 /*����ͼ��*/
functionOrder   NUMBER(5,2)    NOT NULL, /*����˳��*/
functionView    VARCHAR2(30),			 /*���Ʒ�ʽ 1:������ 2:���ýű�����*/
attr            VARCHAR2(50),			 /*�����ֶ�*/
createTime      DATE,					 /*�½�ʱ��*/
updateTime      DATE,					 /*����ʱ��*/
createBy        VARCHAR2(64),			 /*������*/	
CONSTRAINT pk_Indt_Func_id PRIMARY KEY (id),
CONSTRAINT uk_Indt_Func_functionCode UNIQUE (functionCode),
CONSTRAINT fk_Indt_Func_functionSuper FOREIGN KEY (functionSuper) REFERENCES T_Incident_Function(id));

/*------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-ҵ����-�¼��������ñ�
-------------------------------------------------------------------------------*/
CREATE TABLE T_Incident_Function_Config(			
id              VARCHAR2(64)   NOT NULL, /*����*/
eventType       VARCHAR2(64)   NOT NULL, /*�¼�����*/
departId        VARCHAR2(64)   NOT NULL, /*�û�����ID*/
functionCode    VARCHAR2(64)   NOT NULL, /*����ΨһID*/
functionName    VARCHAR2(100)  NOT NULL, /*��������*/
functionSuper   VARCHAR2(64),			 /*�ϼ�����*/
functionOrder   NUMBER(5,2)    NOT NULL, /*����˳��*/
attr            VARCHAR2(50),			 /*�����ֶ�*/
createTime      DATE,					 /*�½�ʱ��*/
updateTime      DATE,					 /*����ʱ��*/
createBy        VARCHAR2(64),			 /*������*/
CONSTRAINT pk_Indt_Func_Cfg_id PRIMARY KEY (id),
CONSTRAINT fk_Indt_Func_Cfg_eventType FOREIGN KEY (eventType) REFERENCES T_Event_EventType(id),
CONSTRAINT fk_Indt_Func_Cfg_departId FOREIGN KEY (departId) REFERENCES Org_Organization(id),
CONSTRAINT fk_Indt_Func_Cfg_functionSuper FOREIGN KEY (functionSuper) REFERENCES T_Incident_Function_Config(id));

/*------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-ҵ����-�¼�ʵ����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Incident_Function_Event  (			
id              VARCHAR2(64)  NOT NULL, /*����*/
eventId         VARCHAR2(64)  NOT NULL, /*�¼�ID*/
eventType       VARCHAR2(64)  NOT NULL,	/*�¼�����*/
functionCode    VARCHAR2(64)  NOT NULL,	/*����ΨһID*/
functionName    VARCHAR2(100) NOT NULL,	/*��������*/
functionType    VARCHAR2(30),			/*�������*/
functionUrl     VARCHAR2(500),			/*����URL*/
functionPic		VARCHAR2(50),			/*����ͼ��*/
functionSuper   VARCHAR2(64),			/*�ϼ�����*/
functionOrder   NUMBER(5,2)   NOT NULL,	/*����˳��*/
functionView    VARCHAR2(30),			/*���Ʒ�ʽ 1:������ 2:���ýű�����*/
attr            VARCHAR2(50),			/*�����ֶ�*/
createTime      DATE,					/*�½�ʱ��*/
updateTime      DATE,					/*����ʱ��*/
createBy        VARCHAR2(64),			/*������*/
CONSTRAINT pk_Indt_Func_Evt_id PRIMARY KEY (id),
CONSTRAINT fk_Indt_Func_Evt_eventId FOREIGN KEY (eventId) REFERENCES T_Event_Info(id) ON DELETE CASCADE,
CONSTRAINT fk_Indt_Func_Evt_functionSuper FOREIGN KEY (functionSuper) REFERENCES T_Incident_Function_Event(id));

/*------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-�¼���Ϣ�ӱ�-Σ��Ʒ��
-------------------------------------------------------------------------------*/
create table T_Event_Dangeritem	(
id         		VARCHAR2(36) 	NOT NULL, /*����*/
xh         		NUMBER(36),				  /*���*/
zwm        		VARCHAR2(300),			  /*������*/
ywm        		VARCHAR2(300),			  /*Ӣ����*/
fzz        		VARCHAR2(300),            /*����ʽ*/
fzl        		VARCHAR2(300),            /*������*/
cash       		VARCHAR2(300),            /*CAS��*/
rtecsh     		VARCHAR2(300),            /*RTECS��*/
unbh       		VARCHAR2(300),            /*UN���*/
wxhwbh     		VARCHAR2(300),            /*Σ�ջ�����*/
imdggzym   		VARCHAR2(300),            /*IMDG����ҳ��*/
wgyxz      		VARCHAR2(500),            /*�������״*/
zyyt       		VARCHAR2(4000),           /*��Ҫ��;*/
rd         		VARCHAR2(300),            /*�۵�*/
fd         		VARCHAR2(300),            /*�е�*/
xdmds      		VARCHAR2(300),            /*����ܶ�ˮ*/
xdmdkq     		VARCHAR2(300),            /*����ܶȿ���*/
bhzqy      		VARCHAR2(300),            /*��������ѹ*/
rjx        		VARCHAR2(500),            /*�ܽ���*/
rsx        		VARCHAR2(300),            /*ȼ����*/
jghxfj     		VARCHAR2(300),            /*������շּ�*/
sd         		VARCHAR2(300),            /*����*/
zrwd       		VARCHAR2(300),            /*��ȼ�¶�*/
bzxx       		VARCHAR2(300),            /*��ը����*/
bzsx       		VARCHAR2(300),            /*��ը����*/
wxtx       		VARCHAR2(4000),           /*Σ������*/
rsfjcw     		VARCHAR2(500),            /*ȼ�շֽ����*/
wdx        		VARCHAR2(300),            /*�ȶ���*/
jhwh       		VARCHAR2(300),            /*�ۺ�Σ��*/
jjw        		VARCHAR2(500),            /*������*/
mdff       		VARCHAR2(4000),           /*��𷽷�*/
wxxlb     		VARCHAR2(500),            /*Σ�������*/
wxhwbzbz   		VARCHAR2(300),            /*Σ�ջ����װ��־*/
bzlb       		VARCHAR2(300),            /*��װ���*/
cyzysx     		VARCHAR2(4000),           /*����ע������*/
jcxz       		VARCHAR2(500),            /*�Ӵ���ֵ*/
qrtj       		VARCHAR2(500),            /*����;��*/
dx         		VARCHAR2(600),            /*����*/
jkwh       		VARCHAR2(4000),           /*����Σ��*/
pfjc       		VARCHAR2(600),            /*Ƥ���Ӵ�*/
yjjc       		VARCHAR2(4000),           /*�۾��Ӵ�*/
xr         		VARCHAR2(4000),           /*����*/
sr         		VARCHAR2(4000),           /*ʳ��*/
gckz       		VARCHAR2(4000),           /*���̿���*/
hxxtfh    		VARCHAR2(4000),           /*����ϵͳ����*/
yjfh       		VARCHAR2(600),            /*�۾�����*/
fhf        		VARCHAR2(600),            /*������*/
shf        		VARCHAR2(600),            /*�ַ���*/
ljwd       		VARCHAR2(300),            /*�ٽ��¶�*/
ljyl       		VARCHAR2(300),            /*�ٽ�ѹ��*/
rsr        		VARCHAR2(300),            /*ȼ����*/
bmjcdtj    		VARCHAR2(500),            /*����Ӵ�������*/
xlcz       		VARCHAR2(4000),           /*й©����*/
qt         		VARCHAR2(4000),           /*����*/
slxlglbj   		VARCHAR2(300),            /*����й©����뾶*/
slxlbtssbj 		VARCHAR2(300),		      /*����й©������ɢ�뾶*/
slxlyjssbj 		VARCHAR2(300),            /*����й©ҹ����ɢ�뾶*/
dlxlglbj   		VARCHAR2(300),			  /*����й©����뾶*/
dlxlbtssbj 		VARCHAR2(300),            /*����й©������ɢ�뾶*/
dlxlyjssbj 		VARCHAR2(300),            /*����й©ҹ����ɢ�뾶*/
zcljl      		FLOAT,                    /*zcljl*/
scljl      		FLOAT,					  /*scljl*/
ljl        		VARCHAR2(10),             /*Σ�ջ�ѧƷ���Ӧ���ٽ���*/
xzxs       		VARCHAR2(10),             /*Σ�ջ�ѧƷ���Ӧ��У��ϵ��*/
showSign   		VARCHAR2(10),             /*��ʾ��ʶ*/
eventId         VARCHAR2(64),             /*�����¼�ID*/
CONSTRAINT pk_Event_DangerItem_id PRIMARY KEY (id));
/*------------------------------------------------------------------------------
 ��ҵ������Ϣ-�ܱ���Ϣ��
-------------------------------------------------------------------------------*/
create table T_UNITS_AROUND
(
  id             VARCHAR2(64) NOT NULL, /*����*/
  unitsid        VARCHAR2(64) NOT NULL, /*��ҵ��ϢID*/
  aroundname     VARCHAR2(200),         /*�ܱ���Ϣ����*/
  address        VARCHAR2(300),         /*��ϸ��ַ*/
  gisx           VARCHAR2(20),          /*����*/
  gisy           VARCHAR2(20),          /*γ��*/
  pic360         VARCHAR2(512),         /*�ܱ�ͼƬ����ID*/
  attr1          VARCHAR2(50),          /*�����ֶ�1*/
  attr2          VARCHAR2(50),          /*�����ֶ�2*/
  remark         VARCHAR2(200),         /*��ע*/
  curstatus      VARCHAR2(32),          /*״̬*/
  createtime     DATE,                  /*����ʱ��*/
  createby       VARCHAR2(32),          /*������*/
  updatetime     DATE,                  /*�޸�ʱ��*/
  updateby       VARCHAR2(32),          /*�޸���*/
  dataflg        VARCHAR2(10)           /*���ݱ�־*/
CONSTRAINT pk_Units_Around_id PRIMARY KEY (id),
CONSTRAINT fk_Units_Around_unitsId FOREIGN KEY (unitsId) REFERENCES T_Base_Units(id) ON DELETE CASCADE);
/*------------------------------------------------------------------------------
Ӧ��Ԥ��-�¼���Ӧ-Ӧ����Դ������ϸ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Plan_Event_Resource_Item  (
   id                 VARCHAR2(64) NOT NULL,    /*����*/
   planId             VARCHAR2(64) NOT NULL,	/*Ӧ��Ԥ��ID*/
   eventId            VARCHAR2(64) NOT NULL,	/*�¼�ID*/
   taskId             VARCHAR2(10),			    /*����ID*/
   resourceId         VARCHAR2(64),			    /*��Դ���*/
   resourceType       VARCHAR2(32),			    /*��Դ����*/
   resourceName       VARCHAR2(100),			/*��Դ����*/
   fatherId           VARCHAR2(64) NOT NULL,	/*����ID*/
   resourceNum        NUMBER(8),				/*��Դ����*/
   gisY               VARCHAR2(32),			    /*γ��*/
   gisX               VARCHAR2(32),			    /*����*/
   state              VARCHAR2(32),			    /*״̬*/
   remark             VARCHAR2(100),			/*��ע*/
   dataflg            VARCHAR2(10),			    /*���ݱ�־*/
   createBy           VARCHAR2(32),			    /*������*/
   createTime         DATE,					    /*����ʱ��*/
   updateBy           VARCHAR2(32),			    /*�޸���*/
   updateTime    	  DATE,					    /*�޸�ʱ��*/
   resourcedeptid     VARCHAR2(64),				/*��������id*/
   resourcpersonid    VARCHAR2(64),				/*������id*/
   resourcepersonname VARCHAR2(64),				/*������*/
   resourcedeptname   VARCHAR2(64),				/*��������*/
   CONSTRAINT pk_PlanEveResItem_id PRIMARY KEY (id));
   
/*------------------------------------------------------------------------------
�¼��ӱ�-��ҵ��Ϣ-�ش�Σ��Դ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_Danger(
   id                        VARCHAR2(64)   NOT NULL, /*����*/
   unitId                    VARCHAR2(64)   NOT NULL, /*��ҵID*/
   unitName                  VARCHAR2(100)  NOT NULL, /*��ҵ����*/
   principalName             VARCHAR2(100)  NOT NULL, /*����������*/
   phoneNumber               VARCHAR2(15)   NOT NULL, /*��ϵ�绰*/
   DangerFountainheadType    VARCHAR2(50)   NOT NULL, /*Σ��Դ����*/
   DangerFountainheadName    VARCHAR2(100)  NOT NULL, /*Σ��Դ����*/
   unitType                  VARCHAR2(15)   NOT NULL, /*��λ����*/
   declarantLevel            VARCHAR2(15)   NOT NULL, /*�걨�ȼ�*/
   
   CONSTRAINT pk_BaseUnitlDanger_id PRIMARY KEY (id),
   CONSTRAINT fk_Base_Danger_unitsId FOREIGN KEY (unitId) REFERENCES T_Base_Units(id) ON DELETE CASCADE);
);

/*-------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-��ҵ����֤��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_QlfctCtfct (
id                     VARCHAR2(64)   NOT NULL, /*����*/
unitsId                VARCHAR2(64),            /*��ҵID*/
qymc                   VARCHAR2(100),           /*��ҵ����*/
credentialsZzbh        VARCHAR2(50),            /*���ʱ��*/    
credentialsZzmc        VARCHAR2(100),           /*��������*/
credentialsZzlx        VARCHAR2(50),            /*��������*/
credentialsFzsj        VARCHAR2(50),            /*��֤ʱ��*/
credentialsYxq         VARCHAR2(50),            /*��Ч��*/
credentialsFzdw        VARCHAR2(100),           /*��֤��λ*/
credentialsBz          VARCHAR2(200),           /*��ע*/
flg                    VARCHAR2(20),            /*״̬*/
createBy               VARCHAR2(64),            /*������*/
createDt               VARCHAR2(64),            /*����ʱ��*/
updateBy               VARCHAR2(64),            /*�޸���*/
updateDt               VARCHAR2(64),            /*�޸�ʱ��*/
attachmentIds          VARCHAR2(1024),          /*����ID*/     
attachmentNames        VARCHAR2(2048),          /*��������*/
attachurl              VARCHAR2(2048 CHAR),   
CONSTRAINT pk_Base_Unit_QlfctCtfct_id PRIMARY KEY (id),
CONSTRAINT fk_Unit_QlfctCtfct_unitsId FOREIGN KEY (unitsId) REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-��ҵ��Ʒ�豸
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_ProdEquip(
id                     VARCHAR2(64) NOT NULL,   /*����*/
unitsId                VARCHAR2(64),			/*��ҵID*/
qymc                   VARCHAR2(100),        	/*��ҵ����*/
prodCpmc               VARCHAR2(100),          	/*��Ʒ����*/    
prodNcl                VARCHAR2(100),          	/*�����*/
prodNcz                NUMBER(12),           	/*���ֵ*/
prodNxssr              NUMBER(12),            	/*����������*/
prodBz                 VARCHAR2(200),        	/*��ע*/
attr1                  VARCHAR2(50),           	/*�����ֶ�1*/
attr2                  VARCHAR2(50),           	/*�����ֶ�2*/
flg                    VARCHAR2(20),           	/*״̬*/
createBy               VARCHAR2(64),           	/*������*/
createDt               VARCHAR2(64),           	/*����ʱ��*/
updateBy               VARCHAR2(64),           	/*�޸���*/
updateDt               VARCHAR2(64),           	/*�޸�ʱ��*/  
CONSTRAINT pk_Base_Unit_ProdEquip_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_ProdEquip_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-�����豸
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_Special(
id                     VARCHAR2(64)   NOT NULL, /*����*/
unitsId                VARCHAR2(64),            /*��ҵID*/
qymc                   VARCHAR2(100),           /*��ҵ����*/
equipName              VARCHAR2(100),           /*�豸����*/    
equipCode              VARCHAR2(100),           /*�豸���*/
equipLevel             VARCHAR2(100),          	/*�豸����*/
madeIn                 VARCHAR2(100),           /*���쵥λ*/
instalDate             VARCHAR2(32),            /*��װʱ��*/
enableDate             VARCHAR2(32),            /*��������*/
checkDate              VARCHAR2(32),            /*���ʱ��*/
lastDate               VARCHAR2(32),            /*��������*/
attachUrl          	   VARCHAR2(200),           /*����*/
CONSTRAINT pk_Base_Unit_Special_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_Special_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*------------------------------------------------------------------------------
�¼��ӱ�-��ҵ��Ϣ-��ȫ��ʩ�豸���
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_SafeEquip(
id                    	VARCHAR2(64)   NOT NULL,  	/*����*/
unitsId                	VARCHAR2(64),  				/*��ҵID*/
equipName         		VARCHAR2(100), 				/*�豸����*/
equipNumber       		NUMBER(8), 					/*�豸����*/
normalNumber        	NUMBER(8), 					/*�����豸����*/
breakNumber     		NUMBER(8), 					/*���豸����*/
stopNumber      		NUMBER(8), 					/*ͣ���豸����*/
attachUrl          	    VARCHAR2(200),           	/*����*/
CONSTRAINT pk_Base_Unit_SafeEquip_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_SafeEquip_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE);
);
/*-------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-������������Ϣ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_TankArea(
id                     VARCHAR2(64)   NOT NULL, /*����*/
unitsId                VARCHAR2(64),            /*��ҵID*/
qymc                   VARCHAR2(100),           /*��ҵ����*/
tankName               VARCHAR2(100),           /*����������*/    
tankCode               VARCHAR2(100),           /*���������*/
tankTotal              VARCHAR2(100),          	/*�ݻ�����*/
tankCount              VARCHAR2(100),           /*���޸���*/
tankDike               VARCHAR2(200),           /*���޷����̷�*/
tankPlace              VARCHAR2(200),           /*λ��*/
attachUrl          	   VARCHAR2(200),           /*����*/
CONSTRAINT pk_Base_Unit_TankArea_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_TankArea_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-������������Ϣ--������Σ��������Ϣ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_TankAreaSub(
id                     VARCHAR2(64)   NOT NULL, /*����*/
tankId                 VARCHAR2(64),            /*������ID*/
objectName             VARCHAR2(100),           /*��������*/
capacity               VARCHAR2(100),           /*����*/
maxQuality             VARCHAR2(100),           /*���洢��������*/    
realQuality            VARCHAR2(100),           /*ʵ�ʴ洢��������*/
CONSTRAINT pk_Unit_TankAreaSub_id PRIMARY KEY (id),
CONSTRAINT fk_Unit_TankAreaSub_tankId FOREIGN KEY (tankId) 
  REFERENCES T_Base_Unit_TankArea(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-����������Ϣ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_Reservoir(
id                     VARCHAR2(64)   NOT NULL, /*����*/
unitsId                VARCHAR2(64),            /*��ҵID*/
qymc                   VARCHAR2(100),           /*��ҵ����*/
reservoirName          VARCHAR2(100),           /*��������*/    
reservoirCount         VARCHAR2(100),           /*��������*/
reservoirArea          VARCHAR2(100),          	/*�������*/
reservoirFun           VARCHAR2(100),           /*����������*/
reservoirTel           VARCHAR2(32),            /*��ϵ�绰*/
attachUrl          	   VARCHAR2(200),           /*����*/
CONSTRAINT pk_Base_Unit_Reservoir_id PRIMARY KEY (id),
CONSTRAINT fk_Base_Unit_Reservoir_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);
/*-------------------------------------------------------------------------------
Ӧ��ָ�Ӵ���-��ҵ��Ϣ-Σ������װ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Unit_DangerProd(
id                     VARCHAR2(64)   NOT NULL, /*����*/
unitsId                VARCHAR2(64),            /*��ҵID*/
qymc                   VARCHAR2(100),           /*��ҵ����*/
dangerName         VARCHAR2(100),           /*����װ������*/    
dangerFun          VARCHAR2(100),           /*����������*/
dangerArea         VARCHAR2(100),          	/*ռ�����*/
dangerValue        VARCHAR2(100),           /*�̶��ʲ���ֵ*/
dangerTel          VARCHAR2(64),            /*��ϵ�绰*/
attachUrl          	   VARCHAR2(200),           /*����*/
CONSTRAINT pk_Base_DangerProd_id PRIMARY KEY (id),
CONSTRAINT fk_Base_DangerProd_unitsId FOREIGN KEY (unitsId) 
	REFERENCES T_Base_Units(id) ON DELETE CASCADE
);

/*-------------------------------------------------------------------------------
Ӧ����Դ-��Դ����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Resource_Type(
id                     	 VARCHAR2(64)   NOT NULL, /*����*/
valueName                VARCHAR2(128),            /*��������*/
isleaf                  VARCHAR2(64),           /*�Ƿ����ӽڵ�0��1*/
parentId    			VARCHAR2(64),           /*���ڵ�id*/    
type          			VARCHAR2(128),           /*�˴�����*/ 
icon 					VARCHAR2(128),			/*ͼ��*/
CONSTRAINT pk_Resource_Type_id PRIMARY KEY (id)
);


create table T_Mobile_Reg_User
(
  id         VARCHAR2(32) not null,/*主键*/
  orgid      VARCHAR2(32),         /*机构id*/
  loginname  VARCHAR2(32) not null,/*登录账号 */
  pwd        VARCHAR2(128) not null,/*密码 */ 
  ifaudit      CHAR(1) default 0 not null ,/* 是否审核通过（默认为不通过�?/
  createtime VARCHAR2(32),   /*创建时间*/
  updatetime VARCHAR2(32),   /*更新时间*/
  creatname  VARCHAR2(32),    /*创建�?*/
  
  CONSTRAINT pk_T_Mobile_User_id PRIMARY KEY (id)

);

/*-------------------------------------------------------------------------------
ֵ��ֵ��-֪ͨ����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_Notice(
id                     	 VARCHAR2(64)   NOT NULL,  /*����*/
orgId                    VARCHAR2(64)   NOT NULL,  /*������֯*/
rcvOrgIds                VARCHAR2(1000) NOT NULL,  /*������֯Ids*/
rcvOrgNames              VARCHAR2(1000) NOT NULL,  /*������֯*/
title                    VARCHAR2(200)  NOT NULL,  /*����*/
summary                  VARCHAR2(1000) NOT NULL,  /*ժҪ*/
typeId                   VARCHAR2(128)  NOT NULL,  /*����*/
checked                  VARCHAR2(1)    NOT NULL,  /*�Ƿ����*/
content         	     CLOB,                     /*����*/    
atchIds                  VARCHAR2(1000),           /*��������*/
createTime               DATE,					   /*����ʱ��*/
updateTime    	         DATE,				       /*�޸�ʱ��*/
creatorId                VARCHAR2(64),             /*������Id*/
creator                  VARCHAR2(400),            /*������*/
CONSTRAINT pk_Duty_Notice_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_Notice_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));

/*-------------------------------------------------------------------------------
ֵ��ֵ��-֪ͨ����֪ͨ��Ա��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_NoticePerson(
id                     	 VARCHAR2(64)   NOT NULL,  /*����*/
fatherId                 VARCHAR2(64)   NOT NULL,  /*����֪ͨ����Id*/
personId                 VARCHAR2(64)   NOT NULL,  /*��ԱId*/
seqc                     NUMBER(10,0)   NOT NULL,  /*���*/
CONSTRAINT pk_Duty_NoticePsn_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_NoticePsn_fatherId FOREIGN KEY (fatherId) REFERENCES T_Duty_Notice(id) ON DELETE CASCADE,
CONSTRAINT fk_Duty_NoticePsn_personId FOREIGN KEY (personId) REFERENCES T_Duty_Address(id));

/*-------------------------------------------------------------------------------
ֵ��ֵ��-��������
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_UndisposedMatter(
id                       VARCHAR2(64)    NOT NULL,  /*����*/
orgId                    VARCHAR2(64)    NOT NULL,  /*������֯*/
title                    VARCHAR2(256)   NOT NULL,  /*�����������*/
content                  VARCHAR2(1000)  NOT NULL,  /*������������*/
status                   VARCHAR2(128),             /*��������״̬*/
currentTime              DATE,                      /*��������ʱ��*/
startTime                DATE,                      /*�������ʼʱ��*/
endTime                  DATE,                      /*�����������ʱ��*/
remark                   VARCHAR2(1000),            /*��ע*/
createBy                 VARCHAR2(64),              /*������*/
createTime               DATE,                      /*����ʱ��*/
updateBy                 VARCHAR2(64),              /*�޸���*/
updateTime               DATE,                      /*�޸�ʱ��*/ 
CONSTRAINT pk_Duty_UndisposedMatter_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_UndisposedMatter_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));

/*-------------------------------------------------------------------------------
ֵ��ֵ��-�ر���������
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_SpecialNotice(
id                       VARCHAR2(64)    NOT NULL,  /*����*/
orgId                    VARCHAR2(64)    NOT NULL,  /*������֯*/
title                    VARCHAR2(256)   NOT NULL,  /*����*/
content                  VARCHAR2(1000)  NOT NULL,  /*����*/
leve					 VARCHAR2(128)   NOT NULL,  /*�ȼ�*/
noticeTime               DATE,                      /*����ʱ��*/
startTime                DATE,                      /*��ʼʱ��*/
endTime                  DATE,                      /*����ʱ��*/
remark                   VARCHAR2(1000),            /*��ע*/
createBy                 VARCHAR2(64),              /*������*/
createTime               DATE,                      /*����ʱ��*/
updateBy                 VARCHAR2(64),              /*�޸���*/
updateTime               DATE,                      /*�޸�ʱ��*/ 
CONSTRAINT pk_Duty_SpecialNotice_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_SpecialNotice_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));


/*-------------------------------------------------------------------------------
�����ֶ�����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_IssueType(
id                     VARCHAR2(64)   NOT NULL, /*����*/
isn                    VARCHAR2(2000) NOT NULL, /*����*/
code                   VARCHAR2(200),           /*����*/
value				   VARCHAR2(200), 			/*ֵ*/ 
name                   VARCHAR2(400)  NOT NULL, /*����*/
isDir                  VARCHAR2(1)    NOT NULL, /*�Ƿ�Ŀ¼��'0'����'1'����*/
isLeaf                 VARCHAR2(1)    NOT NULL, /*�Ƿ���ϸ����'0'����'1'����*/
rank                   NUMBER(2,0)    NOT NULL, /*����*/
parentId    		   VARCHAR2(64),            /*���ڵ�id*/    
CONSTRAINT pk_Infoissue_issueType_id PRIMARY KEY (id),
CONSTRAINT uk_Infoissue_issueType_code UNIQUE (code),
CONSTRAINT fk_Infoissue_Type_parentId FOREIGN KEY (parentId) REFERENCES T_Infoissue_issueType(id));

/*-------------------------------------------------------------------------------
��Ϣ����-�����ֶ�
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_Way(
id 						VARCHAR2(64)	 NOT NULL,	/*����*/
typeId 					VARCHAR2(128)	 NOT NULL,	/*�����ֶ�ID*/
explain 				VARCHAR2(1024),				/*˵��*/
seqc    			    NUMBER(10,0)       NOT NULL,  /*����*/
createTime  			DATE,			            /*����ʱ��*/
updateTime  			DATE,			            /*����ʱ��*/
creatorId   			VARCHAR2(64),               /*������ID*/
createor     			VARCHAR2(64),		        /*������*/
CONSTRAINT pk_Infoissue_way_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
��Ϣ����-�����ֶ���ϸ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_WayItem(
id 						VARCHAR2(64)	 NOT NULL,	/*����*/
fatherId  				VARCHAR2(64)	 NOT NULL,	/*����ID*/
name 					VARCHAR2(128)	 NOT NULL,	/*����*/
orgId 					VARCHAR2(64)	 NOT NULL,	/*���ŵ�λ*/
lgnacc					VARCHAR2(128),				/*�û���*/
pwd						VARCHAR2(128),				/*����*/
ip						VARCHAR2(128),				/*IP*/
port					VARCHAR2(128),				/*�˿�*/
channelNo				VARCHAR2(128),				/*Ƶ��*/
factoryId 				VARCHAR2(128),				/*����ID*/
deviceNo	 			VARCHAR2(128),				/*�豸���*/
createTime  			DATE,			            /*����ʱ��*/
updateTime  			DATE,			            /*����ʱ��*/
creatorId   			VARCHAR2(64),               /*������ID*/
createor     			VARCHAR2(64),		        /*������*/
CONSTRAINT pk_Infoissue_wayitem_id PRIMARY KEY (id),
CONSTRAINT fk_Infoissue_wayitem_fatid FOREIGN KEY (fatherId) REFERENCES T_Infoissue_Way(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
��Ϣ����-����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_Strategy(
id          			VARCHAR2(64)   NOT NULL,   /*����*/
orgid       			VARCHAR2(64)   NOT NULL,   /*������֯*/
name       				VARCHAR2(200)  NOT NULL,   /*����*/
typeid      			VARCHAR2(128)  NOT NULL,   /*��Ϣ����*/
levels     				VARCHAR2(128)  NOT NULL,   /*��Ϣϸ��*/
process					VARCHAR2(128)  NOT NULL,   /*����*/
createtime  			DATE,                      /*����ʱ��*/
updatetime  			DATE,                      /*����ʱ��*/
creatorid   			VARCHAR2(64),              /*������ID*/
creator     			VARCHAR2(400),             /*������*/
CONSTRAINT pk_Infoissue_Strategy_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
��Ϣ����-����-�����ֶ�
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_StrategyWay(
id 						VARCHAR2(64)   NOT NULL,   /*����*/
strategyId 				VARCHAR2(64)   NOT NULL,   /*����ID*/
wayId					VARCHAR2(64)   NOT NULL,   /*�ֶ�ID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*���*/
CONSTRAINT pk_Infoissue_StrategyWay_id PRIMARY KEY(id),
CONSTRAINT fk_Infoissue_SWay_strategyId FOREIGN KEY(strategyId) REFERENCES T_Infoissue_Strategy(id) ON DELETE CASCADE,
CONSTRAINT fk_Infoissue_StrategyWay_wayId FOREIGN KEY (wayId) REFERENCES T_Infoissue_Way(id));

/*-------------------------------------------------------------------------------
��Ϣ����-����-�����ֶ���ϸ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_StrategyWayItem(
id 						VARCHAR2(64)   NOT NULL,   /*����*/
strategyWayId			VARCHAR2(64)   NOT NULL,   /*���Է����ֶ�ID*/
wayItemId 				VARCHAR2(64)   NOT NULL,   /*�����ֶ���ϸID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*���*/
CONSTRAINT pk_Infoissue_SWayItem_id PRIMARY KEY(id),
CONSTRAINT fk_Infoissue_SWItem_SWayId FOREIGN KEY(strategyWayId) REFERENCES T_Infoissue_StrategyWay(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
��Ϣ����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_Info(
id             			 VARCHAR2(64)   NOT NULL,  /*����*/
title         			 VARCHAR2(128),	           /*����*/
summary     			 VARCHAR2(1000),  		   /*ժҪ*/
typeId  				 VARCHAR2(64),			   /*��Ϣ����*/
eventId					 VARCHAR2(64),			   /*�¼�ID*/
levels      			 VARCHAR2(64),			   /*��Ϣϸ��*/
state       			 VARCHAR2(16),			   /*�¼�״̬*/
InfluenceRange        	 VARCHAR2(2048),		   /*Ӱ�췶Χ*/
startTime			     DATE,					   /*Ԥ�ⷢ��ʱ��*/
endTime    			     DATE,			           /*Ԥ�����ʱ��*/
orgId         			 VARCHAR2(64),		       /*������λ*/
recorgIds   			 VARCHAR2(1000) NOT NULL,  /*������֯IDS*/
recorgNames 			 VARCHAR2(1000) NOT NULL,  /*������֯����*/
auth 					 VARCHAR2(64),			   /*������*/
contact				     VARCHAR2(128),			   /*��ϵ��ʽ*/
content      			 CLOB,					   /*����*/
createTime  			 DATE,			           /*����ʱ��*/
updateTime  			 DATE,			           /*����ʱ��*/
atchids       			 VARCHAR2(1000),		   /*����*/
origin      			 VARCHAR2(1000),		   /*��Դ*/
releaseDesc 			 VARCHAR2(2048),		   /*�������*/
release     			 CHAR(1),				   /*���״̬*/
creatorId   			 VARCHAR2(64),             /*������ID*/
createor     			 VARCHAR2(64),		       /*������*/
CONSTRAINT pk_Info_id PRIMARY KEY (id),
CONSTRAINT fk_Info_eventId FOREIGN KEY(eventId) REFERENCES T_Event_Info(id));


/*-------------------------------------------------------------------------------
��Ϣ����-��Ϣ�����ֶ�
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_InfoWay(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
infoId  				VARCHAR2(64)	NOT NULL,  /*T_Infoissue_Info�е�ID*/
wayId 					VARCHAR2(64)	NOT NULL,  /*�����ֶη���ID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*���*/
content					VARCHAR2(1024),			   /*����*/
other					VARCHAR2(1024),			   /*���ⷢ������*/
CONSTRAINT pk_InfoWay_id PRIMARY KEY (id),
CONSTRAINT fk_InfoWay_fatherId FOREIGN KEY(infoId) REFERENCES T_Infoissue_Info(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
��Ϣ����-��Ϣ�����ֶ���ϸ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Infoissue_InfoWayItem(
id 						VARCHAR2(64)   NOT NULL,   /*����*/
infoWayId				VARCHAR2(64)   NOT NULL,   /*��Ϣ�����ֶ�ID*/
wayItemId 				VARCHAR2(64)   NOT NULL,   /*�����ֶ���ϸID*/
seqc                    NUMBER(10,0)   NOT NULL,   /*���*/
CONSTRAINT pk_InfoWayItem_id PRIMARY KEY(id),
CONSTRAINT fk_InfoWayItem_infoWayId FOREIGN KEY(infoWayId) REFERENCES T_Infoissue_InfoWay(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
ͨѶ¼
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_Contacts(
id            			VARCHAR2(64)  NOT NULL, /*ϵͳid*/
name          			VARCHAR2(300) NOT NULL, /*��ϵ������*/
sex						VARCHAR2(32)  NOT NULL, /*�Ա� ��:1,Ů:2*/
alias					VARCHAR2(300), 			/*��ϵ�˱���*/
address         		VARCHAR2(300),			/*��ַ*/
orgId         			VARCHAR2(64)  NOT NULL, /*��λid*/
orgAlias				VARCHAR2(300),			/*��֯����*/
belongArea				VARCHAR2(32),			/*��������*/
quickSearch       		VARCHAR2(400) NOT NULL, /*���ٲ��ң������û��������绰���������Ϣ��*/
firstChar       		VARCHAR2(1)   NOT NULL, /*��ϵ����������ĸ����д��*/
callNos					VARCHAR2(1000),       	/*ͨѶ��ʽ*/
gisX          			NUMBER(20,10),          /*��ͼx����*/
gisY          			NUMBER(20,10),          /*��ͼy����*/
userId					VARCHAR2(64), 			/*�û�id*/
remark          		VARCHAR2(1000),       	/*��ע*/
creatorId       		VARCHAR2(64)  NOT NULL, /*������ID*/
creator         		VARCHAR2(64)  NOT NULL, /*������*/
createTime        		DATE      	NOT NULL, 	/*����ʱ��*/
updateTime        		DATE,           		/*����ʱ��*/
updateBy        		VARCHAR2(64),       	/*������*/
code                    VARCHAR2(200),          /*���*/
CONSTRAINT pk_Duty_Contacts_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_Contacts_userId FOREIGN KEY(userId) REFERENCES Org_User(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
ͨѶ¼��ϸ
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_ContactsItem(
id            			VARCHAR2(64)  NOT NULL, /*ϵͳid*/
fatherId        		VARCHAR2(64)  NOT NULL, /*ͨѶ¼id*/
typeId          		VARCHAR2(64)  NOT NULL, /*ͨѶ����id���ֻ����̻���*/
callNo          		VARCHAR2(64)  NOT NULL, /*ͨѶ��*/
seqc          			NUMBER(10)    NOT NULL, /*�����*/
CONSTRAINT pk_Duty_Contacts_Item_id PRIMARY KEY (id),
CONSTRAINT fk_Contacts_Item_fatherId FOREIGN KEY (fatherId) REFERENCES T_Duty_Contacts(id) ON DELETE CASCADE);


/*-------------------------------------------------------------------------------
ͨѶ¼����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Duty_Group(
id            			VARCHAR2(64)	NOT NULL, 	/*ϵͳid*/
name          			VARCHAR2(400)	NOT NULL,	/*����*/
ownerId					VARCHAR2(64), 				/*������id�����Ϊ�ձ�ʾϵͳ��*/
maintainOrgId			VARCHAR2(64), 				/*ά����λid*/
maintainOrgIds			VARCHAR2(1000),			 	/*ά����λ�����ϼ�ά����λid�ַ������Զ��ŷָ�*/
isPublic				VARCHAR2(1)		NOT NULL,	/*�Ƿ񹫿���'0'����'1'���� (����)*/
defCommuTypeId			VARCHAR2(128)	NOT NULL, 	/*Ĭ��ͨѶ��ʽ*/
code          			NUMBER(10,0)	NOT NULL, 	/*��� (����)*/
isn           			VARCHAR2(2000)  NOT NULL,	/*����*/
isDir         			VARCHAR2(1)		NOT NULL,	/*�Ƿ�Ŀ¼��'0'����'1'����*/
isLeaf          		VARCHAR2(1)		NOT NULL,   /*�Ƿ���ϸ����'0'����'1'����*/
rank          			NUMBER(2,0)		NOT NULL,   /*����*/
parentId        		VARCHAR2(64),				/*���ڵ�id*/    
creatorId       		VARCHAR2(64),				/*������ID*/
createor        		VARCHAR2(64),				/*������*/
createTime        		DATE,						/*����ʱ��*/
updateBy        		VARCHAR2(64),				/*������id*/
updateTime        		DATE,						/*����ʱ��*/
CONSTRAINT pk_Duty_Group_id PRIMARY KEY (id),
CONSTRAINT fk_Duty_Group_parentId FOREIGN KEY (parentId) REFERENCES T_Duty_Group(id));

/*-------------------------------------------------------------------------------
ͨѶ¼�����ϵ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Contacts_Group_Relation(
id            			VARCHAR2(64)	NOT NULL,	/*ϵͳid*/
groupId					VARCHAR2(64)	NOT NULL,	/*����id*/
contactsId				VARCHAR2(64)	NOT NULL,	/*ͨѶ¼id*/
code					NUMBER(10),					/*�����*/
CONSTRAINT pk_Group_Relation_id PRIMARY KEY (id),
CONSTRAINT fk_Group_Relation_groupId FOREIGN KEY (groupId) REFERENCES T_Duty_Group(id) ON DELETE CASCADE,
CONSTRAINT fk_Group_Relation_contactsId FOREIGN KEY (contactsId) REFERENCES T_Duty_Contacts(id) ON DELETE CASCADE);

/*-------------------------------------------------------------------------------
��֯ͨѶ�豸����
-------------------------------------------------------------------------------*/
CREATE TABLE T_Sys_OrganCummEquip(
id						VARCHAR2(64)	NOT NULL,	/*ϵͳid*/
name					VARCHAR2(400)	NOT NULL,	/*����*/
company					VARCHAR2(400),				/*��λ����*/ 
address					VARCHAR2(400),				/*��ַ*/
orgId					VARCHAR2(64)	NOT NULL,	/*������֯*/
typeId					VARCHAR2(128)	NOT NULL,	/*������*/
gisX					NUMBER(20,10),				/*��ͼx����*/
gisY					NUMBER(20,10),				/*��ͼy����*/
linker					VARCHAR2(50),				/*��ϵ��*/
tel						VARCHAR2(50),				/*��ϵ�绰*/
factoryTypeId			VARCHAR2(128),				/*�������*/
ip						VARCHAR2(50),				/*ip*/
port					VARCHAR2(50),				/*�˿�*/
lgnAcc					VARCHAR2(50),				/*��¼�˻�*/
pwd						VARCHAR2(50),				/*����*/
channelNo				VARCHAR2(50),				/*ͨ����*/ 
creatorId				VARCHAR2(64)	NOT NULL,	/*������ID*/
creator					VARCHAR2(64)	NOT NULL,	/*������*/
createTime				DATE,						/*����ʱ��*/
updateBy				VARCHAR2(64),				/*������id*/
updateTime				DATE,						/*����ʱ��*/
code                    VARCHAR2(200),              /*����*/
uniqueNo                VARCHAR2(200),              /*Ψһ��*/
CONSTRAINT pk_Sys_OrganCummEquip_id PRIMARY KEY (id),
CONSTRAINT fk_Sys_OrganCummEquip_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));


/*------------------------------------------------------------------------------
 �ں�ͨѶ-����ͨѶ��־
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_CalledLog(                  
id						VARCHAR2(64)	NOT NULL,	/*����*/
createTime				DATE,						/*ͨ����ʼʱ��*/
endTime					DATE,						/*ͨ������ʱ��*/
commuId					VARCHAR2(64),				/*ͨѶ��������¼ID*/
orgId					VARCHAR2(64)	NOT NULL,	/*��֯id*/
userId					VARCHAR2(64)	NOT NULL,	/*�����ˣ����У��û�ID*/
userName				VARCHAR2(400),				/*�����ˣ����У��û�*/
members					VARCHAR2(1000),				/*�����Ա������"."�ָ�*/
caller					VARCHAR2(100),				/*���к���*/
callee					VARCHAR2(1000),				/*��Ա����*/
type					VARCHAR2(30),				/*ͨѶ���� �����������*/
isRecord				VARCHAR2(1),				/*�Ƿ���¼�� 0:û�� 1:��*/
CONSTRAINT pk_Comm_CallidLog_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 �ں�ͨѶ-֪ͨ��¼����־
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_NotifiedLog(                  
id						VARCHAR2(64)	NOT NULL,	/*����*/
createTime				DATE			NOT NULL,	/*¼����ʼʱ��*/
endTime					DATE,						/*¼������ʱ��*/
commuId					VARCHAR2(64)	NOT NULL,	/*ͨѶ��������¼ID��title��*/
orgId					VARCHAR2(64)	NOT NULL,	/*��֯id*/
userId					VARCHAR2(64)	NOT NULL,	/*�����ˣ����У��û�ID*/
userName				VARCHAR2(400),				/*�����ˣ����У��û�*/
caller					VARCHAR2(100),				/*���к���*/
len						NUMBER(10),                 /*¼������*/
CONSTRAINT pk_Comm_NotifiedLog_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 �ں�ͨѶ-�����㲥������־
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_NotifiedItemLog(  
id						VARCHAR2(64)	NOT NULL,	/*ϵͳID*/
fatherId				VARCHAR2(64)	NOT NULL,	/*�����㲥¼��ID*/
orgId					VARCHAR2(64)	NOT NULL,	/*��֯ID*/
userId					VARCHAR2(64)	NOT NULL,	/*�����ˣ����У��û�ID*/
userName				VARCHAR2(400),				/*�����ˣ����У��û�*/
members					VARCHAR2(1000),				/*�����Ա������"."�ָ�*/
caller					VARCHAR2(100),				/*���к���*/
callee					VARCHAR2(1000),				/*��Ա���룬��"."�ָ�*/
createTime				DATE,						/*����֪ͨʱ��*/
CONSTRAINT pk_Comm_NotifiedItem_id PRIMARY KEY (id),
CONSTRAINT fk_Comm_NotifiedItem_fatherId FOREIGN KEY (fatherId) REFERENCES T_Comm_NotifiedLog(id) ON DELETE CASCADE);


/*------------------------------------------------------------------------------
 �ں�ͨѶ-���ŷ�����־
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_MsgLog(                  
id						VARCHAR2(64)	NOT NULL,	/*����*/
createTime				DATE,						/*ͨ����ʼʱ��*/
orgId					VARCHAR2(64)	NOT NULL,	/*��֯id*/
userId					VARCHAR2(64)	NOT NULL,	/*�����ˣ����У��û�ID*/
userName				VARCHAR2(400),				/*�����ˣ����У��û�*/
members					VARCHAR2(1000),				/*�����Ա������","�ָ�*/
caller					VARCHAR2(100),				/*���ͺ���*/
callee					VARCHAR2(1000),				/*���պ���*/
content					VARCHAR2(500),				/*��������*/
timeschedule			DATE,						/*��ʱ����*/
timesend				DATE,						/*����ʱ��*/
notifytype				VARCHAR2(1),				/*֪ͨ����,0:��ͨ;1:֪ͨ�޻�ִ;2:֪ͨ�л�ִ��Ĭ��0��*/
status					VARCHAR2(1),				/*״̬��0���ȴ����ͣ�1�������У�2�����ͳɹ���3������ʧ�ܣ�4�����ͳ�ʱ��*/
batchglag				VARCHAR2(1),				/*������־���Ƿ����� 0:�� 1:��*/
batchinfo				VARCHAR2(1000),				/*��������ʱ�����������״̬*/
receiptdesc				VARCHAR2(1000),				/*��������*/
CONSTRAINT pk_Comm_MsgLog_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 �ں�ͨѶ-���ŷ���
-------------------------------------------------------------------------------*/
CREATE TABLE T_Comm_MsgGroup(                  
id						VARCHAR2(64)	NOT NULL,	/*����*/
createTime				DATE,						/*ͨ����ʼʱ��*/
orgId					VARCHAR2(64)	NOT NULL,	/*��֯id*/
userId					VARCHAR2(64)	NOT NULL,	/*�����ˣ����У��û�ID*/
userName				VARCHAR2(400),				/*�����ˣ����У��û�*/
caller					VARCHAR2(100),				/*���к���*/
callee					VARCHAR2(1000),				/*��Ա���룬��"."�ָ�*/
members					VARCHAR2(1000),				/*�����Ա������","�ָ�*/
CONSTRAINT pk_Comm_MsgGroup_id PRIMARY KEY (id));


/*------------------------------------------------------------------------------
 �޸Ķ���ģ��������������û�id�������ü���ɾ��
-------------------------------------------------------------------------------*/
ALTER TABLE T_Duty_Smsnotice_Group ADD(ownerId VARCHAR(64));
ALTER TABLE T_Duty_Smsnotice_Group ADD CONSTRAINT fk_Duty_SGroup_ownerId FOREIGN KEY(ownerId) REFERENCES Org_User(id) ON DELETE CASCADE;


/*-------------------------------------------------------------------------------
�¼�������
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventEval(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
name  				    VARCHAR2(400)	NOT NULL,  /*����*/
eventId  				VARCHAR2(64)	NOT NULL,  /*�¼�Id*/
eventName  				VARCHAR2(200)	NOT NULL,  /*�¼�����*/
eventType				VARCHAR2(32)	NOT NULL,  /*�¼����*/
eventArea               VARCHAR2(32)	NOT NULL,  /*��������*/
happenDate  			DATE,			           /*�·�ʱ��*/
happenAddress   		VARCHAR2(100),             /*�·���ϸ��ַ*/
scope        		    VARCHAR2(200),             /*��Χ*/
population     		    VARCHAR2(200),             /*�����˿�*/
injAndDeaths            VARCHAR2(200),             /*��Ա����*/
propertyLoss            VARCHAR2(200),             /*�Ʋ���ʧ*/
eventReason        		VARCHAR2(2000),            /*�¹�ԭ��*/
effect           		VARCHAR2(2000),            /*����Ч��*/
summary        		    VARCHAR2(2000),            /*����˵��*/
memo        		    VARCHAR2(2000),            /*��ע*/
atchIds        		    VARCHAR2(1000),            /*����Ids*/
statusId                VARCHAR2(128)   NOT NULL,  /*״̬Id����塢����*/
evalTime     			DATE            NOT NULL,  /*����ʱ��*/
createTime  			DATE,			           /*����ʱ��*/
updateTime  			DATE,			           /*����ʱ��*/
creatorId   			VARCHAR2(64),              /*������(������)Id*/
createor     			VARCHAR2(400),		       /*������(������)*/
CONSTRAINT pk_Eval_EventEval_id PRIMARY KEY (id)),
CONSTRAINT fk_Eval_EventEval_eventId FOREIGN KEY (eventId) REFERENCES T_Event_Info(id);

/*-------------------------------------------------------------------------------
�¼�����ָ���
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventEvalInid(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
fatherId  				VARCHAR2(64)	NOT NULL,  /*�¼�����Id*/
indiCode       			VARCHAR2(64),              /*ָ�����*/
indiName       			VARCHAR2(50),              /*ָ������*/
indiValue      			VARCHAR2(100),             /*���ָ��ֵ*/
seqc                    NUMBER(10,0)    NOT NULL,  /*���*/
CONSTRAINT pk_Eval_EventEvalI_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_EventEvalI_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_EventEval(id) ON DELETE CASCADE);

/*-------------------------------------------------------------------------------
����ָ���
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EvalInid(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
typeId                  VARCHAR2(128)   NOT NULL,  /*��������Id����ǰ�����С��º�*/
eventTypeId				VARCHAR2(64)	NOT NULL,  /*�¼����*/
code       			    NUMBER(10,0)    NOT NULL,  /*ָ����*/
level1       	        VARCHAR2(400)   NOT NULL,  /*һ��ָ��*/
level2       	        VARCHAR2(400)   NOT NULL,  /*����ָ��*/
level3       	        VARCHAR2(400)   NOT NULL,  /*����ָ��*/
weightValue      		NUMBER(10,2)    NOT NULL,  /*��ֵȨ��*/
formula                 VARCHAR2(400),             /*���㹫ʽ*/
summary        		    VARCHAR2(2000),            /*ָ��˵��*/
CONSTRAINT pk_Eval_EvalInid_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
������Ŀ���ñ�
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProjectCfg(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
eventTypeId				VARCHAR2(64)	NOT NULL,  /*�¼����*/
orgId 					VARCHAR2(64)	NOT NULL,  /*������֯Id*/
code       			    NUMBER(10,0)    NOT NULL,  /*���*/
name         	        VARCHAR2(400)   NOT NULL,  /*����*/
formula                 VARCHAR2(400)   NOT NULL,  /*���۹�ʽ*/
createTime  			DATE,			           /*����ʱ��*/
updateTime  			DATE,			           /*����ʱ��*/
creatorId   			VARCHAR2(64),              /*������ID*/
createor     			VARCHAR2(64),		       /*������*/
CONSTRAINT pk_Eval_PrjCfg_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_PrjCfg_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id));

/*-------------------------------------------------------------------------------
������Ŀ������ϸ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProjectCfgItem(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
fatherId  				VARCHAR2(64)	NOT NULL,  /*������Ŀ����Id*/
inidId       			VARCHAR2(64)	NOT NULL,  /*ָ��Id*/
weightValue      		NUMBER(10,2)    NOT NULL,  /*��ֵȨ��*/
formula                 VARCHAR2(400),             /*���㹫ʽ*/
seqc       			    NUMBER(10,0)    NOT NULL,  /*���*/
CONSTRAINT pk_Eval_PrjCfgI_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_PrjCfgI_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_ProjectCfg(id) ON DELETE CASCADE,
CONSTRAINT fk_Eval_PrjCfgI_inidId FOREIGN KEY (inidId) REFERENCES T_Eval_EvalInid(id));

/*-------------------------------------------------------------------------------
Ӧ������������
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventGrade(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
orgId 					VARCHAR2(64)	NOT NULL,  /*������֯Id*/
typeId  				VARCHAR2(64)	NOT NULL,  /*��¼����Id*/
eventId 				VARCHAR2(64)	NOT NULL,  /*�¼�Id*/
prjCfgId 				VARCHAR2(64)	NOT NULL,  /*������Ŀ����Id*/
name         	        VARCHAR2(400)   NOT NULL,  /*��������*/
evalTime     			DATE            NOT NULL,  /*����ʱ��*/
evalValue       	    NUMBER(20,5)    NOT NULL,  /*����ֵ*/
evalResult         	    VARCHAR2(2000),            /*��������*/
evalSummary        	    VARCHAR2(2000),            /*����˵��*/
memo            	    VARCHAR2(2000),            /*��ע*/
formula                 VARCHAR2(400)   NOT NULL,  /*���۹�ʽ*/
createTime  			DATE,			           /*����ʱ��*/
updateTime  			DATE,			           /*����ʱ��*/
creatorId   			VARCHAR2(64),              /*������(������)ID*/
createor     			VARCHAR2(64),		       /*������(������)*/
CONSTRAINT pk_Eval_EvtGde_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_EvtGde_orgId FOREIGN KEY (orgId) REFERENCES Org_Organization(id),
CONSTRAINT fk_Eval_EvtGde_eventId FOREIGN KEY (eventId) REFERENCES T_Event_Info(id),
CONSTRAINT fk_Eval_EvtGde_prjCfgId FOREIGN KEY (prjCfgId) REFERENCES T_Eval_ProjectCfg(id));

/*-------------------------------------------------------------------------------
Ӧ������������ϸ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_EventGradeItem(
id 						VARCHAR2(64)	NOT NULL,  /*����*/
fatherId  				VARCHAR2(64)	NOT NULL,  /*Ӧ����������Id*/
inidId       			VARCHAR2(64)	NOT NULL,  /*ָ��Id*/
weightValue      		NUMBER(10,2)    NOT NULL,  /*��ֵȨ��*/
formula                 VARCHAR2(400),             /*���㹫ʽ*/
evalValue       	    NUMBER(20,5)    NOT NULL,  /*����ֵ*/
seqc       			    NUMBER(10,0)    NOT NULL,  /*���*/
CONSTRAINT pk_Eval_EvtGdeI_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_EvtGdeI_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_EventGrade(id) ON DELETE CASCADE,
CONSTRAINT fk_Eval_EvtGdeI_inidId FOREIGN KEY (inidId) REFERENCES T_Eval_EvalInid(id));

/*-------------------------------------------------------------------------------
�������ֱ�
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProcessEmersion(
id                      VARCHAR2(64)     NOT NULL,  /*����*/
eventId                 VARCHAR2(64)     NOT NULL,  /*�¼�Id*/
eventType               VARCHAR2(32)     NOT NULL,  /*�¼����*/
eventName               VARCHAR2(200)    NOT NULL,  /*�¼�����*/
happenDate              DATE,                       /*�·�ʱ��*/
happenAddress           VARCHAR2(100),              /*�·���ϸ��ַ*/
eventLever              VARCHAR2(32),               /*�¼�����*/
CONSTRAINT pk_Eval_ProcessEmersion_id PRIMARY KEY (id));

/*-------------------------------------------------------------------------------
����������ϸ��
-------------------------------------------------------------------------------*/
CREATE TABLE T_Eval_ProcessEmersionItem(
id                      VARCHAR2(64) 	NOT NULL,  /*����*/
fatherId                VARCHAR2(64)    NOT NULL,  /*��������id*/
bizId                   VARCHAR2(64),              /*ʵ��id*/
bizTable                VARCHAR2(100),             /*����*/
processType             VARCHAR2(128)   NOT NULL,  /*��������*/
processContent          VARCHAR2(1000),            /*������ϸ��Ϣ*/
createTime              DATE,                      /*����ʱ��*/
creatorId   			VARCHAR2(64),              /*������Id*/
createor     			VARCHAR2(400),		       /*������*/
CONSTRAINT pk_Eval_ProcessEmersionItem_id PRIMARY KEY (id),
CONSTRAINT fk_Eval_ProcessEmersionItem_fatherId FOREIGN KEY (fatherId) REFERENCES T_Eval_ProcessEmersion(id) ON DELETE CASCADE);



/*-------------------------------------------------------------------------------
��Ƶ���
-------------------------------------------------------------------------------*/
CREATE TABLE T_Base_Monitor(
id                     VARCHAR2(64)   NOT NULL, /*����*/
typeId                 VARCHAR2(64)   NOT NULL, /*������*/
name                   VARCHAR2(400)  NOT NULL, /*����*/
company                VARCHAR2(400),           /*��λ����*/ 
gisX                   NUMBER(20,10),           /*��ͼx����*/
gisY                   NUMBER(20,10),           /*��ͼy����*/
linker                 VARCHAR2(50),            /*��ϵ��*/
tel                    VARCHAR2(50),            /*��ϵ�绰*/
code	               VARCHAR2(50),            /*���*/ 
factoryTypeId          VARCHAR2(128),           /*�������*/
ip                     VARCHAR2(50),            /*ip*/
port				   VARCHAR2(50),            /*�˿�*/
lgnAcc                 VARCHAR2(50),            /*��¼�˻�*/
pwd                    VARCHAR2(50),            /*����*/
channelNo              VARCHAR2(50),            /*ͨ����*/ 
disabled               VARCHAR2(1),             /*��ͼչʾ״̬*/
addr                   VARCHAR2(400),           /*��ַ*/
orgId              	   VARCHAR2(64),            /*������֯*/
CONSTRAINT pk_Base_Monitor_id PRIMARY KEY (id));>>>>>>> .r32432
