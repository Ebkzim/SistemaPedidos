package com.example.orderservice.dto;

import com.example.orderservice.model.Product;

import java.util.List;

public class RestPageImpl<T> {

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;

    public RestPageImpl() {
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
