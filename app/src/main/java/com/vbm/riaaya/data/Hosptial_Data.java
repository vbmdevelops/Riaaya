package com.vbm.riaaya.data;

public class Hosptial_Data {
    private String hos_name;
    private String file;
    private String id;

    public Hosptial_Data(String hos_name,String file,String id) {

        this.hos_name = hos_name;
        this.file = file;
        this.id = id;
    }

    public String gethos_name()
    {
        return hos_name;
    }
    public String getfile() {
        return file;
    }
    public String getid()
    {
        return id;
    }


}
