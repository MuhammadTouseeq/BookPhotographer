package com.example.kashif.bookphotographer.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.example.kashif.bookphotographer.Activities.Adapter.CustomDrawerUser;
import com.example.kashif.bookphotographer.Activities.ModelClass.UserModel;
import com.example.kashif.bookphotographer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    EditText editText;
    String ESearch;
    public static ArrayList<String> name , loc , imgrl, id;
    TextView search;
    DatabaseReference databaseReference;
    ImageView ImageDrawer;
    String names[];
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;

//    public static   String[] gridViewString ;
//
//    public static   String[] gridViewString2 ;
//
//
//
//    public static   String[] gridViewImageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("allusers");
        name = new ArrayList<String>();
        loc = new ArrayList<String>();
        imgrl = new ArrayList<String>();
        id = new ArrayList<String>();


        Window window = this.getWindow();
        window.setBackgroundDrawableResource(R.mipmap.bck5);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
        search = (TextView) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.EditTxt);
        ESearch = null;
        ImageDrawer = (ImageView) findViewById(R.id.ImageDrawer);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (editText.getText().toString().isEmpty()){

                    editText.setError("Enter Name");

                }else {


                    ESearch = editText.getText().toString().trim();
                    getData();
                }


            }
        });


        //// Side Drawer///


        names = new String[]{"Home"};
        int img[] = {R.mipmap.back_icon };
        //mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.Left_Drawer);

        mDrawerList.setFitsSystemWindows(true);

        mDrawerList.setAdapter(new CustomDrawerUser(this, img, names));
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.drawer_header, mDrawerList, false);


        mDrawerList.addHeaderView(header, null, false);

        ImageDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    // getSupportActionBar().setCustomView(R.layout.menu_title);
                    // getSupportActionBar().show();


                }
                else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    //getSupportActionBar().hide();
                    // requestWindowFeature(Window.FEATURE_NO_TITLE);
                }

            }
        });



        //////// Side Drawer End //////



    }


    public void getData(){


        databaseReference.child("users").orderByChild("firstname").equalTo(ESearch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                for (DataSnapshot snapshot : dataSnapshot.getChildren()){


                    UserModel userModel = snapshot.getValue(UserModel.class);


                    name.add(userModel.getFirstname());
                    id.add(userModel.getId());
                    loc.add(userModel.getCity());
                    imgrl.add(userModel.getImageUrl()); }

//             img = String.valueOf(Glide.with(getApplicationContext()).load(imgrl));


                Intent intent = new Intent(HomeActivity.this , SearchPhotographer.class);
                startActivity(intent);

                }else {


                    Toast.makeText(HomeActivity.this , "Not Found " , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
