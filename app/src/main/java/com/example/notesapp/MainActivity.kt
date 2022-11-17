package com.example.notesapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NoteAdapter
import com.example.notesapp.Database.NoteDatabase
import com.example.notesapp.Models.Note
import com.example.notesapp.Models.NoteViewModel
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NoteAdapter
    lateinit var selectedNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize UI
        initUI()

        viewModel =ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this){list->
            list?.let {
                adapter.updateList(list)
            }
        }
        database= NoteDatabase.getDatabase(this)
    }

    private fun initUI(){
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter=NoteAdapter(this,this)
        binding.recycleView.adapter=adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if( result.resultCode==Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra('note') as? Note
                if (note!=null){

                    viewModel.insertNote(note)
                }
            }

        }
    }
}