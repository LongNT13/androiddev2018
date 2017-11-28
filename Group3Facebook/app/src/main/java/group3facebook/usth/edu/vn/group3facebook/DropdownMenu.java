package group3facebook.usth.edu.vn.group3facebook;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class DropdownMenu extends Fragment {

    public DropdownMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dropdown_menu, container, false);
        //get the argument passed from main acitivity
        //Bundle tempBundle = getArguments();
        //String profileLastName = getArguments().getString("profileLastName");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dropdown_menu, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView avatarImageView = getView().findViewById(R.id.avatarImageView);
        TextView userNameView = (TextView) getView().findViewById(R.id.userNameView);
        userNameView.setText(MainActivity.profileName);
        Context c = getActivity().getApplicationContext();
        Picasso.with(c).load(MainActivity.profileImageLink1).into(avatarImageView);

    }
}
