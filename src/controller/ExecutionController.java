package controller;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import model.Destination;

public class ExecutionController {

	public static InputStream execDestination(Destination destination) {
		try {
			return exec(destination.getPath());
		} catch (Exception e) {
			System.out.println("Destination Execution Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public static void openExplorerPath(String path) {		
		try {
			exec("explorer", "/select,", path);
		} catch (Exception e) {
			System.out.println("Path Explorer Open Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Destination> everythingSearch(String query, int maxResults) {
		ArrayList<Destination> result = new ArrayList<>();
		
//		if (query.length() <= 3) {
//			result.add(new Destination("Search query too short...", "Search query too short..."));
//			return result;
//		}
		
		try {
			ArrayList<String> queryResult = (ArrayList<String>) IOUtils.readLines(exec("es", "-n", Integer.toString(maxResults), query), "UTF-8");
			for (String s : queryResult) {
				result.add(new Destination(s, s));
			}
		} catch (Exception e) {
			System.out.println("Everything Search Execution Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		return result;
//		ArrayList<Destination> result = new ArrayList<>();
//		
//		InputStream queryResultStream;
//		try {
//			queryResultStream = exec("es", query);
//		} catch (Exception e) {
//			result.add(new Destination("Search Error", "NORES"));
//			return result;
//		}
//		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(queryResultStream));
//		try {
//			String line = reader.readLine();
//			while (line != null) {
//				result.add(new Destination(line, line));
//				line = reader.readLine();
//			}
//		} catch (IOException e) {
//			result.add(new Destination("No Results", "NORES"));
//			return result;
//		}
//		
//		return result;
	}
	
	private static InputStream exec(String...cmd) throws Exception {
		ProcessBuilder builder = new ProcessBuilder(cmd);
		
		Process p = builder.start();
		return p.getInputStream();
	}
	
}
