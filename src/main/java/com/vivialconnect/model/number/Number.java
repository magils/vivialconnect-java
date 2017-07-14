package com.vivialconnect.model.number;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.vivialconnect.model.ResourceCount;
import com.vivialconnect.model.VivialConnectResource;
import com.vivialconnect.model.error.NoContentException;
import com.vivialconnect.model.error.VivialConnectException;
import com.vivialconnect.model.format.JsonBodyBuilder;

@JsonRootName(value = "phone_number")
public class Number extends VivialConnectResource implements AssociatedNumber, AvailableNumber
{
	
	private static final long serialVersionUID = -1224802858893763457L;
	
	private static final String AVAILABLE_US_LOCAL = "available/US/local";
	
	/** Unique identifier of the phone number object */
	@JsonProperty
	private int id;

	/** Creation date (UTC) of the phone number in ISO 8601 format */
	@JsonProperty("date_created")
	private Date dateCreated;

	/** Last modification date (UTC) of phone number in ISO 8601 format */
	@JsonProperty("date_modified")
	private Date dateModified;

	/**
	 * Unique identifier of the account or subaccount associated with the phone number
	 */
	@JsonProperty("account_id")
	private int accountId;
	
	/**
	 * Associated phone number as it is displayed to users. Default format: Friendly national format: (xxx) yyy-zzzz
	 */
	@JsonProperty
	private String name;
	
	/**
	 * Associated phone number in E.164 format (+country code +phone number). For US numbers, the format will be +1xxxyyyzzzz
	 */
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	/**
	 * Type of associated phone number. Possible values: local (non-toll-free) or tollfree
	 */
	@JsonProperty("phone_number_type")
	private String phoneNumberType;
	
	/**
	 * URL to receive message status callback requests for messages sent via the API using this associated phone number
	 */
	@JsonProperty("status_text_url")
	private String statusTextUrl;
	
	/**
	 * URL for receiving SMS messages to the associated phone number. Max. length: 256 characters
	 */
	@JsonProperty("incoming_text_url")
	private String incomingTextUrl;
	
	/**
	 * HTTP method used for the incoming_text_url requests. Max. length: 8 characters. Possible values: GET or POST
	 */
	@JsonProperty("incoming_text_method")
	private String incomingTextMethod;
	
	/**
	 * URL for receiving SMS messages if incoming_text_url fails. Only valid if you provide a value for the incoming_text_url parameter. Max. length: 256 characters
	 */
	@JsonProperty("incoming_text_fallback_url")
	private String incomingTextFallbackUrl;
	
	/**
	 * HTTP method used for incoming_text_fallback_url requests. Max. length: 8 characters. Possible values: GET or POST
	 */
	@JsonProperty("incoming_text_fallback_method")
	private String incomingTextFallbackMethod;
	
	/**
	 * Number to which voice calls will be forwarded
	 */
	@JsonProperty("voice_forwarding_number")
	private String voiceForwardingNumber;
	
	/**
	 * Set of boolean flags indicating the following capabilities supported by the associated phone number
	 */
	@JsonProperty
	private Capabilities capabilities;
	
	/**
	 * City where the available phone number is located
	 */
	@JsonProperty
	private String city;
	
	/**
	 * Two-letter US state abbreviation where the available phone number is located
	 */
	@JsonProperty
	private String region;
	
	/**
	 * Local address and transport area (LATA) where the available phone number is located
	 */
	@JsonProperty
	private String lata;
	
	/**
	 * LATA rate center where the available phone number is located. Usually the same as city
	 */
	@JsonProperty("rate_center")
	private String rateCenter;
	
	@JsonProperty
	private boolean active;
	
	@JsonProperty("connector_id")
	private int connectorId;
	
	static {
		classesWithoutRootValue.add(NumberCollection.class);
	}
	
	
	@Override
	public AssociatedNumber update() throws VivialConnectException
	{
		AssociatedNumber number = request(RequestMethod.PUT, classURLWithSuffix(Number.class, String.valueOf(getId())),
										  buildJsonBodyForUpdate(), null, Number.class);
		updateObjectState(number);
		return this;
	}
	

	private String buildJsonBodyForUpdate()
	{
		JsonBodyBuilder builder = JsonBodyBuilder.withCustomClassName("phone_number");
		fillOptionalFieldsForUpdate(builder);
		
		return builder.build();
	}


	private void fillOptionalFieldsForUpdate(JsonBodyBuilder builder)
	{
		ifParamValidAddToBuilder(builder, "id", getId());
		ifParamValidAddToBuilder(builder, "connector_id", getConnectorId());
		ifParamValidAddToBuilder(builder, "incoming_text_url", getIncomingTextUrl());
		ifParamValidAddToBuilder(builder, "incoming_text_method", getIncomingTextMethod());
		ifParamValidAddToBuilder(builder, "incoming_text_fallback_url", getIncomingTextFallbackUrl());
		ifParamValidAddToBuilder(builder, "incoming_text_fallback_method", getIncomingTextFallbackMethod());
		ifParamValidAddToBuilder(builder, "voice_forwarding_number", getVoiceForwardingNumber());
	}
	
	
	private void updateObjectState(AssociatedNumber number)
	{
		this.id = number.getId();
		this.dateCreated = number.getDateCreated();
		this.dateModified = number.getDateModified();
		this.accountId = number.getAccountId();
		this.phoneNumber = number.getPhoneNumber();
		this.phoneNumberType = number.getPhoneNumberType();
		this.statusTextUrl = number.getStatusTextUrl();
		this.incomingTextUrl = number.getIncomingTextUrl();
		this.incomingTextMethod = number.getIncomingTextMethod();
		this.incomingTextFallbackUrl = number.getIncomingTextFallbackUrl();
		this.incomingTextFallbackMethod = number.getIncomingTextFallbackMethod();
		this.voiceForwardingNumber = number.getVoiceForwardingNumber();
		this.capabilities = number.getCapabilities();
		this.city = number.getCity();
		this.region = number.getRegion();
		this.lata = number.getLata();
		this.rateCenter = number.getRateCenter();
		this.active = number.isActive();
		this.connectorId = number.getConnectorId();
	}
	
	
	@Override
	public AssociatedNumber updateLocalNumber() throws VivialConnectException
	{
		ensureNumberIsLocal();
		
		AssociatedNumber number = request(RequestMethod.PUT, classURLWithSuffix(Number.class, String.format("local/%d", getId())),
				  						  buildJsonBodyForUpdate(), null, Number.class);
		updateObjectState(number);
		return this;
	}
	
	
	private void ensureNumberIsLocal()
	{
		if (!"local".equals(phoneNumberType))
		{
			throw new UnsupportedOperationException("Number is not local");
		}
	}


	@Override
	public boolean delete() throws VivialConnectException
	{
		try
		{
			request(RequestMethod.DELETE, classURLWithSuffix(Number.class, String.valueOf(getId())), null, null, String.class);
		}
		catch(NoContentException e)
		{
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public boolean deleteLocalNumber() throws VivialConnectException
	{
		ensureNumberIsLocal();
		
		try
		{
			request(RequestMethod.DELETE, classURLWithSuffix(Number.class, String.format("local/%d", getId())), null, null, String.class);
		}
		catch(NoContentException e)
		{
			return true;
		}
		
		return false;
	}


	@Override
	public AssociatedNumber buy() throws VivialConnectException
	{
		JsonBodyBuilder builder = JsonBodyBuilder.withCustomClassName("phone_number")
												 .addParamPair("phone_number", getPhoneNumber())
												 .addParamPair("phone_number_type", getPhoneNumberType());
		fillOptionalFieldsForBuy(builder);
		
		return request(RequestMethod.POST, classURL(Number.class), builder.build(), null, Number.class);
	}


	private void fillOptionalFieldsForBuy(JsonBodyBuilder builder)
	{
		ifParamValidAddToBuilder(builder, "name", getName());
		ifParamValidAddToBuilder(builder, "status_text_url", getStatusTextUrl());
		ifParamValidAddToBuilder(builder, "connector_id", getConnectorId());
		ifParamValidAddToBuilder(builder, "incoming_text_url", getIncomingTextUrl());
		ifParamValidAddToBuilder(builder, "incoming_text_method", getIncomingTextMethod());
		ifParamValidAddToBuilder(builder, "incoming_text_fallback_url", getIncomingTextFallbackUrl());
		ifParamValidAddToBuilder(builder, "incoming_text_fallback_method", getIncomingTextFallbackMethod());
	}
	
	
	public static AssociatedNumber buyLocalNumber(String phoneNumber, String areaCode, Map<String, Object> optionalParams) throws VivialConnectException
	{
		JsonBodyBuilder builder = JsonBodyBuilder.withCustomClassName("phone_number").addParams(optionalParams);
		ifParamValidAddToBuilder(builder, "phone_number", phoneNumber);
		ifParamValidAddToBuilder(builder, "area_code", areaCode);
		
		return request(RequestMethod.POST, classURLWithSuffix(Number.class, "local"), builder.build(), null, Number.class);
	}
	
	
	public static AssociatedNumber buy(String phoneNumber, String areaCode, String phoneNumberType, Map<String, Object> optionalParams) throws VivialConnectException
	{
		JsonBodyBuilder builder = JsonBodyBuilder.withCustomClassName("phone_number").addParams(optionalParams);
		ifParamValidAddToBuilder(builder, "phone_number", phoneNumber);
		ifParamValidAddToBuilder(builder, "area_code", areaCode);
		ifParamValidAddToBuilder(builder, "phone_number_type", phoneNumberType);
		
		return request(RequestMethod.POST, classURL(Number.class), builder.build(), null, Number.class);
	}
	
	
	public static List<AssociatedNumber> getAssociatedNumbers() throws VivialConnectException
	{
		return getAssociatedNumbers(null);
	}
	
	
	public static List<AssociatedNumber> getAssociatedNumbers(Map<String, String> queryParams) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURL(Number.class), null, queryParams, NumberCollection.class).getAssociatedNumbers();
	}
	
	
	public static List<AvailableNumber> findAvailableNumbersInRegion(String region) throws VivialConnectException
	{
		return findAvailableNumbersInRegion(region, null);
	}
	
	
	public static List<AvailableNumber> findAvailableNumbersInRegion(String region, Map<String, String> queryParams) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, AVAILABLE_US_LOCAL), null, addQueryParam("in_region", region, queryParams), NumberCollection.class).getAvailableNumbers();
	}
	
	
	public static List<AvailableNumber> findAvailableNumbersByAreaCode(String areaCode) throws VivialConnectException
	{
		return findAvailableNumbersByAreaCode(areaCode, null);
	}
	
	
	public static List<AvailableNumber> findAvailableNumbersByAreaCode(String areaCode, Map<String, String> queryParams) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, AVAILABLE_US_LOCAL), null, addQueryParam("area_code", areaCode, queryParams), NumberCollection.class).getAvailableNumbers();
	}
	
	
	public static List<AvailableNumber> findAvailableNumbersByPostalCode(String postalCode) throws VivialConnectException
	{
		return findAvailableNumbersByPostalCode(postalCode);
	}
	
	
	public static List<AvailableNumber> findAvailableNumbersByPostalCode(String postalCode, Map<String, String> queryParams) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, AVAILABLE_US_LOCAL), null, addQueryParam("in_postal_code", postalCode, queryParams), NumberCollection.class).getAvailableNumbers();
	}
	
	
	public static List<AssociatedNumber> getLocalAssociatedNumbers() throws VivialConnectException
	{
		return getLocalAssociatedNumbers(null);
	}
	
	
	public static List<AssociatedNumber> getLocalAssociatedNumbers(Map<String, String> queryParams) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, "local"), null, queryParams, NumberCollection.class).getAssociatedNumbers();
	}
	
	
	public static int count() throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, "count"), null, null, ResourceCount.class).getCount();
	}
	
	
	public static int countLocal() throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, "local/count"), null, null, ResourceCount.class).getCount();
	}
	
	
	public static AssociatedNumber getNumberById(int numberId) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURL(Number.class), null, null, NumberCollection.class).getAssociatedNumbers().get(0);
	}
	
	
	public static AssociatedNumber getLocalNumberById(int numberId) throws VivialConnectException
	{
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, String.format("local/%d", numberId)), null, null, Number.class);
	}
	
	
	@Override
	public NumberInfo lookup() throws VivialConnectException
	{
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("phone_number", getRawPhoneNumber());
		
		return request(RequestMethod.GET, classURLWithSuffix(Number.class, "lookup"), null, queryParams, NumberInfo.class);
	}
	
	
	private String getRawPhoneNumber()
	{
		/* Removes the leading '+' character from the phone number */
		return getPhoneNumber().substring(1);
	}


	@Override
	public int getId()
	{
		return id;
	}

	
	@Override
	public void setId(int id)
	{
		this.id = id;
	}

	
	@Override
	public Date getDateCreated()
	{
		return dateCreated;
	}

	
	@Override
	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	
	@Override
	public Date getDateModified()
	{
		return dateModified;
	}

	
	@Override
	public void setDateModified(Date dateModified)
	{
		this.dateModified = dateModified;
	}

	
	@Override
	public int getAccountId()
	{
		return accountId;
	}

	
	@Override
	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	
	@Override
	public String getName()
	{
		return name;
	}

	
	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	
	@Override
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	
	@Override
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	
	@Override
	public String getPhoneNumberType()
	{
		return phoneNumberType;
	}
	
	
	@Override
	public void setPhoneNumberType(String phoneNumberType)
	{
		this.phoneNumberType = phoneNumberType;
	}

	
	@Override
	public String getStatusTextUrl()
	{
		return statusTextUrl;
	}

	
	@Override
	public void setStatusTextUrl(String statusTextUrl)
	{
		this.statusTextUrl = statusTextUrl;
	}

	
	@Override
	public String getIncomingTextUrl()
	{
		return incomingTextUrl;
	}

	
	@Override
	public void setIncomingTextUrl(String incomingTextUrl)
	{
		this.incomingTextUrl = incomingTextUrl;
	}

	
	@Override
	public String getIncomingTextMethod()
	{
		return incomingTextMethod;
	}

	
	@Override
	public void setIncomingTextMethod(String incomingTextMethod)
	{
		this.incomingTextMethod = incomingTextMethod;
	}

	
	@Override
	public String getIncomingTextFallbackUrl()
	{
		return incomingTextFallbackUrl;
	}
	
	
	@Override
	public void setIncomingTextFallbackUrl(String incomingTextFallbackUrl)
	{
		this.incomingTextFallbackUrl = incomingTextFallbackUrl;
	}

	
	@Override
	public String getIncomingTextFallbackMethod()
	{
		return incomingTextFallbackMethod;
	}

	
	@Override
	public void setIncomingTextFallbackMethod(String incomingTextFallbackMethod)
	{
		this.incomingTextFallbackMethod = incomingTextFallbackMethod;
	}

	
	@Override
	public String getVoiceForwardingNumber()
	{
		return voiceForwardingNumber;
	}

	
	@Override
	public void setVoiceForwardingNumber(String voiceForwardingNumber)
	{
		this.voiceForwardingNumber = voiceForwardingNumber;
	}
	
	
	@Override
	public Capabilities getCapabilities()
	{
		return capabilities;
	}
	
	
	@Override
	public void setCapabilities(Capabilities capabilities)
	{
		this.capabilities = capabilities;
	}

	
	@Override
	public String getCity()
	{
		return city;
	}

	
	@Override
	public void setCity(String city)
	{
		this.city = city;
	}

	
	@Override
	public String getRegion()
	{
		return region;
	}
	
	
	@Override
	public void setRegion(String region)
	{
		this.region = region;
	}

	
	@Override
	public String getLata()
	{
		return lata;
	}

	
	@Override
	public void setLata(String lata)
	{
		this.lata = lata;
	}

	
	@Override
	public String getRateCenter()
	{
		return rateCenter;
	}

	
	@Override
	public void setRateCenter(String rateCenter)
	{
		this.rateCenter = rateCenter;
	}
	
	
	@Override
	public boolean isActive()
	{
		return active;
	}
	
	
	@Override
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	
	@Override
	public int getConnectorId()
	{
		return connectorId;
	}
	
	
	@Override
	public void setConnectorId(int connectorId)
	{
		this.connectorId = connectorId;
	}
}