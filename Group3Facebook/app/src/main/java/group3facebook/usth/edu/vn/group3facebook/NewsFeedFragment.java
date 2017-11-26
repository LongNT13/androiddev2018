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
import android.widget.ListView;

import com.android.volley.Response;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    Button btnPost, btnPostImg;

    ShareDialog shareDialog;

    Bitmap bitmap;

    public static int selectImage = 1;

    ListView postList;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        setUpVariables(view);

        shareDialog = new ShareDialog(NewsFeedFragment.this);

        //request news feeds info
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
                , new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //parseJSON
                        parseJSON(object);
                    }
                });

        Bundle param = new Bundle();
        //request fields
        param.putString("fields","feed{from, message, created_time, full_picture}");
        param.putString("limit","10");
        graphRequest.setParameters(param);
        graphRequest.executeAsync();
        //end of request

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == selectImage && resultCode == RESULT_OK){
            try {
                InputStream inputStream = getActivity().getContentResolver()
                        .openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                //share after receive image
                shareImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setUpVariables(View v) {
        //Post btn
        btnPost = (Button)v.findViewById(R.id.btnPost);
        btnPostImg = (Button)v.findViewById(R.id.btnPostImg);
        postList = (ListView)v.findViewById(R.id.postList);

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

    //choose image to share from the drive
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
        //show pop up
        shareDialog.show(content);
    }

    private void postFeed() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        shareDialog.show(content);
    }

    //parse received json
    private void parseJSON(JSONObject object) {

        ArrayList<PostItem> postListItems = new ArrayList<PostItem>();

        //there are 10
        for(int i = 0; i < 10; i++){
            try {
                JSONObject currentPost = object.getJSONObject("feed")
                        .getJSONArray("data")
                        .getJSONObject(i);

                String name = currentPost
                        .getJSONObject("from")
                        .getString("name");

                String createDateTime = currentPost.getString("created_time");

                //check if there is a message
                String message;
                if(currentPost.has("message")) {
                    message = currentPost.getString("message");
                }else {
                    message = "";
                }
                //check if there is a picture


                final PostItem p = new PostItem(name, createDateTime, message, null);
                if(currentPost.has("full_picture")){
                    String pictureURLStr = currentPost.getString("full_picture");

                    //request bitmap form url
                    Response.Listener listener = new Response.Listener<Bitmap>(){
                        @Override
                        public void onResponse(Bitmap response) {
                            p.setPicture(response);
                        }
                    };
                    // a simple request to the required image
                    com.android.volley.toolbox.ImageRequest imageRequest = new com.android.volley.toolbox.ImageRequest(
                            pictureURLStr,
                            listener, 0, 0, ImageView.ScaleType.CENTER,
                            Bitmap.Config.ARGB_8888,null);
                    // go!
                    ((FacebookApp)getActivity().getApplication()).getQueue().add(imageRequest);

                }

                postListItems.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //adapter for Post List
        NewsFeedPostsAdapter adapter = new NewsFeedPostsAdapter(
                getActivity(),
                R.layout.fragment_post,
                postListItems
        );

        postList.setAdapter(adapter);
    }

}
