package ru.skillbranch.devintensive.ui.main


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun initViews() {
        chatAdapter = ChatAdapter{
            if(it.chatType == ChatType.ARCHIVE){
                val intent = Intent(this,ArchiveActivity::class.java)
                startActivity(intent)
            }else{
                Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
            }

        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            val chatItem = it

            viewModel.addToArchive(chatItem.id)

            val snackBar =  Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_LONG)
            snackBar.setAction("ОТМЕНА") {
                viewModel.restoreFromArchive(chatItem.id)
            }
            snackBar.show()
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list){
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener{
            val intent = Intent(this,GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }
}
