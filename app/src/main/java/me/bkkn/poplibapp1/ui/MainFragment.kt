package me.bkkn.poplibapp1.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import coil.load
import coil.request.videoFrameMillis
import kotlinx.coroutines.launch
import me.bkkn.myfirstmaterialapp.domain.NasaRepositoryImpl
import me.bkkn.poplibapp1.Const.bottom_sheet_tag
import me.bkkn.poplibapp1.Const.wiki_request_key
import me.bkkn.poplibapp1.MainActivity
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
            viewModel.requestPictureByDate(0)
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

        binding.todayChip.setOnClickListener({ viewModel.requestPictureByDate(0) })
        binding.yesterdayChip.setOnClickListener({ viewModel.requestPictureByDate(1) })
        binding.twoDaysChip.setOnClickListener({ viewModel.requestPictureByDate(6) })

        binding.martianThemeChip.setOnClickListener({(requireActivity() as MainActivity).setCurrentTheme(R.style.Theme_Marsian)})
        binding.earthianThemeChip.setOnClickListener({(requireActivity() as MainActivity).setCurrentTheme(R.style.Theme_Earthian)})

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

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.response.collect { response ->
                response?.let {



                    if(response.mediaType == "video"){

                        val url = response.url
//                        val videoId = url.split("v=")[1]; //for this, the extracted id is "en7IK3iH3wI"
//
//                        val tempThumbnailDefault = "http://img.youtube.com/vi/"+videoId+"/default.jpg" //default quality thumbnail
//                        val tempThumbnailStandard = "http://img.youtube.com/vi/"+videoId+"/sddefault.jpg"
////standard thumbnail
//                        val tempThumbnailInMaxRes = "http://img.youtube.com/vi/"+videoId+"/maxresdefault.jpg"
////maximum resolution thumbnail
//                        val tempThumbnailInMQ = "http://img.youtube.com/vi/"+videoId+"/mqdefault.jpg" //medium quality thumbnail
//                        val tempThumbnailInHQ = "http://img.youtube.com/vi/"+videoId+"/hqdefault.jpg"
//high quality thumbnail
val default = "https://img.youtube.com/vi/%22+aKK7vS2CHC8+%22/default.jpg"

                        val imageLoader = ImageLoader.Builder(requireContext())
                            .components {
                                add(VideoFrameDecoder.Factory())
                            }
                            .build()

                        binding.img.load(imageLoader)
//                        binding.img.load(default) {
//                            videoFrameMillis(1000)
//                        }
                    } else{
                         binding.img.load(response.url)
                    }

                    binding.bottomSheetLayout.bottomSheetDescriptionHeader.text = response.title
                    binding.bottomSheetLayout.bottomSheetExplanation.text = response.explanation
                }
            }
        }
    }
}
