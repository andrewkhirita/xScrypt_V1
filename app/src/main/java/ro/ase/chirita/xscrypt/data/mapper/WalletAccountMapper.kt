package ro.ase.chirita.xscrypt.data.mapper

import android.util.Log
import com.elrond.erdkotlin.domain.account.models.Account
import ro.ase.chirita.xscrypt.domain.model.WalletAccountUi
import java.math.BigDecimal
import java.math.BigInteger

fun Account.toUi() = WalletAccountUi(
    address = address.bech32,
    balance = formatBigInteger(balance).also { Log.d("Account", "Balance: $it") },
)

private fun formatBigInteger(value: BigInteger): String {
    val divisor = BigDecimal.TEN.pow(18)
    return BigDecimal(value).divide(divisor).toString()
}

