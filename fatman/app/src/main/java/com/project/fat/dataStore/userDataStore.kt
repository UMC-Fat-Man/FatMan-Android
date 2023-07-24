package com.project.fat.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object UserDataStoreKey{
    val USER_ID = longPreferencesKey("user_id")
    val NAME = stringPreferencesKey("name")
    val SELECTED_FATMAN_ID = longPreferencesKey("selected_fatman_id")
    val SELECTED_FATMAN_IMAGE = stringPreferencesKey("selected_fatman_image")
}

