package com.jose.guiacom;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.FunDapter;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class EstatisticasActivity extends AppCompatActivity implements Urls{

    ArrayList<Empresa> empresasCadastradas;
    TextView[] tvQtd, tvPorcento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        tvQtd = new TextView[7];
        tvQtd[0] = (TextView) findViewById(R.id.tvQtd7);
        tvQtd[1] = (TextView) findViewById(R.id.tvQtd1);
        tvQtd[2] = (TextView) findViewById(R.id.tvQtd2);
        tvQtd[3] = (TextView) findViewById(R.id.tvQtd3);
        tvQtd[4] = (TextView) findViewById(R.id.tvQtd4);
        tvQtd[5] = (TextView) findViewById(R.id.tvQtd5);
        tvQtd[6] = (TextView) findViewById(R.id.tvQtd6);

        tvPorcento = new TextView[7];
        tvPorcento[0] = (TextView) findViewById(R.id.tvPorcento7);
        tvPorcento[1] = (TextView) findViewById(R.id.tvPorcento1);
        tvPorcento[2] = (TextView) findViewById(R.id.tvPorcento2);
        tvPorcento[3] = (TextView) findViewById(R.id.tvPorcento3);
        tvPorcento[4] = (TextView) findViewById(R.id.tvPorcento4);
        tvPorcento[5] = (TextView) findViewById(R.id.tvPorcento5);
        tvPorcento[6] = (TextView) findViewById(R.id.tvPorcento6);

        HashMap<String, String> postData = new HashMap<>();
        postData.put("android", "android");
        PostResponseAsyncTask taskReadPremium = new PostResponseAsyncTask(EstatisticasActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(EstatisticasActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    if (!s.equals("nenhum")) {
                        empresasCadastradas = new JsonConverter<Empresa>().toArrayList(s, Empresa.class);

                        Integer qtd[] = {0,0,0,0,0,0,0};
                        for (Empresa e: empresasCadastradas) {
                            qtd[0]++;
                            switch (e.pendente) {
                                case 0:
                                    switch (e.tipoCadastro) {
                                        case 0:
                                            qtd[1]++;
                                            break;
                                        case 1:
                                            qtd[2]++;
                                            break;
                                        case 2:
                                            qtd[3]++;
                                            break;
                                        case 3:
                                            qtd[4]++;
                                            break;
                                    }
                                    break;

                                case 1:
                                    switch (e.wantsPremium) {
                                        case 0:
                                            qtd[5]++;
                                            break;
                                        case 1:
                                            qtd[6]++;
                                            break;
                                    }
                                    break;
                            }
                        }

                        for (int i = 0; i < 7; i++) {
                            tvQtd[i].setText(qtd[i].toString());
                            tvPorcento[i].setText(toPercent(qtd[i], qtd[0]));
                        }

                    } else
                        Toast.makeText(EstatisticasActivity.this, "Nenhuma empresa cadastrada!", Toast.LENGTH_LONG).show();
                }
            }
        });
        taskReadPremium.execute(listaEmpresasAdminUrl);
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
            return true;
        } else if (id == R.id.cadastrar) {
            Intent intent = new Intent(this, CadastroAdminActivity.class);
            startActivity(intent);
        } else if (id == R.id.sair) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(EstatisticasActivity.this, LoginAdminActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public String toPercent(Integer qtd, Integer total){
        double p = (qtd.doubleValue()*100)/total.doubleValue();
        return String.format("%.2f", p) + "%";
    }
}
