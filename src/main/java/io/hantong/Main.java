package io.hantong;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
;

public class Main {

	public static void main(String[] args) {
		//DelegatingDataProvider.getInstance().getRoadConditions();

		Date since = null;
		String sDate = "28-May-2018 10:17";
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		try {
			since = formatter.parse(sDate);
			System.out.println("Since this date:" + since);
			DelegatingDataProvider.getInstance().getRoadConditions("DD", since);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
