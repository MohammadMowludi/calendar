package com.byagowi.persiancalendar

import android.app.Application
import androidx.annotation.Keep
import com.byagowi.persiancalendar.utils.initUtils

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
       // ReleaseDebugDifference.mainApplication(this)
        initUtils(applicationContext)
    }

    // Can I haz these resources not removed?!
    // Workaround for weird AGP 4.1.0 >= used resource removal issues
    @Keep
    private val heyAndroidBuildToolsWeNeedTheseAndItIsUnbelievableYouAreRemovingThem = listOf(
        R.drawable.blue_shade_background, R.raw.abdulbasit
    )
}
