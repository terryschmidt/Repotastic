package com.cellpoint.terryschmidt.repotastic.activities

import adapters.RepoAdapter
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import com.cellpoint.terryschmidt.repotastic.R
import model.Repo
import network.GetDataService
import network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntityRepoListActivity : AppCompatActivity() {

    private lateinit var repoRecycler: RecyclerView
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var repoOwner: String
    private var pageNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo_list_activity)
        Log.d(TAG, "Repo URL: " + intent.extras.get("repos").toString())
        repoOwner = intent.extras.get("name").toString()
        Log.d(TAG, "Repo Owner: $repoOwner")
        supportActionBar?.title = intent.extras.get("name").toString() + " (" + intent.extras.get("type") + ")"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        repoRecycler = findViewById(R.id.repoRecycler)
        repoRecycler.layoutManager = LinearLayoutManager(this)
        repoRecycler.hasFixedSize()
        repoAdapter = RepoAdapter(mutableListOf())
        repoRecycler.adapter = repoAdapter
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider)!!)
        repoRecycler.addItemDecoration(itemDecorator)
        setRecyclerViewScrollListener()
        loadRepos(false)
    }

    private fun setRecyclerViewScrollListener() {
        repoRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView?.canScrollVertically(1) == false) {
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
            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                Log.e(TAG, "onFailure: " + t?.message)
            }

            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                Log.d(TAG, "onResponse isSuccessful: " + response?.isSuccessful)
                Log.d(TAG, "onResponse code: " + response?.code())
                Log.d(TAG, "onResponse message: " + response?.message())
                Log.d(TAG, "onResponse call: " + call?.request())

                if (response == null) { Log.e(TAG, "null response") }
                if (response!!.body() == null) { Log.e(TAG, "null body") }
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
                NavUtils.navigateUpFromSameTask(this)
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