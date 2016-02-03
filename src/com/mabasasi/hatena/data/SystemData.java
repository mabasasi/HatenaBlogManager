
package com.mabasasi.hatena.data;

import com.mabasasi.hatena.parameter.SystemParameter;

/**
 *
 * @author mabasasi
 */
public final class SystemData extends Parameter<SystemParameter>{
    private final String consumerKey = "CuJHT/Jw8Qug+A==";
    private final String consumerSecret = "q9D3zkgDvhi+cMjjJtDpbE93b88=";

    
    public SystemData() {
        super(SystemParameter.class);
    }

    public SystemData(String jsonText) {
        super(jsonText, SystemParameter.class);
        this.checkConsumerKey(true);
    }

    /**
     * コンシューマーキーの取り扱い.
     * @param isAdded trueでmapに追加(存在しない場合のみ)、falseで取り除く.
     */
    public void checkConsumerKey(boolean isAdded) {
        if (isAdded) {
            if (this.getParameter(SystemParameter.consumerKey) == null) {
                this.setParameter(SystemParameter.consumerKey, consumerKey);
            }
            if (this.getParameter(SystemParameter.consumerSecret) == null) {
                this.setParameter(SystemParameter.consumerSecret, consumerSecret);
            }
        } else {
            String key = this.removeParameter(SystemParameter.consumerKey);            
            String value = this.removeParameter(SystemParameter.consumerSecret);
        }
    }

    @Override
    public String getJsonString() {
        this.checkConsumerKey(false);
        String value = super.getJsonString(); 
        this.checkConsumerKey(true);
        
        return value;
    }
}
