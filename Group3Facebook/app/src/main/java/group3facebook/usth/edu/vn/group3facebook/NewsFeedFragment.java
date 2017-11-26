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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
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

    Button btnPost, btnPostImg, btnMoreFeed;

    ShareDialog shareDialog;

    Bitmap bitmap;

    public static int selectImage = 1;

    ListView postList;

    //next page of newsfeed
    String nextPageURL = null;

    //array for ListView
    ArrayList<PostItem> postListItems = new ArrayList<PostItem>();
    NewsFeedPostsAdapter adapter;

    //functions

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        setUpVariables(view);

        shareDialog = new ShareDialog(NewsFeedFragment.this);

        //adapter for Post List
        adapter = new NewsFeedPostsAdapter(
                getActivity(),
                R.layout.fragment_post,
                postListItems
        );

        postList.setAdapter(adapter);
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

        //test

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //choose an image
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
        btnMoreFeed = (Button)v.findViewById(R.id.btnMoreFeed);

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

        btnMoreFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextNewsFeedPage();
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
        try {
            //make an origin because JSON file of the next page isn't bounded by "feed" object
            JSONObject origin;
            if(object.has("feed")) {
                origin = object.getJSONObject("feed");
            }else {
                origin = object;
            }

            JSONArray data = origin.getJSONArray("data");
            //there are 10
            for(int i = 0; i < data.length(); i++){

                JSONObject currentPost = data
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
                    Response.Listener listenerPic = new Response.Listener<Bitmap>(){
                        @Override
                        public void onResponse(Bitmap response) {
                            p.setPicture(response);
                        }
                    };
                    // a simple request to the required image
                    com.android.volley.toolbox.ImageRequest imageRequest = new com.android.volley.toolbox.ImageRequest(
                            pictureURLStr,
                            listenerPic, 0, 0, ImageView.ScaleType.CENTER,
                            Bitmap.Config.ARGB_8888,null);
                    // go!
                    ((FacebookApp)getActivity().getApplication()).getQueue().add(imageRequest);

                }

                postListItems.add(p);
            }

            //next page url
            nextPageURL = origin.getJSONObject("paging").getString("next");

        }catch (JSONException e) {
            e.printStackTrace();
        }
        //update listview
        adapter.notifyDataSetChanged();

    }

    public void getNextNewsFeedPage(){
        if(nextPageURL != null) {
            //string rerquest
            StringRequest nextPageRequest = new StringRequest(
                    nextPageURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject object = null;
                            try {
                                object = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            parseJSON(object);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            ((FacebookApp) getActivity().getApplication()).getQueue().add(nextPageRequest);
        }
    }

}
