package com.github.mzule.activityrouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author CPPAlien
 */
public class PojoData implements Parcelable {
    String name;
    int age;

    public PojoData(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    protected PojoData(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<PojoData> CREATOR = new Creator<PojoData>() {
        @Override
        public PojoData createFromParcel(Parcel in) {
            return new PojoData(in);
        }

        @Override
        public PojoData[] newArray(int size) {
            return new PojoData[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(name);
        parcel.writeInt(age);
    }
}
