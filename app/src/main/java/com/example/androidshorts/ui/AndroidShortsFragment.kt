package com.example.androidshorts.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidshorts.R
import com.example.androidshorts.databinding.ActivityAndroidShortsFragmentBinding
import com.example.androidshorts.databinding.RvItemBinding
import com.example.androidshorts.model.Video
import com.example.androidshorts.viewmodels.AndroidShortsViewModel

class AndroidShortsFragment : Fragment() {

    private val viewModel: AndroidShortsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, defaultViewModelProviderFactory).get(AndroidShortsViewModel::class.java)

    }

//  RecyclerView Adapter for converting a list of Video to cards.
    private var viewModelAdapter: AndroidShortsAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<Video>> { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        })
    }


//  Called to have the fragment instantiate its user interface view.

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: ActivityAndroidShortsFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.activity_android_shorts_fragment,
            container,
            false)
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModelAdapter = AndroidShortsAdapter(VideoClick {
            // When a video is clicked this block will be called by AndroidShortsAdapter

            val packageManager = context?.packageManager ?: return@VideoClick

            // Try to generate a direct intent to the YouTube app
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if(intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

//  Helper method to generate YouTube app links
    private val Video.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}


class VideoClick(val block: (Video) -> Unit) {

//  Called when a video is clicked

    fun onClick(video: Video) = block(video)
}


//  RecyclerView Adapter for setting up data binding on the items in the list.

class AndroidShortsAdapter(val callback: VideoClick) : RecyclerView.Adapter<ASRecyclerViewHolder>() {


//  The videos that our Adapter will show

    var videos: List<Video> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ASRecyclerViewHolder {
        val withDataBinding: RvItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ASRecyclerViewHolder.LAYOUT,
            parent,
            false)
        return ASRecyclerViewHolder(withDataBinding)
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: ASRecyclerViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.video = videos[position]
            it.videoCallback = callback
        }
    }

}

class ASRecyclerViewHolder(val viewDataBinding: RvItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.rv_item
    }
}
