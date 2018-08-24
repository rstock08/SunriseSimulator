package goldsquadron.sunrise;

/**
 * Created by wjhmike on 4/11/18.
 */

public class Window {
    private String name;
    private int winAddress;
    private int status;

    public Window(){
        super();
    }

    public Window(String name, int winAddress, int status){
        super();
        this.name = name;
        this.winAddress = winAddress;
        this.status = status;
    }
    public String getWinName(){
        return this.name;
    }

    public int getWinAddress(){
        return this.winAddress;
    }
}
