package ro.ase.chirita.xscrypt.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditTextPersonalized(context: Context?, attrs: AttributeSet?) :
    AppCompatEditText(context!!, attrs) {
    override fun setError(error: CharSequence, icon: Drawable) {
        setCompoundDrawables(null, null, icon, null)
    }
}