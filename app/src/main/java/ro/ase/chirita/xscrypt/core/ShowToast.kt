package ro.ase.chirita.xscrypt.core

import android.content.Context
import android.widget.Toast

object ShowToast {

    private var toast: Toast? = null

    fun show(context: Context, text: String) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }
}
