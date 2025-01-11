package com.example.financialwunderwaffe.main.briefcase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.FragmentMainBriefcaseBinding
import com.example.financialwunderwaffe.main.briefcase.fragments.assets.BriefcaseAssetsFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.BriefcaseSharesOfAssetsFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.strategies.BriefcaseStrategiesFragment

class BriefcaseFragment : Fragment() {

    private var _binding: FragmentMainBriefcaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBriefcaseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textViewTitleBriefcase.text = getString(R.string.assets)

        binding.imageViewMenuBriefcase.setOnClickListener{
            binding.drawerBriefcase.openDrawer(GravityCompat.START)
        }

        binding.navigationBriefcase.setNavigationItemSelectedListener { menuitem ->
            when(menuitem.itemId) {
                R.id.nav_briefcase_strategies -> {
                    binding.textViewTitleBriefcase.text = getString(R.string.strategies)
                    go_to_fragment(BriefcaseStrategiesFragment())
                }
                R.id.nav_briefcase_shares_of_assets -> {
                    binding.textViewTitleBriefcase.text = getString(R.string.shares_of_assets)
                    go_to_fragment(BriefcaseSharesOfAssetsFragment())
                }
                R.id.nav_briefcase_assets -> {
                    binding.textViewTitleBriefcase.text = getString(R.string.assets)
                    go_to_fragment(BriefcaseAssetsFragment())
                }
            }
            binding.drawerBriefcase.closeDrawer(GravityCompat.START)
            true
        }

        go_to_fragment(BriefcaseAssetsFragment())

        return root
    }

    private fun go_to_fragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.container_briefcase, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}