package com.example.swipeassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeassignment.R
import com.example.swipeassignment.databinding.FragmentProductBinding
import com.example.swipeassignment.ui.adapters.ProductAdapter
import com.example.swipeassignment.utils.Helper
import com.example.swipeassignment.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var _binding:FragmentProductBinding? = null
    private val binding
        get() = _binding

    private val viewModel by viewModels<ProductViewModel>()
    private lateinit var adapter:ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentProductBinding.inflate(layoutInflater,container,false)
        adapter = ProductAdapter(requireActivity())
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
        binding!!.productListRv.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding!!.productListRv.adapter= adapter
        binding!!.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_product_fragment_to_add_product_fragment)
        }
        viewModel.getProducts()

    }

    private fun bindObserver() {
        viewModel.productLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Helper.dismissProgressBar()
                    adapter.submitList(it.data)
                }

                is NetworkResult.Error -> {
                    Helper.dismissProgressBar()
                    showValidationError(it.message.toString())
                }

                is NetworkResult.Loading -> {
                  Helper.showCustomProgressBar(requireActivity())
                }

            }

        }
    }

    private fun showValidationError(error:String) {
        Toast.makeText(requireActivity(),error, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}