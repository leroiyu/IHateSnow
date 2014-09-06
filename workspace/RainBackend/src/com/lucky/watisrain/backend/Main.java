package com.lucky.watisrain.backend;

import java.io.File;

import com.lucky.watisrain.backend.data.*;

/**
 * This is merely a test harness for RainBackend
 */
public class Main {

	public static void main(String... args) throws Throwable{
		
		File file = new File("E:/Projects/MHacks/WATisRain/WATisRain/workspace/WATisRain/assets/locations.txt");
		Map map = MapFactory.readMapFromFile(file);
		
		//map.printDataToStdout();
		
		RouteFinder routefinder = new RouteFinder(map);
		Route route = routefinder.findRoute(map.getLocationByID("AH"), map.getLocationByID("LB"));
		
		route.printRouteToStdout();
		System.out.println();
		//route.getContractedRoute().printRouteToStdout();
	}
}
