package com.mparticle.example.higgsshopsampleapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentAccountBinding
import com.mparticle.example.higgsshopsampleapp.viewmodels.AccountViewModel
import com.mparticle.identity.IdentityApiRequest


class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("");
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("My Account")
        val btnCTA = activity?.findViewById(R.id.account_cta) as Button

        accountViewModel.loggedInResponseLiveData.observe(
            viewLifecycleOwner,
            { loggedIn ->
                when(loggedIn) {
                    false -> {
                        btnCTA.text = getString(R.string.account_cta_login)
                    }
                    true -> {
                        btnCTA.text = getString(R.string.account_cta_logout)
                    }
                }
            })

        btnCTA.setOnClickListener {
            when(MParticle.getInstance()?.Identity()?.currentUser?.isLoggedIn) {
                false -> {
                    //no user so login
                    val identityRequest = IdentityApiRequest.withEmptyUser()
                        .email("higgs@example.com")
                        .customerId("123456")
                        .build()
                    MParticle.getInstance()?.Identity()?.login(identityRequest)?.addSuccessListener {
                        accountViewModel.login()
                    }
                }
                true -> {
                    //user exists in sample app so logout
                    MParticle.getInstance()?.Identity()?.logout()?.addSuccessListener {
                        accountViewModel.logout()
                    }
                }
            }
        }
    }
}