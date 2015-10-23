package com.jader.leitura;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GerenciarHidrometros extends Activity implements OnClickListener, LocationListener {

	public static SQLiteDatabase db = null;
	
	private Button btPrimeiro = null;
	private Button btRetrocede = null;
	private Button btInclui = null;
	private Button btAltera = null;
	private Button btExclui = null;
	private Button btAvanca = null;
	private Button btUltimo = null;
	private Button btAtualiza = null;
 
	private EditText etNumero = null;
	private EditText etNome = null;
	private EditText etObservacao = null;
	private EditText etLatitude = null;
	private EditText etLongitude = null;
 
	private Button btOk = null;
	private Button btCancelar = null;

	private Cursor cur = null;
	
	private char operacao = 'V';
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_hidrometros);
        
        iniciaComponentes();
		// travaCampos();
 		
		//db = this.openOrCreateDatabase("Dados", Context.MODE_PRIVATE, null);
		atualizaDados();
 
		if (cur.moveToFirst()) {
			mostraDados();
		}
		
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gerenciar_hidrometros, menu);
        return true;
    }
    
    public void iniciaComponentes() {
		btPrimeiro = (Button) (findViewById(R.id.btPrimeiro));
		btPrimeiro.setOnClickListener(this);
 
		btRetrocede = (Button) (findViewById(R.id.btRetrocede));
		btRetrocede.setOnClickListener(this);
 
		btInclui = (Button) (findViewById(R.id.btInsere));
		btInclui.setOnClickListener(this);
 
		btAltera = (Button) (findViewById(R.id.btAltera));
		btAltera.setOnClickListener(this);
 
		btExclui = (Button) (findViewById(R.id.btExclui));
		btExclui.setOnClickListener(this);
 
		btAvanca = (Button) (findViewById(R.id.btAvanca));
		btAvanca.setOnClickListener(this);
 
		btUltimo = (Button) (findViewById(R.id.btUltimo));
		btUltimo.setOnClickListener( this);
 
		btAtualiza = (Button) (findViewById(R.id.btAtualiza));
		btAtualiza.setOnClickListener(this);
 
		etNumero = (EditText) (findViewById(R.id.etNumero));
		etNome = (EditText) (findViewById(R.id.etNome));
		etObservacao = (EditText) (findViewById(R.id.etObservacao));
		etLatitude = (EditText) (findViewById(R.id.etLatitude));
		etLongitude = (EditText) (findViewById(R.id.etLongitude));
		
		btOk = (Button) (findViewById(R.id.btOk));
		btOk.setOnClickListener(this);
 
		btCancelar = (Button) (findViewById(R.id.btCancelar));
		btCancelar.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btPrimeiro:
			if (cur.moveToFirst()) {
				mostraDados();
			}
			break;
 
		case R.id.btRetrocede:
			if (cur.moveToPrevious()) {
				mostraDados();
			}
			break;
 
		case R.id.btInsere:
			insereDados();
			break;
 
		case R.id.btAltera:
			alteraDados();
			break;
 
		case R.id.btExclui:
			excluiDados();
			break;
 
		case R.id.btAvanca:
			if (cur.moveToNext()) {
				mostraDados();
			}
			break;
 
		case R.id.btUltimo:
			if (cur.moveToLast()) {
				mostraDados();
			}
			break;
 
		case R.id.btAtualiza:
			atualizaDados();
			if (cur.moveToFirst()) {
				mostraDados();
			}
			break;
 
		case R.id.btOk:
			botaoOk();
			// travaCampos();
			mostraManutencao();
			ocultaOkCancelar();
			break;
 
		case R.id.btCancelar:
			// travaCampos();
			mostraManutencao();
			ocultaOkCancelar();
			break;
 
		default:
			break;
		}
		
	}
	
	public void botaoOk() {
		ContentValues dados = new ContentValues();
		dados.put("numero", etNumero.getText().toString());
		dados.put("nome", etNome.getText().toString());
		dados.put("observacao", etObservacao.getText().toString());
		dados.put("latitude", etLatitude.getText().toString());
		dados.put("longitude", etLongitude.getText().toString());
		if (operacao == 'I') {
			Principal.db.insert("cota", null, dados);
			atualizaDados();
			if (cur.moveToLast()) {
				mostraDados();
			}
		} else {
			String registroAtual = cur.getString(0);
			Principal.db.update("cota", dados, "cotaid=?", new String[] { registroAtual });
			atualizaDados();
			if (cur.moveToFirst()) {
				mostraDados();
			}
		}
 
	}
	
	public void atualizaDados() {
		cur = Principal.db.query("cota", new String[] { "cotaid", "numero", "nome", "observacao", "latitude", "longitude" }, null, null, null, null, null);
	}
 
	public void travaCampos() {
		etNumero.setClickable(false);
		etNumero.setFocusable(false);
		
		etNome.setClickable(false);
		etNome.setFocusable(false);
		
		etObservacao.setClickable(false);
		etObservacao.setFocusable(false);
		
		etLatitude.setClickable(false);
		etLatitude.setFocusable(false);
 
		etLongitude.setClickable(false);
		etLongitude.setFocusable(false);
	}
 
	public void liberaCampos() {
		etNumero.setClickable(true);
		etNumero.setFocusable(true);
		
		etNome.setClickable(true);
		etNome.setFocusable(true);
		
		etObservacao.setClickable(true);
		etObservacao.setFocusable(true);
		
		etLatitude.setClickable(true);
		etLatitude.setFocusable(true);
		
		etLongitude.setClickable(true);
		etLongitude.setFocusable(true);
	}
 
	private void ocultaManutencao() {
		btPrimeiro.setVisibility(View.INVISIBLE);
		btRetrocede.setVisibility(View.INVISIBLE);
		btInclui.setVisibility(View.INVISIBLE);
		btAltera.setVisibility(View.INVISIBLE);
		btExclui.setVisibility(View.INVISIBLE);
		btAvanca.setVisibility(View.INVISIBLE);
		btUltimo.setVisibility(View.INVISIBLE);
		btAtualiza.setVisibility(View.INVISIBLE);
	}
	
	private void mostraManutencao() {
		btPrimeiro.setVisibility(View.VISIBLE);
		btRetrocede.setVisibility(View.VISIBLE);
		btInclui.setVisibility(View.VISIBLE);
		btAltera.setVisibility(View.VISIBLE);
		btExclui.setVisibility(View.VISIBLE);
		btAvanca.setVisibility(View.VISIBLE);
		btUltimo.setVisibility(View.VISIBLE);
		btAtualiza.setVisibility(View.VISIBLE);
	}
 
	private void mostraOkCancelar() {
		btOk.setVisibility(View.VISIBLE);
		btCancelar.setVisibility(View.VISIBLE);	
	}
 
	private void ocultaOkCancelar() {
		operacao = 'V';
		btOk.setVisibility(View.INVISIBLE);
		btCancelar.setVisibility(View.INVISIBLE);
	}
 
	public void mostraDados() {
		etNumero.setText(cur.getString(1));
		etNome.setText(cur.getString(2));
		etObservacao.setText(cur.getString(3));
		etLatitude.setText(cur.getString(4));
		etLongitude.setText(cur.getString(5));
	}
 
	public void insereDados() {
		liberaCampos();
	    ocultaManutencao();
		mostraOkCancelar();
		operacao = 'I';
		limparCampos();
	}
 
	public void alteraDados() {
		liberaCampos();
		ocultaManutencao();
		mostraOkCancelar();
		operacao = 'A';
		etNumero.requestFocus();
	}
	
	 public void limparCampos()
	 {
		 etNumero.requestFocus();
		 etNumero.setText("");
		 etNome.setText("");
		 etObservacao.setText("");
		 etLatitude.setText("");
		 etLongitude.setText("");
	 }
	 
	 public void excluiDados() {
			
			int quantidadeRegistros = cur.getCount();
			
			if ( quantidadeRegistros > 0 )
			{
				String registroAtual = cur.getString(0);
				Principal.db.delete("cota", "cotaid=?", new String[] { registroAtual });
				atualizaDados();
				
				if ( quantidadeRegistros > 1 )
				{
					if (cur.moveToFirst()) {
						mostraDados();
					}
				}
				else
				{
					limparCampos();
				}
			}
		}

	public void onLocationChanged(Location loc) {
		if ( operacao == 'A' || operacao == 'I' )
		{
			etLatitude.setText("" + loc.getLatitude());
			etLongitude.setText("" + loc.getLongitude());
		}
	}

	public void onProviderDisabled(String loc) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String loc) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sobre:
			// Mostra informações do aplicativo.
			Toast.makeText(this, "Aplicativo para leitura de hidrômetros\n\nDesenvolvedor: Jader Fiegenbaum\nVersão: 1.0", Toast.LENGTH_LONG).show();
			break;
			
		default:
			break;
		}
 
		return true;
	}
}
