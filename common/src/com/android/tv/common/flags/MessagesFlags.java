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
 * limitations under the License
 */
package com.android.tv.common.flags;

/**
 * Message flags.
 *
 * <p>Used to hide new messages until all translations are ready.
 *
 * <p>Production releases never include the messages protected by these flags.
 */
public interface MessagesFlags {

    /**
     * Whether or not this feature is compiled into this build.
     *
     * <p>This returns true by default, unless the is_compiled_selector parameter was set during
     * code generation.
     */
    boolean compiled();

    /** Use setup_sources_description2 */
    boolean setupSourcesDescription2();
}
