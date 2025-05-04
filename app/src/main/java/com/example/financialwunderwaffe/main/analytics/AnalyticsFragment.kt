package com.example.financialwunderwaffe.main.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.FragmentMainAnalyticsBinding
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.analytics.fragments.AnalyticsAssetFragment
import com.example.financialwunderwaffe.main.analytics.fragments.budget.AnalyticsBudgetFragment
import com.example.financialwunderwaffe.main.analytics.fragments.AnalyticsCapitalFragment

class AnalyticsFragment : Fragment() {
    private var _binding: FragmentMainAnalyticsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: AnalyticsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainAnalyticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = (activity as MainActivity).analyticsViewModel

        viewModel.title.observe(viewLifecycleOwner) {
            binding.textViewTitleAnalytics.text = it
        }

        binding.imageViewMenuAnalytics.setOnClickListener {
            binding.drawerAnalytics.openDrawer(GravityCompat.START)
        }

        binding.navigationAnalytics.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_analytics_budget -> {
                    viewModel.setTitle(getString(R.string.title_budget))
                    goToFragment(AnalyticsBudgetFragment())
                }

                R.id.nav_analytics_capital -> {
                    viewModel.setTitle(getString(R.string.capital))
                    goToFragment(AnalyticsCapitalFragment())
                }

                R.id.nav_analytics_assets -> {
                    viewModel.setTitle(getString(R.string.assets))
                    goToFragment(AnalyticsAssetFragment())
                }
            }
            binding.drawerAnalytics.closeDrawer(GravityCompat.START)
            true
        }
        goToFragment(AnalyticsBudgetFragment())
        viewModel.setTitle(getString(R.string.title_budget))

        return root
    }

    private fun goToFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.container_analytics, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}