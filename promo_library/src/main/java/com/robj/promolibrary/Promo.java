package com.robj.promolibrary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.TimeUnit;

/**
 * Created by jj on 19/10/17.
 */
public class Promo implements Parcelable {

    public final String id;
    public final String title;
    public final String body;
    public final long dateReceived;
    public final int timeToLive; //in hours
    public final int code;
    public final String theme;

    Promo(String id, String title, String body, long dateReceived, int timeToLive, int code, String theme) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.dateReceived = dateReceived;
        this.timeToLive = timeToLive;
        this.code = code;
        this.theme = theme;
    }

    protected Promo(Parcel in) {
        id = in.readString();
        title = in.readString();
        body = in.readString();
        dateReceived = in.readLong();
        timeToLive = in.readInt();
        code = in.readInt();
        theme = in.readString();
    }

    public static final Creator<Promo> CREATOR = new Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel in) {
            return new Promo(in);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeLong(dateReceived);
        dest.writeInt(timeToLive);
        dest.writeInt(code);
        dest.writeString(theme);
    }

    public boolean isExpired() {
        long now = System.currentTimeMillis();
        long diff = now - dateReceived;
        long hoursBetween = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
        return hoursBetween > timeToLive;
    }

    public enum Key {
        ID(true), OFFER_CODE(true), TTL(true), THEME(false);
        public final boolean isRequired;

        Key(boolean isRequired) {
            this.isRequired = isRequired;
        }

        public String value() {
            return name().toLowerCase();
        }
    }

}
