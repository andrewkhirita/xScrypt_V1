package ro.ase.chirita.xscrypt.presentation.create_wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.ActivityCreateWalletBinding
import ro.ase.chirita.xscrypt.domain.model.CreateWalletAction
import ro.ase.chirita.xscrypt.domain.model.CreateWalletViewState
import ro.ase.chirita.xscrypt.presentation.MainActivity

@AndroidEntryPoint
class CreateWalletActivity : AppCompatActivity() {

    private val viewModel: CreateWalletViewModel by viewModels()
    private lateinit var binding: ActivityCreateWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.generateMnemonic()

        val email = intent.getStringExtra("email")

        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is CreateWalletViewState.GeneratedMnemonic -> updateView(viewState,email)
            }
        }

        viewModel.viewAction.observe(this) { viewAction ->
            when (viewAction){
                is CreateWalletAction.CloseScreen -> {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else -> {}
            }
        }
    }

    private fun updateView(viewState: CreateWalletViewState.GeneratedMnemonic, email: String?) {
        val newMnemonic = viewModel.refactorSeedPhraseInView(viewState.mnemonic)
        binding.list.text = newMnemonic
        updateWalletAddress(viewState, email)
        updateCopyAddress(viewState)
    }

    private fun updateCopyAddress(viewState: CreateWalletViewState.GeneratedMnemonic) {
        binding.ibCopy.setOnClickListener {
            viewModel.copySeedPhrase(viewState.mnemonic, applicationContext)
            val bgColor = ContextCompat.getColor(this, R.color.neon_v2)
            val textColor = ContextCompat.getColor(this, R.color.black)
            val snackbar = Snackbar.make(binding.root, "Seed phrase copied", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(bgColor)
            snackbar.setTextColor(textColor)
            snackbar.show()
        }
    }

    private fun updateWalletAddress(
        viewState: CreateWalletViewState.GeneratedMnemonic,
        email: String?
    ) {
        binding.btnCreateWallet.setOnClickListener {
            binding.btnCreateWallet.visibility = View.GONE
            binding.tvCreateWalletTermsOfUse.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.saveWallet(viewState.mnemonic, email!!)
        }
    }
}