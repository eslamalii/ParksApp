package com.example.parksapp.data;

import com.example.parksapp.model.Park;

import java.util.List;

public interface AsyncResponse {
    void processPark(List<Park> parks);
}
