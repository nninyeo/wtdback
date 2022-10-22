package com.example.wtd.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@ToString
@Setter
@Getter
public class FilterGetVO implements Serializable {

    private String userId;
    private List<String> yearList;
    private String years;
    private List<String> locationList;
    private String locations;
    private String limit;
    private String offset;


}

