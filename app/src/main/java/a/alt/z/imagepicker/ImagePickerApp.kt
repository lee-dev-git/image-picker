package a.alt.z.imagepicker

import android.app.Application
import timber.log.LogcatTree
import timber.log.Timber

class ImagePickerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(LogcatTree("imagepicker-app"))
    }
}