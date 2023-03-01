package me.magical.graceful.mvvm.fragment

import android.os.Bundle
import me.magical.graceful.R
import me.magical.graceful.databinding.FragmentMineBinding
import me.magical.mvvmgraceful.base.fragment.BaseFM

class MineFragment:BaseFM<FragmentMineBinding>() {
    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String="", param2: String="") =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayout(savedInstanceState: Bundle?): Int = R.layout.fragment_mine

    override fun lazyLoadData() {
    }
    override fun initView(savedInstanceState: Bundle?) {
    }
}