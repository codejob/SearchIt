
package searchit.webonise.com.searchit.model.placesdetails;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Period {

    @SerializedName("close")
    @Expose
    private Close close;
    @SerializedName("open")
    @Expose
    private Open open;

    /**
     * 
     * @return
     *     The close
     */
    public Close getClose() {
        return close;
    }

    /**
     * 
     * @param close
     *     The close
     */
    public void setClose(Close close) {
        this.close = close;
    }

    /**
     * 
     * @return
     *     The open
     */
    public Open getOpen() {
        return open;
    }

    /**
     * 
     * @param open
     *     The open
     */
    public void setOpen(Open open) {
        this.open = open;
    }

}
