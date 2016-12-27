package com.jose.guiacom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginAdminActivity extends AppCompatActivity implements AsyncResponse, Urls {

    EditText etEmail, etSenha;
    Button btnLogin;
    String voltar, cidadeSigla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        cidadeSigla = getIntent().getStringExtra("cidade");

        ImageView btnHome = (ImageView) findViewById(R.id.toolbar_logo);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAdminActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
                startActivity(intent);
            }
        });

        etEmail = (EditText) findViewById(R.id.txtEmail);
        etSenha = (EditText) findViewById(R.id.txtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> postData = new HashMap<>();
                postData.put("email", etEmail.getText().toString());
                postData.put("senha", etSenha.getText().toString());
                postData.put("android", "android");

                PostResponseAsyncTask task = new PostResponseAsyncTask(LoginAdminActivity.this, postData, LoginAdminActivity.this);
                task.execute(loginUrl);
            }
        });
    }

    @Override
    public void processFinish(String s) {
        if (s.equals("true")){
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        } else if (s == null || s.equals("")) {
            Toast.makeText(LoginAdminActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pesquisar) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(LoginAdminActivity.this, SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginAdminActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginAdminActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.anuncie) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(LoginAdminActivity.this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginAdminActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(LoginAdminActivity.this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginAdminActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(LoginAdminActivity.this, ContatoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginAdminActivity.this, ContatoActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
