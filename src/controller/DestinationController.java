package controller;

import java.util.ArrayList;
import java.util.Comparator;
import model.Destination;
import storage.Storage;

public class DestinationController {
	
	public static ArrayList<Destination> orderMatch(String searchChars) {
		ArrayList<Destination> destinations = Storage.getDestinations();
		
		destinations.sort(new Comparator<Destination>() {
			public int compare(Destination d1, Destination d2) {
				return stringCharMatch(d2.getName(), searchChars) - stringCharMatch(d1.getName(), searchChars);
			};
			
			private int stringCharMatch(String source, String search) {
				source = source.toLowerCase();
				search = search.toLowerCase();
				
				char[] chars = search.toCharArray();
				int matches = 0;
				
				for (int i = 0; i < source.length(); i++) {
					for (int j = 0; j < chars.length; j++) {
						if (chars[j] == source.charAt(i)) {
							matches++;
							chars[j] = 0;
							break;
						}
					}
				}
				
				return matches;
			}
		});

		return destinations;
	}
	
}
