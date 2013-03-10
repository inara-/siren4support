package jp.inara.siren4suport.database;

import java.util.Locale;

/**
 * ItemPriceのモデル
 * @author inara
 *
 */
public class ItemPrice {
    
    /** 通常価格 */
    public static final int PRICE_TYPE_NORMAL = 0;
    
    /** 祝福価格 */
    public static final int PRICE_TYPE_BRESSING = 1;
    
    /** 呪い・封印価格 */
    public static final int PRICE_TYPE_CURSE = 2;
    
    
    /** アイテムID */
    private int mItemId;
    
    /** 使用回数 壺、杖以外のアイテムは0固定 */
    private int mUseCount;
    
    /** お店に売る値段 */
    private int mSellingPrice;
    
    /** お店から買う時の値段 */
    private int mBuyingPrice;
    
    /** アイテム */
    private Item mItem;
    
    /** 価格タイプ */
    private int mPriceType;
    
    
    
    public int getItemId() {
        return mItemId;
    }
    public void setItemId(int itemId) {
        this.mItemId = itemId;
    }
    public int getUseCount() {
        return mUseCount;
    }
    public void setUseCount(int useCount) {
        this.mUseCount = useCount;
    }
    public int getSellingPrice() {
        return mSellingPrice;
    }
    public void setSellingPrice(int sellingPrice) {
        this.mSellingPrice = sellingPrice;
    }
    public int getBuyingPrice() {
        return mBuyingPrice;
    }
    public void setBuyingPrice(int buyingPrice) {
        this.mBuyingPrice = buyingPrice;
    }
    public Item getItem() {
        return mItem;
    }
    public void setItem(Item item) {
        this.mItem = item;
    }
    public int getPriceType() {
        return mPriceType;
    }
    public void setPriceType(int priceType) {
        this.mPriceType = priceType;
    }
    
    /**
     * 検索結果表示時に最適化してアイテム名を返す
     * @return
     */
    public String getSearchListName() {
        StringBuilder sb = new StringBuilder();
        
        // 祝福呪いのアイテムにはマークをつける
        switch (mPriceType) {
            case PRICE_TYPE_BRESSING:
                sb.append("(祝)");
                break;
            case PRICE_TYPE_CURSE:
                sb.append("(呪)");
            default:
                break;
        }
        sb.append(mItem.getName());
        
        // 杖と壺は回数をつける
        switch (mItem.getType()) {
            case Item.ITEM_TYPE_POT:
            case Item.ITEM_TYPE_STAFF:
                String str = String.format(Locale.getDefault(), "[%d]", mUseCount);
                sb.append(str);
                break;
            default:
                break;
        }
        
        return sb.toString();
    }

}
