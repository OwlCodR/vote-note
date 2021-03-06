package com.votenote.net.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.R
import com.votenote.net.databinding.FragmentLoginBinding
import com.votenote.net.retrofit.common.Common
import com.votenote.net.retrofit.service.RetrofitServices
import com.votenote.net.ui.auth.AuthActivity
import com.votenote.net.ui.auth.AuthViewModel

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var inputPhone: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var textViewSignUp: TextView

    private lateinit var authActivity: AuthActivity

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private lateinit var retrofitService: RetrofitServices


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //log(context, "onCreateView()")

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.handler = this

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        inputPhone = binding.textInputLayoutPhone
        inputPassword = binding.textInputLayoutPassword
        countryCodePicker = binding.countryCodePicker
        textViewSignUp = binding.textViewSignUpNow

        navHostFragment =
            activity?.
            supportFragmentManager?.
            findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController

        retrofitService = Common.retrofitService

        authActivity = activity as AuthActivity
        binding.authActivity = authActivity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //log(context, "onViewCreated()")

        textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_nav_login_to_nav_register)
        }

        inputPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                authActivity.checkPasswordValidity(inputPassword)
            }
        })

        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            //log(context, "countryCodePicker.isValidFullNumber = $countryCodePicker.isValidFullNumber")
            if (countryCodePicker.isValidFullNumber) {
                setPhoneError(false)
            }
        }
    }

    fun onLogin() {
        val password: String = inputPassword.editText?.text.toString()
        val phone: String = countryCodePicker.fullNumberWithPlus.trim()

        val isPhoneValid = countryCodePicker.isValidFullNumber
        val isPasswordValid = authActivity.checkPasswordValidity(inputPassword)

        if (isPasswordValid && isPhoneValid) {
            // Toast.makeText(context, "YOU ARE LOGGED IN NOW", Toast.LENGTH_SHORT).show()
            // retrofitService
        } else {
            showSnackbar("Login error.\nCheck the correctness of the entered data.")

            if (!isPhoneValid) {
                setPhoneError(true)
            }
        }
    }

    private fun setPhoneError(isErrorEnabled: Boolean) {
        inputPhone.isErrorEnabled = isErrorEnabled
        if (isErrorEnabled)
            inputPhone.error = "Wrong phone number"
    }

    private fun showSnackbar(s: String) {
        Snackbar.make(requireView(), s, Snackbar.LENGTH_LONG).show()
    }
}

/*
@TODO Debug subtitle - Done
@TODO Error checker - Done
@TODO Retrofit - Done
@TODO onClick TextViews - Done
@TODO Move reading subtitles to SplashScreen
 */
