package com.jose.guiacom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContatoActivity extends AppCompatActivity {

    String cidadeSigla;
    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        cidadeSigla = getIntent().getStringExtra("cidade");

        ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView btnHome = (ImageView) findViewById(R.id.toolbar_logo);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContatoActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
                startActivity(intent);
            }
        });

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=&body=&to=" + txtEmail.getText().toString());
                emailIntent.setData(data);
                startActivity(emailIntent);
                return true;
            }
        });
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
                Intent intent = new Intent(ContatoActivity.this, SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ContatoActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(ContatoActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(ContatoActivity.this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ContatoActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(ContatoActivity.this, CatalogoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ContatoActivity.this, CatalogoActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(ContatoActivity.this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ContatoActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
