package com.robj.promolibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Calendar;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by jj on 19/10/17.
 */

public class PromoManager {

    private static final String TAG = PromoManager.class.getSimpleName();

    private static final String PROMO_ID = "promoId";
    private static final String PROMO_TITLE = "promoTitle";
    private static final String PROMO_BODY = "promoBody";
    private static final String PROMO_RECEIVED = "PROMO_RECEIVED";
    private static final String PROMO_TTL = "promoTtl";
    private static final String PROMO_CODE = "promoCode";
    private static final String PROMO_THEME = "promoTheme";

    public static Promo parsePromo(String title, String body, Map<String, String> data) {
        for(Promo.Key key : Promo.Key.values()) {
            if(key.isRequired && !data.containsKey(key.value())) {
                Log.e(TAG, "Invalid promo, " + key.value() + " is missing..");
                return null;
            }
        }

        int code, ttl;
        try {
            code = Integer.parseInt(data.get(Promo.Key.OFFER_CODE.value()));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid promo, " + Promo.Key.OFFER_CODE.value() + " needs to be an int..");
            return null;
        }
        try {
            ttl = Integer.parseInt(data.get(Promo.Key.TTL.value()));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid promo, " + Promo.Key.TTL.value() + " needs to be an int..");
            return null;
        }
        String id = data.get(Promo.Key.ID.value());
        String theme = data.get(Promo.Key.THEME.value());
        return new Promo(id, title, body, Calendar.getInstance().getTimeInMillis(), ttl, code, theme);
    }

    private static Promo getPromo(Context context) {
        String id = PrefUtils.readStringPref(context, PROMO_ID);
        String title = PrefUtils.readStringPref(context, PROMO_TITLE);
        String body = PrefUtils.readStringPref(context, PROMO_BODY);
        long received = PrefUtils.readLongPref(context, PROMO_RECEIVED);
        int ttl = PrefUtils.readIntPref(context, PROMO_TTL);
        int code = PrefUtils.readIntPref(context, PROMO_CODE);
        String theme = PrefUtils.readStringPref(context, PROMO_THEME);
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body) && received != 0 && ttl > 0 && code > 0)
            return new Promo(id, title, body, received, ttl, code, theme);
        return null;
    }

    public static void savePromo(Context context, Promo promo) {
        PrefUtils.writeStringPref(context, PROMO_ID, promo.id);
        PrefUtils.writeStringPref(context, PROMO_TITLE, promo.title);
        PrefUtils.writeStringPref(context, PROMO_BODY, promo.body);
        PrefUtils.writeLongPref(context, PROMO_RECEIVED, promo.dateReceived);
        PrefUtils.writeIntPref(context, PROMO_TTL, promo.timeToLive);
        PrefUtils.writeIntPref(context, PROMO_CODE, promo.code);
        PrefUtils.writeStringPref(context, PROMO_THEME, promo.theme);
    }

    public static void clearPromo(Context context) {
        String[] prefs = new String[] { PROMO_TITLE, PROMO_BODY, PROMO_RECEIVED, PROMO_TTL, PROMO_CODE, PROMO_THEME };
        PrefUtils.removePrefs(context, prefs);
    }

    public static Observable<Optional<Promo>> getActivePromo(Context context) {
        return Observable.create(e -> {
            Promo promo = getPromo(context);
            if(promo != null && promo.isExpired()) {
                clearPromo(context);
                promo = null;
            }
            e.onNext(new Optional(promo));
            e.onComplete();
            return;
        });
    }
}
