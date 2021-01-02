package com.nanb.wallpaper;

public class sliderclass {
    private String downloadurl;
    private String id;

    public sliderclass(String paramString1, String paramString2)
    {
        this.downloadurl = paramString1;
        this.id = paramString2;
    }

    public String getDownloadurl()
    {
        return this.downloadurl;
    }

    public String getId()
    {
        return this.id;
    }
}
