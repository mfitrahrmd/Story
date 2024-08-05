package com.mfitrahrmd.story.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionDataStoreDataSource private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("token")
    private val nameKey = stringPreferencesKey("name")
    private val emailKey = stringPreferencesKey("email")

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[tokenKey] ?: ""
        }
    }

    suspend fun setToken(setter: (String?) -> String) {
        dataStore.edit {
            it[tokenKey] = setter(it[tokenKey])
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map {
            it[nameKey] ?: ""
        }
    }

    suspend fun setName(setter: (String?) -> String) {
        dataStore.edit {
            it[nameKey] = setter(it[nameKey])
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map {
            it[emailKey] ?: ""
        }
    }

    suspend fun setEmail(setter: (String?) -> String) {
        dataStore.edit {
            it[emailKey] = setter(it[emailKey])
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionDataStoreDataSource? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionDataStoreDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionDataStoreDataSource(dataStore)
                INSTANCE = instance

                instance
            }
        }
    }
}

val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")