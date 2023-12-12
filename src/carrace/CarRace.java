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
import javax.swing.JOptionPane;

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
    boolean singlePlayer = false;
    boolean mediumLevel = false;
    boolean easyLevel = false;
    boolean isPaused = false;

    int orangeCarY = 0;
    int LeftLeftpurpleCarY = 0;
    int LeftRightorangeCarY = 0;
    int RightLeftpurpleCarY = 0;
    int RightRightorangeCarY = 0;
    float carSpeed = 2.0f;


    // Add these variables to your class
    int[] randomX = {294, 190, 407, 520};   
    int[] carAccident = {7 ,7, 21 ,22, 23};   
    int innx=0;
    int life = 4;
    int cnt = 0;
    long lastCollisionTime = System.currentTimeMillis();


    int redCarX = 340;
    int redCarY = 83;
    float redCarSpeed = 5.0f;
    float readCarSpeedMediumLevel = 2.0f;
    float readCarSpeedEasyLevel = 1.0f;
    int roadleftLine = 143;
    int roadrightLine = 543;
    int backgroundY = 0;
    long startTime;
    TextRenderer timerRenderer;
    int score = 0;
    int collisionCount = 0;
    int HP_Bonus = 5;
    long totalElapsedTime = 0;
    long lastFrameTime = 0;
    ///multi position
    int x_Car_multi_one = 370, y_Car_multi_one = -50, x_Car_multi_two = 753, y_Car_multi_two = -50;
    int Live_multi_one[] = {18, 18, 18, 18, 18}, Live_multi_two[] = {18, 18, 18, 18, 18};
    ;
    boolean puase = false;

    //Assets/thephoto.png
    // here put thephoto.png without any path with name we understand
    String textureName[] = {
        "Window.png",       //0
        "howtoplay.png",    //1
        "HIGH-SCORE.png",   //2
        "background.png",   //3
        "Orange Car.png",   //4
        "Purple Car.png",   //5
        "yellow car.png",   //6
        "CarOne.png",      //7
        "CarOne.png",       //8
        "Pause_BTN.png",    //9
        "Close_BTN.png",    //10
        "Play_BTN.png",     //11
        "Barrel_01.png",    //12
        "HP_Bonus.png",     //13
        "Oil.png",          //14
        "background.png",   //15
        "CarTwo.png",       //16
        "HP_Bar.png",       //17
        "HP_Dot.png",       //18
        "HP_Dot_BG.png",    //19
        "1.png",            //20
        "2.png",            //21
        "3.png",            //22
        "4.png",            //23
        "OnePlayer.png"     //24
    };

    int highScore = 0;

    TextureReader.Texture texture;
    int textureIndex[] = new int[textureName.length];

    ///music
    private Clip backgroundMusic;
    private Clip backgroundMusic2;
    boolean musicOn = true;

    ///endmusic
    private int rand(int i) {
        Random rand = new Random();
        return rand.nextInt(i + 1);
    }
    private int rand(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
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
    private void carCrash() {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastCollisionTime;
        if (timeDifference >= 1000) {
            lastCollisionTime = currentTime;
            if (innx < 4) {
                innx++;
            }
            else{
                JOptionPane.showMessageDialog(frame, "You Lose!!");
                System.exit(0);
            }
            life--;
            if(life==-1){
                JOptionPane.showMessageDialog(frame, "You Lose!!");
                System.exit(0);

            }
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

    public void squrePar(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(300, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(300, 70, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0f, 70, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void squreLive(GL gl, int index) {
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
        gl.glVertex3f(70, 50, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0f, 50, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void drawCar(GL gl, int index, int xPos, int yPos, int width, int height) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[index]);

        gl.glPushMatrix();

        gl.glTranslatef(xPos, yPos, 0);

//        gl.glScalef(0.7f, 0.7f, 1.0f);
        gl.glScalef(1.0f, 1.0f, 1.0f);

        gl.glBegin(GL.GL_QUADS);

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

    public void drawHPBonus(GL gl, int x, int y, int width, int height, int numberOfBonuses) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[13]); // Use the index for HP_Bonus texture

        gl.glPushMatrix();
        float scaleFactor = 0.3f;
        gl.glScalef(scaleFactor, scaleFactor, 1.0f);
        int verticalSpacing = 100;
        for (int i = 0; i < numberOfBonuses; i++) {
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(x, y + i * (height + verticalSpacing), -1.0f);

            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(x + width, y + i * (height + verticalSpacing), -1.0f);

            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(x + width, y + height + i * (height + verticalSpacing), -1.0f);

            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(x, y + height + i * (height + verticalSpacing), -1.0f);

            gl.glEnd();
        }

        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    private void drawElapsedTime(GL gl, long elapsedTime , int posx , int posy) {
        int xPos = 10;
        int yPos = 20;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslatef(xPos, yPos, 0);

        timerRenderer.beginRendering(glc.getWidth(), glc.getHeight());
        timerRenderer.draw("Time: " + formatTime(elapsedTime), posx, posy);
        timerRenderer.endRendering();

        gl.glPopMatrix();
    }

    private String formatTime(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void drawScore(GL gl, int score) {
        int xPos = 10;
        int yPos = y + 500;

        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslatef(xPos, yPos, 0);

        TextRenderer scoreRenderer = new TextRenderer(new Font("Arial", Font.PLAIN, 20));
        scoreRenderer.beginRendering(glc.getWidth(), glc.getHeight());
        scoreRenderer.draw("Score: " + score, 290, 630);
        scoreRenderer.endRendering();

        gl.glPopMatrix();
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
        startTime = System.currentTimeMillis();
        timerRenderer = new TextRenderer(new Font("Arial", Font.PLAIN, 20));
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
//        playMusic();
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
            if (singlePlayer) {
                squreOfHome(gl, 24);
            }
            if (HIGHSCORE) {
                squreOfHome(gl, 2);
                printHighScoreName();
                printHighScoreNumber();
            }
            if (MultiPlayer) {
                if (!puase) {
                    if (y_Car_multi_one < 20) {
                        y_Car_multi_one += 5;
                    }
                    if (y_Car_multi_two < 20) {
                        y_Car_multi_two += 5;
                    }

                    moveBackground();
                    drawBackground(gl);
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = 0;
                    if (!isPaused) {
                        if (lastFrameTime != 0) {
                            elapsedTime = currentTime - lastFrameTime;
                            totalElapsedTime += elapsedTime;
                        }
                        lastFrameTime = currentTime;
                    }
                    
                    gl.glPushMatrix();
                    gl.glTranslated(x_Car_multi_one, y_Car_multi_one, 0);
                    TheCarMultiOne(gl, 8);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x_Car_multi_two, y_Car_multi_two, 0);
                    TheCarMultiOne(gl, 16);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(20, y - 80, 0);
                    squreSettings(gl, 9);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(x - 80, y - 80, 0);
                    squreSettings(gl, 10);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(70, 0, 0);
                    gl.glRotated(90, 0, 0, 1);
                    squrePar(gl, 17);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(1200, 0, 0);
                    gl.glRotated(90, 0, 0, 1);
                    squrePar(gl, 17);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(1130, 0, 0);
                    squreLive(gl, Live_multi_two[0]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(1130, 60, 0);
                    squreLive(gl, Live_multi_two[1]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(1130, 120, 0);
                    squreLive(gl, Live_multi_two[2]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(1130, 170, 0);
                    squreLive(gl, Live_multi_two[3]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(1130, 220, 0);
                    squreLive(gl, Live_multi_two[4]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(0, 0, 0);
                    squreLive(gl, Live_multi_one[0]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(0, 60, 0);
                    squreLive(gl, Live_multi_one[1]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(0, 120, 0);
                    squreLive(gl, Live_multi_one[2]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(0, 170, 0);
                    squreLive(gl, Live_multi_one[3]);
                    gl.glPopMatrix();

                    gl.glPushMatrix();
                    gl.glTranslated(0, 220, 0);
                    squreLive(gl, Live_multi_one[4]);
                    gl.glPopMatrix();
                    drawElapsedTime(gl, totalElapsedTime,520,600);

                }
                if (puase) {
                    isPaused = true;
                    gl.glPushMatrix();
                    gl.glTranslated(x - 80, y - 80, 0);
                    squreSettings(gl, 11);
                    gl.glPopMatrix();
                    lastFrameTime = 0; 
                } 
                else {
                    isPaused = false;
                }

            }
            if (hardlevel) {

                score++;
                if (puase) {
                    drawReturnPlay(gl);
                }
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                moveBackground();
                drawHardBackground(gl);
                drawPauseHard(gl);
                drawReturnHard(gl);
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                LeftRightorangeCarY -= carSpeed + rand(10, 15);
                LeftLeftpurpleCarY -= carSpeed + rand(10, 17);
                RightLeftpurpleCarY -= carSpeed + rand(10, 17);
                RightRightorangeCarY -= carSpeed + rand(10, 15);

                if (LeftRightorangeCarY < -500) {
                    LeftRightorangeCarY = y;
                }
                if (LeftLeftpurpleCarY < -300) {
                    LeftLeftpurpleCarY = y;

                }
                if (RightLeftpurpleCarY < -1000) {
                    RightLeftpurpleCarY = y;

                }
                if (RightRightorangeCarY < -200) {
                    RightRightorangeCarY = y;

                }
                
                for (int i = 1; i <= life; i++) {
                    drawHPBonus(gl, 100, 100 + i * 120, 100, 100, 1);

                }
                drawCar(gl, 4, randomX[0], LeftRightorangeCarY, 70, 110);
                drawCar(gl, 5, randomX[1], LeftLeftpurpleCarY, 70, 110);
                drawCar(gl, 4, randomX[3], RightLeftpurpleCarY, 70, 110);
                drawCar(gl, 5, randomX[2], RightRightorangeCarY, 70, 110);

                // Draw the Red car
                drawCar(gl, carAccident[innx], redCarX, redCarY, 70, 110);
                
                
                
                                 
                if (redCarX >= randomX[0] - 55 && redCarX <= randomX[0] + 53 ) {
                    if( Math.abs(LeftRightorangeCarY -redCarY ) <= 100){
                        carCrash();
                    }
                }
                
                if (redCarX >= randomX[1] - 55 && redCarX <= randomX[1] + 53 ) {
                    if( Math.abs(LeftLeftpurpleCarY -redCarY ) <= 100){
                        carCrash();
                    }
                }
                if (redCarX >= randomX[2] - 55 && redCarX <= randomX[2] + 53 ) {
                    if( Math.abs(RightRightorangeCarY -redCarY ) <= 100){
                        carCrash();
                    }
                }
                if (redCarX >= randomX[3] - 55 && redCarX <= randomX[3] + 53 ) {
                    if( Math.abs(RightLeftpurpleCarY-redCarY ) <= 100){
                        carCrash();
                    }
                }

                
                
                drawElapsedTime(gl, elapsedTime,5,5);
                drawScore(gl, score);
                
                
                
                
            }

            if (mediumLevel) {

                score++;
                if (puase) {
                    drawReturnPlay(gl);
                }
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                moveBackground();
                drawHardBackground(gl);
                drawPauseHard(gl);
                drawReturnHard(gl);
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                LeftRightorangeCarY -= readCarSpeedMediumLevel + 7;
                LeftLeftpurpleCarY -= readCarSpeedMediumLevel + 7;
                RightLeftpurpleCarY -= readCarSpeedMediumLevel + 7;
                RightRightorangeCarY -= readCarSpeedMediumLevel + 7;

                if (LeftRightorangeCarY < -500) {
                    LeftRightorangeCarY = y;
                }
                if (LeftLeftpurpleCarY < -300) {
                    LeftLeftpurpleCarY = y;

                }
                if (RightLeftpurpleCarY < -1000) {
                    RightLeftpurpleCarY = y;

                }
                if (RightRightorangeCarY < -200) {
                    RightRightorangeCarY = y;

                }
                for (int i = 1; i <= 5; i++) {
                    drawHPBonus(gl, 100, 100 + i * 120, 100, 100, 1);

                }
                drawCar(gl, 4, randomX[0], LeftRightorangeCarY, 70, 110);
                drawCar(gl, 5, randomX[2], RightRightorangeCarY, 70, 110);

                // Draw the Red car
                drawCar(gl, 7, redCarX, redCarY, 70, 110);
//                drawElapsedTime(gl, elapsedTime);
                drawScore(gl, score);
            }

            if (easyLevel) {

                score++;
                if (puase) {
                    drawReturnPlay(gl);
                }
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                moveBackground();
                drawHardBackground(gl);
                drawPauseHard(gl);
                drawReturnHard(gl);
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                LeftRightorangeCarY -= readCarSpeedEasyLevel + 3;
                LeftLeftpurpleCarY -= readCarSpeedEasyLevel + 3;
                RightLeftpurpleCarY -= readCarSpeedEasyLevel + 3;
                RightRightorangeCarY -= readCarSpeedEasyLevel + 3;

                if (LeftRightorangeCarY < -500) {
                    LeftRightorangeCarY = y;
                }
                if (LeftLeftpurpleCarY < -300) {
                    LeftLeftpurpleCarY = y;

                }
                if (RightLeftpurpleCarY < -1000) {
                    RightLeftpurpleCarY = y;

                }
                if (RightRightorangeCarY < -200) {
                    RightRightorangeCarY = y;

                }
                for (int i = 1; i <= 5; i++) {
                    drawHPBonus(gl, 100, 100 + i * 120, 100, 100, 1);

                }
                drawCar(gl, 4, randomX[0], LeftRightorangeCarY, 70, 110);
                drawCar(gl, 5, randomX[2], RightRightorangeCarY, 70, 110);

                // Draw the Red car
                drawCar(gl, 7, redCarX, redCarY, 70, 110);
//                drawElapsedTime(gl, elapsedTime);
                drawScore(gl, score);
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

    private void drawHardBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[15]);

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, backgroundY / 700.0f);
        gl.glVertex3f(0f, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, backgroundY / 700.0f);
        gl.glVertex3f(700, 0f, -1.0f);

        gl.glTexCoord2f(1.0f, (backgroundY + 700f) / 700.0f);
        gl.glVertex3f(700, 700f, -1.0f);

        gl.glTexCoord2f(0.0f, (backgroundY + 700f) / 700.0f);
        gl.glVertex3f(0f, 700f, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

    }

    private void drawPauseHard(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[9]);

        gl.glPushMatrix();

        gl.glTranslatef(30, 600, 0.0f);
        float scaleFactor = 0.5f;
        gl.glScalef(scaleFactor, scaleFactor, 1.0f);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(100, 0, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(100, 100, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 100, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    private void drawReturnHard(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[10]);

        gl.glPushMatrix();

        gl.glTranslatef(625, 600, 0.0f);
        float scaleFactor = 0.5f;
        gl.glScalef(scaleFactor, scaleFactor, 1.0f);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0, -1.0f); // Top-left corner

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(100, 0, -1.0f); // Top-right corner

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(100, 100, -1.0f); // Bottom-right corner

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 100, -1.0f); // Bottom-left corner

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    private void drawReturnPlay(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[11]);

        gl.glPushMatrix();

        gl.glTranslatef(500, 500, 0.0f);
        float scaleFactor = 0.5f;
        gl.glScalef(scaleFactor, scaleFactor, 1.0f);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0, -1.0f); // Top-left corner

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(100, 0, -1.0f); // Top-right corner

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(100, 100, -1.0f); // Bottom-right corner

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 100, -1.0f); // Bottom-left corner

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
                startTime=0;
                startTime = System.currentTimeMillis();
                x = 1200;
                y = 700;
                frame.setSize(1200, 700);
                centerWindow(frame);
                glc.repaint();

//                readHighScore();
//                System.out.println("High Score: " + highScore);
            } else if ((mx > 205 && mx < 481) && (my > (522) && my < (575))) {
                System.out.println("single Player");
                home = false;
//                hardlevel = true;
                singlePlayer = true;
//                hardlevel = true;

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
        if (singlePlayer) {
            if ((mx > 200 && mx < 485) && (my > (244) && my < (305))) {
                System.out.println("Hard lever");
                hardlevel = true;
                singlePlayer = false;
                startTime=0;
                startTime = System.currentTimeMillis();
            } else if ((mx > 63 && mx < 125) && (my > (615) && my < (675))) {
                home = true;
                singlePlayer = false;
            } else if ((mx > 534 && mx < 589) && (my > (621) && my < (669))) {
                System.out.println("sound");
                if (musicOn) {
                    musicOn = false;
                    stopMusic();
                } else {
                    musicOn = true;
                    playMusic();
                }
            } else if ((mx > 195 && mx < 458) && (my > (340) && my < (405))) {
                System.out.println("Medium Level");
                mediumLevel = true;
                singlePlayer = false;
            } else if ((mx > 210 && mx < 489) && (my > (425) && my < (494))) {
                System.out.println("Easy Level");
                easyLevel = true;
                singlePlayer = false;
            }
        }
        if (easyLevel) {
            if ((mx > 613 && mx < 656) && (my > (616) && my < (651))) {
                if (!puase) {
                    System.out.println("return");
                    singlePlayer = true;
                    easyLevel = false;
                } else {
                    puase = false;
                }
            }
            if ((mx > 30 && mx < 75) && (my > (610) && my < (647))) {
                System.out.println("puase");
                puase = true;
            }
        }
        if (mediumLevel) {
            if ((mx > 613 && mx < 656) && (my > (616) && my < (651))) {
                if (!puase) {
                    System.out.println("return");
                    singlePlayer = true;
                    mediumLevel = false;
                } else {
                    puase = false;
                }
            }
            if ((mx > 30 && mx < 75) && (my > (610) && my < (647))) {
                System.out.println("puase");
                puase = true;
            }
        }
        if (hardlevel) {
            if ((mx > 613 && mx < 656) && (my > (616) && my < (651))) {
                if (!puase) {
                    System.out.println("return");
                    singlePlayer = true;
                    hardlevel = false;
                } else {
                    puase = false;
                }
            }
            if ((mx > 30 && mx < 75) && (my > (610) && my < (647))) {
                System.out.println("puase");
                puase = true;
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
            if (isKeyPressed(KeyEvent.VK_RIGHT) && x_Car_multi_two < 944 && !puase) {
                x_Car_multi_two += 5;
            }
            if (isKeyPressed(KeyEvent.VK_LEFT) && x_Car_multi_two > x / 2 && !puase) {
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
        if (mediumLevel) {
            if (isKeyPressed(KeyEvent.VK_LEFT) && redCarX > roadleftLine + 30) {
                redCarX -= readCarSpeedMediumLevel;
            }
            if (isKeyPressed(KeyEvent.VK_RIGHT) && redCarX + 15 < roadrightLine) {
                redCarX += readCarSpeedMediumLevel;
            }
            if (isKeyPressed(KeyEvent.VK_UP) && redCarY + 50 < y) {
                redCarY += readCarSpeedMediumLevel;
            }
            if (isKeyPressed(KeyEvent.VK_DOWN) && redCarY > 50) {
                redCarY -= readCarSpeedMediumLevel;
            }
        }
        if (easyLevel) {
            if (isKeyPressed(KeyEvent.VK_LEFT) && redCarX > roadleftLine + 30) {
                redCarX -= readCarSpeedEasyLevel;
            }
            if (isKeyPressed(KeyEvent.VK_RIGHT) && redCarX + 15 < roadrightLine) {
                redCarX += readCarSpeedEasyLevel;
            }
            if (isKeyPressed(KeyEvent.VK_UP) && redCarY + 50 < y) {
                redCarY += readCarSpeedEasyLevel;
            }
            if (isKeyPressed(KeyEvent.VK_DOWN) && redCarY > 50) {
                redCarY -= readCarSpeedEasyLevel;
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
        JFrame frme = new frame();
        frme.setLocationRelativeTo(null);
        frme.setVisible(true);
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
