package com.mfitrahrmd.story.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthenticationDataStoreDataSource private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("token")

    fun getToken(): Flow<String?> {
        return dataStore.data.map {
            it[tokenKey]
        }
    }

    suspend fun setToken(setter: (String?) -> String) {
        dataStore.edit {
            it[tokenKey] = setter(it[tokenKey])
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationDataStoreDataSource? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthenticationDataStoreDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthenticationDataStoreDataSource(dataStore)
                INSTANCE = instance

                instance
            }
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")