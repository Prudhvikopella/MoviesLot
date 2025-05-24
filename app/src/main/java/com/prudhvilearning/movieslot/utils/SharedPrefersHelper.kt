package com.prudhvilearning.movieslot.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

    // Save List<Int> to SharedPreferences
    class SharedPrefsHelper(context: Context) {

        private val prefs: SharedPreferences =
            context.getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
        private val gson = Gson()

        private val KEY_WISHLIST = "wishlist_ids"
        private val KEY_SEENLIST = "seenlist_ids"

        // -----------------------------
        // Generic Save/Remove Functions
        // -----------------------------

        private fun saveIds(key: String, ids: List<Int>) {
            val json = gson.toJson(ids)
            prefs.edit().putString(key, json).apply()
        }

        private fun getIds(key: String): List<Int> {
            val json = prefs.getString(key, "[]")
            return gson.fromJson(json, object : TypeToken<List<Int>>() {}.type)
        }

        private fun addId(key: String, id: Int) {
            val currentList = getIds(key).toMutableList()
            if (!currentList.contains(id)) {
                currentList.add(id)
                saveIds(key, currentList)
            }
        }

        private fun removeId(key: String, id: Int) {
            val currentList = getIds(key).toMutableList()
            if (currentList.contains(id)) {
                currentList.remove(id)
                saveIds(key, currentList)
            }
        }

        // -----------------------------
        // Wishlist API
        // -----------------------------

        fun getWishlistIds(): List<Int> = getIds(KEY_WISHLIST)

        fun addWishlistId(id: Int) = addId(KEY_WISHLIST, id)

        fun removeWishlistId(id: Int) = removeId(KEY_WISHLIST, id)

        fun clearWishlist() {
            prefs.edit().remove(KEY_WISHLIST).apply()
        }

        // -----------------------------
        // Seenlist API
        // -----------------------------

        fun getSeenlistIds(): List<Int> = getIds(KEY_SEENLIST)

        fun addSeenlistId(id: Int) = addId(KEY_SEENLIST, id)

        fun removeSeenlistId(id: Int) = removeId(KEY_SEENLIST, id)

        fun clearSeenlist() {
            prefs.edit().remove(KEY_SEENLIST).apply()
        }

        fun isInWishlist(id: Int): Boolean {
            return getWishlistIds().contains(id)
        }

        fun isInSeenlist(id: Int): Boolean {
            return getSeenlistIds().contains(id)
        }
    }


