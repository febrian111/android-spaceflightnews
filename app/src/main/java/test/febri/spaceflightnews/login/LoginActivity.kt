package test.febri.spaceflightnews.login

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.google.android.material.snackbar.Snackbar
import test.febri.spaceflightnews.MainActivity
import test.febri.spaceflightnews.R
import test.febri.spaceflightnews.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var account: Auth0
    private lateinit var binding: ActivityLoginBinding
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // Handle case where the user denies permission
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the account object with the Auth0 application details
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            requestExactAlarmPermission()
        }

        requestNotificationPermission()

        // Bind the button click with the login action
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonLogin.setOnClickListener { loginWithBrowser() }
        binding.buttonLogout.setOnClickListener { logout() }
        binding.buttonGetMetadata.setOnClickListener { getUserMetadata() }
        binding.buttonPatchMetadata.setOnClickListener { patchUserMetadata() }
    }

    private fun updateUI() {
        binding.buttonLogout.isEnabled = cachedCredentials != null
        binding.metadataPanel.isVisible = cachedCredentials != null
        binding.buttonLogin.isEnabled = cachedCredentials == null
        binding.userProfile.isVisible = cachedCredentials != null

        binding.userProfile.text =
            "Name: ${cachedUserProfile?.name ?: ""}\n" +
                    "Email: ${cachedUserProfile?.email ?: ""}"

        if (cachedUserProfile == null) {
            binding.inputEditMetadata.setText("")
        }
    }

    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.
        WebAuthProvider.login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope("openid profile email read:current_user update:current_user_metadata")
            .withAudience("https://${getString(R.string.com_auth0_domain)}/api/v2/")

            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar("Failure: ${exception.getCode()}")
                }

                override fun onSuccess(credentials: Credentials) {
                    cachedCredentials = credentials
                    showSnackBar("Success: ${credentials.accessToken}")
                    updateUI()
                    showUserProfile()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            })
    }

    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!
                    cachedCredentials = null
                    cachedUserProfile = null
                    updateUI()
                }

                override fun onFailure(exception: AuthenticationException) {
                    updateUI()
                    showSnackBar("Failure: ${exception.getCode()}")
                }
            })
    }

    private fun showUserProfile() {
        val client = AuthenticationAPIClient(account)

        // Use the access token to call userInfo endpoint.
        // In this sample, we can assume cachedCredentials has been initialized by this point.
        client.userInfo(cachedCredentials!!.accessToken!!)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar("Failure: ${exception.getCode()}")
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile;
                    updateUI()
                }
            })
    }

    private fun getUserMetadata() {
        // Create the user API client
        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)

        // Get the full user profile
        usersClient.getProfile(cachedUserProfile!!.getId()!!)
            .start(object : Callback<UserProfile, ManagementException> {
                override fun onFailure(exception: ManagementException) {
                    showSnackBar("Failure: ${exception.getCode()}")
                }

                override fun onSuccess(userProfile: UserProfile) {
                    cachedUserProfile = userProfile;
                    updateUI()

                    val country = userProfile.getUserMetadata()["country"] as String?
                    binding.inputEditMetadata.setText(country)
                }
            })
    }

    private fun patchUserMetadata() {
        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)
        val metadata = mapOf("country" to binding.inputEditMetadata.text.toString())

        usersClient
            .updateMetadata(cachedUserProfile!!.getId()!!, metadata)
            .start(object : Callback<UserProfile, ManagementException> {
                override fun onFailure(exception: ManagementException) {
                    showSnackBar("Failure: ${exception.getCode()}")
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    updateUI()
                    showSnackBar("Successful")
                }
            })
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlertDialog.Builder(this)
                .setTitle("Allow Exact Alarms")
                .setMessage("To ensure proper session expiration, please allow exact alarms in settings.")
                .setPositiveButton("Go to Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    this.startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}