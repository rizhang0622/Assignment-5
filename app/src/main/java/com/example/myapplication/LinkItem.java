package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class LinkItem implements Parcelable {
    private String name;
    private String url;

    // Constructor
    public LinkItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    protected LinkItem(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<LinkItem> CREATOR = new Creator<LinkItem>() {
        @Override
        public LinkItem createFromParcel(Parcel in) {
            return new LinkItem(in);
        }

        @Override
        public LinkItem[] newArray(int size) {
            return new LinkItem[size];
        }
    };

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
