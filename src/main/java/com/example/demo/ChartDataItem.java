package com.example.demo;

public class ChartDataItem {
    private String category;
    private String series;
    private Number value;

    public ChartDataItem(String category, String series, Number value) {
        this.category = category;
        this.series = series;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public String getSeries() {
        return series;
    }

    public Number getValue() {
        return value;
    }
}
