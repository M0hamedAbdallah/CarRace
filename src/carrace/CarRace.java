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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Mohamed
 * @author yahia
 * @author Islam
 * @author Nabil
 * @author Timo
 */
public class CarRace extends AnimListener implements GLEventListener, MouseListener {

    GL gl;
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
    int backgroundY = 0;

    ///multi position
    int x_Car_multi_one = 370, y_Car_multi_one = -50, x_Car_multi_two = 753, y_Car_multi_two = -50;
    boolean puase = false;

    //Assets/thephoto.png
    // here put thephoto.png without any path with name we understand
    String textureName[] = {
        "Window.png",    // 0
        "howtoplay.png", //1
        "HIGH-SCORE.png",//2 
        "background.png",//3
        "Orange Car.png",//4
        "Purple Car.png",//5
        "yellow car.png",//6
        "Red Car.png",   //7
        "CarOne.png",    //8
        "Pause_BTN.png",//9
        "Close_BTN.png",//10
        "Play_BTN.png",//11
        "CarTwo.png"   //12
    };
    

    int highScore = 0;

    TextureReader.Texture texture;
    int textureIndex[] = new int[textureName.length];
    int score;

    ///music
    private Clip backgroundMusic;
    private Clip backgroundMusic2;
    boolean musicOn = true;

    ///endmusic
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

    public void readHighScore() {
        try ( BufferedReader br = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = br.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void writeHighScore() {
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt"))) {
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

    public void squreSettings(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(70, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(70, 70, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0f, 70, -1.0f);

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

    public void TheCarMultiOne(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(70, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(70, 100, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0f, 100, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    @Override
    public void init(GLAutoDrawable gld) {
//        GL gl = gld.getGL();
//        gl.glViewport(0, 0, x, y);
//        gl.glMatrixMode(GL.GL_PROJECTION);
//        gl.glOrtho(0, x, 0, y, -1.0, 1.0);
        GL gl = gld.getGL();
        gl.glViewport(0, 0, x, y);

        // Update the orthographic projection based on the new size
        updateOrtho(gl);
        try {
//            music = new FileInputStream(new File("Music//rom.wav"));
//            audios = new AudioStream(music);
//            music1 = new FileInputStream(new File("Music//car.wav"));
//            audios1 = new AudioStream(music1);
            File musicFile = new File("Music//car.wav");
            File musicFile2 = new File("Music//rom.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(musicFile2);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic2 = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic2.open(audioInputStream2);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
//        AudioPlayer.player.start(audios);
//        AudioPlayer.player.start(audios1);
        playMusic();
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

    private void playMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic2.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Call this method to stop the background music
    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic2.stop();
        }
    }

    // Call this method to rewind the background music to the beginning
    private void rewindMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic2.setFramePosition(0);
        }
    }

    private void updateOrtho(GL gl) {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, x, 0, y, -1.0, 1.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
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
                printHighScoreNumber();
            }
            if (MultiPlayer) {
                if (!puase) {
                    if(y_Car_multi_one < 20){
                        y_Car_multi_one += 5;
                    }
                    if(y_Car_multi_two < 20){
                        y_Car_multi_two += 5;
                    }
                    
                    moveBackground();
                    drawBackground(gl);
                    gl.glPushMatrix();
                    gl.glTranslated(x_Car_multi_one, y_Car_multi_one, 0);
                    TheCarMultiOne(gl, 8);
                    gl.glPopMatrix();
                
                    
                    gl.glPushMatrix();
                    gl.glTranslated(x_Car_multi_two, y_Car_multi_two, 0);
                    TheCarMultiOne(gl, 12);
                    gl.glPopMatrix();
                    
                    
                    gl.glPushMatrix();
                    gl.glTranslated(20, y - 80, 0);
                    squreSettings(gl, 9);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x - 80, y - 80, 0);
                    squreSettings(gl, 10);
                    gl.glPopMatrix();
                }
                if (puase) {
                    gl.glPushMatrix();
                    gl.glTranslated(x - 80, y - 80, 0);
                    squreSettings(gl, 11);
                    gl.glPopMatrix();
                }
                
            
                
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
                drawCar(gl, 4, randomX1, orangeCarY, 70, 110);

                // Draw the purple car
                drawCar(gl, 5, randomX2, purpleCarY, 70, 110);

                //drawCar(gl, 6, randomX3,yelloweCarY, 70, 110);
                // Update the drawCar method
                drawCar(gl, 7, redCarX, redCarY, 70, 110);
            }
        } catch (Exception ex) {

        }

    }

    private void moveBackground() {
        int backgroundSpeed = 10;

        backgroundY += backgroundSpeed;
        if (backgroundY >= 700) {
            backgroundY = 0;
        }
    }

    private void drawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[3]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, backgroundY / 700.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, backgroundY / 700.0f);
        gl.glVertex3f(1200, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, (backgroundY + 700f) / 700.0f);
        gl.glVertex3f(1200, 700f, -1.0f);

        gl.glTexCoord2f(0.0f, (backgroundY + 700f) / 700.0f);
        gl.glVertex3f(0f, 700f, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
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

        int xPos = 330;
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
    public void reshape(GLAutoDrawable glad, int i, int i1, int width, int height) {
//        x = width;
//        y = height;

        GL gl = glad.getGL();
        gl.glViewport(0, 0, width, height);

        // Update the orthographic projection based on the new size
        updateOrtho(gl);
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
                x = 1200;
                y = 700;
                frame.setSize(1200, 700);
                centerWindow(frame);
                glc.repaint();

//                readHighScore();
//                System.out.println("High Score: " + highScore);
            } else if ((mx > 205 && mx < 481) && (my > (522) && my < (575))) {
                System.out.println("Hard Level");
                home = false;
                hardlevel = true;
            } else if ((mx > 534 && mx < 589) && (my > (621) && my < (669))) {
                System.out.println("sound");
                if (musicOn) {
                    musicOn = false;
                    stopMusic();
                } else {
                    musicOn = true;
                    playMusic();
                }
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
        if (MultiPlayer) {
            if ((mx > 1105 && mx < 1174) && (my > (624) && my < (692))) {
                if (!puase) {
                    System.out.println("return");
                    home = true;
                    MultiPlayer = false;
                    x = 700;
                    y = 700;
                    frame.setSize(700, 700);
                    centerWindow(frame);
                    glc.repaint();
                } else {
                    puase = false;
                }
            }
            if ((mx > 19 && mx < 88) && (my > (624) && my < (692))) {
                System.out.println("puase");
                puase = true;
            }
        }

    }

    /////////////////////////////////////
    //will use this to control to cars in maltu and in one player
    public void handleKeyPress() {
        if (MultiPlayer) {
            if (isKeyPressed(KeyEvent.VK_D) && x_Car_multi_one < x / 2 - 70 && !puase) {
                x_Car_multi_one += 5;
            }
            if (isKeyPressed(KeyEvent.VK_A) && x_Car_multi_one > 190 && !puase) {
                x_Car_multi_one -= 5;
            }
            if (isKeyPressed(KeyEvent.VK_RIGHT ) && x_Car_multi_two < 944 ) {
                x_Car_multi_two += 5;
            }
            if (isKeyPressed(KeyEvent.VK_LEFT) && x_Car_multi_two > x / 2  ) {
                x_Car_multi_two -= 5;
            }
        }
        if (hardlevel) {
            if (isKeyPressed(KeyEvent.VK_LEFT) && redCarX > roadleftLine + 30) {
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

    public void centerWindow(Component frame) {
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        frame.setLocation(
                (screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1
        );
    }
}
