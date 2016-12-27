package com.jose.guiacom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        Button sair = (Button) findViewById(R.id.btnSair);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);

                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(AdminActivity.this, LoginAdminActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button cadastrar = (Button) findViewById(R.id.btnCadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, CadastroAdminActivity.class);
                startActivity(intent);
            }
        });

        Button pendentes = (Button) findViewById(R.id.btnPendentes);

        pendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListaPendentesActivity.class);
                startActivity(intent);
            }
        });

        Button pendentesPremium = (Button) findViewById(R.id.btnPendentesPremium);

        pendentesPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListaPendentesPremiumActivity.class);
                startActivity(intent);
            }
        });

        Button empresasCadastradas = (Button) findViewById(R.id.btnEmpresas);

        empresasCadastradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListaEmpresasAdminActivity.class);
                startActivity(intent);
            }
        });

        Button estatisticas = (Button) findViewById(R.id.btnEstatisticas);

        estatisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, EstatisticasActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pendentes) {
            Intent intent = new Intent(this, ListaPendentesActivity.class);
            startActivity(intent);
        } else if (id == R.id.pendentesPremium) {
            Intent intent = new Intent(this, ListaPendentesPremiumActivity.class);
            startActivity(intent);
        } else if (id == R.id.empresas) {
            Intent intent = new Intent(this, ListaEmpresasAdminActivity.class);
            startActivity(intent);
        } else if (id == R.id.estatisticas) {
            Intent intent = new Intent(this, EstatisticasActivity.class);
            startActivity(intent);
        } else if (id == R.id.cadastrar) {
            Intent intent = new Intent(this, CadastroAdminActivity.class);
            startActivity(intent);
        } else if (id == R.id.sair) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(AdminActivity.this, LoginAdminActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(AdminActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
