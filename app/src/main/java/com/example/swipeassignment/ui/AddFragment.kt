package com.example.swipeassignment.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.swipeassignment.R
import com.example.swipeassignment.databinding.DialogCustomImageSelectionBinding
import com.example.swipeassignment.databinding.FragmentAddBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindHandlers()
    }

    private fun bindHandlers() {
        binding!!.ivAddProductImage.setOnClickListener {
            customImageSelectionDialog()
        }
        binding!!.btnAddProduct.setOnClickListener {
//            TODO(Send Binded objects to API)
        }
    }



    private fun customImageSelectionDialog() {
        val dialog = Dialog(requireActivity())

        val binding = DialogCustomImageSelectionBinding.inflate(layoutInflater)

        /*
        The resource will be inflated, adding all top-level views to the screen.*/
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            Dexter.withContext(requireActivity())
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        // Here after all the permission are granted launch the CAMERA to capture an image.
                        report?.let {
                            if (report.areAllPermissionsGranted()) {

                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {

            Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        // START
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY)
                        // END
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(
                            requireActivity(),
                            "You have denied the storage permission to select image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        showRationalDialogForPermissions()
                    }
                })
                .onSameThread()
                .check()

            dialog.dismiss()
        }

        //Start the dialog and display it on screen.
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap //Bitmap from Camera

//                  Setting Image from camera to the image view
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(binding!!.ivProductImage)

                }

            }
            if (requestCode == GALLERY){
                data?.let {
                    val selectedImageUri =data.data
                    Glide.with(requireActivity())
                        .load(selectedImageUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding!!.ivProductImage)
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Image","cancelled")
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireActivity())
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri = Uri.fromParts("")
//                    intent.data = uri
//                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}