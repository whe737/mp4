package com.wilson.paino;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class MapInterpreter 
{
    private String mapName;
    private String BPM;
    private String difficulty;
    private String map;
    private int duration;
    private ArrayList<String> words=new ArrayList<>();
    private Note[][] notesList;
    private int[][] notesIntList;

    public MapInterpreter(String mp)
    {
        mapName=mp;
        FileHandle handle = Gdx.files.local("maps//"+mapName+".txt");
        String text = handle.readString();
        String wordsArray[] = text.split("\\r?\\n");
        for(String word : wordsArray) 
        {
            words.add(word);
        }
        BPM=words.get(0); //bpm is the first line
        difficulty=words.get(1); //difficulty is the 2nd line
        duration=Integer.parseInt(words.get(2)); //duration is the 3rd line
        notesList=new Note[duration*4][4];
        notesIntList=new int[words.get(3).length()/2+1][4];
        for (int i=0;i<4;i++)
        {
            int position=0;
            for (int j=0;j<words.get(3).length()-1;j++)
            {
                int temp=Integer.parseInt(words.get(i+3).substring(j,j+1));
                notesIntList[position][i]=temp;
                j++;
                position++;
            }
        }
        map="";
        for (String m:wordsArray)
        {
            map+=m;
            map+="\n";
        }
    }

    public void generate2DString()
    {
        String bruh="";
        for (int i=0;i<4;i++)
        {
            bruh+="{";
            for (int j=0;j<words.get(3).length();j++)
            {
                bruh+=words.get(i+3).substring(j,j+1);
                if (j!=words.get(3).length()-1)
                {
                    bruh+=",";
                }
                j++;
            }
            bruh+="}\n";
        }
        System.out.println(bruh);
    }

    public String getMapName()
    {
        return mapName;
    }

    public String getBPM()
    {
        return BPM;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public int getDurationint()
    {
        return duration;
    }
    
    public String getMap()
    {
        return map;
    }

    public Note[][] getNotesList()
    {
        return notesList;
    }
    public int[][] getNotesIntList()
    {
        return notesIntList;
    }
}
