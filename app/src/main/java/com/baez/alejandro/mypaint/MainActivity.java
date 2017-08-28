package com.baez.alejandro.mypaint;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.UUID;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private  DrawingView drawingView;
    private ImageButton colors,drawBtn,eraseBtn,newBtn,saveBtn;
    private float smallBrush, mediumBrush,largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setIcon(R.drawable.bar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));


        drawingView = (DrawingView)findViewById(R.id.drawing);

        smallBrush=getResources().getInteger(R.integer.small_size);
        mediumBrush= getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);


        drawBtn =(ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        drawingView.setBrushSize(mediumBrush);

        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        colors = (ImageButton)findViewById(R.id.new_Color);
        colors.setOnClickListener(this);


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.draw_btn:
                  drawing();
                break;

            case R.id.erase_btn:
                 erasing();
                 break;

            case R.id.new_btn:
                 createNewDrawing();
                 break;

            case R.id.save_btn:
                saveDrawing();
                break;

            case R.id.new_Color:
                   setNewColor();
                break;
        }
        
    }



    private void drawing() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Tamaño de Pincel");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn= (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setErase(false);
                drawingView.setBrushSize(smallBrush);
                drawingView.setLastBruhsSize(smallBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setErase(false);
                drawingView.setBrushSize(mediumBrush);
                drawingView.setLastBruhsSize(mediumBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setErase(false);
                drawingView.setBrushSize(largeBrush);
                drawingView.setLastBruhsSize(largeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();

    }

    private void erasing() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Borrador");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setErase(true);
                drawingView.setBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setErase(true);
                drawingView.setBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setErase(true);
                drawingView.setBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    private void createNewDrawing() {

        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("Nuevo Dibujo");
        newDialog.setMessage("¿Crear un nuevo Dibujo ?");
        newDialog.setPositiveButton("Si",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawingView.startNew();
                dialog.dismiss();;
            }
        });

        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }


    private void saveDrawing() {

        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Guardar");
        saveDialog.setMessage("¿Deseas Guardar tu Dibujo en tu Galeria?");
        saveDialog.setPositiveButton("SI",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawingView.setDrawingCacheEnabled(true);

                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        drawingView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png",
                        "drawing");
                 if(imgSaved != null){
                    Toast savedToast = Toast.makeText(
                            getApplicationContext(),
                            "Se ha Guardado",
                            Toast.LENGTH_SHORT
                    );
                    savedToast.show();
                }else{
                    Toast unsavedToast = Toast.makeText(
                            getApplicationContext(),
                            "Opps! No se Pudo Guardar",
                            Toast.LENGTH_SHORT
                    );
                    unsavedToast.show();
                }
                drawingView.destroyDrawingCache();
             }
        });

        saveDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        saveDialog.show();
    }


    private void setNewColor() {
        drawingView.setErase(false);
       drawingView.setBrushSize(drawingView.getLastBruhsSize());
        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                drawingView.setColor(updateColor(color));
            }

        });
        colorPickerDialog.show();
    }


    public int updateColor(int color){
        return Color.rgb(Color.red(color),Color.green(color),Color.blue(color));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
