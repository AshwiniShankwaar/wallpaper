package com.nanb.wallpaper;

public class itemclass {
    private String Type,DisplayName,downloadurl;

    public itemclass(String type, String displayName, String downloadurl) {
        this.Type = type;
        this.DisplayName = displayName;
        this.downloadurl = downloadurl;
    }

    public String getType() {
        return Type;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getDownloadurl() {
        return downloadurl;
    }
}
