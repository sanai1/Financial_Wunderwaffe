package com.example.financialwunderwaffe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible

class LoadingFragment : Fragment() {

    lateinit var text: String
    lateinit var visible: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.loading, container, false)

        if (this::text.isInitialized)
            view.findViewById<TextView>(R.id.textViewLoading).text = text
        if (this::visible.isInitialized && visible == "false")
            view.findViewById<ProgressBar>(R.id.progressBarLoading).isVisible = false

        return view
    }

}