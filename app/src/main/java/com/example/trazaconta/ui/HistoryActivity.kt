package com.example.trazaconta.ui

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trazaconta.R
import com.example.trazaconta.adapter.HistoryItemViewAdapter
import com.example.trazaconta.adapter.HistoryViewAdapter
import com.example.trazaconta.data.GuestDataBase
import com.example.trazaconta.data.SecurityPreferences
import com.example.trazaconta.databinding.ActivityHistoryBinding
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.utils.Constants
import com.example.trazaconta.viewModel.HistoryViewModel
import com.example.trazaconta.viewModel.HistoryViewModelFactory
import java.util.concurrent.CompletableFuture


class HistoryActivity : AppCompatActivity(), HistoryViewAdapter.onClickListener,
   HistoryViewAdapter.EstablishmentListCallback {
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val bind by lazy { ActivityHistoryBinding.inflate(layoutInflater)}
    private  val factory : HistoryViewModelFactory by lazy { HistoryViewModelFactory(GuestDataBase.getDatabase(this).guestDao()) }
    private val adapter by lazy { HistoryViewAdapter(this,this,this) }
    private val viewModel by lazy { ViewModelProvider(this,factory)[HistoryViewModel::class.java] }
    private val mShared by lazy { SecurityPreferences(this) }
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        toolbar()
        bind.recyclerHistory.layoutManager = layoutManager
        bind.recyclerHistory.adapter = adapter
        observers()
        attView()


    }

    private fun observers()
    {
        viewModel.establishment.observe(this) { establishmentList ->
            adapter.submitList(establishmentList)

        }
    }


    private fun attView()
    {
        viewModel.getEstablishment(Constants.FILTER.ins)
    }


    private fun toolbar()
    {
        toolbar = bind.toolbarHistory
        toolbar.title=""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun delete(establishment: Establishment)
    {
        confirmDialog().thenAccept { confirmed ->
            if (confirmed) {
                viewModel.deleteEstablishment(establishment.id, establishment)
            }
        }
    }

    override fun getIDAndName(id: Long, name: String) {
       startActivity(id,name)

    }

   private fun startActivity(id:Long,name: String)
    {
        mShared.saveFK(Constants.KEYS.toListItem,id)
        val bundle = Bundle()
        bundle.putString("keyName", name)
        val i = Intent(this, HistoryItemActivity::class.java)
        i.putExtras(bundle)
        startActivity(i)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        menu()
        return true
    }


    private fun menu()
    {
        val menu =  bind.toolbarHistory.menu
        menu.findItem(R.id.tollbar_prec_desc).title = "Data Decrescente"
        menu.findItem(R.id.tollbar_prec_cresc).title = "Data Crescente"
        menu.findItem(R.id.tollbar_ordem_alfabetica).title = "Ordem Alfabética"
        menu.findItem(R.id.tollbar_entrada).title = "Ordem de Inserção"

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
          R.id.tollbar_prec_desc -> viewModel.getEstablishment(Constants.FILTER.desc)

            R.id.tollbar_prec_cresc -> viewModel.getEstablishment(Constants.FILTER.asc)

            R.id.tollbar_ordem_alfabetica ->  viewModel.getEstablishment(Constants.FILTER.alf)

            R.id.tollbar_entrada ->  viewModel.getEstablishment(Constants.FILTER.ins)

            else -> return super.onOptionsItemSelected(item)

        }
        return false

    }

    private fun confirmDialog(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        AlertDialog.Builder(this)
            .setTitle("Remoção")
            .setMessage("Tem certeza que deseja remover?")
            .setPositiveButton("Deletar") { _, _ -> future.complete(true) }
            .setNegativeButton("Cancelar") { _, _ -> future.complete(false) }
            .show()

        return future
    }


    private fun iconsEmptyList(isEmpty: List<Establishment>)
    {
        if(isEmpty.isEmpty())
        {
            bind.iconEmptyIdHistory.visibility = View.VISIBLE
            bind.textEmptyIdHistory.visibility = View.VISIBLE
        }else{
            bind.iconEmptyIdHistory.visibility = View.INVISIBLE
            bind.textEmptyIdHistory.visibility = View.INVISIBLE
        }
    }

    override fun onEstablishmentListUpdated(list: List<Establishment>) {
        iconsEmptyList(list)
    }

}