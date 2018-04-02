package com.basic.solr.model;

import org.apache.solr.client.solrj.beans.Field;

import java.util.Arrays;

/**
 * locate com.basic.solr.model
 * Created by mastertj on 2018/4/2.
 */
public class User {
    @Field("id")
    private String id;
    @Field("user_name")
    private String name;

    @Field("user_age")
    private int age;

    @Field("user_sex")
    private String sex;

    @Field("user_like")
    private String[] likes;
    public User(String id, String name, int age, String sex ,String[] likes) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.likes=likes;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String[] getLikes() {

        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", likes=" + Arrays.toString(likes) +
                '}';
    }
}
