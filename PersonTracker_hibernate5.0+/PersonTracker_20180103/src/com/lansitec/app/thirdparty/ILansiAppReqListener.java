package com.lansitec.app.thirdparty;


import com.lansitec.enumlist.ReqProcRsltType;

public interface ILansiAppReqListener {
  public ReqProcRsltType handlerReq(Object obj,ReqProcRslt rslt);
}
