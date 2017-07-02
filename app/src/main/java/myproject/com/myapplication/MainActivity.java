package myproject.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        initData();
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(datas);
        recyclerView.setAdapter(adapter);
    }

    public void initData(){
        datas = new ArrayList<String>();
        for(int i =0;i<17;i++){
            datas.add("item "+i);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private List<String> datas;

        public RecyclerViewAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.item.setTag(position);
            holder.tv.setText(datas.get(position));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public View item;
            public TextView tv;

            public ViewHolder(View view) {
                super(view);
                item = view;
                tv = (TextView) view.findViewById(R.id.text);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

    }
}
