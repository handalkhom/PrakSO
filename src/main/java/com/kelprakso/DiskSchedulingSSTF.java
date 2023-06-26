package com.kelprakso;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;

import javax.swing.*;

public class DiskSchedulingSSTF {

    public static void main(String[] args) {
        // Memasukkan posisi awal head disk dari input pengguna
        int headPosition = Integer.parseInt(JOptionPane.showInputDialog("Masukkan posisi awal head disk:"));

        // Memasukkan posisi track yang akan diakses dari input pengguna
        String trackPositionsStr = JOptionPane.showInputDialog("Masukkan posisi track yang akan diakses (pisahkan dengan koma):");
        String[] trackPositionsArr = trackPositionsStr.split(",");
        int[] trackPositions = new int[trackPositionsArr.length];
        for (int i = 0; i < trackPositionsArr.length; i++) {
            trackPositions[i] = Integer.parseInt(trackPositionsArr[i].trim());
        }

        // Memanggil fungsi SSTF dan menghasilkan urutan track yang dilalui
        List<Integer> trackOrder = SSTF(headPosition, trackPositions);

        // Membuat dataset untuk grafik
        XYSeries series = new XYSeries("Track Order");
        for (int i = 0; i < trackOrder.size(); i++) {
            series.add(i, trackOrder.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Membuat plot
        XYPlot plot = new XYPlot();
        plot.setDataset(dataset);

        // Mengatur renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        // Mengatur sumbu X dan sumbu Y
        NumberAxis xAxis = new NumberAxis("Step");
        NumberAxis yAxis = new NumberAxis("Track");
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        // Membuat grafik
        JFreeChart chart = new JFreeChart("Disk Scheduling - SSTF", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        // Mengatur tampilan grafik
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));

        // Menampilkan grafik
        JFrame frame = new JFrame("Disk Scheduling - SSTF");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static List<Integer> SSTF(int headPosition, int[] trackPositions) {
        List<Integer> trackOrder = new ArrayList<>();
        List<Integer> remainingTracks = new ArrayList<>();
        for (int track : trackPositions) {
            remainingTracks.add(track);
        }

        while (!remainingTracks.isEmpty()) {
            int closestTrackIndex = 0;
            int minDistance = Math.abs(remainingTracks.get(0) - headPosition);

            // Mencari track terdekat dari posisi head
            for (int i = 1; i < remainingTracks.size(); i++) {
                int distance = Math.abs(remainingTracks.get(i) - headPosition);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestTrackIndex = i;
                }
            }

            // Menambahkan track terdekat ke urutan track yang dilalui
            int closestTrack = remainingTracks.remove(closestTrackIndex);
            trackOrder.add(closestTrack);

            // Memperbarui posisi head
            headPosition = closestTrack;
        }

        return trackOrder;
    }
}
