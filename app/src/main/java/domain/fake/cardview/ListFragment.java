package domain.fake.cardview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    private ListFragment otherFrag;

    public void setOtherFrag(ListFragment frag)
    {
        otherFrag = frag;
    }

    //This RecyclerView holds the cards (receipts)
    RecyclerView rv;
    RVAdapter adapter;

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

        boolean checkId(int iD)
        {
            if(iD==receiptId)
                return true;
            else
                return false;
        }
    }

    private static List<ReceiptContent> receipts;

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ReceiptViewHolder>{

        public List<ReceiptContent> receipts;
        boolean isFavoriteList = false;

        RVAdapter(List<ReceiptContent> receipts)
        {
            this.receipts = receipts;
        }

        RVAdapter(List<ReceiptContent> receipts, boolean isFavoriteList)
        {
            this.receipts = receipts;
            this.isFavoriteList = isFavoriteList;
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

        public class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            CardView cv;
            Toolbar toolbar;
            TextView products;
            TextView prices;
            TextView totalprice;
            CheckBox favorite;
            ImageButton edit;
            ImageButton delete;

            ReceiptViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.card_view);
                toolbar = (Toolbar)cv.findViewById(R.id.toolbar);
                products = (TextView)itemView.findViewById(R.id.products);
                prices = (TextView)itemView.findViewById(R.id.prices);
                totalprice = (TextView)itemView.findViewById(R.id.totalprice);
                favorite = (CheckBox)itemView.findViewById(R.id.action_favorite);
                edit = (ImageButton)itemView.findViewById(R.id.action_edit);
                delete = (ImageButton)itemView.findViewById(R.id.action_delete);
                itemView.setOnClickListener(this);
                favorite.setOnClickListener(this);
                edit.setOnClickListener(this);
                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v)
            {
                //if else because cases require constants
                if (v.getId() == itemView.getId()){
                    openDetailedView();
                } else if (v.getId() == favorite.getId()){
                    favReceipt(getAdapterPosition());
                } else if (v.getId() == edit.getId()){
                    editReceipt();
                } else if (v.getId() == delete.getId()){
                    deleteReceipt(getAdapterPosition());
                }
            }

            public void openDetailedView()
            {
                Toast.makeText(itemView.getContext(), "OPEN CARD", Toast.LENGTH_SHORT).show();
            }

            public void favReceipt(int position)
            {
                Toast.makeText(itemView.getContext(), "FAVORITE", Toast.LENGTH_SHORT).show();
                if(isFavoriteList)  //removes from fav list, changes checkbox in all list
                {
                    favOtherList(receipts.get(position).receiptId, position, isFavoriteList);
                    receipts.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    //update database with isFavorite = false
                }
                else if(receipts.get(position).isFavorite)  //removes from fav list (other screen), updates view
                {
                    delOtherList(receipts.get(position).receiptId);
                    receipts.get(position).isFavorite=false;
                    notifyDataSetChanged();
                    //update database with isFavorite = false
                }
                else    //adds to fav list, updates view
                {
                    receipts.get(position).isFavorite=true;
                    favOtherList(receipts.get(position).receiptId, position, isFavoriteList);
                    notifyDataSetChanged();
                    //update database with isFavorite = true
                }
            }

            public void editReceipt()
            {
                Toast.makeText(itemView.getContext(), "EDIT", Toast.LENGTH_SHORT).show();
                //start edit activity
            }

            public void deleteReceipt(int position)
            {
                Toast.makeText(itemView.getContext(), "DELETE", Toast.LENGTH_SHORT).show();
                delOtherList(receipts.get(position).receiptId);
                receipts.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                //update database
            }
        }
    }

    public void favOtherList(int receiptId, int currentPosition, boolean isFavoriteList)
    {
        if(isFavoriteList)
        {
            int position = -1;
            RVAdapter otherAdapter = otherFrag.adapter;
            for(int i = 0; i < otherAdapter.receipts.size(); i++)
            {
                if(otherAdapter.receipts.get(i).checkId(receiptId))
                    position = i;
            }
            if(position>-1)
            {
                otherAdapter.receipts.get(position).isFavorite=false;
                otherAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            RVAdapter otherAdapter = otherFrag.adapter;
            otherAdapter.receipts.add(0, adapter.receipts.get(currentPosition));
            otherAdapter.notifyDataSetChanged();
        }
    }

    public void delOtherList(int receiptId)
    {
        int position = -1;
        RVAdapter otherAdapter = otherFrag.adapter;
        for(int i = 0; i < otherAdapter.receipts.size(); i++)
        {
            if(otherAdapter.receipts.get(i).checkId(receiptId))
                position = i;
        }
        if(position>-1)
        {
            otherAdapter.receipts.remove(position);
            otherAdapter.notifyItemRemoved(position);
            otherAdapter.notifyItemRangeChanged(position, adapter.getItemCount());
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
            receipts.add(new ReceiptContent("AH\t01-01-2021", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 22));
            receipts.add(new ReceiptContent("Jumbo\t18-06-2011", "#FFFF00", "2x FANTA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 50));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rv = (RecyclerView)rootView.findViewById(R.id.cardList);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
            initializeData();
            adapter = new RVAdapter(receipts, true);
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
            receipts.add(new ReceiptContent("AH\t01-01-2021", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 22));
            receipts.add(new ReceiptContent("Jumbo\t18-06-2011", "#FFFF00", "2x FANTA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", true, 50));
            receipts.add(new ReceiptContent("AH\t01-01-2011", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€90,00", false, 100));
            receipts.add(new ReceiptContent("AH\t01-01-2010", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€80,00", false, 33));
            receipts.add(new ReceiptContent("AH\t01-01-2009", "#00A0E2", "2x COCA-COLA\n2x APPELS\n1x DURR\n1x CUTOFF", "€2,33\n€3,75\n€11,22\n€13,77", "€100,00", false, 12));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_all, container, false);
            rv = (RecyclerView)rootView.findViewById(R.id.cardList);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
            initializeData();
            adapter = new RVAdapter(receipts);
            rv.setAdapter(adapter);
            return rootView;
        }
    }
}