import java.util.Random;
import java.util.concurrent.*;

public class Runway {
	static final int NUM_PLANES = 15;
	static Random r = new Random(5407);
	
	private Semaphore runwayMutex = new Semaphore(1, true);
	private Semaphore[] blockControllerIfNoPlaneRequests = {new Semaphore(0, true), new Semaphore(0, true)};
	private Semaphore[] blockPlaneWaitingToland = {new Semaphore(0, true), new Semaphore(0, true)};
	private Semaphore[] blockControllerWhilePlaneLands = {new Semaphore(0, true), new Semaphore(0, true)};
	
	public static void main(String[] args) {
		Runway runway = new Runway();

		Controller[] controllers = new Controller[2];
		for(int i = 0; i<2; i++)
			controllers[i] = new Controller(i, runway);
		
		Airplane[] airplanes = new Airplane[Runway.NUM_PLANES];
		for(int i = 0; i < airplanes.length; i++)
			airplanes[i] = new Airplane(i, runway);
		
		for(int i = 0; i<2; i++)
			controllers[i].start();
		
		for(int i = 0; i < airplanes.length; i++)
			airplanes[i].start();
	}
	
	// executed by plane
	public void requestToLand(int planeId, int directionToLandIn) {
		try {
			blockControllerIfNoPlaneRequests[directionToLandIn].release();
			blockPlaneWaitingToland[directionToLandIn].acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// executed by plane
	public void finishLanding(int planeId, int directionToLandIn) {
		blockControllerWhilePlaneLands[directionToLandIn].release();
	}
	
	// executed by runway
	public void serviceLandingRequest(int direction) {
		try {
			blockControllerIfNoPlaneRequests[direction].acquire(); // block controller if no planes are requesting to land
			runwayMutex.acquire(); // reserve the runway
			blockPlaneWaitingToland[direction].release(); // let a plane land on the runway
			blockControllerWhilePlaneLands[direction].acquire(); // block while the plane is landing
			runwayMutex.release(); // release the runway
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
