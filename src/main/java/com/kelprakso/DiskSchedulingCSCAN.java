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

public class DiskSchedulingCSCAN {

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

        // Memanggil fungsi CSCAN dan menghasilkan urutan track yang dilalui serta total overhead movement
        DiskSchedulingResult result = CSCAN(headPosition, trackPositions);

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
        JFreeChart chart = new JFreeChart("Disk Scheduling - CSCAN", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        // Mengatur tampilan grafik
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));

        // Menampilkan grafik
        JFrame frame = new JFrame("Disk Scheduling - CSCAN");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);

        // Menampilkan total overhead movement
        System.out.println("Total Overhead Movement: " + result.totalOverheadMovement);
    }

    public static DiskSchedulingResult CSCAN(int headPosition, int[] trackPositions) {
        DiskSchedulingResult result = new DiskSchedulingResult();

        // Mengurutkan posisi track secara ascending
        Arrays.sort(trackPositions);

        // Menentukan indeks track yang akan diakses pertama kali
        int firstIndex = -1;
        for (int i = 0; i < trackPositions.length; i++) {
            if (trackPositions[i] >= headPosition) {
                firstIndex = i;
                break;
            }
        }

        // Memasukkan track pada sisi yang sama dengan head dalam arah SCAN
        for (int i = firstIndex; i < trackPositions.length; i++) {
            result.trackOrder.add(trackPositions[i]);
        }

        // Memasukkan track pada sisi yang berlawanan dengan head dalam arah SCAN
        for (int i = 0; i < firstIndex; i++) {
            result.trackOrder.add(trackPositions[i]);
        }

        // Menghitung total overhead movement
        int totalOverheadMovement = 0;
        int currentTrack = headPosition;
        for (int i = 0; i < result.trackOrder.size(); i++) {
            int nextTrack = result.trackOrder.get(i);
            totalOverheadMovement += Math.abs(nextTrack - currentTrack);
            currentTrack = nextTrack;
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
