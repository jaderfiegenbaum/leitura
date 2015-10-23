package com.jader.leitura;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Classes.Cota;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ExportarDados extends Activity implements OnClickListener {
	
	private Cursor cur = null;
	private Button btExportar = null;
	private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar_dados);
        
        btExportar = (Button) (findViewById(R.id.btExportar));
        btExportar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_exportar_dados, menu);
        return true;
    }
    
    public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btExportar:
			verificarSD();
			
            if (mExternalStorageWriteable)
            {
                gravarArquivo();
            }
            else
            {
            	Toast.makeText(this, "Não é possível gravar dados no SD", Toast.LENGTH_LONG).show();
            }
            
			break;
		}
    }
    
    public void atualizaDados() {
		cur = Principal.db.query("cota", new String[] { "cotaid", "numero", "nome", "observacao", "latitude", "longitude", "leitura" }, null, null, null, null, null);
	}
    
    private String gerarCsv()
    {
    	String csv = "";
    	atualizaDados();
    	cur.moveToPrevious();
    	while ( cur.moveToNext() )
    	{
    		csv += cur.getString(0) + ",";
    		csv += cur.getString(1) + ",";
    		csv += cur.getString(2) + ",";
    		csv += cur.getString(3) + ",";
    		csv += cur.getString(4) + ",";
    		csv += cur.getString(5) + ",";
    		csv += cur.getString(6) + "\n";
    	}
    	
    	return csv;
    }
    
    private void gravarArquivo() {
        try {
            try {
            	String conteudo = gerarCsv();
                File f = new File(Environment.getExternalStorageDirectory()+"/leitura.csv");
                FileOutputStream out = new FileOutputStream(f);
                out.write(conteudo.getBytes());
                out.flush();
                out.close();
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        Toast.makeText(this, "Base exportada com sucesso", Toast.LENGTH_LONG).show();
    }
    
    private void verificarSD() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // SD montado, podemos ler e escrever no disco
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // SD montado como read only, só podemos ler
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Existe algo errado com o disco ou não existe dispositivo
            // Nao podemos fazer nada.
            mExternalStorageAvailable = mExternalStorageWriteable = false;
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
