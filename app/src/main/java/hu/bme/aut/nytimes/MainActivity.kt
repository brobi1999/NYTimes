package hu.bme.aut.nytimes

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import co.zsmb.rainbowcake.navigation.SimpleNavActivity
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.nytimes.ui.list.ListFragment

@AndroidEntryPoint
class MainActivity : SimpleNavActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigator.add(ListFragment())
        }
    }

}

