package group3facebook.usth.edu.vn.group3facebook;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    Button btnPost, btnPostImg;

    ShareDialog shareDialog;

    Bitmap bitmap;

    public static int selectImage = 1;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        setUpButtons(view);

        shareDialog = new ShareDialog(NewsFeedFragment.this);

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
                , new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("JSON", response.getJSONObject().toString());
                    }
                });
        Bundle param = new Bundle();
        param.putString("fields","feed");
        param.putString("limit","10");
        graphRequest.setParameters(param);
        graphRequest.executeAsync();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //choose image to post
        if(requestCode == selectImage && resultCode == RESULT_OK){
            try {
                InputStream inputStream = getActivity().getContentResolver()
                        .openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                shareImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setUpButtons(View v) {
        //Post btn
        btnPost = (Button)v.findViewById(R.id.btnPost);
        btnPostImg = (Button)v.findViewById(R.id.btnPostImg);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeed();
            }
        });

        btnPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImage();
            }
        });
    }

    private void postImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, selectImage);

    }

    private void shareImage() {
        //share on Facebook
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        //show
        shareDialog.show(content);
    }

    private void postFeed() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        shareDialog.show(content);
    }

}
