package com.jose.guiacom;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.jose.guiacom.Urls.getCidadeUrl;
import static com.jose.guiacom.Urls.getDetalhesUrl;
import static com.jose.guiacom.Urls.img;

public class DetalhesCidadeActivity extends AppCompatActivity {

    String cidade, cidadeSigla;
    ArrayList<Foto> fotos;
    ArrayList<Detalhes> detalhes;
    TextView txtCidade, txtApoio, txtDescricao;
    ImageView foto_pref, previous, next;
    CustomSwipeAdapter csAdapter;
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_cidade);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

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
                Intent intent = new Intent(DetalhesCidadeActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
                startActivity(intent);
            }
        });

        fotos = new ArrayList<>();
        fotos = (ArrayList<Foto>) getIntent().getSerializableExtra("fotos");

        cidadeSigla = getIntent().getStringExtra("cidade");
        cidade = cidadeSigla.substring(0, cidadeSigla.indexOf(" -"));

        txtCidade = (TextView) findViewById(R.id.txtCity);
        txtCidade.setText(cidadeSigla);

        txtApoio = (TextView) findViewById(R.id.txtApoio);
        txtDescricao = (TextView) findViewById(R.id.txtDescricao);

        foto_pref = (ImageView) findViewById(R.id.foto_pref);
        previous = (ImageView) findViewById(R.id.previous);
        next = (ImageView) findViewById(R.id.next);

        csAdapter = new CustomSwipeAdapter(this, fotos, 1, 0);
        vp = (ViewPager) findViewById(R.id.view_pager);
        vp.setAdapter(csAdapter);

        if (fotos.size() < 2){
            next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
        }

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(vp.getCurrentItem()-1, true);
                if (vp.getCurrentItem() == 0) {
                    previous.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
                } else {
                    previous.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                }

                if (vp.getCurrentItem() == fotos.size()-1) {
                    next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
                } else {
                    next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(vp.getCurrentItem()+1, true);
                if (vp.getCurrentItem() == 0) {
                    previous.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
                } else {
                    previous.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                }

                if (vp.getCurrentItem() == fotos.size()-1) {
                    next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
                } else {
                    next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                }
            }
        });

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (vp.getCurrentItem() == 0) {
                    previous.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
                } else {
                    previous.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                }

                if (vp.getCurrentItem() == fotos.size()-1) {
                    next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray));
                } else {
                    next.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final HashMap<String, String> postCidade = new HashMap<>();
        postCidade.put("cidade", cidade);
        postCidade.put("android", "android");

        PostResponseAsyncTask getCidade = new PostResponseAsyncTask(DetalhesCidadeActivity.this, postCidade, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (!s.equals("nenhum")) {
                    //System.out.println(s);
                    final String cidade_id = s;
                    final HashMap<String, String> postCidade = new HashMap<>();
                    postCidade.put("cidade_id", cidade_id);
                    postCidade.put("android", "android");

                    PostResponseAsyncTask getDetalhes = new PostResponseAsyncTask(DetalhesCidadeActivity.this, postCidade, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            detalhes = new JsonConverter<Detalhes>().toArrayList(s, Detalhes.class);
                            if (detalhes.get(0).foto_pref != null) {
                                txtApoio.setVisibility(View.VISIBLE);

                                foto_pref.setVisibility(View.VISIBLE);
                                int d = width(width, 2);
                                Picasso.with(DetalhesCidadeActivity.this)
                                        .load(img + detalhes.get(0).foto_pref)
                                        .resize(d, 0)
                                        .into(foto_pref);
                            }
                            txtDescricao.setText(detalhes.get(0).descricao);
                        }
                    });
                    getDetalhes.execute(getDetalhesUrl);
                }
            }
        });
        getCidade.execute(getCidadeUrl);
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
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("cidade", cidadeSigla);
            startActivity(intent);
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            Intent intent = new Intent(this, CadastroActivity.class);
            intent.putExtra("cidade", cidadeSigla);
            startActivity(intent);
        } else if (id == R.id.catalogo) {
            Intent intent = new Intent(this, CatalogoActivity.class);
            intent.putExtra("cidade", cidadeSigla);
            startActivity(intent);
        } else if (id == R.id.admin) {
            Intent intent = new Intent(this, LoginAdminActivity.class);
            intent.putExtra("cidade", cidadeSigla);
            startActivity(intent);
        } else if (id == R.id.contato) {
            Intent intent = new Intent(this, ContatoActivity.class);
            intent.putExtra("cidade", cidadeSigla);
            startActivity(intent);
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public int width(int width, int tipo){
        Double result;
        switch (tipo){
            case 1:
                result = (width-(width*converte(width, 32))-(width*converte(width, 10)))*0.3333;
                return result.intValue()+1;
            case 2:
                result = (width-(width*converte(width, 32)))*0.61;
                return result.intValue();
            case 3:
                result = width-(width*converte(width, 32));
                return result.intValue()+1;
            case 4:
                result = (width-(width*converte(width, 32))-(width*converte(width, 10)))*0.20;
                return result.intValue()+1;
        }
        return 0;
    }

    public float dpToPx(int dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public double converte(int width, int dp) {
        return dpToPx(dp)/width;
    }
}
