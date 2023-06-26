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

public class DiskSchedulingFCFS {

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

        // Memanggil fungsi FCFS dan menghasilkan urutan track yang dilalui serta total overhead movement
        DiskSchedulingResult result = FCFS(headPosition, trackPositions);

        // Membuat dataset untuk grafik
        XYSeries series = new XYSeries("Track Order");
        for (int i = 0; i < result.trackOrder.size(); i++) {
            series.add(i, result.trackOrder.get(i));
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
        JFreeChart chart = new JFreeChart("Disk Scheduling - FCFS", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        // Mengatur tampilan grafik
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));

        // Menampilkan grafik
        JFrame frame = new JFrame("Disk Scheduling - FCFS");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
        
        // Menampilkan total overhead movement
        System.out.println("Total Overhead Movement: " + result.totalOverheadMovement);
    }

    public static DiskSchedulingResult FCFS(int headPosition, int[] trackPositions) {
        DiskSchedulingResult result = new DiskSchedulingResult();

        // Memasukkan track pada urutan sesuai dengan urutan kedatangan
        for (int trackPosition : trackPositions) {
            result.trackOrder.add(trackPosition);
        }

        // Menghitung total overhead movement
        int totalOverheadMovement = 0;
        for (int i = 1; i < result.trackOrder.size(); i++) {
            totalOverheadMovement += Math.abs(result.trackOrder.get(i) - result.trackOrder.get(i - 1));
        }
        result.totalOverheadMovement = totalOverheadMovement;

        return result;
    }

    static class DiskSchedulingResult {
        List<Integer> trackOrder;
        int totalOverheadMovement;

        public DiskSchedulingResult() {
            trackOrder = new ArrayList<>();
            totalOverheadMovement = 0;
        }
    }
}
