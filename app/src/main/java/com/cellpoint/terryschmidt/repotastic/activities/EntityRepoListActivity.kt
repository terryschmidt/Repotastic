package com.cellpoint.terryschmidt.repotastic.activities

import adapters.RepoAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import com.cellpoint.terryschmidt.repotastic.R
import model.Repo
import network.GetDataService
import network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntityRepoListActivity : AppCompatActivity() {

    private lateinit var repoRecycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var repoOwner: String
    private var pageNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo_list_activity)
        Log.d(TAG, "Repo URL: " + intent.extras?.get("repos").toString())
        repoOwner = intent.extras?.get("name").toString()
        Log.d(TAG, "Repo Owner: $repoOwner")
        supportActionBar?.title = intent.extras?.get("name").toString() + " (" + (intent.extras?.get("type")
            ?: "") + ")"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        repoRecycler = findViewById(R.id.repoRecycler)
        repoRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        repoRecycler.hasFixedSize()
        repoAdapter = RepoAdapter(mutableListOf())
        repoRecycler.adapter = repoAdapter
        val itemDecorator = androidx.recyclerview.widget.DividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider)!!)
        repoRecycler.addItemDecoration(itemDecorator)
        setRecyclerViewScrollListener()
        loadRepos(false)
    }

    private fun setRecyclerViewScrollListener() {
        repoRecycler.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadRepos(true)
                }
            }
        })
    }

    private fun loadRepos(isFromScrolling: Boolean) {
        if (isFromScrolling) {
            pageNumber++
        } else {
            pageNumber = 1
        }
        val service = RetrofitInstance.getRetrofitInstance()?.create(GetDataService::class.java)
        val call = service?.getReposByName(repoOwner, pageNumber, getString(R.string.api_key))
        call?.enqueue(object : Callback<List<Repo>> {
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Log.e(TAG, "onFailure: " + t.message)
            }

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d(TAG, "onResponse isSuccessful: " + response.isSuccessful)
                Log.d(TAG, "onResponse code: " + response.code())
                Log.d(TAG, "onResponse message: " + response.message())
                Log.d(TAG, "onResponse call: " + call.request())

                if (response.body() == null) { Log.e(TAG, "null body") }
                //if (response!!.body()!!. == null) { Log.e(EntitySearchActivity.TAG, "null entityList") }

                val newRepoList: MutableList<Repo>? = response.body()?.toMutableList()
                if (newRepoList != null) {
                    if (isFromScrolling) {
                        repoAdapter.addToRepoList(newRepoList)
                    } else {
                        repoAdapter.swap(newRepoList)
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = NavUtils.getParentActivityIntent(this)
                intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                NavUtils.navigateUpTo(this, intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        val TAG = "EntityRepoListActivity"
    }
}