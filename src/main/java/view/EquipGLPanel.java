package view;

import bean.TMLCircle;
import com.jogamp.opengl.util.FPSAnimator;
import com.opendesign.td.OdDbCircle;
import controller.EquipController;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Created by youmiss on 8/31/2014.
 */
public class EquipGLPanel extends GLJPanel implements GLEventListener {

    private static GLCapabilities caps;

    private GLU glu;
    private GL2 gl;

    private EquipController controller;
    private ArrayList<TMLCircle> TMLs;

    static {
        caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        caps.setAlphaBits(8);
    }

    public EquipGLPanel(EquipController controller) {
        super(caps, null);
        this.addGLEventListener(this);
        this.controller = controller;
        TMLs = new ArrayList<TMLCircle>();
        for (OdDbCircle circle : controller.getTMLs()) {
            TMLCircle tmlCircle = new TMLCircle(circle);
            TMLs.add(new TMLCircle(circle));
            System.out.println("Center: (" + tmlCircle.getX() + ", " + tmlCircle.getY() + ") " + "Radius: " + tmlCircle.getRadius());
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();

        glu = new GLU();

        System.err.println("INIT GL IS: " + gl.getClass().getName());

        System.err.println("Chosen GLCapabilities: "
                + drawable.getChosenGLCapabilities());

        gl.setSwapInterval(1);

//        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    // left: 3.55; right: 3.55;
    // top: 5.0; bottom: 5.0
    // Left border is at 230th pixel, the height of the image is 746 pixels, 15.5
    // the width of the image is 762 - 230 = 532 pixels, 11.08
    // Diameter of TML 16.0 is 23 pixels
    // 1 unit in the CAD drawing represents 48 pixel
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Special handling for the case where the GLJPanel is translucent
        // and wants to be composited with other Java 2D content
        if ((drawable instanceof GLJPanel)
                && !((GLJPanel) drawable).isOpaque()
                && ((GLJPanel) drawable)
                .shouldPreserveColorBufferIfTranslucent()) {
            gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        } else {
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        }

        gl.glLoadIdentity();  // reset the model-view matrix

        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----

        gl.glTranslatef(0.0f, 0.0f, -18.75f); // translate into the screen

        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.7f); // red
        if (TMLs != null) {
            // Do some rendering here
            for (TMLCircle circle : TMLs) {
                float x = (float) circle.getX();
                float y = (float) circle.getY();
                float r = (float) circle.getRadius();
//                System.out.println("Center: (" + x + ", " + y + ") " + "Radius: " + r);
                drawFilledCircle(gl, (float) circle.getX(), (float) circle.getY(), (float) circle.getRadius());
            }
        }

    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    private void drawFilledCircle(GL2 gl, float x, float y, float r) {
        int i;
        int triangleAmount = 20;

        float twicePI = (float) (2.0f * Math.PI);

        gl.glBegin(GL_TRIANGLE_FAN);
        gl.glVertex2f(x, y); // center of circle
        for (i = 0; i <= triangleAmount; i++) {
            gl.glVertex2f(
                    (float) (x + (r * Math.cos(i * twicePI / triangleAmount))),
                    (float) (y + (r * Math.sin(i * twicePI / triangleAmount))));
        }
        gl.glEnd();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GLJPanel canvas = new EquipGLPanel(null);
                canvas.setPreferredSize(new Dimension(800, 600));

                final FPSAnimator animator = new FPSAnimator(canvas, 60, true);

                final JFrame frame = new JFrame();
                frame.getContentPane().add(canvas);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Use a dedicate thread to run the stop() to ensure that the
                        // animator stops before program exits.
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) animator.stop();
                                System.exit(0);
                            }
                        }.start();
                    }
                });
                frame.setTitle("Equipment GL Panel");
                frame.pack();
                frame.setVisible(true);
                animator.start(); // start the animation loop
            }
        });
    }



}
