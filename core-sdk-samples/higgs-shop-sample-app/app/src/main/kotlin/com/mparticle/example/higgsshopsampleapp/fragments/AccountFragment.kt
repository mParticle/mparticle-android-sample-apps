package com.mparticle.example.higgsshopsampleapp.fragments

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.utils.theme.Shapes
import com.mparticle.example.higgsshopsampleapp.utils.theme.blue_4079FE
import com.mparticle.example.higgsshopsampleapp.utils.theme.gray_999999
import com.mparticle.example.higgsshopsampleapp.utils.theme.typography
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentAccountBinding
import com.mparticle.example.higgsshopsampleapp.viewmodels.AccountViewModel
import com.mparticle.identity.IdentityApiRequest


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("")


        binding = DataBindingUtil.inflate<FragmentAccountBinding>(
            inflater,
            R.layout.fragment_account,
            container,
            false
        )
            .apply {
                composeView.setContent {
                    AccountFragmentComposable()
                }
            }

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("My Account")
    }

    fun showIdentityAlert(message: String) {
        val parentLayout: View = requireActivity().findViewById(android.R.id.content)
        val snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT)
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)

        val tv = (snackbar.view.findViewById<TextView>(R.id.snackbar_text))
        tv?.textAlignment = View.TEXT_ALIGNMENT_CENTER

        snackbar.setBackgroundTint(requireContext().getColor(R.color.white))
        snackbar.setTextColor(requireContext().getColor(R.color.black))
        snackbar.setActionTextColor(
            getColor(
                requireContext().applicationContext,
                R.color.blue_4079FE
            )
        )
        snackbar.view.layoutParams = layoutParams
        snackbar.view.setPadding(0, 10, 0, 0)
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }

    @Composable
    fun AccountFragmentComposable() {

        val loggedIn by accountViewModel.loggedInResponseLiveData.observeAsState()
        var signInText = ""

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Text(
                stringResource(id = R.string.account_title),
                style = typography.h1,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(top = 30.dp)
            )
            if (loggedIn == false) {
                signInText = stringResource(id = R.string.account_cta_login)
                OutlinedTextField(
                    value = stringResource(R.string.account_user_value),
                    textStyle = typography.h5,
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(R.string.account_user_header),
                            color = Color.White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 30.dp)
                )
            }

            if (loggedIn == true) {
                signInText = stringResource(R.string.account_cta_logout)
                Text(
                    stringResource(id = R.string.account_user_header2),
                    style = typography.h1,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 30.dp)
                )
                Text(
                    stringResource(id = R.string.account_user_value),
                    style = typography.h2,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(
                            top = 10.dp,
                            bottom = 30.dp
                        ),
                    color = gray_999999
                )
            }
            Button(
                onClick = {

                    when (MParticle.getInstance()?.Identity()?.currentUser?.isLoggedIn) {
                        false -> {
                            //no user so login
                            val identityRequest = IdentityApiRequest.withEmptyUser()
                                .email("higgs@mparticle.com")
                                .customerId("higgs123456")
                                .build()
                            MParticle.getInstance()?.Identity()?.login(identityRequest)
                                ?.addSuccessListener {
                                    accountViewModel.login()
                                    showIdentityAlert("mParticle Login Call")
                                }
                        }
                        true -> {
                            //user exists in sample app so logout
                            MParticle.getInstance()?.Identity()?.logout()?.addSuccessListener {
                                accountViewModel.logout()
                                showIdentityAlert("mParticle Logout Call")
                            }
                        }
                        else -> {}
                    }
                },
                colors = ButtonDefaults.buttonColors(blue_4079FE),
                shape = Shapes.small,
                modifier = Modifier
                    .width(190.dp)
                    .height(50.dp)

            ) {
                Text(
                    signInText,
                    style = MaterialTheme.typography.button,
                    color = Color.White
                )
            }

            Text(
                stringResource(R.string.cart_disclaimer),
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .width(262.dp)
                    .wrapContentHeight()
                    .padding(top = 30.dp, bottom = 100.dp)
                    .alpha(0.6F),
                textAlign = TextAlign.Center
            )
        }
    }
}