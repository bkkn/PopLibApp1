package me.bkkn.poplibapp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.bkkn.poplibapp1.R
import me.bkkn.poplibapp1.databinding.FragmentWikiBottomSheetBinding
import me.bkkn.poplibapp1.Const.wiki_base_url
import me.bkkn.poplibapp1.Const.wiki_request_key

class WikiBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWikiBottomSheetBinding
    private lateinit var query: String
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wiki_bottom_sheet, container, false)
        binding = FragmentWikiBottomSheetBinding.bind(view)
        query = arguments?.getString(wiki_request_key).toString()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }
        binding.webview.loadUrl(wiki_base_url + query)
    }
}