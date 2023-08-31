package ro.ase.chirita.xscrypt.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ro.ase.chirita.xscrypt.presentation.sign_in.SignInFragment
import ro.ase.chirita.xscrypt.presentation.sign_up.SignUpFragment

class AuthenticationAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> {
                return SignUpFragment()
            }
        }
        return SignInFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }

}
