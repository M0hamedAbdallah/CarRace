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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

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
    int x, y;
    int mx;
    int my;
    boolean home = true;
    boolean howToPlay = false;
    boolean HIGHSCORE = false;
     int highScore = 0;
    //Assets/thephoto.png
    // here put thephoto.png without any path with name we understand
    String textureName[] = {
        "Window.png", "howtoplay.png", "HIGH-SCORE.png"
    };
    TextureReader.Texture texture;
    int textureIndex[] = new int[textureName.length];
     int score;

    private int rand(int i) {
        Random rand = new Random();
        return rand.nextInt(i + 1);
    }

    public CarRace(String name, int width, int hight) {
        this.name = name;
        x = width;
        y = hight;
    }
    public void readHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = br.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private void writeHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt"))) {
            bw.write(Integer.toString(highScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                printHighScoreNumber();
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
    public void printHighScoreNumber() {
        GL gl = glc.getGL();
        TextRenderer textRenderer = new TextRenderer(new Font("RACE SPACE STR", Font.PLAIN, 30));

        int xPos = 330 ;
        int yPos = 190;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslatef(xPos, yPos, 0);

        textRenderer.beginRendering(glc.getWidth(), glc.getHeight());
        textRenderer.draw(String.valueOf(highScore), xPos, yPos);
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
            }
            else if ((mx > 212 && mx < 468) && (my > (97) && my < (154)))
            {
                System.out.println("High Score");
                home = false;
                HIGHSCORE = true;
                System.out.println(name);
                readHighScore();
                System.out.println("High Score: " + highScore);
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
//            if (score > highScore) {
//                highScore = score;
//                writeHighScore();
//                System.out.println("New High Score: " + highScore);
//            }
//        }


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
