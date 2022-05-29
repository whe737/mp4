package com.wilson.paino;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        BPM=words.get(0);
        difficulty=words.get(1);
        duration=Integer.parseInt(words.get(2));
        notesList=new Note[duration*4][4];
        for (int i=0;i<duration*4;i++)
        {
            for (int j=0;j<4;j++)
            {
                int temp=Integer.parseInt(words.get(j+3).substring(0,1));
                words.get(j+3).substring(2);
                notesList[i][j]=new Note(temp);
            }
        }
        map="";
        for (String m:wordsArray)
        {
            map+=m;
            map+="\n";
        }
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
}
