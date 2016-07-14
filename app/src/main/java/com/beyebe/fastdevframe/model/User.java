package com.beyebe.fastdevframe.model;

import io.realm.RealmObject;

/**
 * Created by Kratos on 2016/1/27.
 */
public class User extends RealmObject {

    private String name;
    private int age;

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
}
