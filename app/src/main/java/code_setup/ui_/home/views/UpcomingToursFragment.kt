package code_setup.ui_.home.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import code_setup.app_core.CoreBottomSheetFragment
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.fragment_upcoming_tours_layout.*
import kotlinx.android.synthetic.main.layout_reach_for_pickup.*
import com.google.android.material.bottomsheet.BottomSheetBehavior as BottomSheetBehavior1


class UpcomingToursFragment : CoreBottomSheetFragment() {
    override fun onActivityInject() {

    }

    private var fragmentView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_upcoming_tours_layout, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initListAdapter()
    }

    private fun initListAdapter() {

    }

}