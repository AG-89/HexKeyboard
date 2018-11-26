package edu.pnw.ece354.hexkeyboard.javafiles;

import java.io.File;
import java.io.IOException;
import android.media.*;

//play monophonic sound (single channel) with AudioSystem clips
//rewrite for android media
/

public class HKAudio
{
    public static void main(String[] args)
    {
        Clip clip;

        AudioInputStream audioInputStream;
        String filePath = "C4.wav";

        float Fsratio = (float)8.0;

        try
        {
            // create AudioInputStream object
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            // create clip reference
            clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            //clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start(); //play
        }
        catch(Exception e)
        {
            System.out.println("something happened.");
        }
    }
}
