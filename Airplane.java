class Airplane extends Thread{
	
	private int id;
	Runway runway;
	
	public Airplane(int identification, Runway r) {
		id = identification;
		runway = r;
	}
	
	public void run() {
		while(true) {
			try {
				int directionToLandIn = Runway.r.nextInt(2); // random between 0 and 1
				runway.requestToLand(id, directionToLandIn); // will block until plane can land
				
				System.out.println("Airplane "+id+" starts landing in direction "+ directionToLandIn);
				sleep(Runway.r.nextInt(2000)); // landing
				System.out.println("Airplane "+id+" finishes landing in direction "+ directionToLandIn);
				
				runway.finishLanding(id, directionToLandIn);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}