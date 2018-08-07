package cn.com.zbev.charger.netzbplus.tcp;

import java.util.EventListener;

import cn.com.zbev.charger.netzbplus.CloseEvent;
import cn.com.zbev.charger.netzbplus.ConnectionStateEvent;
import cn.com.zbev.charger.netzbplus.FrameEvent;

public interface ChargerListener extends EventListener{

	void frameReceived(FrameEvent e);
	
	void chargerOnline(ConnectionStateEvent e);
	
	void chargerOffline(ConnectionStateEvent e);
	
	void loginRequst(Object data);
	
	void deviceInfoSyncRequest(Object data);
	
	void chargerWorkDataUploadRequest(Object data);
	
	void eventUploadRequest(Object data);
	
	void timeSyncRequest(Object data);
	
	void iCCIDSignleQualityUploadRequest(Object data);
	
	void chargerStateUploadRequest(Object data);

	//用户未结算金额查询请求
	void userNoCloseAmountRequest(Object data);
	
	void userReadCardOperateRequest(Object data);
	
	void userCardInfoRequest(Object data);
	
	void elecFeeTableRequest(Object data);
	
	//用户刷卡充电请求
	void userCardChargeRequest(Object data);
	
	//查询用户卡消费清单
	void queryUserCardConsumeListRequest(Object data);
	
	void parameterConfigRequest(Object data);
	
	void chargeStateUploadRequest(Object data);
	
	void summaryMessageRequest(Object data);
	
	void errorMessageRequest(Object data);
	
	void chargeRecordUploadRequest(Object data);
	
	//网关升级充电桩请求
	void gatewayUpgradeChargerRequest(Object data);
	
	void updateChargerParameterRequest(Object data);
	
}
