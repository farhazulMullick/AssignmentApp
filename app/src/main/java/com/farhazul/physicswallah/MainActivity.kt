package com.farhazul.physicswallah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.farhazul.physicswallah.databinding.ActivityMainBinding
import com.farhazul.physicswallah.ui.HomeFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHomeFragment()
    }

    fun setHomeFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, HomeFragment())
        transaction.commit()
        binding.toolbarTxtView.text = getString(R.string.home_fragment)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}