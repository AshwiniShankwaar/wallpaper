package com.nanb.wallpaper;

public class commentmodelclass {
    private String user_id,comment;

    public commentmodelclass(String user_id, String comment) {
        this.user_id = user_id;
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getComment() {
        return comment;
    }
}
