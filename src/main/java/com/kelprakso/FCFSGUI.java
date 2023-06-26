package com.kelprakso;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import java.awt.*;

public class FCFSGUI {
    public static void main(String[] args) {
        // Menampilkan dialog untuk menginput jumlah request
        String totalRequestsString = JOptionPane.showInputDialog("Enter the total number of requests:");
        int totalRequests = Integer.parseInt(totalRequestsString);

        // Membuat array untuk menyimpan request
        int[] requests = new int[totalRequests];

        // Menginput request dari user
        for (int i = 0; i < totalRequests; i++) {
            String requestString = JOptionPane.showInputDialog("Request " + (i + 1) + ":");
            int request = Integer.parseInt(requestString);
            requests[i] = request;
        }

        // Menginput initial position dari user
        String initialPositionString = JOptionPane.showInputDialog("Enter the initial position:");
        int currentPosition = Integer.parseInt(initialPositionString);

        // Menghitung total seek time
        int totalSeekTime = 0;

        // Membuat dataset untuk grafik
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Menambahkan data ke dataset dan menghitung seek time
        for (int i = 0; i < requests.length; i++) {
            int request = requests[i];
            int seekTime = Math.abs(request - currentPosition);
            totalSeekTime += seekTime;
            currentPosition = request;

            // Menambahkan data ke dataset
            dataset.addValue(seekTime, "Seek Time", Integer.toString(request));
        }

        // Membuat grafik menggunakan dataset
        JFreeChart chart = ChartFactory.createBarChart("Disk Scheduling - First-Come, First-Served (FCFS)",
                "Disk Request", "Seek Time", dataset, PlotOrientation.VERTICAL, false, true, false);

        // Menampilkan grafik dalam frame
        ChartFrame frame = new ChartFrame("Disk Scheduling FCFS", chart);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menampilkan total seek time dan average seek time
        JOptionPane.showMessageDialog(null,
                "Total Seek Time = " + totalSeekTime + "\nAverage Seek Time = " + (double) totalSeekTime / requests.length);

    }
}