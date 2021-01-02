package com.nanb.wallpaper.fonts;

public class fontsmodelclass {
    private String Type, DisplayName, downloadurl,backgroundurl;

    public fontsmodelclass(String type, String displayName, String downloadurl, String backgroundurl) {
        this.Type = type;
        this.DisplayName = displayName;
        this.downloadurl = downloadurl;
        this.backgroundurl = backgroundurl;
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

    public String getBackgroundurl() {
        return backgroundurl;
    }
}
