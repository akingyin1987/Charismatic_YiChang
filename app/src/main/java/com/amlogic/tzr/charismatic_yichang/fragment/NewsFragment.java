package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.adapter.NewsAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.NewsListBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    public static final String BUNDLE_URL = "oder";
    public static final String FLAG_NEW = "new";
    public static final String FLAG_HOT = "hot";
    public static final String FLAG_RECOMMAND = "recommand";

    private static final int STATE_REFRESH = 0;// ����ˢ��
    private static final int STATE_MORE = 1;// ���ظ���
    private int limit = 10; // ÿҳ��������10��
    private int curPage = 0; // ��ǰҳ�ı�ţ���0��ʼ
    private ILoadingLayout mILoadingLayout;
    private PullToRefreshListView news_Listview;
    private ListView mMsgListView;


    private String type;
    private int page_total = 0;
    private int page = 1;
    private int default_pageSize = 10;
    private int show_id = 0;
    private View fragmentView;
    private Context context;
    private NewsAdapter mAdapter;

    private List<NewsListBean> list = new ArrayList<NewsListBean>();
    private int pageNum = 0;


    private BmobQuery<NewsListBean> bmobQuery;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            list.clear();
            type = arguments.getString(BUNDLE_URL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_news, null);
        initPullToRefresh();
        queryData(0,STATE_REFRESH,type);
        return fragmentView;
    }

    public static NewsFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, type);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initPullToRefresh() {

        news_Listview = (PullToRefreshListView) fragmentView.findViewById(R.id.prl_nf_list);
        mILoadingLayout = news_Listview.getLoadingLayoutProxy();
        mILoadingLayout.setLastUpdatedLabel("");
        mILoadingLayout.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
        mILoadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
        mILoadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
        news_Listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    mILoadingLayout.setLastUpdatedLabel("");
                    mILoadingLayout
                            .setPullLabel(getString(R.string.pull_to_refresh_top_pull));
                    mILoadingLayout
                            .setRefreshingLabel(getString(R.string.pull_to_refresh_top_refreshing));
                    mILoadingLayout
                            .setReleaseLabel(getString(R.string.pull_to_refresh_top_release));
                } else if (firstVisibleItem + visibleItemCount + 1 == totalItemCount) {
                    mILoadingLayout.setLastUpdatedLabel("");
                    mILoadingLayout
                            .setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
                    mILoadingLayout
                            .setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
                    mILoadingLayout
                            .setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
                }
            }
        });

        // ����ˢ�¼���
        news_Listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // ����ˢ��(�ӵ�һҳ��ʼװ������)
                queryData(0, STATE_REFRESH, type);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // �������ظ���(������һҳ����)
                queryData(curPage, STATE_MORE, type);
            }
        });
        mMsgListView = news_Listview.getRefreshableView();
        mAdapter = new NewsAdapter(context, list);
        // ������adapter
        mMsgListView.setAdapter(mAdapter);
    }

    private void queryData(final int page, final int actionType, String type) {
        bmobQuery = new BmobQuery<NewsListBean>();
        bmobQuery.addWhereEqualTo("news_type", type);
        bmobQuery.setLimit(limit);            // ����ÿҳ����������
        bmobQuery.setSkip(page * limit);        // �ӵڼ������ݿ�ʼ
        bmobQuery.findObjects(context, new FindListener<NewsListBean>() {
            @Override
            public void onSuccess(List<NewsListBean> queryList) {
                if (queryList.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        // ��������ˢ�²���ʱ������ǰҳ�ı������Ϊ0������list��գ��������
                        curPage = 0;
                        list.clear();
                    }
                    // �����β�ѯ��������ӵ�list��
                    for (NewsListBean bean : queryList) {
                        list.add(bean);
                    }
                    // ������ÿ�μ��������ݺ󣬽���ǰҳ��+1������������ˢ�µ�onPullUpToRefresh�����оͲ���Ҫ����curPage��
                    curPage++;
                } else if (actionType == STATE_MORE) {
                    showToast("û�и���������");
                } else if (actionType == STATE_REFRESH) {

                }
                news_Listview.onRefreshComplete();
//                list = queryList;
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                showToast("��ѯʧ��:" + s);
                news_Listview.onRefreshComplete();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
