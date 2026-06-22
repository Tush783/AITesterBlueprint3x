package com.framework.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    private Integer userId;
    private Integer id;
    private String title;
    private String body;

    public Post() {}

    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title  = title;
        this.body   = body;
    }

    public Integer getUserId()              { return userId; }
    public void    setUserId(Integer v)     { this.userId = v; }

    public Integer getId()                  { return id; }
    public void    setId(Integer v)         { this.id = v; }

    public String  getTitle()               { return title; }
    public void    setTitle(String v)       { this.title = v; }

    public String  getBody()                { return body; }
    public void    setBody(String v)        { this.body = v; }
}
