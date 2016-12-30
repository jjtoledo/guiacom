package com.jose.guiacom;

import android.Manifest;
import                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DetalhesPremiumActivity extends AppCompatActivity implements OnMapReadyCallback, Urls {

    Empresa empresa;
    TextView txtNome, txtPresentation, txtTel, txtTel2, txtAddress,
            txtBairro, txtCidade, txtEstado, txtSite, txtEmail,
            tvEmpresa, tvEndereco, tvBairro, tvCidade, tvEstado;
    ImageView imgLogo;
    ArrayList<Estado> estado;
    String cidade, cidadeSigla, voltar, address;
    Double lat=0.0, lng=0.0;
    ImageButton btnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_premium);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnLoc = (ImageButton) findViewById(R.id.btnLoc);

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
                Intent intent = new Intent(DetalhesPremiumActivity.this, CatalogoActivity.class);
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
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
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
                Intent intent = new Intent(DetalhesPremiumActivity.this, SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesPremiumActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(DetalhesPremiumActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesPremiumActivity.this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesPremiumActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesPremiumActivity.this, CatalogoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesPremiumActivity.this, CatalogoActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesPremiumActivity.this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesPremiumActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(DetalhesPremiumActivity.this, ContatoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetalhesPremiumActivity.this, ContatoActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

        final HashMap<String, String> postId = new HashMap<>();
        postId.put("cidade_id", empresa.cidade_id.toString());
        postId.put("android", "android");

        PostResponseAsyncTask getEstado = new PostResponseAsyncTask(DetalhesPremiumActivity.this, postId, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(DetalhesPremiumActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    estado = new JsonConverter<Estado>().toArrayList(s, Estado.class);

                    tvEmpresa.setText(R.string.nome_title);
                    tvBairro.setText(R.string.bairro_title);
                    tvEndereco.setText(R.string.endereco_title);
                    tvCidade.setText(R.string.cidade_title);
                    tvEstado.setText(R.string.estado_title);

                    String logo = img + empresa.logo;

                    Picasso.with(DetalhesPremiumActivity.this)
                            .load(logo)
                            .placeholder(R.drawable.perfil)
                            .error(R.drawable.perfil)
                            .resize(width / 2, width / 2)
                            .into(imgLogo);

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
                    if (empresa.numero != null && !empresa.numero.toString().equals("")) {
                        endereco = empresa.endereco + ", " + empresa.numero;
                        address = estado.get(0).nome + " " + cidade + " " + empresa.endereco + " " + empresa.numero;
                    } else {
                        address = estado.get(0).nome + " " + cidade + " " + empresa.endereco;
                        endereco = empresa.endereco + ", s/n";
                    }
                    //System.out.println(address);

                    HashMap<String, String> postAddress = new HashMap<>();
                    postAddress.put("address", address);
                    postAddress.put("android", "android");
                    PostResponseAsyncTask getLatLong = new PostResponseAsyncTask(DetalhesPremiumActivity.this, postAddress, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.equals("") || s == null){
                                Toast.makeText(DetalhesPremiumActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                            } else {
//                                Log.i("Maps", s);
                                lat = Double.valueOf(s.substring(s.indexOf("[")+1,s.indexOf(",")));
                                //System.out.println(lat);

                                lng = Double.valueOf(s.substring(s.indexOf(",") + 1, s.indexOf("]")));
                                //System.out.println(lng);

                                LatLng city = new LatLng(lat, lng);

                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 17));

                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lng))
                                        .title(txtNome.getText().toString()));

                                btnLoc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + lat + "," + lng + "&zoom=16");
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        startActivity(mapIntent);
                                    }
                                });
                            }
                        }
                    });
                    getLatLong.execute(getLatLongUrl);

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

                    if (empresa.site != null && !empresa.site.equals("")) {
                        txtSite.setText(empresa.site);
                        txtSite.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + empresa.site));

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                                builder.setMessage(R.string.dialog_site_message)
                                        .setTitle(R.string.dialog_site_title);

                                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(browserIntent);
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
                        txtSite.setText(R.string.nao_informado);
                }
            }
        });
        getEstado.execute(getEstadoUrl);
    }
}
