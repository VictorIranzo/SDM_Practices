package labs.sdm.game.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.pojo.Quotation;

/**
 * Created by vicirji on 07/02/2018.
 */

public class QuotationAdapter extends ArrayAdapter
{
    private Context context;
    private int layout;
    private List<Quotation> quotationList;

    public QuotationAdapter(@NonNull Context context, int layout, List<Quotation> quotationList) {
        super(context, layout, quotationList);
        this.context = context;
        this.layout = layout;
        this.quotationList = quotationList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Layout of the view that consists on a quote and author.
            view = inflater.inflate(R.layout.quotation_list_row, null);
        }
        TextView quoteTextView = (TextView) view.findViewById(R.id.tvQuote);
        TextView authorTextView = (TextView) view.findViewById(R.id.tvAuthor);

        // Changes the properties of the text views in the view attending to the position parameter,
        // finding the data in the list.
        quoteTextView.setText(this.quotationList.get(position).getQuote());
        authorTextView.setText(this.quotationList.get(position).getAuthor());

        return view;
    }
}
