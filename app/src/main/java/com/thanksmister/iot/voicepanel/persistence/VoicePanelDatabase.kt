/*
 * Copyright (c) 2018 ThanksMister LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thanksmister.iot.voicepanel.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * The Room database
 */
@Database(entities = arrayOf(IntentMessage::class, MessageMqtt::class, Weather::class, Sun::class), version = 2, exportSchema = false)
@TypeConverters(IntentConverter::class, SlotsConverter::class, StringConverter::class)
abstract class VoicePanelDatabase : RoomDatabase() {

    abstract fun indentDao(): IntentDao
    abstract fun messageDao(): MessageDao
    abstract fun weatherDao(): WeatherDao
    abstract fun sunDao(): SunDao

    companion object {

        @Volatile private var INSTANCE: VoicePanelDatabase? = null

        @JvmStatic fun getInstance(context: Context): VoicePanelDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        VoicePanelDatabase::class.java, "voice_panel.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}