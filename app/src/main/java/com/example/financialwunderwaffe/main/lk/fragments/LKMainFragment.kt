package com.example.financialwunderwaffe.main.lk.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.database.AppDatabase
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.lk.LKFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LKMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lk_main, container, false)

        view.findViewById<LinearLayout>(R.id.linearLayoutEmailLK).setOnClickListener {
            emailLK()
        }

        view.findViewById<LinearLayout>(R.id.linearLayoutTestLK).setOnClickListener {
            testLK()
        }

        view.findViewById<ImageView>(R.id.imageViewExitLK).setOnClickListener {
            exitLK()
        }

        view.findViewById<TextView>(R.id.textViewExitLK).setOnClickListener {
            exitLK()
        }

        return view
    }

    private fun emailLK() {
        (activity as MainActivity).toast("Этот функционал скоро станет доступным")
    }

    private fun testLK() =
        (parentFragment as LKFragment).goToFragment(
            (parentFragment as LKFragment).lkQuestionnaireFragment
        )

    private fun exitLK()  =
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase((activity as MainActivity).context).getUserInfoDao().deleteUserInfo(
                AppDatabase.getDatabase((activity as MainActivity).context).getUserInfoDao().getInfo()[0]
            )
            (activity as MainActivity).goToWelcomeActivity()
        }

}