package com.example.financialwunderwaffe.main.lk.fragments.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.financialwunderwaffe.R

class LKDialogExitMainFragment:
    DialogFragment(),
    View.OnClickListener
{
    private lateinit var dialogExitMainFragment: DialogExitMainFragment

    interface DialogExitMainFragment {
        fun onDialogExitMainFragmentClickListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_fragment_lk_main_clarify_exit, null)

        view.findViewById<Button>(R.id.buttonClarifyExitMainLK).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        dialogExitMainFragment.onDialogExitMainFragmentClickListener()
        dismiss()
    }

    fun setDialogExitMainFragment(listener: DialogExitMainFragment) {
        dialogExitMainFragment = listener
    }

}