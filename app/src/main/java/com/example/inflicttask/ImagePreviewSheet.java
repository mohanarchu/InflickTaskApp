package com.example.inflicttask;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inflicttask.databinding.FragmentImagePreviewSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ImagePreviewSheet extends BottomSheetDialogFragment {


    Bitmap bitmap;
    FragmentImagePreviewSheetBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImagePreviewSheetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.cardUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DropboxClient.retrieveAccessToken(getActivity()) == null ) {
                    DropboxClient.loginDropbox(getActivity());
                } else {
                    FileUploadService.startUpload(getActivity(),bitmap);
                    dismiss();
                }
            }
        });
        binding.image.setImageBitmap(bitmap);
    }





    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}