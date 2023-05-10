package com.example.simplefilemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplefilemanager.MainActivity
import com.example.simplefilemanager.R
import com.example.simplefilemanager.databinding.FragmentFilesListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch

class FilesListFragment : Fragment(R.layout.fragment_files_list) {

    private val adapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val binding by viewBinding(FragmentFilesListBinding::bind)
    private val viewModel by viewModels<FilesListViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments?.getString(FILE_PATH)
        binding.recyclerView.adapter = adapter
        viewModel.fetchContent(path)
        viewModel.fileGroupiesItems.observe(viewLifecycleOwner) {
            adapter.update(it)
            navigationEventEnter()

        }

        with(binding) {
            sortByDateButton.setOnClickListener {
                viewModel.sortByDate()
            }

            sortBySizeButton.setOnClickListener {
                viewModel.sortBySize()
            }

            sortByExtensionButton.setOnClickListener {
                viewModel.sortByExtension()
            }
        }

    }

    private fun navigationEventEnter() {
        lifecycleScope.launch {
            val newDirectoryPath = viewModel.onDirectoryItemClickedEvent.receive()
            Log.e("@@@", "receieve chanel $newDirectoryPath")
            (activity as MainActivity).navigateToDirectory(newDirectoryPath)
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