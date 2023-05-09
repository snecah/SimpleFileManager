package com.example.simplefilemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplefilemanager.R
import com.example.simplefilemanager.databinding.FragmentFilesListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch

class FilesListFragment : Fragment(R.layout.fragment_files_list) {

    private val adapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val binding by viewBinding(FragmentFilesListBinding::bind)
    private val viewModel by viewModels<FilesListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments?.getString(FILE_PATH)
        binding.recyclerView.adapter = adapter
        viewModel.fetchContent(path)
        viewModel.fileGroupiesItems.observe(viewLifecycleOwner) {
            adapter.update(it)
            navigationEventEnter()

        }

    }

    private fun navigationEventEnter() {
        lifecycleScope.launch {
            val newDirectory = viewModel.onDirectoryItemClickedEvent.receive()
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragment_container, newInstance(newDirectory))
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
    }

    companion object {

        private const val FILE_PATH = "file_path"

        fun newInstance(path: String? = null): FilesListFragment {
            val args = Bundle()
            args.putString(FILE_PATH, path)
            val fragment = FilesListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}