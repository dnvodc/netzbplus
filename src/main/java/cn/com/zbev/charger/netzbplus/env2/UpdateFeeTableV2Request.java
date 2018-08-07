package cn.com.zbev.charger.netzbplus.env2;

import java.util.List;

/**
 * 更新收费费率表 
 * @author wanwy  2018-08-02
 *
 */
public class UpdateFeeTableV2Request extends ENDataV2 {

	public static final int PUSH_ELEC_FEE_MODEL = 0x07;
	
	private RateTable rateTable;

	
	
	public UpdateFeeTableV2Request (int msgId,int param,byte[] data) {
		this.ctrl1 = SERVER_REQ;
		this.ctrl2 = PUSH_ELEC_FEE_MODEL;
		this.msgId = msgId;
		this.param = param;
		this.data = data;
	}
	
	public UpdateFeeTableV2Request(int msgId,int param,RateTable table) {
		this.ctrl1 = SERVER_REQ;
		this.ctrl2 = PUSH_ELEC_FEE_MODEL;
		this.msgId = msgId;
		this.param = param;
		this.data = table.ToByteArray();
		this.dataLength = data.length;
	}
	
	public RateTable getRateTable() {
		return rateTable;
	}

	/*public void setRateTable(RateTable rateTable) {
		this.rateTable = rateTable;
	}*/

	public class RateTable{
		
		private String deviceSn;
		
		private List<Integer> elecRate;
		
		private List<Integer> timeRage;
		//电损率
		private int wastageRate;

		private int serviceFee;
		
		//时段值，不理解需要确认
		private int timeRageValue;
		
		public byte[] ToByteArray() {
			
			return null;
		}

		public String getDeviceSn() {
			return deviceSn;
		}

		public void setDeviceSn(String deviceSn) {
			this.deviceSn = deviceSn;
		}

		public List<Integer> getElecRate() {
			return elecRate;
		}

		public void setElecRate(List<Integer> elecRate) {
			this.elecRate = elecRate;
		}

		public List<Integer> getTimeRage() {
			return timeRage;
		}

		public void setTimeRage(List<Integer> timeRage) {
			this.timeRage = timeRage;
		}

		public int getWastageRate() {
			return wastageRate;
		}

		public void setWastageRate(int wastageRate) {
			this.wastageRate = wastageRate;
		}

		public int getServiceFee() {
			return serviceFee;
		}

		public void setServiceFee(int serviceFee) {
			this.serviceFee = serviceFee;
		}

		public int getTimeRageValue() {
			return timeRageValue;
		}

		public void setTimeRageValue(int timeRageValue) {
			this.timeRageValue = timeRageValue;
		}
		
		
		
	}
	
	
	
}
