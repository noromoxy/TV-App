/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.android.tv.common.flags.impl;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import com.android.tv.common.flags.BackendKnobsFlags;
import com.android.tv.common.flags.CloudEpgFlags;
import com.android.tv.common.flags.ConcurrentDvrPlaybackFlags;
import com.android.tv.common.flags.DvrFlags;
import com.android.tv.common.flags.LegacyFlags;
import com.android.tv.common.flags.StartupFlags;
import com.android.tv.common.flags.TunerFlags;
import com.android.tv.common.flags.UiFlags;

/** Provides default flags. */
@Module
public class DefaultFlagsModule {

    @Provides
    @Reusable
    BackendKnobsFlags provideBackendKnobsFlags() {
        return new DefaultBackendKnobsFlags();
    }

    @Provides
    @Reusable
    CloudEpgFlags provideCloudEpgFlags() {
        return new DefaultCloudEpgFlags();
    }

    @Provides
    @Reusable
    ConcurrentDvrPlaybackFlags provideConcurrentDvrPlaybackFlags() {
        return new DefaultConcurrentDvrPlaybackFlags();
    }

    @Provides
    @Reusable
    DvrFlags provideDvrFlags() {
        return new DefaultDvrFlags();
    }

    @Provides
    @Reusable
    LegacyFlags provideLegacyFlags() {
        return DefaultLegacyFlags.DEFAULT;
    }

    @Provides
    @Reusable
    StartupFlags provideStartupFlags() {
        return new DefaultStartupFlags();
    }

    @Provides
    @Reusable
    TunerFlags provideTunerFlags() {
        return new DefaultTunerFlags();
    }

    @Provides
    @Reusable
    UiFlags provideUiFlags() {
        return new DefaultUiFlags();
    }
}
