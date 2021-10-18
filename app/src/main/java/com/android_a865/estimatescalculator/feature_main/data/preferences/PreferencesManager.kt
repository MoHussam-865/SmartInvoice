package com.android_a865.estimatescalculator.feature_main.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

    val preferencesFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("PreferencesManager", "Error reading the preferences", exception)
                emit(emptyPreferences())
            }else {
                throw exception
            }

        }
        .map { preferences ->
            AppSettings(
                example = preferences[PreferencesKeys.EXAMPLE_SETTING] ?: ""
            )
        }


    suspend fun updateExample(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.EXAMPLE_SETTING] = name
        }
    }


    private object PreferencesKeys {
        val EXAMPLE_SETTING = stringPreferencesKey("name")
    }
}