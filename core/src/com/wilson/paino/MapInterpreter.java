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

    public MapInterpreter(String mp)
    {
        mapName=mp;
        FileHandle handle = Gdx.files.local("maps/"+mapName+".txt");
        String text = handle.readString();
        String wordsArray[] = text.split("\\r?\\n");
        for(String word : wordsArray) 
        {
            words.add(word);
        }
        BPM=words.get(0);
        difficulty=words.get(1);
        duration=Integer.parseInt(words.get(2));
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
}
