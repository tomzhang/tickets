package com.tickets.tickets.tests;

public class TicketsfenxiTest {

	public static void main(String[] args) {
		//qita();
		wuzuo();
	}
	
	
	/**
	 *
	      第几次出现：26  --其它
	 */
	public static void qita() {
		String str="hZEvF5eek9eN6p%2BFa2c6qiLTetTyM6cBhcPzIWqqQBKadK3x5OgfgMVq3lp8OaIHg3W%2F%2F4%2Bqp5Ng%0AYmQDy29N6%2FFJD5qD%2BFZxpJgyfhC3PyHvOmvcjhg5wjeRAlIAWbfRib7VrVyYPxwgT9Z4w%2FKCSF6X%0AzajuctmUhKs%2FIn8kIpe7%2Bi%2Bzb5aPdGDQV8QwZYTJAOPZgpiHCBjIjTSjFK5sPClOylwEs8uH1xWf%0AvnMH5%2BRJpokPan06L2kyONqPNWPh59fKQwhk9JE%3D|预订|240000T21508|T215|BJP|UTH|BJP|TXP|19:40|21:06|01:26|Y|xONxLE7Kb9xMMDWcKYI%2BDUzMY%2FbWFjoeQdlFPNEJVlGstFSnmzogrI2z3lo%3D|20180220|3|P4|01|02|0|0||||无|||无||18|有|||||10401030|1413|0";
		String strs[] = str.split("\\|");
		for(int i=0;i<strs.length;i++) {
			if(strs[i].equals("无")) {
				System.out.println("第几次出现："+i);
			}
		}
		
	}

	
	/**
	 *
	      第几次出现： --无座
	 */
	public static void wuzuo() {
		String str="U5NQedPbsfE0ZCIoTmSRQ6PQbKX1hcnUVMRbUcZbwXFBriSggqjIvFW%2FprW49x9jEPV79w%2F%2FOemY%0AqMDPiLcARV9G4J91lXM%2FkvJiVM%2BkhFaLBiMJXDeHUtQAqN1w4cMYSntUmra7Hsz5%2Fe2leKis756z%0AmVmE%2FbaabBhbwbIDslAawM4XeFQNH8oxI0yPvrFCPEqDbNTPYrjpPKbAnJzFCKNB5fi7mTzhRs0R%0Ao0GgK3Me09OOvBQLy%2F2uy27%2BD9DZ8xFE7g%3D%3D|预订|240000K48901|K489|BJP|JMB|BJP|TJP|02:42|04:36|01:54|Y|fFifUQ%2ByRswqdGWTc%2F5pz%2FfqyiDYkAKOFCxSnJdoi4iMvAskp1Ll9ZQ%2FJOU%3D|20180220|3|P4|01|02|0|0||||20|||无||有|有|||||10401030|1413|0";
		String strs[] = str.split("\\|");
		for(int i=0;i<strs.length;i++) {
			if(strs[i].equals("无")) {
				System.out.println("第几次出现："+i);
			}
		}
		
	}
	
	
}
