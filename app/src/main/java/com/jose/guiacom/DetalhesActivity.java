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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DetalhesActivity extends AppCompatActivity implements Urls {

    Empresa empresa;
    TextView txtNome, txtPresentation, txtTel, txtTel2, txtAddress,
            txtBairro, txtCidade, txtEstado, txtSite, txtEmail,
            tvEmpresa, tvEndereco, tvBairro, tvCidade, tvEstado;
    ArrayList<Estado> estado;
    String cidade, cidadeSigla, voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        empresa = (Empresa) getIntent().getSerializableExtra("empresa");

        cidadeSigla = getIntent().getStringExtra("cidade");
        cidade = cidadeSigla.substring(0, cidadeSigla.indexOf(" -"));

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
                Intent intent = new Intent(DetalhesActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
                startActivity(intent);
            }
        });

        tvEmpresa = (TextView) findViewById(R.id.txtNomeTitle);
        tvBairro = (TextView) findViewById(R.id.txtBairroTitle);
        tvCidade = (TextView) findViewById(R.id.txtCidadeTitle);
        tvEndereco = (TextView) findViewById(R.id.txtAddressTitle);
        tvEstado = (TextView) findViewById(R.id.txtEstadoTitle);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtPresentation = (TextView) findViewById(R.id.txtPresentation);
        txtTel = (TextView) findViewById(R.id.txtTel);
        txtTel2 = (TextView) findViewById(R.id.txtTel2);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtBairro = (TextView) findViewById(R.id.txtBairro);
        txtCidade = (TextView) findViewById(R.id.txtCidade);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtSite = (TextView) findViewById(R.id.txtSite);

        final HashMap<String, String> postId = new HashMap<>();
        postId.put("cidade_id", empresa.cidade_id.toString());
        postId.put("android", "android");

        PostResponseAsyncTask getEstado = new PostResponseAsyncTask(DetalhesActivity.this, postId, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(DetalhesActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    estado = new JsonConverter<Estado>().toArrayList(s, Estado.class);

                    tvEmpresa.setText(R.string.nome_title);
                    tvBairro.setText(R.string.bairro_title);
                    tvEndereco.setText(R.string.endereco_title);
                    tvCidade.setText(R.string.cidade_title);
                    tvEstado.setText(R.string.estado_title);

                    txtNome.setText(empresa.nome);
                    txtPresentation.setText(empresa.apresentacao);

                    txtTel.setText(empresa.telefone1);
                    txtTel.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            String uri = "tel:" + empresa.telefone1;
                            final Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(uri));

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                            builder.setMessage(R.string.dialog_fone_message)
                                    .setTitle(R.string.dialog_fone_title);

                            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(intent);
                                }
                            });
                            builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        }
                    });

                    if (empresa.telefone2 != null && !empresa.telefone2.equals("")) {
                        txtTel2.setText(empresa.telefone2);
                        txtTel2.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                String uri = "tel:" + empresa.telefone2;
                                final Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(uri));

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                                builder.setMessage(R.string.dialog_fone_message)
                                        .setTitle(R.string.dialog_fone_title);

                                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                                return true;
                            }
                        });
                    }
                    else
                        txtTel2.setText("");

                    String endereco;
                    if (empresa.numero != null && !empresa.numero.toString().equals(""))
                        endereco = empresa.endereco + ", " + empresa.numero;
                    else
                        endereco = empresa.endereco + ", s/n";

                    txtAddress.setText(endereco);
                    txtBairro.setText(empresa.bairro);
                    txtCidade.setText(cidade);
                    txtEstado.setText(estado.get(0).nome);

                    if (empresa.email != null && !empresa.email.equals("")) {
                        txtEmail.setText(empresa.email);
                        txtEmail.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                                Uri data = Uri.parse("mailto:?subject=&body=&to=" + empresa.email);
                                emailIntent.setData(data);
                                startActivity(emailIntent);
                                return true;
                            }
                        });
                    }
                    else
                        txtEmail.setText(R.string.nao_informado);

                    if (empresa.site != null && !empresa.site.equals(""))
                        txtSite.setText(empresa.site);
                    else
                        txtSite.setText(R.string.nao_informado);
                }
            }
        });
        getEstado.execute(getEstadoUrl);
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
                Intent intent = new Intent(DetalhesActivity.this, SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(DetalhesActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesActivity.this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesActivity.this, CatalogoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesActivity.this, CatalogoActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesActivity.this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesActivity.this, ContatoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesActivity.this, ContatoActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
