package me.dm7.golive.sayac.sample;

import java.util.Map;

public class NumbersFeature {
    public int ClassId;
    public int left;
    public int right;
    public int top;
    public int bottom;
    public int center = (left + right)/2;
    public static Map<Integer, NumbersFeature> sortedMap;
}
