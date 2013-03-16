
package jp.inara.siren4support;

import jp.inara.siren4suport.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // ActionBarの検索ボタンが押された場合は検索処理を実行
        final Intent queryIntent = getIntent();
        final String queryAction = queryIntent.getAction();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(Intent.ACTION_SEARCH.endsWith(queryAction)) {     
            // Fragmentを切り替える
            ItemSearchListFragment fragment = new ItemSearchListFragment();
            
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            ItemListFragment fragment = new ItemListFragment();
            ft.add(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
        
    }

}
