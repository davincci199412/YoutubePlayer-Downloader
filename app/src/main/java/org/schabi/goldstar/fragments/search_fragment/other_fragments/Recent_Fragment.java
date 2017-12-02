package org.schabi.goldstar.fragments.search_fragment.other_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.schabi.goldstar.MainActivity;
import org.schabi.goldstar.R;
import org.schabi.goldstar.extractor.InfoItem;
import org.schabi.goldstar.info_list.InfoItemBuilder;
import org.schabi.goldstar.info_list.InfoListAdapter;
import org.schabi.goldstar.util.NavStack;

import java.util.List;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recent_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recent_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recent_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public Recent_Fragment() {
        // Required empty public constructor
    }


    public static Recent_Fragment newInstance(String param1, String param2) {
        Recent_Fragment fragment = new Recent_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    public static List<InfoItem> recent_list = new Vector<>();
    View mainview;
    public static InfoListAdapter infoListAdapter = null;
    private LinearLayoutManager streamInfoListLayoutManager = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_, container, false);
        mainview = view;
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recent);
        streamInfoListLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(streamInfoListLayoutManager);
        infoListAdapter = new InfoListAdapter(getActivity(),
                view);
        infoListAdapter.setFooter(inflater.inflate(R.layout.pignate_footer, recyclerView, false));
        infoListAdapter.showFooter(false);

        infoListAdapter.setOnStreamInfoItemSelectedListener(
                new InfoItemBuilder.OnInfoItemSelectedListener() {
                    @Override
                    public void selected(String url, int serviceId) {
                        NavStack.getInstance()
                                .openDetailActivity(getContext(), url, serviceId);
                    }
                });

        infoListAdapter.addInfoItemList(MainActivity.database_class.getFavouriteList());
        recyclerView.setAdapter(infoListAdapter);
        recent_list = MainActivity.database_class.getRecentList();
        if(recent_list.size() > 0)
            mainview.findViewById(R.id.recent_textview).setVisibility(View.GONE);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MainActivity.nfragmentstate = 1;
            if(infoListAdapter != null)
            {
                infoListAdapter.clearSteamItemList();
                recent_list = MainActivity.database_class.getRecentList();
                if(recent_list.size() > 0)
                    mainview.findViewById(R.id.recent_textview).setVisibility(View.GONE);
                infoListAdapter.addInfoItemList(recent_list);
                infoListAdapter.notifyDataSetChanged();
                if(recent_list.size() > 0)
                    mainview.findViewById(R.id.recent_textview).setVisibility(View.GONE);
            }

        }
        else {
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
