package unal.edu.co.reto9

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class RadiusDialogFragment : DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var listener: RadiusDialogListener? = null

    interface RadiusDialogListener {
        fun onRadiusUpdated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RadiusDialogListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement RadiusDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val input = TextInputEditText(requireContext())
        input.hint = "Número máximo de lugares"
        input.setText(sharedPreferences.getInt("max_entries", 50).toString())

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Configurar límite de lugares")
            .setMessage("Ingrese el número máximo de lugares a mostrar")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val maxEntries = input.text.toString().toIntOrNull() ?: 50
                sharedPreferences.edit().putInt("max_entries", maxEntries).apply()
                listener?.onRadiusUpdated()
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}