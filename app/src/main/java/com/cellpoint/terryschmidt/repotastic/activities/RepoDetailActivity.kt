package com.cellpoint.terryschmidt.repotastic.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.cellpoint.terryschmidt.repotastic.R

class RepoDetailActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var languageTextView: TextView
    private lateinit var createdAtTextView: TextView
    private lateinit var htmlUrlTextView: TextView
    private lateinit var forkTextView: TextView
    private lateinit var stargazerCountTextView: TextView
    private lateinit var openIssuesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo_detail_activity)
        nameTextView = findViewById(R.id.repoDetailName)
        languageTextView = findViewById(R.id.repoDetailLanguage)
        createdAtTextView = findViewById(R.id.repoDetailCreatedAt)
        htmlUrlTextView = findViewById(R.id.repoDetailHtmlUrl)
        forkTextView = findViewById(R.id.repoDetailFork)
        stargazerCountTextView = findViewById(R.id.repoDetailStargazersCount)
        openIssuesTextView = findViewById(R.id.repoDetailOpenIssues)
        supportActionBar?.title = intent.extras.get("name").toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameTextView.text = nameTextView.text.toString() + intent.extras?.get("name").toString()
        languageTextView.text = languageTextView.text.toString() + intent.extras?.get("language").toString()
        createdAtTextView.text = createdAtTextView.text.toString() + intent.extras?.get("created_at").toString()
        htmlUrlTextView.text = htmlUrlTextView.text.toString() + intent.extras?.get("html_url").toString()
        forkTextView.text = forkTextView.text.toString() + intent.extras?.get("fork").toString()
        stargazerCountTextView.text = stargazerCountTextView.text.toString() + intent.extras?.get("stargazers_count").toString()
        openIssuesTextView.text = openIssuesTextView.text.toString() + intent.extras?.get("open_issues").toString()
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
}