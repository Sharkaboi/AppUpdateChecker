package com.github.sharkaboi.appupdatechecker.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.sharkaboi.appupdatechecker.sample.databinding.ActivityMainBinding
import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerException
import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidEndPointException
import com.sharkaboi.appupdatechecker.models.InvalidPackageNameException
import com.sharkaboi.appupdatechecker.models.InvalidRepositoryNameException
import com.sharkaboi.appupdatechecker.models.InvalidUserNameException
import com.sharkaboi.appupdatechecker.models.InvalidVersionException
import com.sharkaboi.appupdatechecker.models.PackageNotFoundException
import com.sharkaboi.appupdatechecker.models.RemoteError
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.sources.github.GithubTagSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is AppUpdateCheckerException) {
            when (throwable) {
                is GenericError -> {
                    // Handle explicitly if needed
                }

                is InvalidEndPointException -> {
                    // Handle explicitly if needed
                }

                is InvalidPackageNameException -> {
                    // Handle explicitly if needed
                }

                is InvalidRepositoryNameException -> {
                    // Handle explicitly if needed
                }

                is InvalidUserNameException -> {
                    // Handle explicitly if needed
                }

                is InvalidVersionException -> {
                    // Handle explicitly if needed
                }

                is PackageNotFoundException -> {
                    // Handle explicitly if needed
                }

                is RemoteError -> {
                    // Handle explicitly if needed
                }
            }
        }

        binding.tvUpdateDetails.text =
            "Error occurred when checking for updates:\n\n$throwable"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ideally manage this using your DI implementation, creates a retrofit service underneath.
        val updateChecker = AppUpdateChecker(
            source = GithubTagSource(
                ownerUsername = "Sharkaboi",
                repoName = "AppUpdateChecker",
                currentVersion = "v0.0.0"
            )
        )

        binding.tvUpdateDetails.text = "Checking for updates"

        lifecycleScope.launch(errorHandler) {
            // Automatically switches to IO thread behind the scenes.
            binding.tvUpdateDetails.text = when (val result = updateChecker.checkUpdate()) {
                UpdateResult.NoUpdate -> "Checking for updates"
                is UpdateResult.UpdateAvailable<*> -> "Update found : " + result.versionDetails.toString()
            }
        }
    }
}