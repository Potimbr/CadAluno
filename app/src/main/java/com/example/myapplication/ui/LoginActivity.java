package com.example.myapplication.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.model.UsuarioViewModel;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewNovoCadastro;
    private Button buttonLogin;

    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioCorrente;
    private EditText editTextEmail;
    private EditText editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Hawk.init(this).build();

        editTextEmail = findViewById(R.id.editTextUsuario);
        editTextSenha = findViewById(R.id.editTextSenha);

        textViewNovoCadastro = findViewById(R.id.textViewNovoCadastro);
        buttonLogin = findViewById(R.id.buttonLogin);


        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        usuarioViewModel.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                usuarioCorrente = usuario;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        verificaCadastro();
    }

    private void verificaCadastro(){
        if(Hawk.contains("tem_cadastro")){

            if(Hawk.get("tem_cadastro")){
                textViewNovoCadastro.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                buttonLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }else{
                textViewNovoCadastro.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                buttonLogin.setBackgroundColor(Color.GRAY);
            }

        }else{
            // fazer outra coisa
            textViewNovoCadastro.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
            buttonLogin.setBackgroundColor(Color.GRAY);
        }
    }



    private void habilitarLogin(){
        textViewNovoCadastro.setVisibility(View.GONE);
        buttonLogin.setEnabled(true);
        buttonLogin.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    private void desabilitarLogin(){
        textViewNovoCadastro.setVisibility(View.VISIBLE);
        buttonLogin.setEnabled(false);
        buttonLogin.setBackgroundColor(getColor(R.color.gray));
    }

    public void novoCadastro(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        if(usuarioCorrente != null){
            String usuario = editTextEmail.getText().toString();
            String senha = editTextSenha.getText().toString();
            if(usuario.equalsIgnoreCase(usuarioCorrente.getEmail())
            && senha.equals(usuarioCorrente.getSenha())){

                Intent intent =  new Intent(this, MainActivity.class);
                startActivity(intent);
                editTextSenha.setText("");
            }else{
                Toast.makeText(this, "Usuario ou Senha Inválidos",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
