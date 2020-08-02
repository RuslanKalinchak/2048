package java;

import java.awt.*;

public class Tile {
    int value;

    public Tile() {
        this.value = 0;
    }

    public Tile(int value) {
        this.value = value;
    }

    public boolean isEmpty (){
        if (value==0){
            return  true;
        } else return false;
    }
    public Color getFontColor (){
        if (value<16){
            return new Color(0x776e65);
        } else return new Color(0xf9f6f2);
    }
    public Color getTileColor (){
        if (value==0){
            return new Color(0xcdc1b4);
        } else  if (value==2){
            return new Color(0xeee4da);
        } else  if (value==4){
            return new Color(0xede0c8);
        } else  if (value==8){
            return new Color(0xf2b179);
        } else  if (value==16){
            return new Color(0xf59563);
        } else  if (value==32){
            return new Color(0xf67c5f);
        } else  if (value==64){
            return new Color(0xf65e3b);
        } else  if (value==128){
            return new Color(0xedcf72);
        } else  if (value==256){
            return new Color(0xedcc61);
        } else  if (value==512){
            return new Color(0xedc850);
        } else  if (value==1024){
            return new Color(0xedc53f);
        } else  if (value==2048){
            return new Color(0xedc22e);
        }
        else return new Color(0xff0000);
    }
}

