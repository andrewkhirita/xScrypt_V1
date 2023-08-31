package ro.ase.chirita.xscrypt.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.core.ShowToast
import ro.ase.chirita.xscrypt.databinding.FragmentProfileBinding
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.presentation.health_records.HealthRecordsFragment
import ro.ase.chirita.xscrypt.presentation.medical_id.MedicalIdFragment

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        setupViewPager(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFragment", "onCreate() called. Fragment ID: " + this.hashCode())
        setCollapsingWithUserInfo()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(MedicalIdFragment(), "xPass")
        adapter.addFragment(HealthRecordsFragment(), "Records")
        viewPager.adapter = adapter
    }

    private fun setCollapsingWithUserInfo() {
        if (user == null) {
            viewModel.getCurrentUser()
            viewModel.getCurrentUserResponse.removeObservers(viewLifecycleOwner)
            viewModel.getCurrentUserResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        user = response.data
                        binding.collapsing.setProfileName(user!!.name)
                        binding.collapsing.setSubtitle(user!!.email)
                        binding.collapsing.setMisc("")
                        val collapsingHeaderView = binding.collapsing
                        Glide.with(this).load(user!!.photoUrl).into(collapsingHeaderView.profileImage)
                    }
                    is Response.Failure -> {
                        ShowToast.show(requireContext(),"${response.e.message}")
                    }
                }
            }
        } else {
            binding.collapsing.setProfileName(user!!.name)
            binding.collapsing.setSubtitle(user!!.email)
            Glide.with(this).load(user!!.photoUrl).into(binding.collapsing.profileImage)
        }
    }


    internal class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val fragmentTitleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }
    }



}