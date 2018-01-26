package com.tickets.tickets.tests;

import java.util.Scanner;

public class TicketTests {

	public static void main(String[] args) {
		
		while(true) {
			System.out.println("请输入字符串:");
			Scanner scan = new Scanner(System.in);
			String str = scan.nextLine();
			
			System.out.println("请输入要匹配的字符串:");		
			Scanner scan2 = new Scanner(System.in);
			String read2 = scan2.nextLine();
			
			
			String strs[] = str.split("\\|");
			for(int i=0;i<strs.length;i++) {
				if(strs[i].equals(read2)) {
					System.out.println("匹配"+read2+": "+i);
				}
			}
		}
		
	}

}
