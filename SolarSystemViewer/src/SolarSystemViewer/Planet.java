package SolarSystemViewer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Planet {
	private String name;
	private int distanceFromSun;
	private double x;
	private double y;
	private double speed;
	private int radius;
	private float rotation;
	private double rotationSpeed;
	private Planet[] moons;

	Planet(String name, int distanceFromSun, double x, double y, double speed, int radius, float rotation,
			double rotationSpeed, Planet[] moons) {
		this.name = name;
		this.distanceFromSun = distanceFromSun;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.radius = radius;
		this.rotation = rotation;
		this.moons = moons;
		this.rotationSpeed = rotationSpeed;
	}

	// Rotates the planet
	private void rotate() {
		rotation += rotationSpeed;
		if (rotation > 360.0f) {
			rotation = 0.0f;
		}
	}

	// Calculate point on the orbit
	// and move it
	private void orbit() {
		double radiansRotation = Math.toRadians(rotation);
		this.x = Math.cos(radiansRotation) * distanceFromSun;
		this.y = Math.sin(radiansRotation) * distanceFromSun;
	}

	private void Draw(GL2 gl, double r, int n, int m) {
		double alpha, beta;
		int i, j;

		for (beta = 180.0 / m, i = 0; beta <= 180.0; beta += 180.0 / m, i++)
			for (alpha = 0.0, j = 0; alpha < 360.0; alpha += 360.0 / n, j++) {
				gl.glPushAttrib(GL2.GL_CURRENT_BIT);
				if ((i + j) % 2 == 0)
					gl.glColor3f(0.0f, 0.0f, 0.0f);
				gl.glBegin(GL2.GL_QUADS);

				gl.glNormal3d(Math.sin(Math.PI * (beta - 180.0 / m) / 180.0) * Math.cos(Math.PI * alpha / 180.0),
						Math.cos(Math.PI * (beta - 180.0 / m) / 180.0),
						Math.sin(Math.PI * (beta - 180.0 / m) / 180.0) * Math.sin(Math.PI * alpha / 180.0));
				gl.glVertex3d(r * Math.sin(Math.PI * (beta - 180.0 / m) / 180.0) * Math.cos(Math.PI * alpha / 180.0),
						r * Math.cos(Math.PI * (beta - 180.0 / m) / 180.0),
						r * Math.sin(Math.PI * (beta - 180.0 / m) / 180.0) * Math.sin(Math.PI * alpha / 180.0));
				gl.glNormal3d(Math.sin(Math.PI * beta / 180.0) * Math.cos(Math.PI * alpha / 180.0),
						Math.cos(Math.PI * beta / 180.0),
						Math.sin(Math.PI * beta / 180.0) * Math.sin(Math.PI * alpha / 180.0));
				gl.glVertex3d(r * Math.sin(Math.PI * beta / 180.0) * Math.cos(Math.PI * alpha / 180.0),
						r * Math.cos(Math.PI * beta / 180.0),
						r * Math.sin(Math.PI * beta / 180.0) * Math.sin(Math.PI * alpha / 180.0));
				gl.glNormal3d(Math.sin(Math.PI * beta / 180.0) * Math.cos(Math.PI * (alpha - 360.0 / n) / 180.0),
						Math.cos(Math.PI * beta / 180.0),
						Math.sin(Math.PI * beta / 180.0) * Math.sin(Math.PI * (alpha - 360.0 / n) / 180.0));
				gl.glVertex3d(r * Math.sin(Math.PI * beta / 180.0) * Math.cos(Math.PI * (alpha - 360.0 / n) / 180.0),
						r * Math.cos(Math.PI * beta / 180.0),
						r * Math.sin(Math.PI * beta / 180.0) * Math.sin(Math.PI * (alpha - 360.0 / n) / 180.0));
				gl.glNormal3d(
						Math.sin(Math.PI * (beta - 180.0 / m) / 180.0)
								* Math.cos(Math.PI * (alpha - 360.0 / n) / 180.0),
						Math.cos(Math.PI * (beta - 180.0 / m) / 180.0), Math.sin(Math.PI * (beta - 180.0 / m) / 180.0)
								* Math.sin(Math.PI * (alpha - 360.0 / n) / 180.0));
				gl.glVertex3d(
						r * Math.sin(Math.PI * (beta - 180.0 / m) / 180.0)
								* Math.cos(Math.PI * (alpha - 360.0 / n) / 180.0),
						r * Math.cos(Math.PI * (beta - 180.0 / m) / 180.0),
						r * Math.sin(Math.PI * (beta - 180.0 / m) / 180.0)
								* Math.sin(Math.PI * (alpha - 360.0 / n) / 180.0));
				gl.glEnd();
				gl.glPopAttrib();
			}
	}

	public void render(GL2 gl, int shaderProgram) {
		gl.glPushMatrix();
		gl.glUseProgram(shaderProgram);
		gl.glTranslated(x, y, 0);
		GLU glu = new GLU();
//		GLUquadric quad = glu.gluNewQuadric();

		// this was the issue
		// by using gluSphere i couldn't use shaders
//		glu.gluSphere(quad, (double) this.radius, 25, 25);
		gl.glPushMatrix();
		this.Draw(gl, radius, 10, 10);
		gl.glPopMatrix();

		// TODO: here i should add render of moons
		if (this.moons != null) {
			for (Planet moon : this.moons) {
				moon.render(gl, shaderProgram);
			}
		}

//		glu.gluDeleteQuadric(quad);
		rotate();
		orbit();
		gl.glUseProgram(0);
		gl.glPopMatrix();
	}

	public void debug() {
		System.out.println(this.name + " rotation: " + this.rotation + " coordinates (x,y): (" + this.x + "," + this.y
				+ ") distance from sun: " + this.distanceFromSun);
	}
}
