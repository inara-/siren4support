package jp.inara.siren4suport.database;

/**
 * ItemPriceのモデル
 * @author inara
 *
 */
public class ItemPrice {
    
    private int mItemId;
    private int mUseCount;
    private int mSellingPrice;
    private int mBuyingPrice;
    private Item mItem;
    
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

}
