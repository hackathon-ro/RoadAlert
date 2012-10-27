
package com.kaciula.utils.net;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceReturnInfo implements Parcelable {

    public String errorMessage;

    public boolean success;

    public ServiceReturnInfo() {
    }

    public ServiceReturnInfo(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    private void readFromParcel(Parcel in) {
    }

    /**
     * This field is needed for Android to be able to create new objects,
     * individually or as arrays. This also means that you can use use the
     * default constructor to create the object and use another method to
     * hyrdate it as necessary. I just find it easier to use the constructor. It
     * makes sense for the way my brain thinks ;-)
     */
    public static final Parcelable.Creator<ServiceReturnInfo> CREATOR = new Parcelable.Creator<ServiceReturnInfo>() {
        public ServiceReturnInfo createFromParcel(Parcel in) {
            return new ServiceReturnInfo(in);
        }

        public ServiceReturnInfo[] newArray(int size) {
            return new ServiceReturnInfo[size];
        }
    };
}
