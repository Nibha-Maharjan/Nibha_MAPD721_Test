package com.example.nibha_mapd721_test.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class UserStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserData")

        val PRODUCT_ID = stringPreferencesKey("product_id")
        val PRODUCT_NAME = stringPreferencesKey("product_name")
        val PRICE = stringPreferencesKey("price")
    }

    //List of string
    val getDetails: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            val productList = mutableListOf<String>()

            val productId = preferences[PRODUCT_ID] ?: ""
            val productName = preferences[PRODUCT_NAME] ?: ""
            val price = preferences[PRICE] ?: ""

            productList.add("Product ID: $productId")
            productList.add("Product Name: $productName")
            productList.add("Price: $price")

            productList
        }

    suspend fun saveDetails(productId: String, productName: String, price: String) {
        context.dataStore.edit { preferences ->
            val currentTimeMillis = System.currentTimeMillis().toString()

            preferences[PRODUCT_ID] = "$productId "
            preferences[PRODUCT_NAME] = "$productName "
            preferences[PRICE] = "$price "
        }
    }
    suspend fun clearCart() {
        context.dataStore.edit { preferences ->
            preferences.remove(PRODUCT_ID)
            preferences.remove(PRODUCT_NAME)
            preferences.remove(PRICE)
        }
    }

}