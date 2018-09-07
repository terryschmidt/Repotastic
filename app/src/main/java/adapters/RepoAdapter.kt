package adapters

import adapters.RepoAdapter.RepoViewHolder
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cellpoint.terryschmidt.repotastic.R
import com.cellpoint.terryschmidt.repotastic.activities.RepoDetailActivity
import model.Repo

class RepoAdapter(var repos: MutableList<Repo>?) : RecyclerView.Adapter<RepoViewHolder>() {
    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repos?.get(position)
        holder.name.text = repo?.name
        holder.language.text = repo?.language
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.repo_adapter_item, parent, false)
        return RepoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return repos?.size ?: 0
    }

    inner class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var name: TextView = view.findViewById(R.id.repoName)
        var language: TextView = view.findViewById(R.id.language)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d(TAG, "Item pressed: $layoutPosition")
            val repoDetailIntent = Intent(view?.context, RepoDetailActivity::class.java)
            val repo = repos?.get(layoutPosition)
            repoDetailIntent.putExtra("name", name.text)
            repoDetailIntent.putExtra("language", language.text)
            repoDetailIntent.putExtra("created_at", repo?.created_at)
            repoDetailIntent.putExtra("html_url", repo?.html_url)
            repoDetailIntent.putExtra("fork", repo?.isFork)
            repoDetailIntent.putExtra("stargazers_count", repo?.stargazers_count)
            repoDetailIntent.putExtra("open_issues", repo?.open_issues)
            view?.context?.startActivity(repoDetailIntent)
        }
    }

    fun swap(repos: MutableList<Repo>) {
        this.repos = repos
        notifyDataSetChanged()
    }

    fun addToRepoList(repos: MutableList<Repo>) {
        this.repos?.addAll(repos)
        notifyDataSetChanged()
    }

    fun removeAll() {
        repos?.clear()
        Handler(Looper.getMainLooper()).post {
            notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        val TAG = "RepoAdapter"
    }
}