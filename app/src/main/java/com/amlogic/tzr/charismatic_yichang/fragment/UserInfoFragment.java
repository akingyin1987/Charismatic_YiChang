package com.amlogic.tzr.charismatic_yichang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amlogic.tzr.charismatic_yichang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {

    public static final String USER_ID = "user_id";
    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_user_info, container, false);
    }


    public static UserInfoFragment newInstance(String userId){
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

}
