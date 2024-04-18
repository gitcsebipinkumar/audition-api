package com.audition.model;


import lombok.Data;

@Data
public class AuditionComment {

    private int id;
    private String name;
    private String email;
    private String body;

}