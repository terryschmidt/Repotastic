package adapters

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cellpoint.terryschmidt.repotastic.R
import com.cellpoint.terryschmidt.repotastic.activities.EntityRepoListActivity
import model.Entity

class EntityAdapter(var entities: MutableList<Entity>?) : androidx.recyclerview.widget.RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {
    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = entities?.get(position)
        holder.login.text = entity?.login
        holder.type.text = entity?.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.entity_adapter_item, parent, false)
        return EntityViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return entities?.size ?: 0
    }

    inner class EntityViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {
        var login: TextView = view.findViewById(R.id.login)
        var type: TextView = view.findViewById(R.id.type)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d(TAG, "Item pressed: $layoutPosition")
            val repoListActivityIntent = Intent(view?.context, EntityRepoListActivity::class.java)
            repoListActivityIntent.putExtra("name", login.text)
            repoListActivityIntent.putExtra("type", type.text)
            repoListActivityIntent.putExtra("repos", entities?.get(layoutPosition)?.reposUrl)
            view?.context?.startActivity(repoListActivityIntent)
        }
    }

    fun swap(newEntities: MutableList<Entity>) {
        entities = newEntities
        notifyDataSetChanged()
    }

    fun addToEntityList(newEntities: MutableList<Entity>) {
        entities?.addAll(newEntities)
        notifyDataSetChanged()
    }

    fun removeAll() {
        entities?.clear()
        Handler(Looper.getMainLooper()).post {
            notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        val TAG = "EntityAdapter"
    }
}