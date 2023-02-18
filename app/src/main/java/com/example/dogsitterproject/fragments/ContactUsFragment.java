package com.example.dogsitterproject.fragments;

import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME_REQUIRED;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.dogsitterproject.R;
import com.google.android.material.textfield.TextInputEditText;

public class ContactUsFragment extends Fragment {
    private TextInputEditText name;
    private TextInputEditText subject;
    private TextInputEditText message;
    private AppCompatButton sendBtn;
    private AppCompatImageView whatsapp;
    private AppCompatImageView telegram;
    private AppCompatImageView facebook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        findViews(view);
        init();
        return view;
    }

    private void findViews(View view){
        name = view.findViewById(R.id.contact_IN_Name);
        subject = view.findViewById(R.id.contact_IN_email);
        message = view.findViewById(R.id.contact_IN_text);
        sendBtn = view.findViewById(R.id.contact_BTN_send);
        whatsapp = view.findViewById(R.id.contact_IMG_whatsapp);
        telegram = view.findViewById(R.id.contact_IMG_telegram);
        facebook = view.findViewById(R.id.contact_IMG_facebook);
    }


    private void init(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameMessage = name.getText().toString().trim();
                String subjectMessage = subject.getText().toString().trim();
                String editedMessage = message.getText().toString().trim();
                checkerSender(nameMessage,subjectMessage,editedMessage);
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+972585266174"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = v.getContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://telegram.me/vladiAfeka"));
                final String appName = "org.telegram.messenger";

                    if (isAppAvailable(v.getContext(), appName)) {
                        i.setPackage(appName);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getActivity(), "Telegram app not installed in your phone", Toast.LENGTH_SHORT).show();
                    }


            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.facebook.com/vladislav.ishchenko.9"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    private boolean isAppAvailable(Context context, String appName){
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private void checkerSender(String nameMessage,String subjectMessage,String editedMessage){
        if(nameMessage.isEmpty()){
            name.setError(FULL_NAME_REQUIRED);
            name.requestFocus();
        }
        else if(subjectMessage.isEmpty()){
            subject.setError(EMAIL_REQUIRED);
            subject.requestFocus();
        }
        else if(editedMessage.isEmpty()){
            //TODO: MESSAGE REQUIRED
            message.setError(EMAIL_REQUIRED);
            message.requestFocus();
        }
        else{
            String mail = "mailto:" + "jokernaale@gmail.com"
                    +"?&subject=" + Uri.encode(nameMessage) +": "+ Uri.encode(subjectMessage)
                    +"&body=" + Uri.encode(editedMessage);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(mail));
            startActivity(Intent.createChooser(intent, "Send email...."));
        }
    }
}
