package com.zalpha.FantasyNetwork.PvP.util;

import java.util.ArrayList;
import java.util.List;

public class SimpleScroller
{
    protected int position;
    protected List<String> frames;
    
    public SimpleScroller(final String texto, String corPrimaria, String corSecundaria, final int espaco) {
        this.position = 0;
        this.frames = new ArrayList<String>();
        int substring = 0;
        corPrimaria = corPrimaria.replace("&", "§");
        corSecundaria = corSecundaria.replace("&", "§");
        if (substring + espaco <= texto.length()) {
            while (substring + espaco <= texto.length()) {
                this.frames.add(String.valueOf(corPrimaria) + texto.substring(0, substring += espaco) + corSecundaria + texto.substring(substring));
            }
        }
        substring = 0;
        if (substring + espaco <= texto.length()) {
            while (substring + espaco <= texto.length()) {
                this.frames.add(String.valueOf(corSecundaria) + texto.substring(0, substring += espaco) + corPrimaria + texto.substring(substring));
            }
        }
        this.frames.add(String.valueOf(corPrimaria) + corPrimaria + "KITPVP");
        this.frames.add(String.valueOf(corSecundaria) + corSecundaria + "KITPVP");
        this.frames.add(String.valueOf(corPrimaria) + corPrimaria + "KITPVP");
        this.frames.add(String.valueOf(corSecundaria) + corSecundaria + "KITPVP");
    }
    
    public String next() {
        if (this.position == this.frames.size()) {
            this.position = 0;
        }
        return this.frames.get(this.position++);
    }
}
