package unal.edu.co.reto9

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class POITypeDialogFragment : DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var listener: POITypeDialogListener? = null

    interface POITypeDialogListener {
        fun onPOITypesUpdated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is POITypeDialogListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement POITypeDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val selectedTypes = sharedPreferences.getStringSet("poi_types", null) ?: emptySet()
        
        val poiTypes = POICategory.values().map { it.display }.toTypedArray()
        val checkedItems = poiTypes.map { it in selectedTypes }.toBooleanArray()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccionar Categorías")
            .setMultiChoiceItems(poiTypes, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("Guardar") { _, _ ->
                val selected = poiTypes.filterIndexed { index, _ -> checkedItems[index] }.toSet()
                sharedPreferences.edit().putStringSet("poi_types", selected).apply()
                listener?.onPOITypesUpdated()
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}