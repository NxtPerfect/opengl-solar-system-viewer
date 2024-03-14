package SolarSystemViewer;

public class Planet {
	private String name;
	private int distanceFromSun;
	private double x;
	private double y;
	private double speed;
	private int radius;
	private double rotation;
	private double rotationSpeed;
	private Planet[] moons;

	private Planet(String name, int distanceFromSun, double x, double y, double speed, int radius, double rotation,
			double rotationSpeed, Planet[] moons) {
		this.name = name;
		this.distanceFromSun = distanceFromSun;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.radius = radius;
		this.rotation = rotation;
		this.moons = moons;
	}

	// Rotates the planet
	private void rotate() {
		rotation += rotationSpeed;
	}

	// Moves the planet to corresponding spot on the circle
	private void move(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Calculate point on the orbit, as well as rotation
	public void orbit() {
		// calculate place on circle with origin at 0,0, and radius of distanceFromSun
		x = Math.cos(rotation) * distanceFromSun;
		y = Math.sin(rotation) * distanceFromSun;
		move(x, y);
		rotate();
	}
}
