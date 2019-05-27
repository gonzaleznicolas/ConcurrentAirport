public class Controller extends Thread{
	
	int direction;
	Runway runway;
	
	public Controller(int d, Runway r) {
		direction = d;
		runway = r;
	}
	
	public void run() {
		while(true) {
			runway.serviceLandingRequest(direction);
		}
	}
}
