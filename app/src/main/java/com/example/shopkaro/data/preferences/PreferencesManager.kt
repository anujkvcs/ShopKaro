package com.example.shopkaro.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesManager @Inject constructor(private val context: Context) {
    
    private object PreferencesKeys {
        val RECENTLY_VIEWED = stringSetPreferencesKey("recently_viewed")
        val SEARCH_HISTORY = stringSetPreferencesKey("search_history")
        val PREFERRED_CATEGORIES = stringSetPreferencesKey("preferred_categories")
    }
    
    suspend fun addRecentlyViewed(productId: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.RECENTLY_VIEWED] ?: emptySet()
            val updated = (current + productId).take<String>(20).toSet()
            preferences[PreferencesKeys.RECENTLY_VIEWED] = updated
        }
    }
    
    fun getRecentlyViewed(): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.RECENTLY_VIEWED] ?: emptySet()
        }
    }
    
    suspend fun addSearchQuery(query: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.SEARCH_HISTORY] ?: emptySet()
            val updated = (current + query).take(10).toSet()
            preferences[PreferencesKeys.SEARCH_HISTORY] = updated
        }
    }
    
    fun getSearchHistory(): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SEARCH_HISTORY] ?: emptySet()
        }
    }
}