package com.rd.ifaes.core.tpp.model.cbhb.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

@SuppressWarnings("serial")
public class CbhbNetLoanRegisterModel extends CbhbAppBaseModel{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbNetLoanRegisterModel.class);	
	/**
	 * 构造器
	 */
	public CbhbNetLoanRegisterModel() {
		super();
		this.setTransid("CBHBNetLoanRegister");
		this.setBackUrl("http://localhost:8080"+"/cbhbApp/appRegister/notify.html");
		this.setFrontUrl("http://localhost:8080"+"/cbhbApp/appRegister/return.html");
	}
	
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		try {
			this.setPlaCustId(URLDecoder.decode(StringUtils.isNull(map.get("PlaCustId")),"GBK"));
			this.setOpenBankId(URLDecoder.decode(StringUtils.isNull(map.get("OpenBankId")),"GBK"));
			this.setOpenAcctId(URLDecoder.decode(StringUtils.isNull(map.get("OpenAcctId")),"GBK"));
			this.setIdentType(URLDecoder.decode(StringUtils.isNull(map.get("IdentType")),"GBK"));
			this.setIdentNo(URLDecoder.decode(StringUtils.isNull(map.get("IdentNo")),"GBK"));
			this.setUsrName(URLDecoder.decode(StringUtils.isNull(map.get("UsrName")),"GBK"));
			this.setMobileNo(URLDecoder.decode(StringUtils.isNull(map.get("MobileNo")),"GBK"));
			this.setFeeAmt(URLDecoder.decode(StringUtils.isNull(map.get("Fee_Amt")),"GBK"));
			this.setCharSet(URLDecoder.decode(StringUtils.isNull(map.get("Char_Set")),"GBK"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE));
			throw new BussinessException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE),BussinessException.TYPE_JSON);
		}
		LOGGER.info("CBHBNetLoanRegister APP 回调...{}",this.toString());
	}

	/**
	 * APP明文加密字段
	 */
	private String[] requestParamNames = new String[] {"PartnerId","MerBillNo","MobileNo","BackUrl","FrontUrl"
			,"IdentType","IdentNo","UsrName","UsrFlag","OpenBankId","OpenAcctId","MerPriv"};

	/**
	 * 响应参数字段数组 包含所有的
	 */
	private String[] responseParamNames = new String[] {"PartnerId","MerBillNo","PlaCustId","OpenBankId","OpenAcctId"
			,"IdentType","IdentNo","UsrName","MobileNo","MerPriv","Fee_Amt","Version_No","Biz_Type","RpCode","RpDesc"
			,"Mac","Sign_Type","Char_Set"};
	/**
	 * 银行预留手机号
	 */
	private String mobileNo;
	/**
	 * 证件类型
	 */
	private String identType;
	/**
	 * 证件号码
	 */
	private String identNo;
	/**
	 * 姓名
	 */
	private String usrName;
	/**
	 * 新老用户标识
	 */
	private String usrFlag;
	/**
	 * 开户银行代号	
	 */
	private String openBankId;
	/**
	 * 开户银行账号
	 */
	private String openAcctId;
	
	/**
	 * 托管平台客户号  
	 * @return
	 */
	private String plaCustId;
	/**
	 * 手续费
	 */
	private String feeAmt;
	
	/**
	 * 字符集
	 */
	private String charSet;

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIdentType() {
		return identType;
	}

	public void setIdentType(String identType) {
		this.identType = identType;
	}

	public String getIdentNo() {
		return identNo;
	}

	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getUsrFlag() {
		return usrFlag;
	}

	public void setUsrFlag(String usrFlag) {
		this.usrFlag = usrFlag;
	}

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getOpenAcctId() {
		return openAcctId;
	}

	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}

	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}
	
	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	@Override
	public String toString() {
		return "CbhbNetLoanRegisterModel [mobileNo=" + mobileNo + ", identType=" + identType + ", identNo=" + identNo
				+ ", usrName=" + usrName + ", openBankId=" + openBankId + ", openAcctId=" + openAcctId + ", plaCustId="
				+ plaCustId + ", feeAmt=" + feeAmt + ", getPartnerId()=" + getPartnerId() + ", getMerBillNo()="
				+ getMerBillNo() + ", getMerPriv()=" + getMerPriv() + ", getVersionNo()=" + getVersionNo()
				+ ", getBizType()=" + getBizType() + ", getRpCode()=" + getRpCode() + ", getRpDesc()=" + getRpDesc()
				+ ", getMac()=" + getMac() + ", getSignType()=" + getSignType() + ", getMerBillNoStr()="
				+ getMerBillNoStr() + "]";
	}
	
}
