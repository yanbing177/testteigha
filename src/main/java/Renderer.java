import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Created by youmiss on 8/31/2014.
 */
public class Renderer {
    private static FPSAnimator animator;

    static {
        animator = new FPSAnimator(60);
    }

    public static FPSAnimator getAnimator() {
        return animator;
    }
}
