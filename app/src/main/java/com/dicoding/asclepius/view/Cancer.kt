package com.dicoding.asclepius.view

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler.Callback
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ApiConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.Responses
import com.dicoding.asclepius.databinding.ActivityCancerBinding
import retrofit2.Call
import retrofit2.Response

class Cancer : AppCompatActivity(), RVonclick{

    private lateinit var binding: ActivityCancerBinding

    companion object {
        private val adapter = RVCancer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCancerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        findUsers("kanker")
    }

    private fun findUsers(LoginUsers: String) {
        showLoading(true)
        val client = ApiConfig.getListNews().getUsers(LoginUsers)
        client.enqueue(object : retrofit2.Callback<Responses> {
            override fun onResponse(call: Call<Responses>,
                                    response: Response<Responses>
            ){
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        SetListUsers(responseBody.articles)

                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Responses>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun SetListUsers(listUsername: List<ArticlesItem>) {
        adapter.submitList(listUsername)
        binding.rvReview.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onItemClicked(view: View, username: ArticlesItem) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(username.url))
        startActivity(i)
    }
}