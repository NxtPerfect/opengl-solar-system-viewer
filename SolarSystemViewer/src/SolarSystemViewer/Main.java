package SolarSystemViewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class Main extends JFrame implements GLEventListener {
	private static final long serialVersionUID = 1L;
	private volatile GLCanvas canvas;
	private GL2 gl;
	private GLU glu;
	private GLUT glut;
	private FPSAnimator animator;
	private int shaderProgram;
	private int MAX_FPS = 30;
	private Planet[] planets = new Planet[5];

	// Constructor
	public Main() {
		super("Solar System Viewer");

		planets[0] = new Planet("Sun", 0, 0, 0, 20.0, 20, 0, 0, null);
		planets[1] = new Planet("Mercury", 40, 0, 0, 10.0, 2, 45, 0.25, null);
		planets[2] = new Planet("Venus", 60, 0, 0, 10.0, 5, 10, 0.5, null);
		planets[3] = new Planet("Earth", 100, 0, 0, 10.0, 12, 135, 0.75, null);
		planets[4] = new Planet("Mars", 140, 0, 0, 10.0, 2, 180, 0.75, null);

		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension d = kit.getScreenSize();
		setBounds(d.width / 4, d.height / 4, d.width / 2, d.height / 2);
		setMinimumSize(new Dimension(d.width/4, d.height/4));
		setMaximumSize(new Dimension(d.width, d.height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		animator = new FPSAnimator(canvas, MAX_FPS);
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
		float scale[] = { 1.0f, 1.0f, 1.0f };
		gl.glScalef(scale[0], scale[1], scale[2]);

		for (Planet p : planets) {
			p.render(gl, shaderProgram);
		}

		canvas.repaint();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		gl.glDeleteProgram(shaderProgram);
	}

	@Override
	public void init(GLAutoDrawable glAutoDrawable) {
		// Initialize OpenGL settings
		GL2 gl = glAutoDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		float ambientlight[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, ambientlight, 0);

		// TODO Disabled until i figure out how to seperate them
		// for each planet
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
		float ambient[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		float diffuse[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		float specular[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float position[] = { -5.0f, 1.0f, 5.0f, 0.0f };

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

		gl.glEnable(GL2.GL_LIGHT0);

		// Load and compile vertex shader
		int vertexShader = compileShader(gl, GL2.GL_VERTEX_SHADER, "src/SolarSystemViewer/planet.vert");
		// Load and compile fragment shader
		int fragmentShader = compileShader(gl, GL2.GL_FRAGMENT_SHADER, "src/SolarSystemViewer/planet.frag");

		// Create shader program
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertexShader);
		gl.glAttachShader(shaderProgram, fragmentShader);

		// Link program
		gl.glLinkProgram(shaderProgram);
		int[] linked = new int[1];
		gl.glGetProgramiv(shaderProgram, GL2.GL_LINK_STATUS, linked, 0);
		if (linked[0] == 0) {
			int[] logLength = new int[1];
			gl.glGetProgramiv(shaderProgram, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
			byte[] log = new byte[logLength[0]];
			gl.glGetProgramInfoLog(shaderProgram, logLength[0], (int[]) null, 0, log, 0);
			System.err.println("Error linking shader program: " + new String(log));
		}

		// Cleanup individual shaders
		gl.glDeleteShader(vertexShader);
		gl.glDeleteShader(fragmentShader);
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
		glu.gluPerspective(45.0, aspectRatio, 1.0, 500.0);
		gl.glTranslatef(1.0f, 1.0f, -300.0f);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	private int compileShader(GL2 gl, int type, String filename) {
		int shader = gl.glCreateShader(type);
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			StringBuilder shaderSource = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			br.close();
			gl.glShaderSource(shader, 1, new String[] { shaderSource.toString() }, null);
			// Compile shader
			gl.glCompileShader(shader);
			int[] compiled = new int[1];
			gl.glGetShaderiv(shader, GL2.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				int[] logLength = new int[1];
				gl.glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
				byte[] log = new byte[logLength[0]];
				gl.glGetShaderInfoLog(shader, logLength[0], (int[]) null, 0, log, 0);
				System.err.println("Error compiling shader: " + new String(log));
			}
			return shader;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
