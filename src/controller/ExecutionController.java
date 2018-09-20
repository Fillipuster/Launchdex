package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import model.Destination;

public class ExecutionController {

	public static InputStream execDestination(Destination destination) {
		return exec(destination.getPath());
	}
	
	public static void openExplorerPath(String path) {		
		exec("explorer", "/select,", path);
	}
	
	public static ArrayList<Destination> everythingSearch(String query, int maxResults) {
		ArrayList<Destination> result = new ArrayList<>();
		
		ArrayList<String> queryResult = new ArrayList<>();
		try {
			queryResult = (ArrayList<String>) IOUtils.readLines(exec("es.exe", "-n", Integer.toString(maxResults), "\"" + query + "\""), "UTF-8");
		} catch (IOException e) {
			System.out.println("Failed to read es result stream!");
			e.printStackTrace();
		}
		for (String s : queryResult) {
			result.add(new Destination(s, s));
		}
		return result;
	}
	
	private static InputStream exec(String...cmd) {
		ProcessBuilder builder = new ProcessBuilder(cmd);
		
		try {
			Process p = builder.start();
			return p.getInputStream();
		} catch (IOException e) {
			System.out.println("Failed to execute!");
			e.printStackTrace();
		}

		return null;
	}
	
}
