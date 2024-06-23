package com.dicoding.asclepius.view

import android.view.View
import com.dicoding.asclepius.data.response.ArticlesItem

interface RVonclick {
    fun onItemClicked(view: View, username: ArticlesItem)
}