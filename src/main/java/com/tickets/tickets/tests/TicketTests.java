package com.tickets.tickets.tests;

import net.dongliu.requests.Requests;
import net.dongliu.requests.body.Part;

import java.io.File;


public class TicketTests {

	public static void main(String[] args) throws Exception {
		String url ="http://123.57.138.40:9443/12306/code";
		String response = Requests.post(url).multiPartBody(
				Part.file("file",new File("D:\\work2\\img\\captcha-image2.jpg")),
				Part.param("user","111"),
				Part.param("key","12345")
		).send().readToText();
		System.out.println(response);
	}



}
