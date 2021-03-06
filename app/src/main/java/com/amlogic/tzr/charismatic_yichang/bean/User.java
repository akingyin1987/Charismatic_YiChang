package com.amlogic.tzr.charismatic_yichang.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/7/11.
 */
public class User extends BmobUser {
    private String nick;
    private boolean sex;
    private BmobFile head_thumb;
    private String address;
    private String introduce;

    public boolean isSex() {
        return sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public BmobFile getHead_thumb() {
        return head_thumb;
    }

    public void setHead_thumb(BmobFile head_thumb) {
        this.head_thumb = head_thumb;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='"  + '\'' +
                ", nick='" + nick + '\'' +
                ", sex=" + sex +
                ", head_thumb=" + head_thumb +
                ",user_name="+
                '}';
    }
}
