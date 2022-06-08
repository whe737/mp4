package com.wilson.paino;

import java.security.Key;
import java.sql.Time;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.TimeUtils;

public class InputReader implements InputProcessor{

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Keys.ESCAPE:
                if (GameScreen.gameState==0) GameScreen.gameState=1;
                break;
            case Keys.D:
                System.out.println("D was pressed");
                GameScreen.box1Active=true;
                long lastPressedD=TimeUtils.nanoTime();
                if (GameScreen.box1Active&&TimeUtils.nanoTime()-lastPressedD>500)
                {
                    GameScreen.box1Active=false;
                }
                break;
            case Keys.F:
                System.out.println("F was pressed");
                GameScreen.box2Active=true;
                long lastPressedF=TimeUtils.nanoTime();
                if (GameScreen.box2Active&&TimeUtils.nanoTime()-lastPressedF>500)
                {
                    GameScreen.box2Active=false;
                }
                break;
            case Keys.J:
                System.out.println("J was pressed");
                GameScreen.box3Active=true;
                long lastPressedJ=TimeUtils.nanoTime();
                if (GameScreen.box3Active&&TimeUtils.nanoTime()-lastPressedJ>500)
                {
                   GameScreen.box3Active=false;
                }
                break;
            case Keys.K:
                System.out.println("K was pressed");
                GameScreen.box4Active=true;
                long lastPressedK=TimeUtils.nanoTime();
                if (GameScreen.box4Active&&TimeUtils.nanoTime()-lastPressedK>500)
                {
                    GameScreen.box4Active=false;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode)
        {
            case Keys.D:
                //System.out.println("D was let go of");
                GameScreen.box1Active=false;
                break;
            case Keys.F:
                GameScreen.box2Active=false;
                break;
            case Keys.J:
                GameScreen.box3Active=false;
                break;
            case Keys.K:
                GameScreen.box4Active=false;
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
