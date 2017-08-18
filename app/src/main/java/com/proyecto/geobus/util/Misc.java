package com.proyecto.geobus.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Misc {

    public static String leyendaMinutosSegundos(float seg) {
        int minutos = 0;
        int segundos = 0;
        if(seg >= 60){
            minutos = (int) (seg/60);
            segundos = (int) (seg- minutos*60);
        } else {
            segundos = (int) seg;
        }
        return minutos+" min. "+segundos+" seg.";
    }

    public static int convertirAKMPorSegundo(double metrosXSegundo) {
        return (int) ((metrosXSegundo*3600)/1000);
    }

    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
