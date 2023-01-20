package hu.ait.weatherinfo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import hu.ait.weatherinfo.adapter.Adapter
import hu.ait.weatherinfo.data.AppDatabase
import hu.ait.weatherinfo.data.City
import hu.ait.weatherinfo.databinding.ActivityScrollingBinding
import hu.ait.weatherinfo.dialog.CityDialog
import hu.ait.weatherinfo.touch.RecyclerTouchCallback
import hu.ait.weatherinfo.viewmodel.CityViewModel
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(),
    CityDialog.ItemDialogHandler {

    companion object {
        const val PREF_SETTINGS = "SETTINGS"
        const val KEY_FIRST_START = "KEY_FIRST"
    }

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: Adapter

    private lateinit var viewModel: CityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CityViewModel::class.java]

        initRecyclerView()

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        binding.fabAdd.setOnClickListener { view ->
            val cityDialog = CityDialog()
            cityDialog.show(supportFragmentManager, "CityDialog")
        }

        binding.fabDeleteAll.setOnClickListener{
            viewModel.deleteAllItems()
        }

        if (isItFirstStart()){
            MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText("Add city")
                .setSecondaryText("Click here to add a city")
                .setTarget(binding.fabAdd)
                .show()
            saveAppWasStarted()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView =
            searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                AppDatabase.getInstance(this@ScrollingActivity).itemDao().findItems(newText!!)
                    .observe(
                        this@ScrollingActivity, Observer { items ->
                            adapter.submitList(items)
                        })
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun saveAppWasStarted() {
        val sp = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST_START, false)
        editor.commit()
    }

    private fun isItFirstStart() : Boolean {
        val sp = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        return sp.getBoolean(KEY_FIRST_START, true)
    }

    private fun initRecyclerView() {
        adapter = Adapter(this, viewModel)
        binding.recyclerItem.adapter = adapter

        val callback: ItemTouchHelper.Callback = RecyclerTouchCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerItem)

        viewModel.allItems.observe(this) { items ->
            adapter.submitList(items)
        }
    }

    override fun itemCreated(city: City) {
        viewModel.insertItem(city)

        Snackbar.make(
            binding.root,
            "Item added",
            Snackbar.LENGTH_LONG
        ).setAction(
            "Undo"
        ) {
            // remove last todo from RecyclerView..
            adapter.deleteLast()
        }.show()
    }
    }