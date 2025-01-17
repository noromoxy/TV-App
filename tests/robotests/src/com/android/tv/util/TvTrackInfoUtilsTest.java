/*
 * Copyright (C) 2015 The Android Open Source Project
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
package com.android.tv.util;

import static com.android.tv.util.TvTrackInfoUtils.getBestTrackInfo;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertEquals;

import android.media.tv.TvTrackInfo;
import com.android.tv.testing.ComparatorTester;
import com.android.tv.testing.constants.ConfigConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/** Tests for {@link com.android.tv.util.TvTrackInfoUtils}. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = ConfigConstants.SDK)
public class TvTrackInfoUtilsTest {

    /** Tests for {@link TvTrackInfoUtils#getBestTrackInfo}. */
    private static final String UN_MATCHED_ID = "no matching ID";

    private final TvTrackInfo info1En1 = create("1", "en", 1);

    private final TvTrackInfo info2En5 = create("2", "en", 5);

    private final TvTrackInfo info3Fr8 = create("3", "fr", 8);

    private final TvTrackInfo info4Null2 = create("4", null, 2);

    private final TvTrackInfo info5Null6 = create("5", null, 6);

    private TvTrackInfo create(String id, String fr, int audioChannelCount) {
        return new TvTrackInfo.Builder(TvTrackInfo.TYPE_AUDIO, id)
            .setLanguage(fr)
            .setAudioChannelCount(audioChannelCount)
            .build();
    }

    private final List<TvTrackInfo> all =
        Arrays.asList(info1En1, info2En5, info3Fr8, info4Null2, info5Null6);
    private final List<TvTrackInfo> nullLanguageTracks =
        Arrays.asList(info4Null2, info5Null6);

    @Test
    public void testGetBestTrackInfo_empty() {
        TvTrackInfo result = getBestTrackInfo(Collections.emptyList(), UN_MATCHED_ID, "en", 1);
        assertWithMessage("best track ").that(result).isEqualTo(null);
    }

    @Test
    public void testGetBestTrackInfo_exactMatch() {
        TvTrackInfo result = getBestTrackInfo(all, "1", "en", 1);
        assertWithMessage("best track ").that(result).isEqualTo(info1En1);
    }

    @Test
    public void testGetBestTrackInfo_langAndChannelCountMatch() {
        TvTrackInfo result = getBestTrackInfo(all, UN_MATCHED_ID, "en", 5);
        assertWithMessage("best track ").that(result).isEqualTo(info2En5);
    }

    @Test
    public void testGetBestTrackInfo_languageOnlyMatch() {
        TvTrackInfo result = getBestTrackInfo(all, UN_MATCHED_ID, "fr", 1);
        assertWithMessage("best track ").that(result).isEqualTo(info3Fr8);
    }

    @Test
    public void testGetBestTrackInfo_channelCountOnlyMatchWithNullLanguage() {
        TvTrackInfo result = getBestTrackInfo(all, UN_MATCHED_ID, null, 8);
        assertWithMessage("best track ").that(result).isEqualTo(info3Fr8);
    }

    @Test
    public void testGetBestTrackInfo_noMatches() {
        TvTrackInfo result = getBestTrackInfo(all, UN_MATCHED_ID, "kr", 1);
        assertWithMessage("best track ").that(result).isEqualTo(info1En1);
    }

    @Test
    public void testGetBestTrackInfo_noMatchesWithNullLanguage() {
        TvTrackInfo result = getBestTrackInfo(all, UN_MATCHED_ID, null, 0);
        assertWithMessage("best track ").that(result).isEqualTo(info1En1);
    }

    @Test
    public void testGetBestTrackInfo_channelCountAndIdMatch() {
        TvTrackInfo result = getBestTrackInfo(nullLanguageTracks, "5", null, 6);
        assertWithMessage("best track ").that(result).isEqualTo(info5Null6);
    }

    @Test
    public void testComparator() {
        Comparator<TvTrackInfo> comparator = TvTrackInfoUtils.createComparator("1", "en", 1);
        ComparatorTester.withoutEqualsTest(comparator)
            // lang not match
            .addComparableGroup(
                create("1", "kr", 1),
                create("2", "kr", 2),
                create("1", "ja", 1),
                create("1", "ch", 1))
            // lang match not count match
            .addComparableGroup(
                create("2", "en", 2), create("3", "en", 3), create("1", "en", 2))
            // lang and count match
            .addComparableGroup(create("2", "en", 1), create("3", "en", 1))
            // all match
            .addComparableGroup(create("1", "en", 1), create("1", "en", 1))
            .test();
    }

    /** Tests for {@link TvTrackInfoUtils#needToShowSampleRate}. */

    private final TvTrackInfo info6En1 = create("6", "en", 1);

    private final TvTrackInfo info7En0 = create("7", "en", 0);

    private final TvTrackInfo info8En0 = create("8", "en", 0);

    private List<TvTrackInfo> trackList;

    @Test
    public void needToShowSampleRate_false() {
        trackList = Arrays.asList(info1En1, info2En5);
        assertEquals(
            false,
            TvTrackInfoUtils.needToShowSampleRate(
                RuntimeEnvironment.application,
                trackList));
    }

    @Test
    public void needToShowSampleRate_sameLanguageAndChannelCount() {
        trackList = Arrays.asList(info1En1, info6En1);
        assertEquals(
            true,
            TvTrackInfoUtils.needToShowSampleRate(
                RuntimeEnvironment.application,
                trackList));
    }

    @Test
    public void needToShowSampleRate_sameLanguageNoChannelCount() {
        trackList = Arrays.asList(info7En0, info8En0);
        assertEquals(
            true,
            TvTrackInfoUtils.needToShowSampleRate(
                RuntimeEnvironment.application,
                trackList));
    }

    /** Tests for {@link TvTrackInfoUtils#getMultiAudioString}. */
    private static final String TRACK_ID = "test_track_id";
    private static final int AUDIO_SAMPLE_RATE = 48000;

    @Test
    public void testAudioTrackLanguage() {
        assertEquals(
            "Korean",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("kor"),
                false));
        assertEquals(
            "English",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng"),
                false));
        assertEquals(
            "Unknown language",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo(null),
                false));
        assertEquals(
            "Unknown language",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo(""),
                false));
        assertEquals(
            "abc",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("abc"),
                false));
    }

    @Test
    public void testAudioTrackCount() {
        assertEquals(
            "English",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", -1),
                false));
        assertEquals(
            "English",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 0),
                false));
        assertEquals(
            "English (mono)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 1),
                false));
        assertEquals(
            "English (stereo)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 2),
                false));
        assertEquals(
            "English (3 channels)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 3),
                false));
        assertEquals(
            "English (4 channels)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 4),
                false));
        assertEquals(
            "English (5 channels)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 5),
                false));
        assertEquals(
            "English (5.1 surround)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 6),
                false));
        assertEquals(
            "English (7 channels)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 7),
                false));
        assertEquals(
            "English (7.1 surround)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("eng", 8),
                false));
    }

    @Test
    public void testShowSampleRate() {
        assertEquals(
            "Korean (48kHz)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("kor", 0),
                true));
        assertEquals(
            "Korean (7.1 surround, 48kHz)",
            TvTrackInfoUtils.getMultiAudioString(
                RuntimeEnvironment.application,
                createAudioTrackInfo("kor", 8),
                true));
    }

    private static TvTrackInfo createAudioTrackInfo(String language) {
        return createAudioTrackInfo(language, 0);
    }

    private static TvTrackInfo createAudioTrackInfo(String language, int channelCount) {
        return new TvTrackInfo.Builder(TvTrackInfo.TYPE_AUDIO, TRACK_ID)
            .setLanguage(language)
            .setAudioChannelCount(channelCount)
            .setAudioSampleRate(AUDIO_SAMPLE_RATE)
            .build();
    }

    /** Tests for {@link TvTrackInfoUtils#toString */
    @Test
    public void toString_audioWithDetails() {
        assertEquals(
            "TvTrackInfo{type=Audio, id=1, language=en, "
            + "description=test, audioChannelCount=1, audioSampleRate=5}",
            TvTrackInfoUtils.toString(
                new TvTrackInfo
                    .Builder(TvTrackInfo.TYPE_AUDIO, "1")
                    .setLanguage("en")
                    .setAudioChannelCount(1)
                    .setDescription("test")
                    .setAudioSampleRate(5)
                    .build()
            ));
    }

    @Test
    public void toString_audioWithDefaults() {
        assertEquals(
            "TvTrackInfo{type=Audio, id=2, language=null, "
                + "description=null, audioChannelCount=0, audioSampleRate=0}",
            TvTrackInfoUtils.toString(
                new TvTrackInfo
                    .Builder(TvTrackInfo.TYPE_AUDIO, "2")
                    .build()
            ));
    }

    @Test
    public void toString_videoWithDetails() {
        assertEquals(
            "TvTrackInfo{type=Video, id=3, language=en, description=test, "
            + "videoWidth=1, videoHeight=1, videoFrameRate=1.0, videoPixelAspectRatio=2.0}",
            TvTrackInfoUtils.toString(
                new TvTrackInfo
                    .Builder(TvTrackInfo.TYPE_VIDEO, "3")
                    .setLanguage("en")
                    .setDescription("test")
                    .setVideoWidth(1)
                    .setVideoHeight(1)
                    .setVideoFrameRate(1)
                    .setVideoPixelAspectRatio(2)
                    .build()
            ));
    }

    @Test
    public void toString_videoWithDefaults() {
        assertEquals(
            "TvTrackInfo{type=Video, id=4, language=null, description=null, "
                + "videoWidth=0, videoHeight=0, videoFrameRate=0.0, videoPixelAspectRatio=1.0}",
            TvTrackInfoUtils.toString(
                new TvTrackInfo
                    .Builder(TvTrackInfo.TYPE_VIDEO, "4")
                    .build()
            ));
    }

    @Test
    public void toString_subtitleWithDetails() {
        assertEquals(
            "TvTrackInfo{type=Subtitle, id=5, language=en, description=test}",
            TvTrackInfoUtils.toString(
                new TvTrackInfo
                    .Builder(TvTrackInfo.TYPE_SUBTITLE, "5")
                    .setLanguage("en")
                    .setDescription("test")
                    .build()
            ));
    }

    @Test
    public void toString_subtitleWithDefaults() {
        assertEquals(
            "TvTrackInfo{type=Subtitle, id=6, language=null, description=null}",
            TvTrackInfoUtils.toString(
                new TvTrackInfo
                    .Builder(TvTrackInfo.TYPE_SUBTITLE, "6")
                    .build()
            ));
    }
}
