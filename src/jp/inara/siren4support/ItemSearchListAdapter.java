package jp.inara.siren4support;

import java.util.List;

import jp.inara.siren4support.database.Item;
import jp.inara.siren4support.database.ItemPrice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemSearchListAdapter extends ArrayAdapter<ItemPrice> {
    
    private LayoutInflater mInflater;
    private int mResourceId;
    private ViewHolder mHolder;
    
    static class ViewHolder {
        TextView itemName;
    }
    
    /**
     * コンストラクタ
     * 
     * @param context 親クラスのコンテキスト
     * @param resourceId 行のレイアウトリソースID
     */
    public ItemSearchListAdapter(Context context, int resourceId) {
        super(context, resourceId);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResourceId = resourceId;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = mInflater.inflate(mResourceId, null);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            mHolder = new ViewHolder();
            mHolder.itemName = textView;
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder)view.getTag();
        }
        
        // 文字列作成
        ItemPrice itemPrice = getItem(position);
        mHolder.itemName.setText(itemPrice.getSearchListName());
        
        // listの色付け
        int textColor;
        Resources res = getContext().getResources();
        Item item = itemPrice.getItem();
        if(item.isIdentify()){
            textColor = Color.YELLOW;
        } else {
            textColor = res.getColor(android.R.color.primary_text_dark);
        }
        mHolder.itemName.setTextColor(textColor);
        
        return view;
    }
    
    public void setData(List<ItemPrice> items){
        clear();
        if(items != null){
            for(ItemPrice item : items) {
                add(item);
            }
        }
    }
}
