package me.magical.graceful.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.magical.graceful.mvvm.fragment.ExploreFragment
import me.magical.graceful.mvvm.fragment.HomeFragment
import me.magical.graceful.mvvm.fragment.MineFragment

class BottomAdapter(
    private val fragmentStringList: MutableList<String>,
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm,lifecycle) {

    companion object{

    }
    override fun getItemCount(): Int {
        return fragmentStringList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(fragmentStringList[position]){
            "ExploreFragment"->{
                ExploreFragment.newInstance()
            }
            "HomeFragment"->{
                HomeFragment.newInstance()
            }
            "MineFragment"->{
                MineFragment.newInstance()
            }
            else->{
                HomeFragment.newInstance()
            }
        }
    }
}