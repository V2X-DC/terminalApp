package com.example.utils;

public class EventInfo {
	private int id;
	private String time;
	private String eventContent;
	
	public EventInfo() {
		super();
	}
	
	public EventInfo(String time, String eventContent) {
		super();
		this.time = time;
		this.eventContent = eventContent;
		this.id = id;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
}

