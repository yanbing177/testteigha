import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

/**
 * Created by youmiss on 8/27/2014.
 */
public class TeighaDemo {

    private static FPSAnimator animator;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame mainFrame = new MainFrame();
                Renderer.getAnimator().start();
            }
        });
    }
}
