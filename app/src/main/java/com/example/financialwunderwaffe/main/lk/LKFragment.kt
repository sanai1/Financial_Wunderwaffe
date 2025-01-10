package com.example.financialwunderwaffe.main.lk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.R
//import androidx.lifecycle.ViewModelProvider
import com.example.financialwunderwaffe.databinding.FragmentMainLkBinding
import com.example.financialwunderwaffe.main.lk.fragments.main.LKMainFragment
import com.example.financialwunderwaffe.main.lk.fragments.questionnaire.LKQuestionnaireFragment

class LKFragment : Fragment() {

    private var _binding: FragmentMainLkBinding? = null
    private val binding get() = _binding!!
    lateinit var lkMainFragment: LKMainFragment
    lateinit var lkQuestionnaireFragment: LKQuestionnaireFragment
    lateinit var loadingFragment: LoadingFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(LKViewModel::class.java)

        _binding = FragmentMainLkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lkMainFragment = LKMainFragment()
        lkQuestionnaireFragment = LKQuestionnaireFragment()
        loadingFragment = LoadingFragment()

//        val textView: TextView = binding.textLk
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        goToFragment(lkMainFragment)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun goToFragment(fragment: Fragment) {
        val lk = childFragmentManager.beginTransaction()
        lk.replace(R.id.container_lk, fragment)
        lk.addToBackStack(null)
        lk.commit()
    }

}