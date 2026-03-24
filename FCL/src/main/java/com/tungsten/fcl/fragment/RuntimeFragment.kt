package com.tungsten.fcl.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.SplashActivity
import com.tungsten.fcl.databinding.FragmentRuntimeBinding
import com.tungsten.fcl.util.ReadTools
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.FCLFragment
import com.tungsten.fcllibrary.component.theme.Theme
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.logging.Level

class RuntimeFragment : FCLFragment(), View.OnClickListener {
    private lateinit var bind: FragmentRuntimeBinding
    var lwjgl = false
    var cacio = false
    var cacio17 = false
    var java8 = false
    var java25 = false
    var java17 = false
    var java21 = false
    var jna = false
    var gameResource = false
    var others = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_runtime, container, false)
        bind = FragmentRuntimeBinding.bind(view)
        var textColor: Int = ThemeEngine.getInstance().getTheme().getColor2()
        setColorByTag(view, "runtime_text", textColor)
        bind.install.setOnClickListener(this)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { initState() }
            refreshDrawables()
            check()
        }
        return view
    }

    private fun setColorByTag(view: View, tag: String, color: Int) {
        if (view is TextView && view.tag == tag) {
            view.setTextColor(color)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setColorByTag(view.getChildAt(i), tag, color)
            }
        }
    }

    private fun initState() {
        lwjgl = (activity as SplashActivity).lwjgl
        cacio = (activity as SplashActivity).cacio
        cacio17 = (activity as SplashActivity).cacio17
        java8 = (activity as SplashActivity).java8
        java17 = (activity as SplashActivity).java17
        java21 = (activity as SplashActivity).java21
        java25 = (activity as SplashActivity).java25
        jna = (activity as SplashActivity).jna
        gameResource = (activity as SplashActivity).gameResource
        others = (activity as SplashActivity).others
    }

    private fun refreshDrawables() {
        if (context != null) {
            val stateUpdate =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_update_24)
            val stateDone =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_done_24)

            stateUpdate?.setTint(Color.RED)
            stateDone?.setTint(Color.GREEN)

            bind.apply {
                lwjglState.setBackgroundDrawable(if (lwjgl) stateDone else stateUpdate)
                cacioState.setBackgroundDrawable(if (cacio) stateDone else stateUpdate)
                cacio17State.setBackgroundDrawable(if (cacio17) stateDone else stateUpdate)
                java8State.setBackgroundDrawable(if (java8) stateDone else stateUpdate)
                java17State.setBackgroundDrawable(if (java17) stateDone else stateUpdate)
                java21State.setBackgroundDrawable(if (java21) stateDone else stateUpdate)
                java25State.setBackgroundDrawable(if (java25) stateDone else stateUpdate)
                jnaState.setBackgroundDrawable(if (jna) stateDone else stateUpdate)
                gameResourceState.setBackgroundDrawable(if (gameResource) stateDone else stateUpdate)
                othersState.setBackgroundDrawable(if (others) stateDone else stateUpdate)
            }
        }
    }

    private val isLatest: Boolean
        get() = lwjgl && cacio && cacio17 && java8 && java17 && java21 && java25 && jna && gameResource

    private fun check() {
        if (!isLatest) return
        if (others) {
            if (!installing) {
                (activity as SplashActivity).enterLauncher()
            } else {
                (activity as SplashActivity).finish()
                System.exit(0)
            }
        } else if (installing) {
            checkOthers()
        }
    }

    private var installingOthers = false
    private fun checkOthers() {
        if (installingOthers) return
        installingOthers = true
        bind.apply {
            if (!others) {
                othersProgress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.reloadConfiguration(context)
                            RuntimeUtils.copyAssetsDirToLocalDir(context, "modpackInternal", FCLPath.INTERNAL_DIR)
                            others = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    othersState.visibility = View.VISIBLE
                    othersProgress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }.start()
            }
        }
    }

    private var installing = false
    private fun install() {
        if (installing) return
        installing = true
        bind.apply {
            if (!gameResource) {
                gameResourceState.visibility = View.GONE
                gameResourceProgress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.deleteOldFiles(context)
                            RuntimeUtils.copyAssetsDirToLocalDir(context, "modpackExternal", FCLPath.EXTERNAL_DIR)
                            gameResource = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    gameResourceState.visibility = View.VISIBLE
                    gameResourceProgress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }.start()
            }
            if (!lwjgl) {
                lwjglState.visibility = View.GONE
                lwjglProgress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.install(context, FCLPath.LWJGL_DIR, "app_runtime/lwjgl")
                            lwjgl = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    lwjglState.visibility = View.VISIBLE
                    lwjglProgress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!cacio) {
                cacioState.visibility = View.GONE
                cacioProgress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.install(context, FCLPath.CACIOCAVALLO_8_DIR, "app_runtime/caciocavallo")
                            cacio = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    cacioState.visibility = View.VISIBLE
                    cacioProgress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!cacio17) {
                cacio17State.visibility = View.GONE
                cacio17Progress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.install(context, FCLPath.CACIOCAVALLO_17_DIR, "app_runtime/caciocavallo17")
                            cacio17 = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    cacio17State.visibility = View.VISIBLE
                    cacio17Progress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!java8) {
                java8State.visibility = View.GONE
                java8Progress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.installJava(context, FCLPath.JAVA_8_PATH, "app_runtime/java/jre8")
                            java8 = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    java8State.visibility = View.VISIBLE
                    java8Progress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!java17) {
                java17State.visibility = View.GONE
                java17Progress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.installJava(context, FCLPath.JAVA_17_PATH, "app_runtime/java/jre17")
                            java17 = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    java17State.visibility = View.VISIBLE
                    java17Progress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!java21) {
                java21State.visibility = View.GONE
                java21Progress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.installJava(context, FCLPath.JAVA_21_PATH, "app_runtime/java/jre21")
                            java21 = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    java21State.visibility = View.VISIBLE
                    java21Progress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!java25) {
                java25State.visibility = View.GONE
                java25Progress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.installJava(context, FCLPath.JAVA_25_PATH, "app_runtime/java/jre25")
                            java25 = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    java25State.visibility = View.VISIBLE
                    java25Progress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!jna) {
                jnaState.visibility = View.GONE
                jnaProgress.visibility = View.VISIBLE
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        runCatching {
                            RuntimeUtils.installJna(context, FCLPath.JNA_PATH, "app_runtime/jna")
                            jna = true
                        }.exceptionOrNull()?.let { showErrorDialog(it) }
                    }
                    jnaState.visibility = View.VISIBLE
                    jnaProgress.visibility = View.GONE
                    refreshDrawables()
                    check()
                }
            }
            if (!others) {
                othersState.visibility = View.GONE
                check()
            }
        }
    }

    override fun onClick(view: View) {
        if (view === bind.install) {
            install()
        }
    }

    private fun showErrorDialog(e: Throwable) {
        Logging.LOG.log(Level.SEVERE, "Failed to install assets", e)
        installing = false
        lifecycleScope.launch(Dispatchers.Main){
            FCLAlertDialog.Builder(requireContext())
                .setMessage(e.toString())
                .setPositiveButton{
                }
                .create()
                .show()
        }
    }
}
