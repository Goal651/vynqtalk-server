package com.vynqtalk.server.service;

import com.sun.management.OperatingSystemMXBean;
import com.vynqtalk.server.dto.SystemMetric;

import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemMetricsService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public List<SystemMetric> getSystemMetrics() {
        List<SystemMetric> metrics = new ArrayList<>();

        // CPU Usage
        double cpuLoad = osBean.getCpuLoad() * 100;
        String cpuStatus = cpuLoad < 60 ? "good" : cpuLoad < 80 ? "warning" : "critical";
        metrics.add(new SystemMetric("CPU Usage", DECIMAL_FORMAT.format(cpuLoad) + "%", cpuStatus));

        // Memory Usage
        long totalMemory = osBean.getTotalMemorySize();
        long freeMemory = osBean.getFreeMemorySize();
        double memoryUsage = ((double) (totalMemory - freeMemory) / totalMemory) * 100;
        String memoryStatus = memoryUsage < 60 ? "good" : memoryUsage < 80 ? "warning" : "critical";
        metrics.add(new SystemMetric("Memory Usage", DECIMAL_FORMAT.format(memoryUsage) + "%", memoryStatus));

        // Disk Usage
        try {
            FileStore fileStore = Files.getFileStore(Paths.get("/"));
            long totalSpace = fileStore.getTotalSpace();
            long usableSpace = fileStore.getUsableSpace();
            double diskUsage = ((double) (totalSpace - usableSpace) / totalSpace) * 100;
            String diskStatus = diskUsage < 60 ? "good" : diskUsage < 80 ? "warning" : "critical";

            metrics.add(new SystemMetric("Disk Usage", DECIMAL_FORMAT.format(diskUsage) + "%", diskStatus));
        } catch (Exception e) {
            System.err.println("Error retrieving disk usage: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            metrics.add(new SystemMetric("Disk Usage", "N/A", "critical"));
        }
        return metrics;
    }
}