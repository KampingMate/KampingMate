package com.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedData {
    private List<String> doNm;
    private List<String> gungu;
    private List<String> faclt;
    private List<String> lct;
    private List<String> induty;
    private List<String> bottom;
    private List<String> sbrs;
    private int page;
}
