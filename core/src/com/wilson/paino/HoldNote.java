package com.wilson.paino;
import java.util.ArrayList;

public class HoldNote extends Note
{
    private boolean key1Pressed;
    private boolean key2Pressed;
    private boolean key3Pressed;
    private boolean key4Pressed;
    private ArrayList<Boolean> keysPressed;
    private int countdown;
    
    public HoldNote()
    {
        key1Pressed=false;
        key2Pressed=false;
        key3Pressed=false;
        key4Pressed=false;
        keysPressed=new ArrayList<>();
        keysPressed.add(key1Pressed); keysPressed.add(key2Pressed); keysPressed.add(key3Pressed); keysPressed.add(key4Pressed);
        countdown=100;
    }

    public HoldNote(boolean k1, boolean k2, boolean k3, boolean k4)
    {
        key1Pressed=k1;
        key2Pressed=k2;
        key3Pressed=k3;
        key4Pressed=k4;
        keysPressed=new ArrayList<>();
        keysPressed.add(key1Pressed); keysPressed.add(key2Pressed); keysPressed.add(key3Pressed); keysPressed.add(key4Pressed);
        countdown=100;
    }

    public ArrayList<Boolean> getKeysList()
    {
        return keysPressed;
    }

    public boolean getKey1()
    {
        return key1Pressed;
    }

    public boolean getKey2()
    {
        return key2Pressed;
    }
    
    public boolean getKey3()
    {
        return key3Pressed;
    }

    public boolean getKey4()
    {
        return key4Pressed;
    }

    public int getPoints(ArrayList<Boolean> input)
    {
        if (key1Pressed==false&&key2Pressed==false&&key3Pressed==false&&key4Pressed==false)
        {
            return 0;
        }
        else
        {
            int sum=0;
            if (input.get(0)==true&&key1Pressed==true)
            {
                sum+=50;
            }
            if (input.get(1)==true&&key2Pressed==true)
            {
                sum+=50;
            }
            if (input.get(2)==true&&key3Pressed==true)
            {
                sum+=50;
            }
            if (input.get(3)==true&&key4Pressed==true)
            {
                sum+=50;
            }
            return sum;
        }
    }

    public int addCombo(ArrayList<Boolean> input) //0 means do nothing, 1 means add combo, 2 means drop combo
    {
        if (key1Pressed==false&&key2Pressed==false&&key3Pressed==false&&key4Pressed==false)
        {
            return 0;
        }
        else
        {
            if (input.get(0)==false&&key1Pressed==true)
            {
                return 2;
            }
            if (input.get(1)==false&&key2Pressed==true)
            {
                return 2;
            }
            if (input.get(2)==false&&key3Pressed==true)
            {
                return 2;
            }
            if (input.get(3)==false&&key4Pressed==true)
            {
                return 2;
            }
            return 1;
        }
    }   
}
