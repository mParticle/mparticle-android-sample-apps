package com.mparticle.example.higgsshopsampleapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.mparticle.MParticle
import com.mparticle.identity.AliasRequest
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentWebviewBridgeBinding

class WebViewBridgeFragment : Fragment() {

    private lateinit var binding: FragmentWebviewBridgeBinding

    private val tag = "WebViewBridgeFragment"
    private val bridgeName = "higgsWebviewBridge"
    private val assetUrl = "file:///android_asset/webview_bridge_test.html"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("")
        inflater.context.setTheme(R.style.Theme_mParticle_SampleApp)
        binding = FragmentWebviewBridgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // All events should come from JS, not native - screen view will be logged from WebView

        binding.btnAliasNative.setOnClickListener {
            aliasUsersNative()
        }

        configureWebView(binding.webview)
        // Per docs, the WebView must be registered before loading content.
        MParticle.getInstance()?.registerWebView(binding.webview, bridgeName)
        binding.webview.loadUrl(assetUrl)
    }

    private fun configureWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                injectAliasRequestIntoPage(view)
            }
        }
    }

    /**
     * Inject MPIDs into the page so the WebView can send alias requests via mParticle.Identity.aliasUsers()
     */
    private fun injectAliasRequestIntoPage(webView: WebView) {
        val identity = MParticle.getInstance()?.Identity() ?: return
        val users = try {
            identity.users
        } catch (_: Throwable) {
            identity.getUsers()
        }
        if (users.size < 2) return

        // Match docs: users are in reverse chronological order:
        // source = users[1], destination = users[0]
        val sourceUser = users[1]
        val destinationUser = users[0]

        val sourceMpid = try {
            sourceUser.id
        } catch (_: Throwable) {
            sourceUser.getId()
        }.toString()

        val destinationMpid = try {
            destinationUser.id
        } catch (_: Throwable) {
            destinationUser.getId()
        }.toString()

        Log.i(tag, "Mansi sourceMpid=$sourceMpid destinationMpid=$destinationMpid")

        if (sourceMpid.isBlank() || destinationMpid.isBlank()) return
        if (sourceMpid == destinationMpid) return

        val now = System.currentTimeMillis()
        val startTime = now - (30 * 60 * 1000)
        val endTime = now

        val js =
            "window.__mp = window.__mp || {};" +
                "window.__mp._pendingAliasRequest = {sourceMpid:'$sourceMpid',destinationMpid:'$destinationMpid',startTime:$startTime,endTime:$endTime,scope:'device'};" +
                "if (window.setNativeAliasRequest) { window.setNativeAliasRequest(window.__mp._pendingAliasRequest); }"
        webView.evaluateJavascript(js, null)
    }

    /**
     * Native alias invoked from the fragment button (and from the WebView bridge).
     * Uses the "working" approach: source=users[1], destination=users[0], time window = last 30 minutes.
     */

    // Kept native button as-is (optional): it calls the Android SDK alias directly.
    private fun aliasUsersNative() {
        val identity = MParticle.getInstance()?.Identity() ?: return
        val users = try {
            identity.users
        } catch (_: Throwable) {
            identity.getUsers()
        }
        if (users.size < 2) {
            binding.subtitle.text = getString(R.string.webview_bridge_alias_need_two_users)
            return
        }
        val sourceUser = users[1]
        val destinationUser = users[0]
        val now = System.currentTimeMillis()
        val request: AliasRequest = AliasRequest.Builder()
            .sourceMpid(sourceUser.id.toString().toLongOrNull() ?: 0L)
            .destinationMpid(destinationUser.id.toString().toLongOrNull() ?: 0L)
            .startTime(now - (30 * 60 * 1000))
            .endTime(now)
            .build()
        identity.aliasUsers(request)
    }

    override fun onDestroyView() {
        // Avoid leaking the WebView.
        binding.webview.apply {
            stopLoading()
            loadUrl("about:blank")
            clearHistory()
            removeAllViews()
            destroy()
        }
        super.onDestroyView()
    }
}

