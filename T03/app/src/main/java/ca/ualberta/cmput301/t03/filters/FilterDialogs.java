package ca.ualberta.cmput301.t03.filters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.filters.item_criteria.CategoryFilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.StringQueryFilterCriteria;

/**
 * Created by quentinlautischer on 2015-12-03.
 */
public class FilterDialogs {


    static public AlertDialog createAddFilterDialog(final Context context, ArrayList<FilterCriteria> passedFilters, final Callable<Void> updateFilters) {
        final ArrayList<FilterCriteria> filters = passedFilters;
        AlertDialog.Builder builder = new AlertDialog.Builder(  context);
        final View dialogContent = View.inflate(context, R.layout.content_add_filter_dialog, null);

        builder.setView(dialogContent);
        //itemCategoryText.setSelection(((ArrayAdapter) itemCategoryText.getAdapter()).getPosition(itemModel.getItemCategory()));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Iterator<FilterCriteria> i = filters.iterator();
                while(i.hasNext()){
                    FilterCriteria filter = i.next();
                    if (filter.getType().equals("category")) {
                        i.remove();
                    }
                }
                Spinner spinner = (Spinner) dialogContent.findViewById(R.id.itemFilterCategory);
                String categoryType = spinner.getSelectedItem().toString();
                if (!categoryType.toLowerCase().equals("none")) {
                    filters.add(new CategoryFilterCriteria(categoryType));
                    try {
                        updateFilters.call();
                    } catch (Exception e){
                        throw new RuntimeException("Error updating filters", e);
                    }
                    Toast.makeText(context, "Category Filter: '" + categoryType + "'", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        updateFilters.call();
                    } catch (Exception e){
                        throw new RuntimeException("Error updating filters", e);
                    }
                }
                Toast.makeText(context, "Category Filter: '" + categoryType + "'", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("Set a Category Filter");
        AlertDialog d = builder.create();
        return d;
    }

    static public AlertDialog createAddSearchDialog(final Context context, ArrayList<FilterCriteria> passedFilters, final Callable<Void> updateFilters) {
        final ArrayList<FilterCriteria> filters = passedFilters;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogContent = View.inflate(context, R.layout.content_add_search_dialog, null);
        final EditText e = (EditText) dialogContent.findViewById(R.id.addSearchFilterText);

        builder.setView(dialogContent); //todo replace with layout

        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String usr = e.getText().toString().trim();
                filters.add(new StringQueryFilterCriteria(usr));
                try {
                    updateFilters.call();
                } catch (Exception e){
                    throw new RuntimeException("Error updating filters", e);
                }
                Toast.makeText(context, "Textual Filter: '" + usr + "' Added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("Textual Filters");

        ArrayList<String> filterNames = new ArrayList<String>();
        for (FilterCriteria filter : filters) {
            if(filter.getType().equals("textual")){
                filterNames.add(filter.getName());
            }
        }
        final CharSequence[] fNames = filterNames.toArray(new String[filterNames.size()]);
        builder.setItems(fNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = fNames[item].toString();
                removeSearchFilter(selectedText, filters);
                try {
                    updateFilters.call();
                } catch (Exception e){
                    throw new RuntimeException("Error updating filters", e);
                }
                Toast.makeText(context, "Textual Filter: '" + selectedText + "' Removed", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = builder.create();
        return d;
    }

    static public void removeSearchFilter(String filterName, ArrayList<FilterCriteria> filters) {
        Iterator<FilterCriteria> i = filters.iterator();
        while (i.hasNext()) {
            FilterCriteria filter = i.next();
            if (filter.getType().equals("textual") && filter.getName().equals(filterName)) {
                i.remove();
            }
        }
    }


}
