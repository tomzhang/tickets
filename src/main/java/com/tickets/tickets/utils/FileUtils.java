package com.tickets.tickets.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

   private static  String filePath = "D:\\work2\\img\\cookies.txt";

    public static boolean writeXmlToFile(String xml){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(xml);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static String readXmlToFile(){
        FileWriter fileWriter = null;
        StringBuffer result_xml = new StringBuffer();
        try {
            File file = new File(filePath);
            FileReader fd = new FileReader(file);
            char[] chs = new char[(int)file.length() ];
            fd.read(chs);
            result_xml.append(chs);
            fd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_xml.toString();
    }

    public static void main(String[] args) {
       String xml = readXmlToFile();
        System.out.println(xml);
    }


}
