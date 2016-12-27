package com.jose.guiacom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ListaPendentesActivity extends AppCompatActivity implements Urls {

    ArrayList<Empresa> empresasPendentes;
    ListView lvPendentes;
    FunDapter<Empresa> pendenteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pendentes2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        lvPendentes = (ListView) findViewById(R.id.lvPendentes);

        //Pegar empresas pendentes
        HashMap<String, String> postData = new HashMap<>();
        postData.put("android", "android");
        PostResponseAsyncTask taskReadPendentes = new PostResponseAsyncTask(ListaPendentesActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(ListaPendentesActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    if (!s.equals("nenhum")) {
                        empresasPendentes = new JsonConverter<Empresa>().toArrayList(s, Empresa.class);

                        BindDictionary<Empresa> dictPendente = new BindDictionary<>();
                        dictPendente.addStringField(R.id.tvNome, new StringExtractor<Empresa>() {
                            @Override
                            public String getStringValue(Empresa empresa, int position) {
                                return empresa.nome;
                            }
                        });

                        dictPendente.addStringField(R.id.tvCidade, new StringExtractor<Empresa>() {
                            @Override
                            public String getStringValue(Empresa empresa, int position) {
                                return empresa.cidade_nome;
                            }
                        });

                        pendenteAdapter = new FunDapter<>(
                                ListaPendentesActivity.this, empresasPendentes, R.layout.layout_pendentes, dictPendente);

                        lvPendentes.setAdapter(pendenteAdapter);

                        lvPendentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Empresa selected = empresasPendentes.get(position);
                                Intent intent = new Intent(ListaPendentesActivity.this, EditarActivity.class);
                                intent.putExtra("empresa", selected);
                                startActivity(intent);
                            }
                        });
                        registerForContextMenu(lvPendentes);
                    }
                }
            }
        });
        taskReadPendentes.execute(listaPendentesUrl);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Empresa selectedEmpresa = pendenteAdapter.getItem(info.position);

        if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Tem certeza que deseja excluir " + selectedEmpresa.nome + "?")
                    .setTitle(R.string.excluir_title);

            builder.setPositiveButton(R.string.excluir_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    empresasPendentes.remove(selectedEmpresa);
                    pendenteAdapter.notifyDataSetChanged();

                    HashMap<String, String> postId = new HashMap<>();
                    postId.put("id", selectedEmpresa.id.toString());
                    postId.put("android", "android");

                    PostResponseAsyncTask delete = new PostResponseAsyncTask(ListaPendentesActivity.this, postId, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            //System.out.println(s);
                            if (s.equals("sucesso")) {
                                Toast.makeText(ListaPendentesActivity.this, "Empresa deletada com sucesso!", Toast.LENGTH_LONG).show();
                            } else if (s == null || s.equals("")) {
                                Toast.makeText(ListaPendentesActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(ListaPendentesActivity.this, "Algum erro ocorreu, tente novamente!", Toast.LENGTH_LONG).show();
                            }
                        }

                        );
                        delete.execute(deleteEmpresaUrl);
                    }
                }

                );
                builder.setNegativeButton(R.string.excluir_no, null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            return super.onContextItemSelected(item);
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
            return true;
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
                    Intent intent = new Intent(ListaPendentesActivity.this, LoginAdminActivity.class);
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
}
