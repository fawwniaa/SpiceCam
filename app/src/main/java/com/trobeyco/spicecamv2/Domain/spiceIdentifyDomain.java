package com.trobeyco.spicecamv2.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class spiceIdentifyDomain implements Parcelable {

    private String title;
    private String subtitle;
    private String picAddress;

    public spiceIdentifyDomain(String title, String subtitle, String picAddress) {
        this.title = title;
        this.subtitle = subtitle;
        this.picAddress = picAddress;
    }

    protected spiceIdentifyDomain(Parcel in){
        title = in.readString();
        subtitle = in.readString();
        picAddress = in.readString();
    }

    public static final Parcelable.Creator<spiceIdentifyDomain> CREATOR = new Parcelable.Creator<spiceIdentifyDomain>() {
        @Override
        public spiceIdentifyDomain createFromParcel(Parcel source) {
            return new spiceIdentifyDomain(source);
        }

        @Override
        public spiceIdentifyDomain[] newArray(int size) {
            return new spiceIdentifyDomain[size];
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
