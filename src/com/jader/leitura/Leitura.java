package com.jader.leitura;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import Classes.Cota;
import Classes.Calculo;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Leitura extends Activity implements OnClickListener, LocationListener{

	private Thread t = null;
	private TextView tvProximaLeitura = null;
	private TextView tvNumero = null;
	private TextView tvNome = null;
	private TextView tvObservacaoC = null;
	
	private EditText etLeitura = null;
	private Button btProximaLeitura = null;
	private Button btLeituraAnterior = null;
	private Button btLeituraProxima = null;
	private Button btSalvar = null;
	private Cursor cur = null;
	private ArrayList<Cota> cotas = new ArrayList<Cota>();
	
	public static double latitude;
	public static double longitude;
	
	private int posMaisPerto = 0;
	private int cotaIdMaisPerto = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitura);
        
        tvProximaLeitura = (TextView) (findViewById(R.id.tvProximaLeitura));
        tvNumero = (TextView) (findViewById(R.id.tvNumero));
        tvNome = (TextView) (findViewById(R.id.tvNome));
        tvObservacaoC = (TextView) (findViewById(R.id.tvObservacaoC));
        
        etLeitura = (EditText) (findViewById(R.id.etLeitura));
        
        btProximaLeitura = (Button) (findViewById(R.id.btProximaLeitura));
        btProximaLeitura.setOnClickListener(this);
        btProximaLeitura.setVisibility(View.INVISIBLE);
        
        btLeituraAnterior = (Button) (findViewById(R.id.btLeituraAnterior));
        btLeituraAnterior.setOnClickListener(this);
        
        btLeituraProxima = (Button) (findViewById(R.id.btLeituraProxima));
        btLeituraProxima.setOnClickListener(this);
        
        btSalvar = (Button) (findViewById(R.id.btSalvar));
        btSalvar.setOnClickListener(this);
        
        // Obtém as leituras.
        atualizaDados();
        cur.moveToPrevious();
    	while ( cur.moveToNext() )
    	{
    		Cota cota = new Cota();
    		cota.cotaid = cur.getInt(0);
    		
    		if ( cur.getString(1) != null )
    		{
    			cota.numero = Integer.parseInt(cur.getString(1));
    		}
    		
    		cota.nome = cur.getString(2);
    		cota.observacao = cur.getString(3);
    		cota.latitude = cur.getString(4);
    		cota.longitude = cur.getString(5);
    		
    		cotas.add(cota);
    	}
    	
    	if ( cotas.size() == 0 )
    	{
    		tvProximaLeitura.setText("Sem hidrômetros.");
    		btLeituraAnterior.setVisibility(View.INVISIBLE);
    		btLeituraProxima.setVisibility(View.INVISIBLE);
    		btSalvar.setVisibility(View.INVISIBLE);
    		
    	}
    	else
    	{
	        if ( cur.moveToFirst() )
	        {
	        	mostraDados();
	        }
	        
	        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
	        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_leitura, menu);
        return true;
    }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btProximaLeitura:
			encontrar();
			break;
 
		case R.id.btLeituraAnterior:
			if (cur.moveToPrevious()) {
				mostraDados();
			}
			break;
		
		case R.id.btLeituraProxima:
			if (cur.moveToNext()) {
				mostraDados();
			}
			break;
		
		case R.id.btSalvar:
			salvar();
			break;
		}
		
	}
	
	private void salvar()
	{
		try
		{
			Toast.makeText(this, "Leitura salva com sucesso", Toast.LENGTH_LONG).show();
			ContentValues dados = new ContentValues();
			
			if ( etLeitura.getText().toString().length() > 0 )
			{
				dados.put("leitura", etLeitura.getText().toString());
			}
			else
			{
				dados.put("leitura", "0");
			}
			
			String registroAtual = cur.getString(0);
			Principal.db.update("cota", dados, "cotaid=?", new String[] { registroAtual });
			cotaIdMaisPerto = Integer.parseInt(registroAtual);
			encontrar();
		}
		catch (Exception e)
		{
		}
	}
	
	private void mostraDados()
	{
		tvNumero.setText("Número: " + cur.getString(1));
		tvNome.setText("Nome: " + cur.getString(2));
		etLeitura.setText(cur.getString(6));
		tvObservacaoC.setText("Observação: " + cur.getString(3));
	}
	
	public void atualizaDados() {
		cur = Principal.db.query("cota", new String[] { "cotaid", "numero", "nome", "observacao", "latitude", "longitude", "leitura" }, null, null, null, null, null);
	}

	public void onLocationChanged(Location loc) {
			
		Double latitude = loc.getLatitude();
		Double longitude = loc.getLongitude();
		
		Leitura.latitude = Calculo.converte(latitude);
		Leitura.longitude = Calculo.converte(longitude);
		localizar();
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}
	
	public void localizar()
	{
		double menorDistancia = 99999999999999999999999999999999.99;
		int pos = 0;
		for ( Cota cota : cotas )
		{
			String latitude = cota.latitude != null ? cota.latitude : "";
			String longitude = cota.longitude != null ? cota.longitude : "";
		
			if ( latitude.length() > 0 && longitude.length() > 0 )
			{
				double latB = Calculo.converte(Double.parseDouble(latitude));
				double longB = Calculo.converte(Double.parseDouble(longitude));
				
				double distancia = Calculo.getDistancia(Leitura.latitude, Leitura.longitude, latB, longB);
				
				if ( distancia < menorDistancia )
				{
					menorDistancia = distancia;
					posMaisPerto = pos;
				}
			}
			
			pos++;
		}
		
		String cota = cotas.get(posMaisPerto).nome;
		cota = cota != null ? cota : "Sem nome";
		int cotaIdMaisPertoAnterior = cotaIdMaisPerto;
		cotaIdMaisPerto = cotas.get(posMaisPerto).cotaid;
		
		// Quando for encontrado outro hidrômetro. toca um sino.
		if ( cotaIdMaisPertoAnterior != cotaIdMaisPerto )
		{
			MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sino2);
			mediaPlayer.start();
			btProximaLeitura.setVisibility(View.VISIBLE);
		}
		
		tvProximaLeitura.setText("Próximo: " + cota);
	}

	private void encontrar()
	{
		atualizaDados();
		while ( cur.moveToNext() )
		{
			if ( cotaIdMaisPerto == cur.getInt(0) )
			{
				break;
			}
		}
		
		mostraDados();
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
