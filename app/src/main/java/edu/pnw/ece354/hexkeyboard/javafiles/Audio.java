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

    /**
     * Internal class to hold audio file information like a struct
     */
    private static class HKAudioFile
    {
        private String fileName;
        private int noteIndex;
        private double frequency;
        private String instrument;

        public HKAudioFile(String name, int ni, double f, String i)
        {
            fileName = name;
            noteIndex = ni;
            frequency = f;
            instrument = i;
        }
        public String getFileName()
        {
            return fileName;
        }
        public int getNoteIndex()
        {
            return noteIndex;
        }
        public double getFrequency()
        {
            return frequency;
        }
        public String getInstrument()
        {
            return instrument;
        }
    }

    /**
     * Play a sound file from assets folder.
     * @param file_and_rate HKAudioFile that contains the filename and the samplerate modifier
     * @param context Activity context to be passed
     */
    public static void playSound(HKAudioFile file_and_rate, Context context){

        double Rate = file_and_rate.getFrequency(); //actually the Fs rate, not frequency
        String filename = file_and_rate.getFileName();

        int Fs_default = 44100;
        int Fs = (int)Math.round(Fs_default * Rate); //samplerate
        int minBufferSize = AudioTrack.getMinBufferSize(Fs, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        int bufferSize = 512;
        //using AudioTrack allows samplerate change without going up an API level
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

    //make the files, may be temporary code idk
    public static HKAudioFile[] generateFileList()
    {
        final double C4 = 440.0 * Math.pow(2.0,(-9.0 / 12.0));

        HKAudioFile[] files = new HKAudioFile[8];
        for(int x = 0; x < 8; x++)
        {
            //the samples I used are an octave number lower
            int xa = x-3; //adjusted x starting at -3
            //.WAV is case-sensitive...
            files[x] = new HKAudioFile("C" + Integer.toString(x+2) + ".WAV", 12*xa, C4 * Math.pow(2.0,(double)xa), "Piano");
        }
        return files;
    }

    //get HKAudioFile of closest note index
    public static HKAudioFile getHKAudioFileFromNI(int ni, MusicScale scale, double A4)
    {
        HKAudioFile[] files = generateFileList(); //get the asset file information

        double f = scale.noteIndexPitch(ni,A4); //get input frequency

        //files into separate arrays
        //String[] filenames = new String[files.length];
        double[] frequencies = new double[files.length];
        int[] nis = new int[files.length];
        for(int x = 0; x < files.length; x++)
        {
            //filenames[x] = files[x].getFileName();
            frequencies[x] = files[x].getFrequency();
            nis[x] =  files[x].getNoteIndex();
        }

        int closestx = 0;
        int closetsy = Integer.MAX_VALUE;
        double closestf;
        double Fs_mod = 1.0; //default 1.0 (no modifier)

        for(int x = 0; x < files.length; x++) //find closest note index
        {
            int diff = nis[x] - ni;
            if(diff < 0) //abs()
            {
                diff = -diff;
            }
            if(diff < closetsy)
            {
                closetsy = diff;
                closestx = x;
            }
        }
        closestf = frequencies[closestx];
        Fs_mod = f/closestf;
        //note: frequency part used as Fs_mod here, not a frequency
        return new HKAudioFile(files[closestx].getFileName(),files[closestx].getNoteIndex(),Fs_mod,files[closestx].getInstrument());
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
