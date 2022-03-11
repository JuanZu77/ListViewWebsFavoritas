package com.example.listviewwebfavoritas;


import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText et1;
	ListView lv1;
	
	ArrayList<String>lista1;
	ArrayAdapter<String>adaptador1;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        et1=(EditText)findViewById(R.id.editText1);
        lv1=(ListView)findViewById(R.id.listView1);
        
        agregarWeb(); //metodo de inicio de la APP
        
    }
 
//PASO 1: metodo agregarWeb()
    
	private void agregarWeb() {
		// TODO Auto-generated method stub
		
//PASO 1-A: Array List y Traer "table web" de la clase AdminSOLiteOpenHelper 
		
		//ArrayList --> creamos una lista
		lista1 = new ArrayList<String>();
		
		AdminSOLiteOpenHelper admin=new AdminSOLiteOpenHelper(this, "base1", null, 1);
		SQLiteDatabase bd=admin.getWritableDatabase();
    	
    	//Recuperar Datos
    	Cursor registro=bd.rawQuery("select descripcion from web", null);
    	
    	//While para recorrer y se repite mientras encuentre datos
    	
    	while (registro.moveToNext())
    	{
    		//movetoFirt --> se ubica en la primera fila encontrada. No hay lugar para traer otro dato (EditText)
    		//movetoNext --> para que dentro del While vaya avanzando el puntero y leer/traer fila por fila
    		lista1.add(registro.getString(0));
    		   		
    	}
    	
    	//ArrayAdapter --> relacionamos con el ListView	
        adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista1);
    	lv1.setAdapter(adaptador1);

//PASO 1-B: Mostra lista de Web al iniciar la APP
    	
    //	Intent intento1=new Intent (Intent.ACTION_VIEW);
		//startActivity(intento1);/*Funciona pero muestra cosas al iniciar que no deberia*/
    	
        /*     Esto tambien funciona pero hace lo mismo:
        Intent intento1=new Intent (Intent.ACTION_VIEW);
    	File web = new File (getExternalFilesDir(null), et1.getText().toString());
    	intento1.putExtra(Intent.ACTION_VIEW, Uri.fromFile(web));
		startActivity(intento1);        */
		
//PASO 1-C: capturar Click
		
		//Capturar Click para ir a la WEB
		//lv1.setOnItemClickListener(new OnItemClickListener(){}); --> para poder importar
		lv1.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
                /*Recordar arg2 tiene la posiciones del Item seleccionado*/
				/*Recordamos con un Toast para que muestra la posicion*/
				
				Toast.makeText(MainActivity.this, lista1.get(arg2), Toast.LENGTH_LONG).show();
				/*MainActivity.this, --> debemos aclarar a que clase hacemos la referncia*/
				//lista1 ahora le agregamos el metodo get() para mostrar el item seleccionado --> lista1.get(arg2)
								
				/*Ahora al seleccionar un item ir a la web con la clase Intent*/
				Intent intento1=new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+lista1.get(arg2)));
				startActivity(intento1);
				
			}});
		
	}
	
//PASO 2: boton agregar *
	public void agregar(View v)
	{
	   //En el Nombre de la clase de la Tabla me confundi la "O" por la "Q"
		AdminSOLiteOpenHelper admin=new AdminSOLiteOpenHelper(this, "base1", null, 1);
		
		SQLiteDatabase bd = admin.getWritableDatabase();  //devuelve referencia de "base1"
    	
    	//Ahora PASAR DATOS
    	ContentValues registro= new ContentValues();
    	registro.put("descripcion", et1.getText().toString());
    	    	
    	bd.insert("web", null, registro);
    	//Insert en la tabla "articulos" y registramos (el null porque no se usa ese parametro)
    	
    	String sitio=et1.getText().toString();  
    	lista1.add(sitio);
    	adaptador1.notifyDataSetChanged();   
    	et1.setText("");
    	
    	Toast.makeText(this, "Se almaceno la Web", Toast.LENGTH_LONG).show();
    	
    	bd.close(); //liberar base de datos

	}
	
	/*PASO 3: boton borrar */
	public void borrar (View view)
	{
		 //En el Nombre de la clase de la Tabla me confundi la "O" por la "Q"
		AdminSOLiteOpenHelper admin=new AdminSOLiteOpenHelper(this, "base1", null, 1);
   	 
		SQLiteDatabase bd = admin.getWritableDatabase();
		
        String descripcion = et1.getText().toString();
    	
    	if (descripcion.length()>0)
    	{
    		int cant = bd.delete("web", "descripcion='"+descripcion+"'", null);
    		
    		//Esto Actualiza EL ListView
            String sitio=et1.getText().toString();    	  
        	lista1.remove(sitio);
         	adaptador1.notifyDataSetChanged();   
    		
    		if(cant>0){
    			Toast.makeText(this, "Se elimino el sitio web", Toast.LENGTH_LONG).show();
        		et1.setText("");
    		}
    	    else{
    			Toast.makeText(this, "No existe el sitio web", Toast.LENGTH_LONG).show();
    		}  	
    	}else{
    		Toast.makeText(this, "No se ingresaron datos", Toast.LENGTH_LONG).show();
    	}
	  	
	}
	
	/**/
	public void salir(View v)
	{
		finish();
	}



}
