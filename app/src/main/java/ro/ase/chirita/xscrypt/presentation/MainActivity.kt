package ro.ase.chirita.xscrypt.presentation

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.ActivityMainBinding
import ro.ase.chirita.xscrypt.presentation.import_wallet.ImportWalletFragment
import ro.ase.chirita.xscrypt.presentation.profile.ProfileFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openDialog()
    }

    private fun openDialog() {
        binding.fabMain.setOnClickListener {
            val dialogFragment = ImportWalletFragment()
            dialogFragment.show(supportFragmentManager,"ImportWalletFragment")
        }
    }

    override fun onStart() {
        super.onStart()
        if (!::navController.isInitialized) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
            navController = navHostFragment.navController
            setupWithNavController(binding.bnvMain, navController)
        }
        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.popBackStack(R.id.homeFragment, false)
                    }
                }
                R.id.profileFragment -> {
                    if (navController.currentDestination?.id != R.id.profileFragment) {
                        navController.navigate(R.id.profileFragment)
                    }
                }
            }
            true
        }
    }

}
