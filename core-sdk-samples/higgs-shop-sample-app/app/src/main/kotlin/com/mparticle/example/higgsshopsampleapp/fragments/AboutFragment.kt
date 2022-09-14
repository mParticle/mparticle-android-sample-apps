package com.mparticle.example.higgsshopsampleapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("");
        inflater.context.setTheme(R.style.Theme_mParticle_SampleApp)
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("About")
    }
}