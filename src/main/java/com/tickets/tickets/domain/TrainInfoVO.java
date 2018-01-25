package com.tickets.tickets.domain;

import java.util.List;

public class TrainInfoVO {

	private String flag; //标志	
	private List<TrainStationInfoVO>  trainStationInfos; //所经车站信息
	private List<TrainLineInfoVO> trainLineInfos; 		//车次信息	
}
