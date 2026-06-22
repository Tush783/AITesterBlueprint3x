package com.framework.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Integer id;
    private String  name;
    private String  username;
    private String  email;

    public User() {}

    public Integer getId()                  { return id; }
    public void    setId(Integer v)         { this.id = v; }

    public String  getName()                { return name; }
    public void    setName(String v)        { this.name = v; }

    public String  getUsername()            { return username; }
    public void    setUsername(String v)    { this.username = v; }

    public String  getEmail()               { return email; }
    public void    setEmail(String v)       { this.email = v; }
}
