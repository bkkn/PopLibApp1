package me.bkkn.poplibapp1.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import coil.load
import kotlinx.coroutines.launch
import me.bkkn.myfirstmaterialapp.domain.NasaRepositoryImpl
import me.bkkn.poplibapp1.Const.bottom_sheet_tag
import me.bkkn.poplibapp1.Const.wiki_request_key
import me.bkkn.poplibapp1.R
import me.bkkn.poplibapp1.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(NasaRepositoryImpl())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.requestPictureOfTheDay()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)

        binding.textInput.setEndIconOnClickListener {
            val bottomSheetDialogFragment = WikiBottomSheetDialogFragment()
            val bundle = Bundle()
            bundle.putString(wiki_request_key, binding.editTextWikiRequest.text.toString())
            bottomSheetDialogFragment.arguments = bundle

            bottomSheetDialogFragment.show(
                parentFragmentManager,
                bottom_sheet_tag
            )
        }

        binding.todayChip.setOnClickListener({ viewModel.requestPictureOfTheDay() })
        binding.yesterdayChip.setOnClickListener({ viewModel.requestPictureByDate(1)})
        binding.twoDaysChip.setOnClickListener({viewModel.requestPictureByDate(2)})

//        binding.group.setOnClickListener {it->
//            when (it.id){
//                binding.twoDaysChip.id -> viewModel.requestPictureByDate(2)
//                binding.yesterdayChip.id -> viewModel.requestPictureByDate(1)
//                binding.todayChip.id -> viewModel.requestPictureOfTheDay()
//            }
//        }
//        setOnCheckedChangeListener { group, checkedId ->
//            if (checkedId == 1){
//                viewModel.requestPictureByDate(2)
//            }
//            if (checkedId == 2){
//                viewModel.requestPictureByDate(1)
//            }
//            if (checkedId == 3){
//                viewModel.requestPictureOfTheDay()
//            }
//        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.loading.collect {
                binding.progress.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.error.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.image.collect { url ->
                url?.let {
                    binding.img.load(it)
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.title.collect { title ->
                title?.let {
                    binding.bottomSheetLayout.bottomSheetDescriptionHeader.text = it
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.explanation.collect { expl ->
                expl?.let {
                    binding.bottomSheetLayout.bottomSheetExplanation.text = it
                }
            }
        }
    }
}