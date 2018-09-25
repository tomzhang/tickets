package com.tickets.tickets.tests;

import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.service.impl.TicketsServiceImpl2;

import java.io.File;
import java.io.FileInputStream;

public class TicketTest4 {
    public static void main(String[] args)  {

        TicketsServiceImpl2 i = new TicketsServiceImpl2();

        String resp_initDc = "";
        File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            resp_initDc=  new String(filecontent, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrainInfoVO trainInfoVO = i.getTrainInfoVO(resp_initDc);
        i.printTrainInfoVO(trainInfoVO);
    }
}
