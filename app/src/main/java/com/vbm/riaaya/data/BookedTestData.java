package com.vbm.riaaya.data;

public class BookedTestData {
    private String booking_no;
    private String p_name;
    private String id;
    private String test_name;
    private String book_date;
    private String status;

    private String hospital_name;
    private String datetime;
    private String hospital_address_eng;
    private String p_age;
    private String p_gender;
    private String phone;
    private String email;
    private String address;
    private String test_img;



    public BookedTestData(String booking_no,String p_name,String id,String test_name,String book_date,String status,String hospital_name,String datetime,String hospital_address_eng,
                          String p_age,String p_gender,String phone,String email,String address,String test_img) {

        this.booking_no = booking_no;
        this.p_name = p_name;
        this.id = id;
        this.test_name = test_name;
        this.book_date = book_date;
        this.status = status;
        this.hospital_name = hospital_name;
        this.datetime = datetime;
        this.hospital_address_eng = hospital_address_eng;
        this.p_age = p_age;
        this.p_gender = p_gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.test_img = test_img;
    }

    public String getbooking_no()
    {
        return booking_no;
    }
    public String getp_name() {
        return p_name;
    }
    public String getid()
    {
        return id;
    }

    public String gettest_name()
    {
        return test_name;
    }
    public String getbook_date()
    {
        return book_date;
    }
    public String getstatus()
    {
        return status;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getHospital_address_eng() {
        return hospital_address_eng;
    }

    public String getP_age() {
        return p_age;
    }

    public String getP_gender() {
        return p_gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getTest_img() {
        return test_img;
    }

}
