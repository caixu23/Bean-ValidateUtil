package com.caixu.beanvalidateutil.imlclass;


import com.caixu.beanvalidateutil.utils.ObjectUtil;

import java.util.List;

/**
 * Created by xu on 2015/12/4.
 */
public class VResult {
    private boolean relult;
    private List<VVResult> vvResult;
    private String messgae;

public String getAllString(){
    if (!ObjectUtil.isEmptyAll(vvResult)){
        String m="";
        for (VVResult vv:vvResult){
            m+=vv.getMessage();
        }
        return m;
    }else {
        return "";
    }
}

    public List<VVResult> getVvResult() {
        return vvResult;
    }

    public void setVvResult(List<VVResult> vvResult) {
        this.vvResult = vvResult;
    }

    public boolean isRelult() {
        return relult;
    }

    public void setRelult(boolean relult) {
        this.relult = relult;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }
}