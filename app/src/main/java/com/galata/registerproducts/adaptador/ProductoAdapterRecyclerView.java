package com.galata.registerproducts.adaptador;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.galata.registerproducts.MainActivity;
import com.galata.registerproducts.R;
import com.galata.registerproducts.modelo.CardViewProducto;
import com.galata.registerproducts.modelo.ConexionSQLite;
import com.galata.registerproducts.utilidades.Utilidades;
import com.galata.registerproducts.vistas.EditProductActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ProductoAdapterRecyclerView
        extends RecyclerView.Adapter<ProductoAdapterRecyclerView.ProductoViewHolder>
        implements Filterable {
    private ArrayList<CardViewProducto> cardViewProductos;
    private ArrayList<CardViewProducto> cardViewProductsAll;
    private Context contexto;
    int recurso;
    ConexionSQLite conn;

    public ProductoAdapterRecyclerView(ArrayList<CardViewProducto> cardViewProductos, Context contexto, int recurso) {
        this.cardViewProductos = cardViewProductos;
        this.cardViewProductsAll = new ArrayList<>(cardViewProductos);
        this.contexto = contexto;
        this.recurso = recurso;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(recurso, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        CardViewProducto cardViewProducto;
        cardViewProducto = cardViewProductos.get(position);

        holder.linear.setBackgroundColor( cardViewProducto.getColor() );
        holder.initial.setText(Character.toString(cardViewProducto.getNombre().charAt(0)).toUpperCase());
        holder.clave.setText("id: "+cardViewProducto.getClave());
        holder.name.setText(cardViewProducto.getNombre());
        holder.precio.setText("$ "+cardViewProducto.getPrecio());
        holder.detalle.setText("Detalle: "+cardViewProducto.getDetalle());
        holder.cantidad.setText("Cantidad: "+cardViewProducto.getCantidad());
    }


    public void updateCard(int list_position, String name, String cla, String pre,
                           String det, String can, int color) {
        cardViewProductos.get(list_position).setNombre(name);
        cardViewProductos.get(list_position).setClave(cla);
        cardViewProductos.get(list_position).setPrecio(pre);
        cardViewProductos.get(list_position).setDetalle(det);
        cardViewProductos.get(list_position).setCantidad(can);

        notifyItemChanged(list_position);
    }


    public void addCard(String clave, String nom, String pre, String det, String can, int color) {
        CardViewProducto card = new CardViewProducto();

        card.setClave(clave);
        card.setNombre(nom);
        card.setId(getItemCount());
        card.setPrecio(pre);
        card.setDetalle(det);
        card.setColor(color);
        card.setCantidad(can);

        int id = getItemCount();
        cardViewProductos.add(card);
        cardViewProductsAll.add(card);

        //Activity ac = (Activity) contexto;
        //((Activity) contexto).getParentActivityIntent();
        ((MainActivity)contexto).doSmoothScroll(getItemCount());
        notifyItemInserted(getItemCount());
    }

    @Override
    public long getItemId(int position)
    {
        return cardViewProductos.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return cardViewProductos.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularReveal(View view )
    {
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(),view.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animation.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularDelete(final View view, final int list_position) {
        int centerX = view.getWidth();
        int centerY = view.getHeight();
        int startRadius = view.getWidth();
        int endRadius = 0;
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
                cardViewProductos.remove(list_position);
                //cardViewProductsAll.remove(list_position);
                notifyItemRemoved(list_position);
            }
        });
        animation.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewAttachedToWindow(ProductoViewHolder viewHolder){
        super.onViewAttachedToWindow(viewHolder);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateCircularReveal(viewHolder.itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CardViewProducto> filteredList = new ArrayList<>();
            if( charSequence.toString().isEmpty() ){
                filteredList.addAll(cardViewProductsAll);
            }else{
                for( CardViewProducto p : cardViewProductsAll  ){
                    if(p.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(p);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            cardViewProductos.clear();
            cardViewProductos.addAll((Collection<? extends CardViewProducto>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        private TextView initial;
        private TextView name;
        private TextView clave;
        private TextView precio;
        private TextView detalle;
        private TextView cantidad;
        private LinearLayout linear;
        private Button delete;

        public ProductoViewHolder(@NonNull View itemVie) {
            super(itemVie);

            linear = itemVie.findViewById(R.id.fondo_color_card);
            initial = itemVie.findViewById(R.id.initial_cli);
            clave = itemVie.findViewById(R.id.clave);
            name = itemVie.findViewById(R.id.nom_pro);
            precio = itemVie.findViewById(R.id.precio);
            detalle = itemVie.findViewById(R.id.detalle);
            cantidad = itemVie.findViewById(R.id.cantidad);

            delete = itemVie.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    final CharSequence opciones[] = {"SI", "CANCELAR"};
                    final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(contexto);
                    alertOpciones.setTitle("¿Está seguro que desea eliminar este producto?");
                    alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if( opciones[i].equals("SI") ){
                                conn = new ConexionSQLite(contexto, "bd_prod", null, 1);
                                SQLiteDatabase bd = conn.getWritableDatabase();
                                int requestCode = getAdapterPosition();
                                cardViewProductsAll.remove(requestCode);
                                String paramentros[] = {cardViewProductos.get(requestCode).getClave()};
                                bd.delete(Utilidades.TABLA_PRODUCTOS, Utilidades.CAMPO_CLAVE+"=?", paramentros);
                                Toast.makeText(contexto, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                bd.close();
                                animateCircularDelete(itemView, getAdapterPosition());
                            }else{
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    alertOpciones.show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, EditProductActivity.class);

                    int requestCode = getAdapterPosition();
                    String name = cardViewProductos.get(requestCode).getNombre();
                    String cla = cardViewProductos.get(requestCode).getClave();
                    String pre = cardViewProductos.get(requestCode).getPrecio();
                    String det = cardViewProductos.get(requestCode).getDetalle();
                    String can = cardViewProductos.get(requestCode).getCantidad();
                    int color = cardViewProductos.get(requestCode).getColor();

                    intent.putExtra(MainActivity.EXTRA_NAME, name);
                    intent.putExtra(MainActivity.EXTRA_INITIAL, Character.toString(name.charAt(0)));
                    intent.putExtra(MainActivity.EXTRA_COLOR, color);
                    intent.putExtra(MainActivity.EXTRA_CLA, cla);
                    intent.putExtra(MainActivity.EXTRA_PRE, pre);
                    intent.putExtra(MainActivity.EXTRA_DET, det);
                    intent.putExtra(MainActivity.EXTRA_CAN, can);
                    intent.putExtra(MainActivity.EXTRA_UPDATE, false);

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        Explode explode = new Explode();
                        explode.setDuration(500);
                        ((AppCompatActivity)contexto).getWindow().setExitTransition(explode);
                        ((AppCompatActivity)contexto).startActivityForResult(intent, requestCode,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(((AppCompatActivity)contexto),  view,
                                        ((AppCompatActivity)contexto).getString(R.string.transition_card)).toBundle());

                    }
                    else{
                        ((AppCompatActivity)contexto).startActivityForResult(intent, requestCode);
                    }
                }
            });
        }
    }
}
