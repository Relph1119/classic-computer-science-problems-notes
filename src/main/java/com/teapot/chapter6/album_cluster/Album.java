package com.teapot.chapter6.album_cluster;

import com.teapot.chapter6.kmeans.DataPoint;
import com.teapot.chapter6.kmeans.KMeans;

import java.util.ArrayList;
import java.util.List;

/**
 * P125~126
 * 按从专辑的时间长度（以分钟为单位）和歌曲数量对迈克尔·杰克逊的专辑进行聚类
 */
public class Album extends DataPoint {
    private String name;
    private int year;

    public Album(String name, int year, double length, double tracks) {
        // 专辑的时间长度（以分钟为单位）和歌曲数量
        super(List.of(length, tracks));
        this.name = name;
        this.year = year;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + year + ")";
    }

    public static void main(String[] args) {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("Got to Be There", 1972, 35.45, 10));
        albums.add(new Album("Ben", 1972, 31.31, 10));
        albums.add(new Album("Music & Me", 1973, 32.09, 10));
        albums.add(new Album("Forever, Michael", 1975, 33.36, 10));
        albums.add(new Album("Off the Wall", 1979, 42.28, 10));
        albums.add(new Album("Thriller", 1982, 42.19, 9));
        albums.add(new Album("Bad", 1987, 48.16, 10));
        albums.add(new Album("Dangerous", 1991, 77.03, 14));
        albums.add(new Album("HIStory: Past, Present and Future, Book I", 1995, 148.58, 30));
        albums.add(new Album("Invincible", 2001, 77.05, 16));
        KMeans<Album> kmeans = new KMeans<>(2, albums);
        List<KMeans<Album>.Cluster> clusters = kmeans.run(100);
        for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
            System.out.printf("Cluster %d Avg Length %f Avg Tracks %f: %s%n",
                    clusterIndex, clusters.get(clusterIndex).centroid.dimensions.get(0),
                    clusters.get(clusterIndex).centroid.dimensions.get(1),
                    clusters.get(clusterIndex).points);
        }
    }
}
