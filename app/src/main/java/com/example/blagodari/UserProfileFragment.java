package com.example.blagodari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blagodari.Models.DBhelper;
import com.example.blagodari.Models.User;

import java.util.Objects;

public class UserProfileFragment extends Fragment {
    ImageView avatar;
    TextView name, likes;
    Button edit;
    DBhelper dBhelper;
    public static Fragment newInstance(){
        return new UserProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.user_profile_fragment, container, false );
        avatar=v.findViewById(R.id.imgAvatarOnProfile);
        name=v.findViewById(R.id.txtNameOnProfile);
        likes=v.findViewById(R.id.txtLikeCountOnProfile);
        edit=v.findViewById(R.id.btnEditProfile);
        dBhelper=new DBhelper(getContext());
        User user=dBhelper.getCurrentUser();
        name.setText(user.getFirstName()+" "+user.getSurname());
        likes.setText(dBhelper.likeCount(user)+"");
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), EditProfileActivity.class);
               getContext().startActivity(i);
            }
        });
        if (user.getAvatar()!=null){
            avatar.setImageBitmap(user.getAvatar());
        }
        return v;
    }
}
