package com.project.fat.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.fat.dataStore.UserDataStore.dataStore
import kotlinx.coroutines.flow.Flow

object UserDataStore{
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    val USER_ID = longPreferencesKey("user_id")
    val NAME = stringPreferencesKey("name")
    val SELECTED_FATMAN_ID = longPreferencesKey("selected_fatman_id")
    val SELECTED_FATMAN_IMAGE = stringPreferencesKey("selected_fatman_image")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val TODAY_MONSTER_NUM = intPreferencesKey("today_monster_num")
}

