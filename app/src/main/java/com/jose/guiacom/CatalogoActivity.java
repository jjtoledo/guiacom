package com.jose.guiacom;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jose.textJustify.TextViewEx;
import com.squareup.picasso.Picasso;

public class CatalogoActivity extends AppCompatActivity implements Urls {

    String cidadeSigla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
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

        TextView info = (TextView) findViewById(R.id.txtInfo);
        info.setText(R.string.info);

        TextView info1 = (TextView) findViewById(R.id.txtInfo1);
        info1.setText(R.string.info1);

        TextView info2 = (TextView) findViewById(R.id.txtInfo2);
        info2.setText(R.string.info2);

        TextView info3 = (TextView) findViewById(R.id.txtInfo3);
        info3.setText(R.string.info3);
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
                Intent intent = new Intent(CatalogoActivity.this, SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CatalogoActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(CatalogoActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(CatalogoActivity.this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CatalogoActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            return true;
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(CatalogoActivity.this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CatalogoActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(CatalogoActivity.this, ContatoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CatalogoActivity.this, ContatoActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
