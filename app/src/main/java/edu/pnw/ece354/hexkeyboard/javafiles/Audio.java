package edu.pnw.ece354.hexkeyboard.javafiles;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.PlaybackParams;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Audio {

    public static void playSound(String filename, Context context, double Rate){

        // define the buffer size for audio track
        int Fs_default = 44100;
        int Fs = (int)Math.round(Fs_default * Rate); //samplerate
        int minBufferSize = AudioTrack.getMinBufferSize(Fs, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        int bufferSize = 512;
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, Fs, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);

        int count = 0;
        byte[] data = new byte[bufferSize];
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(filename);
            FileInputStream fileInputStream = afd.createInputStream();
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            audioTrack.play();

            while((count = dataInputStream.read(data, 0, bufferSize)) > -1){
                audioTrack.write(data, 0, count);
            }
            audioTrack.stop();
            audioTrack.release();
            dataInputStream.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void playSound(Context context, String fileName) {
        MediaPlayer  mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            //PlaybackParams PbP = mediaPlayer.getPlaybackParams();
            //mediaPlayer.setPlaybackParams()
            mediaPlayer.set
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }*/
}
