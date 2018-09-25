package com.tickets.tickets.domain;

import lombok.Data;

@Data
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
	

}
