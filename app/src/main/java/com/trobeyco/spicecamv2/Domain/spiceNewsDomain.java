package com.trobeyco.spicecamv2.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class spiceNewsDomain implements Parcelable {

    private String title;
    private String subtitle;
    private String picAddress;
//    private String isi_berita;
//    private String link_berita;


    public spiceNewsDomain(String title, String subtitle, String picAddress ) {
        this.title = title;
        this.subtitle = subtitle;
        this.picAddress = picAddress;

    }
    protected spiceNewsDomain(Parcel in){
        title = in.readString();
        subtitle = in.readString();
        picAddress = in.readString();
    }

    public static final Creator<spiceNewsDomain> CREATOR = new Creator<spiceNewsDomain>() {
        @Override
        public spiceNewsDomain createFromParcel(Parcel source) {
            return new spiceNewsDomain(source);
        }

        @Override
        public spiceNewsDomain[] newArray(int size) {
            return new spiceNewsDomain[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(picAddress);
    }
}
