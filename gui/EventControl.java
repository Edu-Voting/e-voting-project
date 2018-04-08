package gui;

public class EventControl {

	public EventControl() {
		
	}
	
	public enum event {
		REGISTRATION,
		ELECTION,
		COUNTING,
		RESULTS,		
		NO_EVENT
	}
	
	public static event getStatus() {
		return event.REGISTRATION;
	}
}
