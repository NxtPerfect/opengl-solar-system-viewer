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

public class Main extends JFrame implements GLEventListener {
	private static final long serialVersionUID = 1L;
	private volatile GLCanvas canvas;
	private GL2 gl;
	private GLU glu;
	private GLUT glut;
	private FPSAnimator animator;

	// Constructor
	public Main() {
		super("Solar System Viewer");
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
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
				new Main();
			}
		});

	}

	@Override
	public void display(GLAutoDrawable glAutoDrawable) {
		// Render the scene
		GL2 gl = glAutoDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -10.0f);

//		gl.glRotatef(kat, 1.0f, 0.0f, 0.0f);
//		gl.glRotatef(kat, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(kat, 0.0f, 0.0f, 1.0f);
		float scale[] = { 1.0f, 2.0f, 1.0f };
		gl.glScalef(scale[0], scale[1], scale[2]);
		gl.glTranslatef(1.0f, -2.0f, 0.0f);

//		drawCube(gl);
		// Rotating animation
//		kat += 1.0f;
//		if (kat >= 360.0f) {
//			kat -= 360.0f;
//		}

		canvas.repaint();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable glAutoDrawable) {
		// Initialize OpenGL settings
		GL2 gl = glAutoDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		float ambientlight[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, ambientlight, 0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL_FACE);
		gl.glEnable(GL2.GL_EMISSION);
		float matAmbient[] = { 0.1f, 0.2f, 0.1f, 1.0f };
		float matSpec[] = { 0.0f, 0.5f, 0.0f, 1.0f };
		float emission[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		gl.glMaterialfv(GL2.GL_FRONT_FACE, GL2.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_FACE, GL2.GL_SPECULAR, matSpec, 0);
		gl.glMaterialfv(GL2.GL_FRONT_FACE, GL2.GL_EMISSION, emission, 0);
		gl.glMaterialf(GL2.GL_FRONT_FACE, GL2.GL_SHININESS, 1);

		gl.glEnable(GL2.GL_LIGHTING);
		float ambient[] = { 0.6f, 0.1f, 0.1f, 1.0f };
		float diffuse[] = { 0.2f, 0.0f, 0.2f, 1.0f };
		float specular[] = { 0.8f, 0.0f, 0.0f, 1.0f };
		float position[] = { -5.0f, 1.0f, 5.0f, 0.0f };

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

		gl.glEnable(GL2.GL_LIGHT0);

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
		// Adjust viewport and perspective when the window is resized
		GL2 gl = glAutoDrawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		if (height == 0)
			height = 1;

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float aspectRatio = (float) width / height;
		GLU glu = new GLU();
		glu.gluPerspective(45.0, aspectRatio, 1.0, 100.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	private class Planet implements GLEventListener {
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
	}
}
