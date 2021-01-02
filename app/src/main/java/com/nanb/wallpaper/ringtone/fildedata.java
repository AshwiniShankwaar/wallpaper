package com.nanb.wallpaper.ringtone;

public class fildedata {
    private String id,Type,Tag,size,DisplayName,Total_downloads;

    public fildedata(String id, String type, String tag, String size, String displayName, String total_downloads) {
        this.id = id;
        this.Type = type;
        this.Tag = tag;
        this.size = size;
        this.DisplayName = displayName;
        this.Total_downloads = total_downloads;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return Type;
    }

    public String getTag() {
        return Tag;
    }

    public String getSize() {
        return size;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getTotal_downloads() {
        return Total_downloads;
    }
}
