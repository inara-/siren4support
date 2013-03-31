package jp.inara.siren4support;

import jp.inara.siren4suport.R;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * 値段識別画面のActivity
 * @author inara
 *
 */
public class ItemSearchActivity extends SherlockFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("", "onNewIntent");
        String queryString = intent.getStringExtra(SearchManager.QUERY);
        ItemSearchListFragment fragment = (ItemSearchListFragment) getSupportFragmentManager().findFragmentById(R.id.search_list);
        fragment.search(queryString);
    }
}
