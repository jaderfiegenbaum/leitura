package com.jader.leitura;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Principal extends Activity implements OnClickListener {

	private Button btNovaLeitura = null;
	private Button btGerenciarHidrometros = null;
	private Button btExportarLeituras = null;
	private Button btLimparLeituras = null;
	private Button btSair = null;
	
	// Base de dados.
	public static SQLiteDatabase db = null;
	private final String createTablesSQL = "CREATE TABLE cota (cotaid integer primary key autoincrement, numero text, nome text, observacao text, latitude text, longitude text, leitura text);";
	private final String dropTablesSQL = "DROP TABLE cota;";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

		// Inicia a base de dados.
        db = new DataBaseManager(this, "leitura", 1, createTablesSQL, dropTablesSQL).getWritableDatabase();
     		
        // Grava instância do banco de dados.
		Parametro.setObjeto(db);
		
        // Instância componentes da tela.
        btNovaLeitura = (Button) findViewById(R.id.btNovaLeitura);
        btNovaLeitura.setOnClickListener(this);
        
        btGerenciarHidrometros = (Button) findViewById(R.id.btGerenciarHidrometros);
        btGerenciarHidrometros.setOnClickListener(this);
        
        btExportarLeituras = (Button) findViewById(R.id.btExportarLeituras);
        btExportarLeituras.setOnClickListener(this);
        
        btLimparLeituras = (Button) findViewById(R.id.btLimparLeituras);
        btLimparLeituras.setOnClickListener(this);
        
        btSair = (Button) findViewById(R.id.btSair);
        btSair.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        return true;
    }

	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.btNovaLeitura:
				Intent intent = new Intent(this, Leitura.class);
				this.startActivity(intent);
				break;
			
			case R.id.btSair:
				finish();
				break;
				
			case R.id.btGerenciarHidrometros:
				Intent intent2 = new Intent(this, GerenciarHidrometros.class);
				this.startActivity(intent2);
				break;
				
			case R.id.btExportarLeituras:
				Intent intent3 = new Intent(this, ExportarDados.class);
				this.startActivity(intent3);
				break;
			case R.id.btLimparLeituras:
				db.execSQL("UPDATE cota SET leitura = '';");
				Toast.makeText(this, "Leituras apagadas com sucesso!", Toast.LENGTH_LONG).show();
				break;
				
		    default:
		    	break;
		}
		
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
