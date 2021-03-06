package com.missclick.eco.main.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile_positive.*


class ProfilePositive : Fragment() {

    private val positive = listOf(
        PositiveItem(1,"Поднял бутылку", 10),
        PositiveItem(2,"Поднял бумажку", 5),
        PositiveItem(3,"Провел уборку", 25),
        PositiveItem(4,"Посадил дерево", 100),
        PositiveItem(5,"Добавил урну", 8),
        PositiveItem(6,"Поднял окурок", 5),
        PositiveItem(7,"Отсортровал мусор", 20)
    )
    private val negative = listOf(
        PositiveItem(-1,"Выкинул бутылку", -10),
        PositiveItem(-2,"Выкинул бумажку", -5)

    )
    private var actions = negative

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val forward = MaterialFadeThrough.create(requireContext())
        enterTransition = forward

        val backward = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, false)
        returnTransition = backward
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_positive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments!!.getBoolean("arg")) actions = positive
        val myAdapter = ListPositiveAdapter(
            actions,
            object : ListPositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {
                    val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                    val profileEditPost = ProfileEditPost()
                    val bundle = Bundle()
                    bundle.putParcelable("arg",item)
                    profileEditPost.arguments = bundle
                    transaction.replace(R.id.fragment_holder, profileEditPost)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            })

        PositiveActions.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        PositiveActions.adapter = myAdapter
    }




}
