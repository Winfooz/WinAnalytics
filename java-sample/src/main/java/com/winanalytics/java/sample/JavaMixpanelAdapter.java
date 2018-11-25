package com.winanalytics.java.sample;

import android.content.Context;
import android.util.Pair;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.winfooz.AnalyticsAdapter;
import com.winfooz.MapUtils;
import java.util.HashMap;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public class JavaMixpanelAdapter implements AnalyticsAdapter {

    private Context context;
    private String token;

    JavaMixpanelAdapter(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override
    public void log(Pair<String, HashMap<String, Object>> pair) {
        MixpanelAPI api = MixpanelAPI.getInstance(context, token);
        api.track(pair.first, MapUtils.toJsonObject(pair.second));
    }
}
