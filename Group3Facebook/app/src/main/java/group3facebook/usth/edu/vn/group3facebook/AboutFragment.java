package group3facebook.usth.edu.vn.group3facebook;


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
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView avatarImageView2 = getView().findViewById(R.id.avatarImageView2);
        TextView userNameView = (TextView) getView().findViewById(R.id.userNameView);
        userNameView.setText(MainActivity.profileName);
        Context c = getActivity().getApplicationContext();
        Picasso.with(c).load(MainActivity.profileImageLink1).into(avatarImageView2);
        TextView userId = (TextView) getView().findViewById(R.id.userId);
        userId.setText(LoginActivity.id);
        TextView userMail = (TextView) getView().findViewById(R.id.userMail);
        userMail.setText(LoginActivity.email);
        TextView userGender = (TextView) getView().findViewById(R.id.userGender);
        userGender.setText(LoginActivity.gender);
    }
}
