package SolarSystemViewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class SolarSystemViewer extends JFrame implements GLEventListener {
	private GL2 gl;
	private GLU glu;
	private GLUT glut;
	private FPSAnimator animator;

	// Constructor
	public SolarSystemViewer() {
		super("Solar System Viewer");
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		GLCanvas canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension d = kit.getScreenSize();
		setBounds(d.width / 4, d.height / 4, d.width / 2, d.height / 2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		animator = new FPSAnimator(canvas, 30);
		animator.start();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SolarSystemViewer();
			}
		});

	}

	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	// Planet class
	private class Planet {
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

}
