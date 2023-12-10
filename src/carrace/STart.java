package carrace;

import Textures.AnimListener;
import com.sun.opengl.util.*;
import java.awt.*;
import javax.media.opengl.*;
import javax.swing.*;
//import checken.AnimGLEventListener3;

public class STart extends JFrame {
    static int y =700;
    static int x =700;
    static int blbl=0;
    public STart(AnimListener aListener,String name) {
        GLCanvas glcanvas;
        Animator animator;
        if(blbl==0){
            CarRace md = new CarRace(name,x,y);
            setResizable(false);
            glcanvas = new GLCanvas();
            glcanvas.addGLEventListener(md);
            glcanvas.addMouseListener(md);
            glcanvas.addKeyListener(md);
            md.setGLCanvas(glcanvas);
            add(glcanvas, BorderLayout.CENTER);
            animator = new FPSAnimator(60);
            animator.add(glcanvas);
            animator.start();
        }
        else{
            TwoPlayer md = new TwoPlayer(name,x,y);
            setResizable(false);
            glcanvas = new GLCanvas();
            glcanvas.addGLEventListener(md);
            glcanvas.addMouseListener(md);
            glcanvas.addKeyListener(md);
            md.setGLCanvas(glcanvas);
            add(glcanvas, BorderLayout.CENTER);
            animator = new FPSAnimator(60);
            animator.add(glcanvas);
            animator.start();
            x=1200;
            y=1200;
        }
        
        setTitle("Car Race");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(x, y);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
    
}
