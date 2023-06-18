package uk.aston.maprapp;
/*
 * Copyright (C) 2017 The Android Open Source Project
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

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

    /**
     * Get LiveData object. waiting for LiveData for 2 seconds to emit.
     * Once notification > onChanged, stop observing.
     */
    public static <T> T getValue(final LiveData<T> tLiveData) throws InterruptedException {
        final Object[] objects = new Object[1];
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Observer<T> tObserver = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                objects[0] = o;
                countDownLatch.countDown();
                tLiveData.removeObserver(this);
            }
        };
        tLiveData.observeForever(tObserver);
        countDownLatch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) objects[0];
    }
}
