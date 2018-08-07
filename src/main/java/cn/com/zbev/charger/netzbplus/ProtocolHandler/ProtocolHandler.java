package cn.com.zbev.charger.netzbplus.ProtocolHandler;

/**
 * 
 * @author wanwy
 * 参数为什么用 data,其实也可以用 cmi，因为对象不用向上转换，只能向下转换
 * 不同的实现自已转换适合的对象再处理
 * 这个方法不好的地方在于 对象大量的装箱、拆箱
 */
public interface ProtocolHandler {
	
	void handleLoginRequst(Object data);
	
	void handleDeviceInfoSyncRequest(Object data);
	
	void handleChargerWorkDataUploadRequest(Object data);
	
	void handleEventUploadRequest(Object data);
	
	void handleTimeSyncRequest(Object data);
	
	void handleICCIDSignleQualityUploadRequest(Object data);
	
	void handleChargerStateUploadRequest(Object data);

	//用户未结算金额查询请求
	void handleUserNoCloseAmountRequest(Object data);
	
	void handleUserReadCardOperateRequest(Object data);
	
	void handleUserCardInfoRequest(Object data);
	
	void handleElecFeeTableRequest(Object data);
	
	//用户刷卡充电请求
	void handleUserCardChargeRequest(Object data);
	
	//查询用户卡消费清单
	void handleQueryUserCardConsumeListRequest(Object data);
	
	void handleParameterConfigRequest(Object data);
	
	void handleChargeStateUploadRequest(Object data);
	
	void handleSummaryMessageRequest(Object data);
	
	void handleErrorMessageRequest(Object data);
	
	void handleChargeRecordUploadRequest(Object data);
	
	//网关升级充电桩请求
	void handleGatewayUpgradeChargerRequest(Object data);
	
	void handleUpdateChargerParameterRequest(Object data);
}
