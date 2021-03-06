package com.silviaodwyer.rssfeedreader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by silvi on 02/03/2018.
 */

class ViewHolder(v: View){
    val publishedDate: TextView = v.findViewById(R.id.publishedDate)
    val titleText: TextView = v.findViewById(R.id.title)
    // There exists no "summary" tag in the Atom GitHub feeds I've seen,
    // but I've left the lines including it commented out.
    val tvSummary: TextView = v.findViewById(R.id.tvSummary) // Summary doesn't exist, but if I find another tag to replace it, I'll place it instead of this variable.
}
class FeedAdapter(context: Context, private val resource: Int, private val applications: List<FeedEntry>): ArrayAdapter<FeedEntry>(context, resource) {
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        Log.d(TAG, "getCount() called")
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // The position parameter is the list item object that the adapter needs to display the data and inflate the layout with the data for the item

        Log.d(TAG, "getView() called")
        val view: View
        // More memory-efficient, creates a new view only if a view can be recycled by the adapter
        if (convertView == null){
            Log.d(TAG, "getView called with null convertView")
            view = inflater.inflate(resource, parent, false)
        } else{
            Log.d(TAG, "getView provided a convertView")
            view = convertView
        }
        val publishedDate: TextView = view.findViewById(R.id.publishedDate)
        val titleText: TextView = view.findViewById(R.id.title)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)

        val currentApp = applications[position]

        publishedDate.text = currentApp.published
        titleText.text = currentApp.title
        tvSummary.text = currentApp.summary

        return view

    }
}