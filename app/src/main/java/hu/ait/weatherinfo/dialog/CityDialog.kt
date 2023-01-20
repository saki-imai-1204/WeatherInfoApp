package hu.ait.weatherinfo.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import hu.ait.weatherinfo.data.City
import hu.ait.weatherinfo.databinding.DialogBinding

class CityDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    interface ItemDialogHandler {
        fun itemCreated(city: City)
    }

    lateinit var itemDialogHandler: ItemDialogHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemDialogHandler) {
            itemDialogHandler = context
        } else {
            throw java.lang.RuntimeException(
                "The activity does not implement the ItemHandler interface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("New City")

        val dialogViewBinding = DialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->
            if (dialogViewBinding.etCity.text!!.isNotEmpty()){
                itemDialogHandler.itemCreated(
                    City(
                        null,
                        dialogViewBinding.etCity.text.toString()
                    )
                )
            }
        }

        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }


        return dialogBuilder.create()
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long){
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

}