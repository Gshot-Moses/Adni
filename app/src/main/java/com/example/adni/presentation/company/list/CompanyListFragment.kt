package com.example.adni.presentation.company.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adni.R
import com.example.adni.databinding.FragmentCompanyListBinding
import com.example.adni.presentation.uimodel.CompanyUi
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

@AndroidEntryPoint
class CompanyListFragment: Fragment(), SearchView.OnQueryTextListener, SearchVisibilityCallBack {

    private var _binding: FragmentCompanyListBinding? = null
    private val binding: FragmentCompanyListBinding get() = _binding!!
    private val viewModel: CompanyListViewModel by viewModels()
    private var companyListAdapter: CompanyListAdapter? = null
    private var searchAdapter: CompanyListAdapter? = null
    private var searchView: SearchView? = null
    private var toBeRemoved = -1
    private var companyList: List<CompanyUi> = listOf()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        companyListAdapter = CompanyListAdapter(requireContext(), CompanyListAdapter.COMPANY_LIST_ADAPTER)
        setupRecyclerView(companyListAdapter, binding.companyRecycler)
        searchAdapter = CompanyListAdapter(requireContext(), CompanyListAdapter.SEARCH_ADAPTER)
        setupRecyclerView(searchAdapter, binding.searchRecycler)
        observeState()
        binding.addCompanyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_companyListFragment_to_addCompanyFragment)
        }
    }

    private fun setupRecyclerView(adapter: CompanyListAdapter?, recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = SlideInLeftAnimator()
        adapter?.setClickListener { company, type, position ->
            if (type == CompanyListAdapter.DETAIL_TYPE) {
                val bundle = bundleOf("id" to company.id)
                findNavController().navigate(R.id.action_companyListFragment_to_companyDetailsFragment, bundle)
            }
            else {
                toBeRemoved = position
                //viewModel.onEvent(CompanyListEvents.RemoveCompanyFromStateList(company))
                adapter.removeCompany(company, position)
                confirmCompanyDelete(company, position).show()
            }
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreen(it)
        }
    }

    private fun confirmCompanyDelete(company: CompanyUi, position: Int): Snackbar {
        var flag = false
        return Snackbar.make(binding.addCompanyBtn, "Supprimee", Snackbar.LENGTH_LONG)
            .setAction("Annuler") {
                //viewModel.onEvent(CompanyListEvents.InsertRemovedCompany(company, position))
                companyListAdapter?.addCompany(company, position)
                flag = true
            }.addCallback(object: Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    //remove from db
                    if (!flag)
                        viewModel.onEvent(CompanyListEvents.RemoveCompany(company.id))
                }
            })
    }

    private fun updateScreen(state: CompanyListState) {
        Log.d("state", "$state")
        when (state) {
            is CompanyListState.Loading -> displayProgressBar(binding.progressBar,true)
            is CompanyListState.OnCompanyList -> {
                displayProgressBar(binding.progressBar, false)
                displayError(state.data, false)
                companyListAdapter?.setData(state.data)
                companyList = state.data
                toggleSearchRecyclerVisibility(false)
            }
            is CompanyListState.OnSearch -> {
                displayProgressBar(binding.progressBar, false)
                displayError(state.data, true)
                searchAdapter?.setData(state.data)
                toggleSearchRecyclerVisibility(true)
            }
            is CompanyListState.OnCompanyToBeRemoved -> {
                if (state.id > -1)
                    companyListAdapter?.notifyItemRemoved(toBeRemoved)
            }
            is CompanyListState.OnInsertRemovedCompany -> {
                companyListAdapter?.notifyItemInserted(state.id)
            }
            is CompanyListState.OnRemoveCompanyFromListState -> {
                companyListAdapter?.notifyItemRemoved(state.id)
            }
        }
    }

    private fun displayProgressBar(view: ProgressBar, visibility: Boolean) =
        if (visibility) view.visibility = View.VISIBLE else view.visibility = View.INVISIBLE

    private fun displayError(data: List<CompanyUi>, search: Boolean) {
        if (data.isNullOrEmpty()) {
            if (search) binding.noCompany.text = "Pas de correspondance"
            else binding.noCompany.text = "Aucune entreprise dans la base de donnees"
            binding.noCompany.visibility = View.VISIBLE
        }
        else binding.noCompany.visibility = View.INVISIBLE
    }

    private fun toggleSearchRecyclerVisibility(visibility: Boolean) {
        if (visibility) {
            binding.searchRecycler.visibility = View.VISIBLE
            binding.companyRecycler.visibility = View.INVISIBLE
            binding.addCompanyBtn.visibility = View.INVISIBLE
        }
        else {
            binding.searchRecycler.visibility = View.INVISIBLE
            binding.companyRecycler.visibility = View.VISIBLE
            binding.addCompanyBtn.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView?.queryHint = "Nom de l'entreprise"
        searchView?.setOnQueryTextListener(this)
    }

    override fun searchViewVisible(): Boolean? {
        return searchView?.isIconified == false
    }

    override fun closeSearchView() {
        searchView?.isIconified = true
        viewModel.onEvent(CompanyListEvents.StopSearch(companyList))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { viewModel.onEvent(CompanyListEvents.Search(it)) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { viewModel.onEvent(CompanyListEvents.Search(it)) }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        companyListAdapter = null
        searchAdapter = null
        _binding = null
    }
}