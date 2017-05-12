package pl.poznan.put.cs.io.beerdiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

//TODO - refaktoryzacja - zmienić obsługe eventów podając typ screena do konstruktora

public class ExpandableListAdapter<TypeOfScreen extends AbstractScreen> extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;
    private TypeOfScreen screenObject;

    /** konstruktor 
     *  @param context          kontekst
     *  @param listDataHeader   lista  nagłówków danych
     *  @param listChildData    lista danych dzieci
     *  @param screenObject     typ ekranu
     */
    public ExpandableListAdapter (Context context, List<String> listDataHeader,
                                           HashMap<String, List<String>> listChildData, TypeOfScreen screenObject) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.screenObject = screenObject;
    }

    /** metoda zwracająca dziecko
     * @param groupPosition     pozycja grupy
     * @param childPosition     pozycja dziecka
     * @return                  dziecko
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    /** metoda zwracająca id dziecka
     * @param groupPosition     pozycja grupy
     * @param childPosition     pozycja dziecka
     * @return                  id dziecka              
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /** metoda zwracająca widok dziecka
     * @param groupPosition     pozycja grupy
     * @param childPosition     pozycja dziecka
     * @param isLastChild       czy jest ostanim dzieckiem?
     * @param convertView       konwertowany widok
     * @param parent            grupa rodzica
     * @return                  widok dziecka
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    /** metoda zwracająca liczbę dzieci
     * @param groupPosition    pozycja grupy
     * @return                 liczba dzieci
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    /** metoda zwracająca grupę
     * @param groupPosition     pozycja grupy
     * @return                  grupa
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    /** metoda zwracająca liczbę grup
     * @return                  liczba grup
     */
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    /** metoda zwracająca id grupy
     * @param groupPosition     pozycja grupy
     * @return                  id grupy
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /** metoda zwracająca widok grupy
     * @param groupPosition     pozycja grupy
     * @param isExpanded        czy jest rozwinięta?
     * @param convertView       skonwertowany widok
     * @param parent            grupa rodzica
     * @return                  widok grupy
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        //if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_group, null);

        final TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        final ImageButton lblListEdit = (ImageButton) convertView.findViewById(R.id.lblListEdit);
        lblListEdit.setFocusable(false);

        lblListEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                screenObject = (TypeOfScreen)context;
                screenObject.edit(v, groupPosition);
            }

        });

        final ImageButton lblListRemove = (ImageButton) convertView.findViewById(R.id.lblListRemove);

        lblListRemove.setFocusable(false);

        lblListRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //obsługa usuwania
                                screenObject = (TypeOfScreen)context;
                                screenObject.deleteByGroupId(groupPosition);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Czy na pewno chcesz usunąć "+lblListHeader.getText()+"?").setPositiveButton("Tak", dialogClickListener)
                        .setNegativeButton("Nie", dialogClickListener).show();
            }

        });
        //}
        return convertView;
    }
    
    /** metoda mówiąca czy grupa ma stabilne id
     * @return                  czy stabilne id?
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /** metoda mówiąca czy dziecko można wybierać
     * @param groupPosition     pozycja grupy
     * @param childPosition     pozycja dziecka
     * @return                  czy dziecko można wybierać?
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}