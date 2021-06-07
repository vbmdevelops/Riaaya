package com.vbm.riaaya.data;
public class MedicalTest_Data {

    private String test_name_eng;
    private String file;
    private String id;

    public MedicalTest_Data(String test_name_eng,String file,String id) {

        this.test_name_eng = test_name_eng;
        this.file = file;
        this.id = id;
    }

    public String gettest_name_eng()
    {
        return test_name_eng;
    }
    public String getfile() {
        return file;
    }
    public String getid()
    {
        return id;
    }


}