package com.android_a865.estimatescalculator.feature_settings.data.data_source

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.android_a865.estimatescalculator.feature_network.temp.Role
import com.android_a865.estimatescalculator.feature_settings.domain.models.AppSettings
import com.android_a865.estimatescalculator.feature_settings.domain.models.Company
import com.android_a865.estimatescalculator.utils.DATE_FORMATS
import com.android_a865.estimatescalculator.utils.toJson
import com.android_a865.estimatescalculator.utils.toObject
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
                company = preferences[PreferencesKeys.COMPANY_INFO]?.toObject() ?: Company(),
                dateFormat = preferences[PreferencesKeys.DATE_FORMAT] ?: DATE_FORMATS[0],
                currency = preferences[PreferencesKeys.CURRENCY] ?: "",
                isFirst = preferences[PreferencesKeys.IS_FIRST] ?: true,
                isSubscribed = preferences[PreferencesKeys.IS_SUBSCRIBED] ?: false,
                myRole = Role.entries[preferences[PreferencesKeys.HOW_AM_I] ?: 0],
                deviceName = preferences[PreferencesKeys.DEVICE_NAME] ?: "",
                deviceId = preferences[PreferencesKeys.DEVICE_ID] ?: 0
            )
        }


    suspend fun updateCompanyInfo(company: Company) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COMPANY_INFO] = company.toJson()
        }
    }

    suspend fun updateDateFormat(dateFormat: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DATE_FORMAT] = dateFormat
        }
    }

    suspend fun updateCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENCY] = currency
        }
    }

    suspend fun updateIsFirst(isFirst: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST] = isFirst
        }
    }

    suspend fun updateIsSubscribed(isSubscribed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SUBSCRIBED] = isSubscribed
        }
    }

    suspend fun setMyRole(deviceName: String, role: Role) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HOW_AM_I] = role.ordinal
            preferences[PreferencesKeys.DEVICE_NAME] = deviceName
            if (preferences[PreferencesKeys.DEVICE_ID]==null) {
                preferences[PreferencesKeys.DEVICE_ID] = System.currentTimeMillis()
            }
        }
    }


    private object PreferencesKeys {
        val COMPANY_INFO = stringPreferencesKey("company_info")
        val DATE_FORMAT = stringPreferencesKey("date_format")
        val CURRENCY = stringPreferencesKey("currency")
        val IS_FIRST = booleanPreferencesKey("is_first")
        val IS_SUBSCRIBED = booleanPreferencesKey("is_subscribed")
        val HOW_AM_I = intPreferencesKey("how_am_i")
        val DEVICE_NAME = stringPreferencesKey("device_name")
        val DEVICE_ID = longPreferencesKey("device_id")
    }
}