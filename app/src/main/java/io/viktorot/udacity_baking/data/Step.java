package io.viktorot.udacity_baking.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

public class Step implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("description")
    public String description;

    @SerializedName("shortDescription")
    public String shortDescription;

    @SerializedName("videoURL")
    @Nullable
    public String videoURL;

    @SerializedName("thumbnailURL")
    public String thumbnailURL;

    @Override
    public int describeContents() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.description);
        dest.writeString(this.shortDescription);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnailURL);
    }

    public Step() {
    }

    protected Step(Parcel in) {
        this.id = in.readInt();
        this.description = in.readString();
        this.shortDescription = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
