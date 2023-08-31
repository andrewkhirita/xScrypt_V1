package ro.ase.chirita.xscrypt.presentation.auth

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.components.AuthenticationAdapter
import ro.ase.chirita.xscrypt.databinding.ActivityAuthenticationBinding

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private var authenticationAdapter: AuthenticationAdapter? = null
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
        setupTabLayout()
        setupKeyboard()
    }

    private fun setupViewPager() {
        authenticationAdapter = AuthenticationAdapter(supportFragmentManager, lifecycle)
        binding.vpProfileFragment.apply {
            offscreenPageLimit = 2
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = authenticationAdapter
            registerOnPageChangeCallback(onPageChangeCallback())
        }
    }

    private fun setupTabLayout() {
        binding.tlAuthenticationActivity.apply {
            addTab(newTab().setText("Sign In"))
            addTab(newTab().setText("Sign Up"))
            addOnTabSelectedListener(onTabSelectedListener())
        }
    }

    private fun onPageChangeCallback() = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.tlAuthenticationActivity.selectTab(binding.tlAuthenticationActivity.getTabAt(position))
        }
    }

    private fun onTabSelectedListener() = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            binding.vpProfileFragment.currentItem = tab.position
        }
        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboard() {
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        v.clearFocus()
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
            false
        }
    }

}