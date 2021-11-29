//package com.example.alertmeapp.fragments;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import androidx.fragment.app.Fragment;
//
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.alertmeapp.R;
////import com.example.alertmeapp.dummy.AlertContent;
//import com.example.alertmeapp.dummy.RecyclerItemClickListener;
//import com.example.alertmeapp.fragments.alert.AlertContent;
//
//
///**
// * A fragment representing a list of Items.
// */
//public class ListFragment extends Fragment {
//
//    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public ListFragment() {
//    }
//
//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static ListFragment newInstance(int columnCount) {
//        ListFragment fragment = new ListFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
//
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            //initialize list with items and sets adapter
//            AlertContent alertContent = new AlertContent(recyclerView);
//
//            recyclerView.addOnItemTouchListener(
//                    new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                        @Override public void onItemClick(View view, int position) {
//                            Bundle bundle = new Bundle();
//                            bundle.putLong("alertId", alertContent.getItem(position).id);
//                            NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentController);
//                            navController.navigate(R.id.alertDetailsFragment, bundle);
//                        }
//
//                        @Override public void onLongItemClick(View view, int position) {}
//                    })
//            );
//        }
//        return view;
//    }
//}