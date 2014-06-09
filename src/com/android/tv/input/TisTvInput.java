/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.input;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.net.Uri;

import com.android.tv.data.ChannelMap;
import com.android.tv.util.TvInputManagerHelper;
import com.android.tv.util.Utils;

public class TisTvInput extends TvInput {
    private final TvInputManagerHelper mInputManagerHelper;
    private final TvInputInfo mInputInfo;
    private final Context mContext;
    private final String mId;

    public TisTvInput(TvInputManagerHelper inputManagerHelper, TvInputInfo inputInfo,
            Context context) {
        mInputManagerHelper = inputManagerHelper;
        mInputInfo = inputInfo;
        mContext = context;
        mId = mInputInfo.getId();
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getDisplayName() {
        return Utils.getDisplayNameForInput(mContext, mInputInfo);
    }

    @Override
    public Intent getIntentForSetupActivity() {
        return mInputInfo.getIntentForSetupActivity();
    }

    @Override
    public Intent getIntentForSettingsActivity() {
        return mInputInfo.getIntentForSettingsActivity();
    }

    @Override
    public boolean isAvailable() {
        return mInputManagerHelper.isAvailable(mId);
    }

    @Override
    public boolean hasChannel(boolean browsableOnly) {
        return Utils.hasChannel(mContext, mInputInfo, browsableOnly);
    }

    @Override
    public ChannelMap buildChannelMap(Activity activity, long initialChannelId,
            Runnable onChannelsLoadFinished) {
        return new ChannelMap(activity, this, initialChannelId, mInputManagerHelper,
                onChannelsLoadFinished);
    }

    @Override
    public Uri buildChannelsUri() {
        ServiceInfo info = mInputInfo.getServiceInfo();
        ComponentName name = new ComponentName(info.packageName, info.name);
        return TvContract.buildChannelsUriForInput(name, false);
    }

    @Override
    public String buildChannelsSortOrder() {
        return Utils.CHANNEL_SORT_ORDER_BY_DISPLAY_NUMBER;
    }
}