package com.example.wtd.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
public class DetailDataVO {
    private Object jobList;

    @ToString
    @Getter
    @Setter
    public static class JobDepth {
        private Object address;
        private Object company;
        private Object title_img;
        private Object detail;

        private String status;
        private String position;
        private String due_time;
    }


    @Getter
    @Setter
    public static class DetailData { //depth 올라감
        private String requirements;
        private String main_tasks;
        private String intro;
        private String benefits;
        private String preferred_points;

    }

    @Getter
    @Setter
    public static class AddressData { //depth 올라감
        private String country;
        private String full_location;
        private Map<String, Object> geo_location; //depth 두번올라감
    }

    @Getter
    @Setter
    public static class CompanyData { //depth 올라감
        private String name;
    }

    @Getter
    @Setter
    public static class TitleImgData { //depth 올라감
        private String thumb;
    }

    @Getter
    @Setter
    public static class location { //depth 올라감
        private String lat;
        private String lng;
    }

}