package com.tickets.tickets.service.impl;

import com.google.gson.Gson;
import com.tickets.tickets.content.Headers;
import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.domain.TrainLineInfoVO;
import com.tickets.tickets.domain.TrainStationInfoVO;
import com.tickets.tickets.service.TicketService;
import com.tickets.tickets.utils.SessionUtils;
import com.tickets.tickets.utils.StationDataUtils;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;

@Service
public class TicketSerivceImpl implements TicketService {
    static Gson gson = new Gson();

    @Override
    public TrainInfoVO query(String fromStation,String fromStation_code,
                             String toStation,String toStation_code,
                             String train_date) {
        String queryA_url="https://kyfw.12306.cn/otn/leftTicket/queryA?leftTicketDTO.train_date={0}&leftTicketDTO.from_station={1}&leftTicketDTO.to_station={2}&purpose_codes=ADULT";
        queryA_url = queryA_url.replace("{0}",train_date);
        queryA_url = queryA_url.replace("{1}",fromStation);
        queryA_url = queryA_url.replace("{2}",toStation);

        Map<String, Object> cookie_queryA = new HashMap<>();
        try {
            cookie_queryA.put("_jc_save_fromStation", URLEncoder.encode(fromStation+","+fromStation_code,"UTF-8"));
            cookie_queryA.put("_jc_save_toStation", URLEncoder.encode(toStation+","+toStation_code,"UTF-8"));
            cookie_queryA.put("_jc_save_fromDate", train_date);
            cookie_queryA.put("_jc_save_toDate", "2018-09-28");
            cookie_queryA.put("_jc_save_wfdc_flag", "dc");
        }catch (Exception e){
            e.printStackTrace();
        }
        String response_queryA = SessionUtils.session().get(queryA_url).verify(false).headers(Headers.queryAHeader()).cookies(cookie_queryA).timeout(30*1000).send().readToText();
        System.out.println(response_queryA);
        TrainInfoVO trainInfoVO = getTrainInfoVO(response_queryA);
        return null;
    }

    /**
     * 将车票返回信息实例化
     * @param json
     * @return
     */
    public static TrainInfoVO getTrainInfoVO(String json) {
        Map mapRoot = gson.fromJson( json, HashMap.class);
        Map<String,Object> mapdata =  (Map) mapRoot.get("data");

        String flag= (String) mapdata.get("flag");

        //获取mp中的值
        Map<String,String> map_jsonMap = (Map) mapdata.get("map");

        List<TrainStationInfoVO> trainStationInfos = new ArrayList<TrainStationInfoVO>();
        Set<String> set = map_jsonMap.keySet(); //取出所有的key值
        for (String key:set) {
            TrainStationInfoVO trainStationInfo = new TrainStationInfoVO();
            trainStationInfo.setCode(key);
            trainStationInfo.setName(map_jsonMap.get(key));
            trainStationInfos.add(trainStationInfo);
        }

        //获取result中的值
        List<String> result_trains =  (ArrayList) mapdata.get("result");

        List<TrainLineInfoVO> trainLineInfos = new ArrayList();

        for(String trainInfo:result_trains) {
            String[] sp = trainInfo.split("\\|");
            TrainLineInfoVO trainLineInfo = new TrainLineInfoVO();

            trainLineInfo.setSecretStr(sp[0]); //订单请求时使用
            trainLineInfo.setStatus(sp[1]); //状态
            trainLineInfo.setTrain_no(sp[2]); //获取价格，获取车次车站信息使用
            trainLineInfo.setStation_train_code(sp[3]); //车次
            trainLineInfo.setFrom_station_y_code(sp[4]); //列车始发站code
            trainLineInfo.setFrom_station_y_name(StationDataUtils.getstation_name(sp[4]));
            trainLineInfo.setTo_station_y_code(sp[5]); //列车终点站code
            trainLineInfo.setTo_station_y_name(StationDataUtils.getstation_name(sp[5]));
            trainLineInfo.setFrom_station_code(sp[6]);//出发站点
            trainLineInfo.setFrom_station_name(StationDataUtils.getstation_name(sp[6]));
            trainLineInfo.setTo_station_code(sp[7]); //到站点
            trainLineInfo.setTo_station_name(StationDataUtils.getstation_name(sp[7]));
            trainLineInfo.setStart_time(sp[8]); //出发时间点
            trainLineInfo.setArrive_time(sp[9]);//到达时间
            trainLineInfo.setDuration(sp[10]); //历时
            trainLineInfo.setIsBuy(sp[11]); //是否可预定
            trainLineInfo.setLeftTicket(sp[12]); // leftTicket
            trainLineInfo.setTrainLocation(sp[15]); //15位
            //座位信息
            trainLineInfo.setSwz_num(sp[32]);  //商务座/特等座
            trainLineInfo.setZy_num(sp[31]); //一等座
            trainLineInfo.setZe_num(sp[30]);//二等座
            trainLineInfo.setGjrw_num(sp[21]);//高级软卧---
            trainLineInfo.setRw_num(sp[23]); //软卧
            trainLineInfo.setDw_num(sp[33]); //动卧--
            trainLineInfo.setYw_num(sp[28]);//硬卧
            trainLineInfo.setRz_num(sp[24]);//软座
            trainLineInfo.setYz_num(sp[29]); //硬座
            trainLineInfo.setWz_num(sp[26]);//无座
            trainLineInfo.setQt_num(sp[22]);//其它----


            trainLineInfos.add(trainLineInfo);
        }

        TrainInfoVO trainInfoVO = new TrainInfoVO();
        trainInfoVO.setFlag(flag);
        trainInfoVO.setTrainLineInfos(trainLineInfos);
        trainInfoVO.setTrainStationInfos(trainStationInfos);

        return trainInfoVO;
    }
}
