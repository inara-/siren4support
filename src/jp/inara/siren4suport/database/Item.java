package jp.inara.siren4suport.database;

/**
 * アイテムのモデル
 * @author inara
 *
 */
public class Item {
    /** 巻物 */
    public static final int ITEM_TYPE_SCROLL = 1;
    /** 草 */
    public static final int ITEM_TYPE_GRASS = 2;
    /** 腕輪　*/
    public static final int ITEM_TYPE_BANGLE = 3;
    /** 札 */
    public static final int ITEM_TYPE_CHARM = 4;
    /** 壺 */
    public static final int ITEM_TYPE_POT = 5;
    /** 杖 */
    public static final int ITEM_TYPE_STAFF = 6;

    
    private int mId;
    private String mName;
    private int mType;
    private boolean mIsIdentify;
    
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        this.mId = id;
    }
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        this.mName = name;
    }
    public int getType() {
        return mType;
    }
    public void setType(int type) {
        this.mType = type;
    }
    
    public boolean isIdentify() {
        return mIsIdentify;
    }
    public void setIdentify(boolean isIdentify) {
        this.mIsIdentify = isIdentify;
    }
    
}
