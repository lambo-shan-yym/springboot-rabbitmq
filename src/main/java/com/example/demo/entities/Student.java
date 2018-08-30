package com.example.demo.entities;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yym
 * @create create by yym 2018/8/29 0029 14:30
 * @desc
 **/
@Data
public class Student {


    private Integer id;
    private String name;
    private Integer age;
    private Timestamp createTime;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                '}';
    }
}
