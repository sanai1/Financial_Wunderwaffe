package com.example.financialwunderwaffe.main.briefcase

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.financialwunderwaffe.R

class BriefcaseDialogAssetAdd(
    private val toast: (String) -> Unit,
    private val callback: (String) -> Unit
) : DialogFragment() {
    private var listAsset = listOf<String>()

    fun setAssetList(listAssetNew: List<String>) {
        listAsset = listAssetNew
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_fragment_briefcase_asset_add, null)

        val spinner = view.findViewById<Spinner>(R.id.spinnerAsset)
        val editText = view.findViewById<EditText>(R.id.editTextTextAssetTitle)
        spinner.apply {
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listAsset
            )
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = arrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    (view as? TextView)?.apply {
                        gravity = Gravity.CENTER
                        setTextColor(Color.BLACK)
                        textSize = 15f
                    }
                    editText.visibility = when (listAsset[position]) {
                        "Свой вариант" -> View.VISIBLE
                        else -> View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        view.findViewById<Button>(R.id.buttonAssetAdd).setOnClickListener {
            var title = spinner.selectedItem.toString()
            if (title == "Свой вариант") {
                title = editText.text.toString()
                if (title.isEmpty()) {
                    toast("Введите название актива")
                }
            }
            dismiss()
            callback(title)
        }

        return view
    }

}