package domain.fake.cardview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 11-1-2016.
 */
public class ListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListFragment newInstance(int sectionNumber) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //This RecyclerView holds the cards (receipts)
    RecyclerView rv;

    LinearLayoutManager llm = new LinearLayoutManager(getContext());

    //These are the receipt previews
    class ReceiptContent
    {
        String marketAndDate;
        String marketColor; //changes the toolbar color to match branding
        String products;
        String prices;
        String totalprice;
        boolean isFavorite;
        int receiptId;  //this ID is used to keep track of which full receipt to open when pressed

        ReceiptContent(String marketAndDate, String marketColor, String products, String prices, String totalprice, boolean isFavorite, int receiptId)
        {
            this.marketAndDate = marketAndDate;
            this.marketColor = marketColor;
            this.products = products;
            this.prices = prices;
            this.totalprice = totalprice;
            this.isFavorite = isFavorite;
            this.receiptId = receiptId;
        }
    }

    private static List<ReceiptContent> receipts;

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ReceiptViewHolder>{

        List<ReceiptContent> receipts;

        RVAdapter(List<ReceiptContent> receipts)
        {
            this.receipts = receipts;
        }

        @Override
        public int getItemCount() {
            return receipts.size();
        }

        @Override
        public ReceiptViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipt_card, viewGroup, false);
            ReceiptViewHolder rvh = new ReceiptViewHolder(v);
            return rvh;
        }

        @Override
        public void onBindViewHolder(ReceiptViewHolder receiptViewHolder, int i)
        {
            receiptViewHolder.toolbar.setTitle(receipts.get(i).marketAndDate);
            receiptViewHolder.toolbar.setBackgroundColor(Color.parseColor(receipts.get(i).marketColor));
            receiptViewHolder.prices.setText(receipts.get(i).prices);
            receiptViewHolder.products.setText(receipts.get(i).products);
            receiptViewHolder.totalprice.setText(receipts.get(i).totalprice);
            receiptViewHolder.favorite.setChecked(receipts.get(i).isFavorite);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class ReceiptViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            Toolbar toolbar;
            TextView products;
            TextView prices;
            TextView totalprice;
            CheckBox favorite;

            ReceiptViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.card_view);
                toolbar = (Toolbar)cv.findViewById(R.id.toolbar);
                products = (TextView)itemView.findViewById(R.id.products);
                prices = (TextView)itemView.findViewById(R.id.prices);
                totalprice = (TextView)itemView.findViewById(R.id.totalprice);
                favorite = (CheckBox)itemView.findViewById(R.id.action_favorite);
            }
        }

    }

    //This fragment holds the favourite cards
    public static class FavFragment extends ListFragment {
        public static ListFragment newInstance(int sectionNumber) {
            ListFragment fragment = new FavFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private void initializeData()
        {
            receipts = new ArrayList<>();
            receipts.add(new ReceiptContent("AH\t01-01-2021", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 0));
            receipts.add(new ReceiptContent("Jumbo\t18-06-2011", "#FFFF00", "2x FANTA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 0));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rv = (RecyclerView)rootView.findViewById(R.id.cardList);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
            initializeData();
            RVAdapter adapter = new RVAdapter(receipts);
            rv.setAdapter(adapter);
            return rootView;
        }
    }


    //This fragment holds all cards
    public static class AllFragment extends ListFragment {
        public static ListFragment newInstance(int sectionNumber) {
            ListFragment fragment = new AllFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private void initializeData()
        {
            receipts = new ArrayList<>();
            receipts.add(new ReceiptContent("AH\t01-01-2021", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 0));
            receipts.add(new ReceiptContent("Jumbo\t18-06-2011", "#FFFF00", "2x FANTA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 0));
            receipts.add(new ReceiptContent("AH\t01-01-2011", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€90,00", false, 0));
            receipts.add(new ReceiptContent("AH\t01-01-2010", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€80,00", false, 0));
            receipts.add(new ReceiptContent("AH\t01-01-2009", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", false, 0));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_all, container, false);
            rv = (RecyclerView)rootView.findViewById(R.id.cardList);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
            initializeData();
            RVAdapter adapter = new RVAdapter(receipts);
            rv.setAdapter(adapter);
            return rootView;
        }
    }
}