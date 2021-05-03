package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.databinding.ActivityLoginBinding;
import com.duarbd.duarriderapp.model.ModelResponseRider;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";
    private ActivityLoginBinding binding;

    Observable<String> phoneObservable;
    Observable<String> passwordObservable;
    Observable<Boolean> observable;

    private ViewModelRiderApp viewModelRiderApp;
    private Dialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initObservables();

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(binding.phoneNumber.getText().toString(),binding.password.getText().toString());
            }
        });
    }

    void init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Rider Login");

        dialogLoading=setupDialog(ActivityLogin.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);
    }

    void login(String phonenumber,String password){
        dialogLoading.show();
        ModelRider rider=new ModelRider(phonenumber,password);
        viewModelRiderApp.riderLogin(rider).observe(ActivityLogin.this,
                new Observer<ModelResponseRider>() {
                    @Override
                    public void onChanged(ModelResponseRider modelResponseRider) {
                        if(modelResponseRider!=null&&modelResponseRider.getResponse()==1){
                            Utils.savePref(KEYS.RIDER_ID,modelResponseRider.getRiderid());
                            Utils.savePref(KEYS.RIDER_NAME,modelResponseRider.getRiderName());
                            Utils.savePrefBoolean(KEYS.IS_LOGGED_IN,true);
                            dialogLoading.dismiss();
                            startActivity(new Intent(ActivityLogin.this,ActivityHome.class));finish();
                        }else {
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityLogin.this, "Wrong Credential!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void initObservables(){
        phoneObservable = RxTextView.textChanges(binding.phoneNumber).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });

        passwordObservable = RxTextView.textChanges(binding.password).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });

        observable=observable.combineLatest(phoneObservable, passwordObservable, new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String s, String s2) throws Exception {
                return isValid(s,s2);
            }
        });

        observable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                updateSignnButtor(aBoolean);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        phoneObservable.subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                isValidNumber(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private Boolean isValid(String s1,String s2){
        if(s1.equals("") || s2.equals("") || s1.length()!=11){
            return false;
        }else {
            return true;
        }
    }

    private void isValidNumber(String s) {
        if(s.equals("") ){
            binding.outlinedTextField1.setHelperText("Can't be empty.");
        }else if(s.length()!=11) {
            binding.outlinedTextField1.setHelperText("Invalid phone number.");
        }else {
            binding.outlinedTextField1.setHelperText("");
        }
    }

    private void updateSignnButtor(Boolean b){
        if(b){
            binding.btnSignin.setEnabled(b);
        }else {
            binding.btnSignin.setEnabled(b);
        }
    }

    private Dialog setupDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //this prevents dimming effect
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}