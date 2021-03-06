package ru.raslav.wirelessscan.fragments

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import ru.raslav.wirelessscan.adapters.SnapshotsListAdapter

import kotlinx.android.synthetic.main.layout_list.view.*
import ru.raslav.wirelessscan.BuildConfig
import ru.raslav.wirelessscan.MainActivity
import ru.raslav.wirelessscan.R

class SnapshotsListFragment : Fragment() {
    private var fragmentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentView = view
    }

    override fun onStart() {
        super.onStart()

        activity.setTitle(R.string.title_snapshots)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (fragmentView != null)
            return fragmentView

        val view = inflater.inflate(R.layout.layout_list, container, false)

        val adapter = SnapshotsListAdapter(activity)
        adapter.onSnapshotShareListener = { name -> share(name) }
        view.list_view.adapter = adapter
        view.list_view.setOnItemClickListener { _, _, position, _ ->
            activity.startActivity(
                    Intent(activity, MainActivity::class.java)
                            .setAction(MainActivity.ACTION_OPEN_SNAPSHOT)
                            .putExtra(MainActivity.EXTRA_SNAPSHOT_NAME, adapter.getItem(position))
            )
        }

        return view
    }

    private fun share(name: String) {
        val suffix = if (BuildConfig.DEBUG) ".debug" else ""
        val intent = Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, Uri.parse("content://ru.raslav.wirelessscan$suffix/$name"))
                .setType("text/xml")

        if (intent.resolveActivity(activity.packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(activity, R.string.no_activity, Toast.LENGTH_SHORT).show()
    }
}