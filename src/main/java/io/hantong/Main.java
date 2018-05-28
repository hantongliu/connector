package io.hantong;

import java.io.IOException;
import java.net.MalformedURLException;

public class Main {

	public static void main(String[] args) throws MalformedURLException, IOException {
		DelegatingDataProvider.getInstance().getRoadConditions();
	}

}
