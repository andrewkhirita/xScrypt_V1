package ro.ase.chirita.xscrypt.presentation.wallet_account


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.components.WalletAccountAdapter
import ro.ase.chirita.xscrypt.databinding.ActivityWalletAccountBinding

@AndroidEntryPoint
class WalletAccountActivity : AppCompatActivity(){

    private var authenticationAdapter: WalletAccountAdapter? = null
    private lateinit var binding: ActivityWalletAccountBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpWalletActivity.offscreenPageLimit = 3
        binding.vpWalletActivity.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        getPageFragmentSelected()
    }

    private fun getPageFragmentSelected() {
        val fragmentManager = supportFragmentManager
        authenticationAdapter = WalletAccountAdapter(fragmentManager, lifecycle)
        binding.vpWalletActivity.adapter = authenticationAdapter
        binding.tlWalletActivity.addTab(binding.tlWalletActivity.newTab().setText("Account"))
        binding.tlWalletActivity.addTab(binding.tlWalletActivity.newTab().setText("Transactions"))
        binding.tlWalletActivity.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpWalletActivity.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.vpWalletActivity.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tlWalletActivity.selectTab(binding.tlWalletActivity.getTabAt(position))
            }
        })
    }

}