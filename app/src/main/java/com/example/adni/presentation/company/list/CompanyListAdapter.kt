package com.example.adni.presentation.company.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adni.R
import com.example.adni.databinding.CompanyListItemBinding
import com.example.adni.presentation.uimodel.CompanyUi

class CompanyListAdapter(private val context: Context, private val visibilityFlag: Int)
    : RecyclerView.Adapter<CompanyListAdapter.ViewHolder>() {

    private val companyList: MutableList<CompanyUi> = mutableListOf()
    private lateinit var listener: (CompanyUi, Int, Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CompanyListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    fun setClickListener(listener: (CompanyUi, Int, Int) -> Unit) {
        this.listener = listener
    }

    fun addCompany(company: CompanyUi, position: Int) {
        companyList.add(position, company)
        notifyItemInserted(position)
    }

    fun removeCompany(company: CompanyUi, position: Int) {
        companyList.remove(company)
        notifyItemRemoved(position)
    }

    fun setData(companies: List<CompanyUi>) {
        companyList.clear()
        companyList.addAll(companies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val companyUi = companyList[position]
        holder.bind(companyUi)
        holder.itemView.setOnClickListener {
            listener.invoke(companyUi, DETAIL_TYPE, position)
        }
        holder.deleteCompanyIv.setOnClickListener {
            listener.invoke(companyUi, REMOVE_TYPE, position)
        }
    }

    companion object {
        const val DETAIL_TYPE = 0
        const val REMOVE_TYPE = 1
        const val SEARCH_ADAPTER = 2
        const val COMPANY_LIST_ADAPTER = 3
    }

    inner class ViewHolder(root: View): RecyclerView.ViewHolder(root) {

        private val companyNameTextView: TextView = root.findViewById(R.id.company_name)
        private val companyLogoIv: ImageView = root.findViewById(R.id.company_logo)
        val deleteCompanyIv: ImageView = root.findViewById(R.id.remove_company_iv)

        init {
            if (visibilityFlag == SEARCH_ADAPTER)
                deleteCompanyIv.visibility = View.INVISIBLE
            else
                deleteCompanyIv.visibility = View.VISIBLE
        }

        fun bind(company: CompanyUi) {
            companyNameTextView.text = company.name
            if (company.logoPath != "notSet")
                Glide.with(context).load(company.logoPath).into(companyLogoIv)
        }
    }
}

val COMPANY_DIFF_UTIL = object: DiffUtil.ItemCallback<CompanyUi>() {
    override fun areItemsTheSame(oldItem: CompanyUi, newItem: CompanyUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CompanyUi, newItem: CompanyUi): Boolean {
        return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude
    }
}