package com.example.user.cowaplication;

/**
 * Created by user on 2015-02-12.
 */
//소의 기본 정보를 리스트뷰에 출력해 주기 위한 클래스 이다.

public class listdata {
    public String location;
    public String sex;
    public String birthday;
    public String number;

    public listdata()
    {
        location = "";
        number = "";
        sex  = "";
        birthday  = "";
    }
    public listdata(String str_location, String str_number, String str_birthday, String str_sex)
    {
        location = str_location;
        number = str_number;
        birthday  = str_birthday;
        sex  = str_sex;
    }
}
