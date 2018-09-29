package com.cellpoint.terryschmidt.repotastic.activities

import adapters.EntityAdapter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import com.cellpoint.terryschmidt.repotastic.R
import model.Entity
import model.EntityListWrapper
import network.GetDataService
import network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EntitySearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var entityRecycler: RecyclerView
    private var timer = Timer()
    private val delay: Long = 1000
    private var pageNumber: Int = 1
    private lateinit var entityAdapter: EntityAdapter
    private lateinit var toggle: Switch
    private var currentToggleState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_search)
        toggle = findViewById(R.id.toggle)
        searchEditText = findViewById(R.id.search_box)
        entityRecycler = findViewById(R.id.entityRecycler)
        setSearchBarListener()
        entityRecycler.layoutManager = LinearLayoutManager(this)
        entityRecycler.hasFixedSize()
        entityAdapter = EntityAdapter(mutableListOf())
        toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            currentToggleState = isChecked
            entityAdapter.removeAll()
            loadEntities(false)
        }
        entityRecycler.adapter = entityAdapter
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider)!!)
        entityRecycler.addItemDecoration(itemDecorator)
        setRecyclerViewScrollListener()
        loadEntities(false)
    }

    private fun setSearchBarListener() {
        val searchBox = findViewById<View>(R.id.search_box) as EditText
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                Log.e("Query users", searchEditText.text.toString())
                                loadEntities(false)
                            }
                        }, delay)
            }
        })
    }

    private fun loadEntities(isFromScrolling: Boolean) {
        if (isFromScrolling) {
            pageNumber++
        } else {
            pageNumber = 1
        }
        val service = RetrofitInstance.getRetrofitInstance()?.create(GetDataService::class.java)
        val query = searchEditText.text.toString()
        if (query.equals("")) {
            entityAdapter.removeAll()
            return
        }
        val type = if (currentToggleState) {
            "user"
        } else {
            "org"
        }
        val call = service?.getEntityListWrapper(query + "type:$type", getString(R.string.api_key), pageNumber)
        call?.enqueue(object : Callback<EntityListWrapper> {
            override fun onFailure(call: Call<EntityListWrapper>?, t: Throwable?) {
                Log.e(TAG, "onFailure: " + t?.message)
            }

            override fun onResponse(call: Call<EntityListWrapper>?, response: Response<EntityListWrapper>?) {
                Log.d(TAG, "onResponse isSuccessful: " + response?.isSuccessful)
                Log.d(TAG, "onResponse code: " + response?.code())
                Log.d(TAG, "onResponse message: " + response?.message())
                Log.d(TAG, "onResponse call: " + call?.request())

                if (response == null) { Log.e(TAG, "null response") }
                if (response!!.body() == null) { Log.e(TAG, "null body") }

                val newEntityList: MutableList<Entity>? = response.body()?.entityList?.toMutableList()
                if (newEntityList != null) {
                    if (isFromScrolling) {
                        entityAdapter.addToEntityList(newEntityList)
                    } else {
                        entityAdapter.swap(newEntityList)
                    }
                }
            }
        })
    }

    private fun setRecyclerViewScrollListener() {
        entityRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView?.canScrollVertically(1) == false) {
                    loadEntities(true)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    companion object {
        @JvmStatic
        val TAG = "EntitySearchActivity"
    }
}