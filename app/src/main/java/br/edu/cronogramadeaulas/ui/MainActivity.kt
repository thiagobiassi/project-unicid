package br.edu.cronogramadeaulas.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import br.edu.cronogramadeaulas.R
import br.edu.cronogramadeaulas.fragment.LoginFragment
import br.edu.cronogramadeaulas.fragment.RegisterFragment
import br.edu.cronogramadeaulas.fragment.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deslizarActivity() // ViewPage2 para deslizar entre Fragments

    }

    private fun deslizarActivity() {
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val fragments: ArrayList<Fragment> = arrayListOf(
            LoginFragment(),
            RegisterFragment()
        )

        // Criando Adapter e ViewHolder
        val adapter = ViewPagerAdapter(fragments, this)
        viewPager.adapter = adapter
    }
}