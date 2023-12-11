package carrace;

import Textures.AnimListener;
import Textures.TextureReader;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.Font;
import com.sun.opengl.util.j2d.TextRenderer;
import javax.swing.JFrame;

/**
 *
 * @author Mohamed
 * @author yahia
 * @author Islam
 * @author Nabil
 * @author Timo
 */
public class CarRace extends AnimListener implements GLEventListener, MouseListener {

    String name;
    JFrame frame;
    int x, y;
    int mx;
    int my;
    boolean home = true;
    boolean howToPlay = false;
    boolean HIGHSCORE = false;
    boolean hardlevel = false;
    boolean MultiPlayer = false;
    String Name;
     int orangeCarY = 0;
     int purpleCarY = 0;
     int yelloweCarY = 0;

     float carSpeed = 2.0f;
     int randomX1 = 0;
     int randomX2 = 0;
     int randomX3 = 0;
    // Add these variables to your class
    int redCarX = 340;
    int redCarY = 83;
    float redCarSpeed = 5.0f;

    int roadleftLine = 143;
     int roadrightLine = 543;
    //Assets/thephoto.png
    // here put thephoto.png without any path with name we understand
    String textureName[] = {
        "Window.png", "howtoplay.png", "HIGH-SCORE.png", "background.png", "Orange Car.png", "Purple Car.png", "yellow car.png",
            "Red Car.png"
    };
    TextureReader.Texture texture;
    int textureIndex[] = new int[textureName.length];

    private int rand(int i) {
        Random rand = new Random();
        return rand.nextInt(i + 1);
    }

    public CarRace(String name, int width, int hight, JFrame frame) {
        this.name = name;
        x = width;
        y = hight;
        this.frame = frame;
    }

    public void squreOfHome(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(700, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(700f, 700f, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0f, 700f, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    public void drawCar(GL gl, int index, int xPos, int yPos, int width, int height) {
        gl.glEnable(GL.GL_BLEND);   // Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        // Set the position of the car
        gl.glTranslatef(xPos, yPos, 0);

        // Adjust the scaling factor based on direction (positive or negative)
        gl.glScalef(0.7f, 0.7f, 1.0f);

        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-width / 2, -height / 2, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(width / 2, -height / 2, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(width / 2, height / 2, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-width / 2, height / 2, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void squreINLeft(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(700, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(700f, 700f, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0f, 700f, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    
    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glViewport(0, 0, x, y);
//        gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glMatrixMode(GL.GL_PROJECTION);
//        gl.glOrtho(-450, 450, -250, 250, -1.0, 1.0);
        gl.glOrtho(0, x, 0, y, -1.0, 1.0);

//        try {
//            music = new FileInputStream(new File("Music//chicken dance song.wav"));
//            audios = new AudioStream(music);
//        } catch (IOException ex) {
//            System.err.println(ex.getMessage());
//        }
//        AudioPlayer.player.start(audios);
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        //number of textures, array to hold the indeces
        gl.glGenTextures(textureName.length, textureIndex, 0);

        for (int i = 0; i < textureName.length; i++) {
            try {
                texture = TextureReader.readTexture(assetsFolderName + "//" + textureName[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[i]);

//          mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture.getWidth(), texture.getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture.getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        handleKeyPress();
        try {
            GL gl = gld.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
            handleKeyPress();
            if (home) {
                squreOfHome(gl, 0);
            }
            if (howToPlay) {
                squreOfHome(gl, 1);
            }
            if (HIGHSCORE) {
                squreOfHome(gl, 2);
                printHighScoreName();
            }
            if (MultiPlayer) {
                squreOfHome(gl, 3);
            }
            if (hardlevel) {
                squreOfHome(gl, 3);

                // Update y-positions of the cars to make them move
                orangeCarY -= carSpeed + 13;
                purpleCarY -= carSpeed + 14;
//                yelloweCarY -= carSpeed + 9;

                // Reset positions if the cars go off-screen
                if (orangeCarY < -100) {
                    orangeCarY = y;
                    randomX1 = rand(543 - 145) + 145;
                }
                if (purpleCarY < -100) {
                    purpleCarY = y;
                    randomX2 = rand(543 - 145) + 145;
                }
//                if (yelloweCarY < -100) {
//                    yelloweCarY = y;
//                    randomX3 = rand(543 - 145) + 145;
//                }

                // Draw the orange car
                drawCar(gl, 4, randomX1,orangeCarY, 70, 110);

                // Draw the purple car
                drawCar(gl, 5, randomX2,purpleCarY, 70, 110);

//                drawCar(gl, 6, randomX3,yelloweCarY, 70, 110);
// Update the drawCar method
                drawCar(gl, 7,redCarX, redCarY, 70, 110);
            }
        } catch (Exception ex) {

        }

    }

    public void printHighScoreName() {
        GL gl = glc.getGL();
        TextRenderer textRenderer = new TextRenderer(new Font("RACE SPACE STR", Font.PLAIN, 30));

        int xPos = 270;
        int yPos = 430;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslatef(xPos, yPos, 0);

        textRenderer.beginRendering(glc.getWidth(), glc.getHeight());
        textRenderer.draw(name, xPos, yPos);
        textRenderer.endRendering();

        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care 
    }

    public String getName() {
        return name;
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    //will use this to control the menu
    @Override
    public void mouseClicked(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        my = y - my;
        System.out.println(mx + " " + my);

        if (home) {
            if ((mx > 195 && mx < 458) && (my > (240) && my < (305))) {
                System.out.println("exit");
                System.exit(0);
            } else if ((mx > 195 && mx < 458) && (my > (340) && my < (405))) {
                System.out.println("How To Play");
                home = false;
                howToPlay = true;
            } else if ((mx > 212 && mx < 468) && (my > (97) && my < (154))) {
                System.out.println("High Score");
                home = false;
                HIGHSCORE = true;
                System.out.println(name);
            } else if ((mx > 210 && mx < 489) && (my > (425) && my < (494))) {
                System.out.println("MultiPlayer");
                home = false;
                MultiPlayer = true;
                x = x * 2;
                frame.setSize(1400, 700);
            }
            else if ((mx > 205  && mx < 481) && (my > (522) && my < (575))) {
                System.out.println("Hard Level");
                home = false;
                hardlevel = true;
            }
//            else {
//                    musicOn = true;
////                    AudioPlayer.player.start(audios);
//                }
        }
        if (howToPlay) {
            if ((mx > 14 && mx < 84) && (my > (634) && my < (686))) {
                System.out.println("return");
                home = true;
                howToPlay = false;
            }
        }
        if (HIGHSCORE) {
            if ((mx > 63 && mx < 153) && (my > (632) && my < (664))) {
                System.out.println("return");
                home = true;
                HIGHSCORE = false;
            }
        }

    }

    /////////////////////////////////////
    //will use this to control to cars in maltu and in one player
    public void handleKeyPress() {

//        if (isKeyPressed(KeyEvent.VK_A)) {
//            
//        }
        if (isKeyPressed(KeyEvent.VK_LEFT) && redCarX > roadleftLine+30 ) {
            redCarX -= redCarSpeed;
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT) && redCarX + 15 < roadrightLine) {
            redCarX += redCarSpeed;
        }
        if (isKeyPressed(KeyEvent.VK_UP) && redCarY + 50 < y) {
            redCarY += redCarSpeed;
        }
        if (isKeyPressed(KeyEvent.VK_DOWN) && redCarY > 50) {
            redCarY -= redCarSpeed;
        }
    }
    ////////////////////////////////////////////////////

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setGLCanvas(GLCanvas glc) {
        this.glc = glc;
    }
    GLCanvas glc;

    public static void main(String[] args) {
        new frame().setVisible(true);
    }
}
