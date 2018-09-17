package model;

public class Destination {

//	private int order;
	private String name;
	private String path;
	
	public Destination(String name, String path) {
//		this.order = order;
		this.name = name;
		this.path = path;
	}
	
//	public int getOrder() {
//		return order;
//	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
//		return "(" + order + ") " + path;
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Destination) {
			if (((Destination) obj).getPath() == this.path) {
				return true;
			}
		}
		
		return false;
	}
	
}
