package words.list.task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import words.list.task.R
import words.list.task.databinding.ItemProgressLoadingBinding
import words.list.task.observer.OnRecyclerItemClickListener
import words.list.task.databinding.ItemRecyclerWordsBinding
import words.list.task.model.WordModel


class RecyclerWordsAdapter(
    var wordModels: ArrayList<WordModel>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    val ItemViewData = 1
    val ItemViewProgress = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        if (viewType == ItemViewData) {
            val binding = DataBindingUtil.inflate(
                inflater,
                R.layout.item_recycler_words,
                parent,
                false
            ) as ItemRecyclerWordsBinding
            return ViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate(
                inflater,
                R.layout.item_progress_loading,
                parent,
                false
            ) as ItemProgressLoadingBinding
            return ProgressViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (wordModels[position].holderType!!.isEmpty()) ItemViewData else ItemViewProgress
    }

    override fun getItemCount() = wordModels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProgressViewHolder)
            return

        var holder = holder as ViewHolder

        holder.binding.model = wordModels[holder.adapterPosition]


        holder.binding.layoutItemRecyclerService.setOnClickListener {
            onRecyclerItemClickListener.onRecyclerItemClickListener(position)
        }

    }

    fun setList(list: ArrayList<WordModel>) {
        this.wordModels = list
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemRecyclerWordsBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(var binding: ItemProgressLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

}