package in.srain.cube.views.ptr.demo.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.demo.R;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;

/**
 * Author：Cxb on 2015/12/14 15:37
 */
public class PtrLoadMoreActivity extends Activity {


    PtrClassicFrameLayout mPtrFrame;
    ListView listView;
    MyAapter mAdapter;
    List<String> data=new ArrayList<>();
    LoadMoreListViewContainer loadMoreListViewContainer;
    int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        mPtrFrame= (PtrClassicFrameLayout) findViewById(R.id.ptrlayout);
        listView= (ListView) findViewById(R.id.listview);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        data.clear();
                        data.addAll(getData(1));
                        mPtrFrame.refreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        //100ms后自动刷新
//        mPtrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.autoRefresh();
//            }
//        }, 100);

       loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);

        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                page++;
                loadMoreListViewContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItem(getData(page));
                        loadMoreListViewContainer.loadMoreFinish(false,true);
                    }
                },1000);


            }
        });

        loadMoreListViewContainer.loadMoreFinish(false,true);
        data=getData(1);
        mAdapter=new MyAapter();
        listView.setAdapter(mAdapter);




    }

    private List<String> getData(int page) {
        List<String> temp=new ArrayList<>();
        for(int i=1;i<=20;i++){
            temp.add("page"+page+":item-"+i);
        }
        return temp;
    }


    class MyAapter extends  BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        public  void addItem(List<String> temp){
            data.addAll(temp);
            notifyDataSetChanged();
        }



        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=View.inflate(PtrLoadMoreActivity.this,R.layout.list_item_more,null);
            TextView tv= (TextView) convertView.findViewById(R.id.tv);
            tv.setText(getItem(position));
            return convertView;
        }
    }
}
