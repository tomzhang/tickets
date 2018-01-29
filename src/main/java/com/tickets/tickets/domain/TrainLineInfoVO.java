package com.tickets.tickets.domain;

public class TrainLineInfoVO {
	
	private String secretStr;           //订单请求时使用 秘钥字符串
	private String train_no;         //获取价格，获取车次车站信息使用
	private String status;              //操作状态
	private String flag;               //序号
	private String station_train_code; //车次
	private String from_station_y_code; //列车始发站code
	private String from_station_y_name; //列车始发站name
	private String to_station_y_code;   //列车终点站code
	private String to_station_y_name;   //列车终点站name
	private String from_station_code;  //出发站点 
	private String from_station_name;  //出发站点 
	private String to_station_code;    //到达站点
	private String to_station_name;    //到达站点
	private String isBuy;			   //是否可购买
	
	private String leftTicket;   //12位
	
	private String trainLocation; //trainLocation 15位
	
	private String start_time;		  // 发车时间
	private String arrive_time;		  //到达时间
	private String duration; 		 //历时
	
	private String swz_num;			 //商务座/特等座
	private String zy_num; 			//一等座
	private String ze_num;			//二等座
	private String gjrw_num;			//高级软卧
	private String rw_num; 			//软卧
	private String yw_num; 			//硬卧
	private String rz_num;			//软座
	private String dw_num;			//动卧
	private String yz_num;			//硬座
	private String wz_num;			//无座
	private String qt_num;			//其它
	
	
	//车次价格
	private String swz_num_price;			 //商务座/特等座
	private String zy_num_price; 			//一等座
	private String ze_num_price;			//二等座
	private String rw_num_price; 			//软卧
	private String yw_num_price; 			//硬卧
	private String yz_num_price;			//硬座
	private String wz_num_price;			//无座
	private String rz_num_price;			//软座
	private String qt_num_price;			//其它
	private String gjrw_num_price;			//高级软卧
	private String dw_num_price;			//动卧
	
	
	
	
	public String getSecretStr() {
		return secretStr;
	}
	public void setSecretStr(String secretStr) {
		this.secretStr = secretStr;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getStation_train_code() {
		return station_train_code;
	}
	public void setStation_train_code(String station_train_code) {
		this.station_train_code = station_train_code;
	}
	public String getFrom_station_name() {
		return from_station_name;
	}
	public void setFrom_station_name(String from_station_name) {
		this.from_station_name = from_station_name;
	}
	public String getTo_station_name() {
		return to_station_name;
	}
	public void setTo_station_name(String to_station_name) {
		this.to_station_name = to_station_name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getArrive_time() {
		return arrive_time;
	}
	public void setArrive_time(String arrive_time) {
		this.arrive_time = arrive_time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSwz_num() {
		return swz_num;
	}
	public void setSwz_num(String swz_num) {
		this.swz_num = swz_num;
	}
	public String getZy_num() {
		return zy_num;
	}
	public void setZy_num(String zy_num) {
		this.zy_num = zy_num;
	}
	public String getZe_num() {
		return ze_num;
	}
	public void setZe_num(String ze_num) {
		this.ze_num = ze_num;
	}
	public String getRw_num() {
		return rw_num;
	}
	public void setRw_num(String rw_num) {
		this.rw_num = rw_num;
	}
	public String getYw_num() {
		return yw_num;
	}
	public void setYw_num(String yw_num) {
		this.yw_num = yw_num;
	}
	public String getYz_num() {
		return yz_num;
	}
	public void setYz_num(String yz_num) {
		this.yz_num = yz_num;
	}
	public String getWz_num() {
		return wz_num;
	}
	public void setWz_num(String wz_num) {
		this.wz_num = wz_num;
	}
	public String getSwz_num_price() {
		return swz_num_price;
	}
	public void setSwz_num_price(String swz_num_price) {
		this.swz_num_price = swz_num_price;
	}
	public String getZy_num_price() {
		return zy_num_price;
	}
	public void setZy_num_price(String zy_num_price) {
		this.zy_num_price = zy_num_price;
	}
	public String getZe_num_price() {
		return ze_num_price;
	}
	public void setZe_num_price(String ze_num_price) {
		this.ze_num_price = ze_num_price;
	}
	public String getRw_num_price() {
		return rw_num_price;
	}
	public void setRw_num_price(String rw_num_price) {
		this.rw_num_price = rw_num_price;
	}
	public String getYw_num_price() {
		return yw_num_price;
	}
	public void setYw_num_price(String yw_num_price) {
		this.yw_num_price = yw_num_price;
	}
	public String getYz_num_price() {
		return yz_num_price;
	}
	public void setYz_num_price(String yz_num_price) {
		this.yz_num_price = yz_num_price;
	}
	public String getWz_num_price() {
		return wz_num_price;
	}
	public void setWz_num_price(String wz_num_price) {
		this.wz_num_price = wz_num_price;
	}
	public String getFrom_station_y_code() {
		return from_station_y_code;
	}
	public void setFrom_station_y_code(String from_station_y_code) {
		this.from_station_y_code = from_station_y_code;
	}
	public String getFrom_station_y_name() {
		return from_station_y_name;
	}
	public void setFrom_station_y_name(String from_station_y_name) {
		this.from_station_y_name = from_station_y_name;
	}
	public String getTo_station_y_code() {
		return to_station_y_code;
	}
	public void setTo_station_y_code(String to_station_y_code) {
		this.to_station_y_code = to_station_y_code;
	}
	public String getTo_station_y_name() {
		return to_station_y_name;
	}
	public void setTo_station_y_name(String to_station_y_name) {
		this.to_station_y_name = to_station_y_name;
	}
	public String getFrom_station_code() {
		return from_station_code;
	}
	public void setFrom_station_code(String from_station_code) {
		this.from_station_code = from_station_code;
	}
	public String getTo_station_code() {
		return to_station_code;
	}
	public String getIsBuy() {
		return isBuy;
	}
	public void setIsBuy(String isBuy) {
		this.isBuy = isBuy;
	}
	public void setTo_station_code(String to_station_code) {
		this.to_station_code = to_station_code;
	}
	public String getRz_num() {
		return rz_num;
	}
	public void setRz_num(String rz_num) {
		this.rz_num = rz_num;
	}
	public String getRz_num_price() {
		return rz_num_price;
	}
	public void setRz_num_price(String rz_num_price) {
		this.rz_num_price = rz_num_price;
	}
	public String getQt_num() {
		return qt_num;
	}
	public void setQt_num(String qt_num) {
		this.qt_num = qt_num;
	}
	public String getQt_num_price() {
		return qt_num_price;
	}
	public void setQt_num_price(String qt_num_price) {
		this.qt_num_price = qt_num_price;
	}
	public String getGjrw_num() {
		return gjrw_num;
	}
	public void setGjrw_num(String gjrw_num) {
		this.gjrw_num = gjrw_num;
	}
	public String getDw_num() {
		return dw_num;
	}
	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}
	public String getGjrw_num_price() {
		return gjrw_num_price;
	}
	public void setGjrw_num_price(String gjrw_num_price) {
		this.gjrw_num_price = gjrw_num_price;
	}
	public String getDw_num_price() {
		return dw_num_price;
	}
	public void setDw_num_price(String dw_num_price) {
		this.dw_num_price = dw_num_price;
	}
	public String getLeftTicket() {
		return leftTicket;
	}
	public void setLeftTicket(String leftTicket) {
		this.leftTicket = leftTicket;
	}
	public String getTrainLocation() {
		return trainLocation;
	}
	public void setTrainLocation(String trainLocation) {
		this.trainLocation = trainLocation;
	}
	
	
	
	
	


	
	
}
