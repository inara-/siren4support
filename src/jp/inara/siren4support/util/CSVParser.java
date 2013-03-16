package jp.inara.siren4support.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVのパース処理を実施する
 * @author inara
 *
 */
public class CSVParser {
    public static final String SEPARATOR =",";
    private BufferedReader mReader;
    
    
    /**
     * コンストラクタ
     * @param in
     */
    public CSVParser(InputStream in) {
        mReader = new BufferedReader(new InputStreamReader(in));
    }
    
    /**
     * CSVファイルを1行読み込んで行を返す。最終行の場合はnullを返す
     * @return
     */
    public List<String> readLine() {
        String line = null;
        try {
            line = mReader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(line == null) {
            return null;
        } else {
            String[] items = line.split(SEPARATOR);
            final List<String> row = new ArrayList<String>(items.length);
            for (String item : items) {
                row.add(item);
            }
            return row;
        } 
    }
    
}
