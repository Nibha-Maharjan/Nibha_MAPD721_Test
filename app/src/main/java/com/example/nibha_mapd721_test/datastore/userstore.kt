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
    // Companion object to provide static access to DataStore and keys
    companion object {
        // DataStore for storing user preferences
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserData")
        // Keys for different preferences
        val PRODUCT_ID = stringPreferencesKey("product_id")
        val PRODUCT_NAME = stringPreferencesKey("product_name")
        val PRICE = stringPreferencesKey("price")
        val PRODUCTS = stringPreferencesKey("products")
    }

    // Flow to observe and collect user details
    //List of string
    val getDetails: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            val productList = mutableListOf<String>()
// Retrieving product details from preferences
            val productId = preferences[PRODUCT_ID] ?: ""
            val productName = preferences[PRODUCT_NAME] ?: ""
            val price = preferences[PRICE] ?: ""
            // Adding product details to the list
            productList.add("Product ID: $productId")
            productList.add("Product Name: $productName")
            productList.add("Price: $price")

            productList

        }

    // Function to save product details to DataStore

    suspend fun saveDetails(productId: String, productName: String, price: String) {
        context.dataStore.edit { preferences ->
            // Updating individual preferences
            preferences[PRODUCT_ID] = "$productId "
            preferences[PRODUCT_NAME] = "$productName "
            preferences[PRICE] = "$price "
            // Concatenating product details for the PRODUCTS key
            val existingProducts = preferences[PRODUCTS] ?: ""
            val updatedProducts = "$existingProducts,$productId:$productName:$price"
            preferences[PRODUCTS] = updatedProducts
        }
    }
    // Function to clear cart by removing preferences
    suspend fun clearCart() {
        context.dataStore.edit { preferences ->
            preferences.remove(PRODUCT_ID)
            preferences.remove(PRODUCT_NAME)
            preferences.remove(PRICE)
            preferences.remove(PRODUCTS)
        }
    }

}