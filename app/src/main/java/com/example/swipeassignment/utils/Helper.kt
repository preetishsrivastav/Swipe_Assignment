package com.example.swipeassignment.utils

import android.app.Dialog
import android.content.Context
import com.example.swipeassignment.R

object Helper {
    private var mProgressDialog: Dialog? = null

    fun showCustomProgressBar(context: Context){
        mProgressDialog = Dialog(context)
        mProgressDialog?.let {
            it.setContentView(R.layout.custom_dialog_progress)
            it.show()
        }
    }

    fun dismissProgressBar(){
        mProgressDialog?.dismiss()
    }
}