/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.sunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.sunshine.MainActivity;
import com.sunshine.SettingsFragment;
import com.sunshine.data.Weather;
import com.sunshine.data.WeatherDatabase;
import com.sunshine.utilities.AppExecutors;


import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SunshineSyncUtils {

    private static String UNIQUE_PERIODIC_WORK_NAME = "my-unique-work";
    private static boolean sInitialized;



    public static void initializeWorkManager(final Context context) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */

        if (sInitialized) return;
        sInitialized = true;

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(SunshineWorker.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(UNIQUE_PERIODIC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,request);


    }


    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        AppExecutors.getInstance().networkIO().execute(() -> SunshineSyncTask.syncWeather(context));
    }
}