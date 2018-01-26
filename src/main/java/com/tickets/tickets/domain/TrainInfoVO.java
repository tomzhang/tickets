package com.tickets.tickets.domain;

import java.util.List;

public class TrainInfoVO {

	private String flag; //标志	
	private List<TrainStationInfoVO>  trainStationInfos; //所经车站信息
	private List<TrainLineInfoVO> trainLineInfos; 		//车次信息	
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<TrainStationInfoVO> getTrainStationInfos() {
		return trainStationInfos;
	}
	public void setTrainStationInfos(List<TrainStationInfoVO> trainStationInfos) {
		this.trainStationInfos = trainStationInfos;
	}
	public List<TrainLineInfoVO> getTrainLineInfos() {
		return trainLineInfos;
	}
	public void setTrainLineInfos(List<TrainLineInfoVO> trainLineInfos) {
		this.trainLineInfos = trainLineInfos;
	}
}
