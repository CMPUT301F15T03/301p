package ca.ualberta.cmput301.t03.filters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.filters.item_criteria.StringQueryFilterCriteria;

/**
 * Created by quentinlautischer on 2015-11-29.
 */
public class FilterManager <T> {
    ArrayList<T> list;
    Context context;

    public FilterManager(){}

    public FilterManager(Context context, ArrayList<T> list){
        this.context = context;
        this.list = list;
    }

//    private AlertDialog createAddSearchDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
//        View dialogContent = View.inflate(this.context, R.layout.content_add_search_dialog, null);
//        final EditText e = (EditText) dialogContent.findViewById(R.id.addSearchFilterText);
//
//        builder.setView(dialogContent);
//
//        builder.setCancelable(false);
//        builder.setNegativeButton("Cancel", null);
//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                final String usr = e.getText().toString().trim();
//                addFilter(new StringQueryFilterCriteria(usr));
//                Toast.makeText(this.context, "Textual Filter: '" + usr + "' Added", Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setTitle("Textual Filters");
//
//
//        ArrayList<String> filterNames = new ArrayList<String>();
//        for (T t : list) {
//            if(list.getType().equals("textual")){
//                filterNames.add(filter.getName());
//            }
//        }
//        final CharSequence[] fNames = filterNames.toArray(new String[filterNames.size()]);
//        builder.setItems(fNames, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                String selectedText = fNames[item].toString();
//                removeFilter(selectedText);
//
//                Toast.makeText(this.context, "Textual Filter: '" + selectedText + "' Removed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        AlertDialog d = builder.create();
//        return d;
//    }
//
//    public void addFilter(FilterCriteria filter){
//        filters.add(filter);
//        onRefresh();
//    }
//
//    public void removeFilter(String filterName){
////        for (FilterCriteria filter: filters){
////            if (filter.getType().equals("textual") && filter.getName().equals(filterName)){
////                filters.remove(filter);
////            }
////        }
//        Iterator<FilterCriteria> i = filters.iterator();
//        while(i.hasNext()){
//            FilterCriteria filter = i.next();
//            if (filter.getType().equals("textual") && filter.getName().equals(filterName)) {
//                i.remove();
//            }
//        }
//        onRefresh();
//    }

}
