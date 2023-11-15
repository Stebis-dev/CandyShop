package com.CandyShop.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>> {
    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Pair<K, V> other) {
        int firstCompare = this.first.compareTo(other.first);
        if (firstCompare != 0) {
            return firstCompare;
        }
        return this.second.compareTo(other.second);
    }
}