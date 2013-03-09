package jp.inara.siren4suport.database;

/**
 * アイテムのモデル
 * @author inara
 *
 */
public class Item {
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
