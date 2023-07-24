import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.fat.R
import com.project.fat.data.store.StoreAvata
import com.project.fat.databinding.StoreViewBinding

class StorePagerAdapter(private val storeAvataList: List<StoreAvata>) : RecyclerView.Adapter<StorePagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= StoreViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = storeAvataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = storeAvataList.size

    override fun getItemViewType(position: Int): Int = position

    inner class ViewHolder(private val binding : StoreViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoreAvata) {

            var clickCheck = false

            Glide.with(binding.root)
                .load(data.fatmanImage)
                .into(binding.fatmanImage)
            binding.fatmanName.text = data.fatmanName

            if(data.activated){
                binding.select.visibility = View.VISIBLE
                binding.lockBackground.visibility = View.GONE
                binding.lock.visibility = View.GONE
            }else{
                binding.select.visibility = View.GONE
                binding.lockBackground.visibility = View.VISIBLE
                binding.lock.visibility = View.VISIBLE
            }

            binding.select.setOnClickListener{
                if(clickCheck){
                    binding.select.setImageResource(R.drawable.default_store_avata)
                    clickCheck = false
                }else{
                    binding.select.setImageResource(R.drawable.selected_store_avata)
                    clickCheck = true
                }
            }
        }
    }
}
