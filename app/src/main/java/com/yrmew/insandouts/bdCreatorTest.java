package com.yrmew.insandouts;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRouter;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javax.annotation.Nonnull;

public class bdCreatorTest {

    //TODO: Rename this whole class and add more methods

    //Generates a Token for Synchronization between the client and server values
    public String tokenGenerate(){
        String token="";
        for(int x=0;x<5;x++){
            Random rand = new Random();
            int numRandom= rand.nextInt(35);
            if(numRandom>9){
                token+=(char)(numRandom+55);
            }else{
                token+=numRandom;
            }
        }

        return token;

    }

    //Get today's date and return an Array
    public String[] getCurrentDate(){

        DateFormat df = new SimpleDateFormat("dd MM yyyy");
        String dateToday = df.format(Calendar.getInstance().getTime());
        String[] dateValues = dateToday.split(" "); // contains Day[0], Month[1], Year[2]

        return dateValues;
    }

   //Gets token callback from Firebase
    public interface SimpleCallback<String> {
        void callback(String tokenFB);
    }

    //Get token data from firebase
    public static void getTokenData(@Nonnull SimpleCallback<String> finishedCallback){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Gets token value
                finishedCallback.callback(snapshot.child("token").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener);

    }

    //Class responsible for comparing the local and the online token
    static abstract class tokenClass{

        //Receives both local token(data) and online token(tokenFB)
        void getToken(Object data){
            getTokenData(tokenFB -> {
                //Sends the results between the comparison back to the class that called this method
                proceed(tokenFB.equals(data));
            });
        }

        //Method implemented by the caller, which contains actions to be made after the token comparison finishes
        abstract void proceed(Boolean data);
    }

    //Calls the tokenClass from the caller
    public void callTokenClass(tokenClass tokenClass, Object data){
        tokenClass.getToken(data);
    }


}


