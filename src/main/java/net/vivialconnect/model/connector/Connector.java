package net.vivialconnect.model.connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import net.vivialconnect.model.ResourceCount;
import net.vivialconnect.model.VivialConnectResource;
import net.vivialconnect.model.error.NoContentException;
import net.vivialconnect.model.error.VivialConnectException;
import net.vivialconnect.model.format.JsonBodyBuilder;

@JsonRootName("connector")
public class Connector extends VivialConnectResource implements ConnectorWithCallbacks, ConnectorWithPhoneNumbers{

    private static final long serialVersionUID = 9106799578930523035L;

    /** Unique identifier of the Connector object */
    @JsonProperty
    private int id;

    /** Creation date (UTC) of the Connector in ISO 8601 format */
    @JsonProperty("date_created")
    private Date dateCreated;

    /** Last modification date (UTC) of the Connector in ISO 8601 format */
    @JsonProperty("date_modified")
    private Date dateModified;

    /**
     * Unique identifier of the owning Account
     */
    @JsonProperty("account_id")
    private int accountId;

    @JsonProperty
    private boolean active;

    /** User-defined descriptive label */
    @JsonProperty
    private String name;

    /**
     * A list of callbacks representing the callback configurations for the Connector
     */
    @JsonProperty
    private List<Callback> callbacks;

    /**
     * A list of phone numbers representing the phone numbers associated to the Connector. Max 50 listed in response.
     */
    @JsonProperty("phone_numbers")
    private List<PhoneNumber> phoneNumbers;

    /**
     * A boolean that is true if the Connector has more than 50 associated numbers
     */
    @JsonProperty("more_phone_numbers")
    private boolean morePhoneNumbers;

    static {
        classesWithoutRootValue.add(ConnectorCollection.class);
    }

    /**
     * Search for a {@link Connector} by its ID using the API.
     * <p>
     * If the {@link Connector} is not found, a VivialConnectException will be thrown.
     * 
     * @param           connectorId the id of the connector to look up
     * 
     * @return          the Connector that was found given the id
     * @throws		VivialConnectException if there is an API-level error
     * 
     * @see 		#getConnectors()
     */
    public static Connector getConnectorById(int connectorId) throws VivialConnectException{
        return request(RequestMethod.GET, classURLWithSuffix(Connector.class, String.valueOf(connectorId)), null, null, Connector.class);
    }

    /**
     * Gets all connectors associated with the current account. If there are none, the method will return an empty {@link List}
     * 
     * @return          a list of connectors
     * @throws		VivialConnectException if there is an API-level error
     * 
     * @see 		#getConnectorById(int) 
     */
    public static List<Connector> getConnectors() throws VivialConnectException{
        return request(RequestMethod.GET, classURL(Connector.class), null, null, ConnectorCollection.class).getConnectors();
    }

    /**
     * Total number of users in the account specified. If there are none, this method will return <code>0</code>.
     * 
     * @return          connector count
     * @throws		VivialConnectException if there is an API-level error
     *
     */
    public static int count() throws VivialConnectException{
        return request(RequestMethod.GET, classURLWithSuffix(Connector.class, "count"), null, null, ResourceCount.class).getCount();
    }
    
    /**
     * Creates a new Connector resource for the account
     * 
     * @return          the Connector that was created
     * @throws		VivialConnectException if there is an API-level error
     *
     */
    public Connector create() throws VivialConnectException{
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("name", getName());

        Connector createdConnector = request(RequestMethod.POST, classURL(Connector.class),
                                                    builder.build(), null, Connector.class);

        updateObjectState(createdConnector);
        
        return this;
    }

    /**
     * Updates the Connector resource for the account
     * 
     * @return          the Connector that was updated
     * @throws		VivialConnectException if there is an API-level error
     *
     */
    public Connector update() throws VivialConnectException{
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("id", getId())
                                                 .addParamPair("name", getName());

        Connector updatedConnector = request(RequestMethod.PUT, classURLWithSuffix(Connector.class, String.valueOf(getId())),
                                                                                    builder.build(), null, Connector.class);

        updateObjectState(updatedConnector);
        return this;
    }


    private void updateObjectState(Connector connector){
        this.id = connector.getId();
        this.dateCreated = connector.getDateCreated();
        this.dateModified = connector.getDateModified();
        this.accountId = connector.getAccountId();
        this.active = connector.isActive();
        this.callbacks = connector.getCallbacks();
        this.name = connector.getName();
        this.phoneNumbers = connector.getPhoneNumbers();
        this.morePhoneNumbers = connector.isMorePhoneNumbers();
    }


    /**
     * Removes the connector from the account.
     * <p>
     * Returns <code>true<code> if the connector was successfully deleted.
     * 
     * @return          whether the connector was deleted or not
     * @throws		VivialConnectException if there is an API-level error
     *
     */
    public boolean delete() throws VivialConnectException{
        try{
            request(RequestMethod.DELETE, classURLWithSuffix(Connector.class, String.valueOf(getId())), null, null, String.class);
        }catch (NoContentException nce){
                return true;
        }

        return false;
    }


    public int getId(){
        return id;
    }


    public void setId(int id){
        this.id = id;
    }


    public Date getDateCreated(){
        return dateCreated;
    }


    public void setDateCreated(Date dateCreated){
        this.dateCreated = dateCreated;
    }


    @Override
    public Date getDateModified(){
        return dateModified;
    }


    public void setDateModified(Date dateModified){
        this.dateModified = dateModified;
    }


    public int getAccountId(){
        return accountId;
    }


    public void setAccountId(int accountId){
        this.accountId = accountId;
    }


    public boolean isActive(){
        return active;
    }


    public void setActive(boolean active){
        this.active = active;
    }


    public String getName(){
        return name;
    }


    public void setName(String name){
        this.name = name;
    }


    @Override
    public List<Callback> getCallbacks(){
        return callbacks;
    }


    public void setCallbacks(List<Callback> callbacks){
        this.callbacks = callbacks;
    }

    /**
     * Use this method to add callbacks which can later be saved to the server using {@link Connector#createCallbacks()}
     * or {@link Connector#updateCallbacks()}. This method call can be chained in a builder-like fashion, like so:
     * <pre>
     * <code>
     * class CallbackCreator {
     *  
     *  public void addAllCallbacks(Connector connector) {
     *    Callback c1 = new Callback();
     *    c1.setMessageType("text");
     *    c1.setEventType("incoming");
     * 
     *    Callback c2 = new Callback();
     *    c2.setMessageType("text");
     *    c2.setEventType("status");   
     * 
     *    connector.addCallback(c1).addCallback(c2).createCallbacks();
     *  }
     * }
     * </code>
     * </pre>
     * 
     * @param callback the callback to be added
     * @return this instance of {@link Connector}
     * 
     * @see Callback
     * @see Connector#setCallbacks(java.util.List)
     * @see Connector#createCallbacks()
     * @see Connector#updateCallbacks()
     */
    public Connector addCallback(Callback callback) {
        if (callbacks == null){
            callbacks = new ArrayList<Callback>();
        }

        callbacks.add(callback);
        
        return this;
    }

    /**
     * Overwrites and associates the list of callbacks added through {@link Connector#addCallback(net.vivialconnect.model.connector.Callback)} and
     * {@link Connector#setCallbacks(java.util.List)} to this connector.
     * <p>
     * Returns an instance of the {@link ConnectorWithCallbacks} interface, which can be used to access the newly-created callbacks.
     * <p>
     * For more details, go to the VivialConnect API documentation's
     * <a href="https://www.vivialconnect.net/docs/api.html#post--api-v1.0-accounts-(int-account_id)-connectors-(int-connector_id)-callbacks.json">callback section</a>.
     * 
     * @see Callback
     * @see Connector#addCallback(net.vivialconnect.model.connector.Callback) 
     * @see Connector#setCallbacks(java.util.List)
     * 
     * @return an instance {@link ConnectorWithCallbacks} holding the list of created callbacks
     * @throws VivialConnectException if there is an API-level error
     */
    public ConnectorWithCallbacks createCallbacks() throws VivialConnectException {
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("callbacks", callbacks);

        Connector connectorWithCallbacks = request(RequestMethod.POST, classURLWithSuffix(Connector.class, String.format("%d/callbacks", getId())),
                                                                                                        builder.build(), null, Connector.class);
        mergeCallbackFields(connectorWithCallbacks);
        
        return this;
    }

    /**
     * Updates the list of callbacks, editing any existing ones and adding any new ones, associated to
     * this connector. Here's an example of how to edit an existing callback:
     * <pre>
     * <code>
     * class CallbackUpdate {
     *  
     *  public void updateCallback(Connector connector, String newMessageType) {
     *    Callback callback = connector.getCallbacks().get(0);
     *    callback.setMesssageType(newMessageType);
     *    
     *    connector.updateCallbacks();
     *  }
     * }
     * </code>
     * </pre>
     * Returns an instance of the {@link ConnectorWithCallbacks} interface, which can be used to access the updated callbacks.
     * 
     * @see Callback
     * @see Connector#addCallback(net.vivialconnect.model.connector.Callback) 
     * @see Connector#setCallbacks(java.util.List)
     * 
     * @return an instance {@link ConnectorWithCallbacks} holding the list of updated callbacks
     * @throws VivialConnectException if there is an API-level error
     */
    public ConnectorWithCallbacks updateCallbacks() throws VivialConnectException {
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("callbacks", callbacks);

        Connector connectorWithCallbacks = request(RequestMethod.PUT, classURLWithSuffix(Connector.class, String.format("%d/callbacks", getId())),
                                                                                                        builder.build(), null, Connector.class);
        mergeCallbackFields(connectorWithCallbacks);
        
        return this;
    }


    private void mergeCallbackFields(ConnectorWithCallbacks connectorWithCallbacks){
        this.dateModified = connectorWithCallbacks.getDateModified();
        this.callbacks = connectorWithCallbacks.getCallbacks();
    }

    /**
     * Removes all the callbacks associated to this connector.
     * 
     * @see Callback
     * @see Connector#addCallback(net.vivialconnect.model.connector.Callback) 
     * @see Connector#setCallbacks(java.util.List)
     * 
     * @return an instance {@link ConnectorWithCallbacks} holding an empty list of callbacks
     * @throws VivialConnectException
     */
    public ConnectorWithCallbacks deleteAllCallbacks() throws VivialConnectException {
        return deleteCallbacks(this.callbacks);
    }

    /**
     * Removes a single callback.
     * 
     * @param callback the callback to be removed
     * 
     * @see Callback
     * 
     * @return an instance {@link ConnectorWithCallbacks} holding the updated list of callbacks
     * @throws VivialConnectException
     */
    public ConnectorWithCallbacks deleteSingleCallback(Callback callback) throws VivialConnectException {
        List<Callback> singleCallbackList = new ArrayList<Callback>(1);
        singleCallbackList.add(callback);

        return deleteCallbacks(singleCallbackList);
    }

    /**
     * Removes a series of callbacks
     * 
     * @param callbacks the callbacks to be removed
     * 
     * @see Callback
     * 
     * @return an instance {@link ConnectorWithCallbacks} holding the updated list of callbacks
     * @throws VivialConnectException
     */
    public ConnectorWithCallbacks deleteCallbacks(List<Callback> callbacks) throws VivialConnectException {
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("callbacks", callbacks);

        return request(RequestMethod.DELETE, classURLWithSuffix(Connector.class, String.format("%d/callbacks", getId())),
                                                                                builder.build(), null, Connector.class);
    }


    @Override
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }


    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }


    public Connector addPhoneNumber(int phoneNumberId, String phoneNumber){
        return addPhoneNumber(new PhoneNumber(phoneNumberId, phoneNumber));
    }


    public Connector addPhoneNumber(PhoneNumber phoneNumber){
        if (phoneNumbers == null){
            phoneNumbers = new ArrayList<PhoneNumber>();
        }

        this.phoneNumbers.add(phoneNumber);
        
        return this;
    }


    public ConnectorWithPhoneNumbers createPhoneNumbers() throws VivialConnectException{
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("phone_numbers", phoneNumbers);

        Connector connectorWithPhoneNumbers = request(RequestMethod.POST, classURLWithSuffix(Connector.class, String.format("%d/phone_numbers", getId())),
                                                                                                                builder.build(), null, Connector.class);
        mergePhoneNumberFields(connectorWithPhoneNumbers);
        
        return this;
    }


    public ConnectorWithPhoneNumbers updatePhoneNumbers() throws VivialConnectException{
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("phone_numbers", phoneNumbers);

        Connector connectorWithPhoneNumbers = request(RequestMethod.PUT, classURLWithSuffix(Connector.class, String.format("%d/phone_numbers", getId())),
                                                                                                                 builder.build(), null, Connector.class);
        mergePhoneNumberFields(connectorWithPhoneNumbers);
        
        return this;
    }


    public ConnectorWithPhoneNumbers deleteAllPhoneNumbers() throws VivialConnectException{
        return deletePhoneNumbers(this.phoneNumbers);
    }


    public ConnectorWithPhoneNumbers deleteSinglePhoneNumber(PhoneNumber phoneNumber) throws VivialConnectException{
        List<PhoneNumber> singlePhoneNumberList = new ArrayList<PhoneNumber>(1);
        singlePhoneNumberList.add(phoneNumber);

        return deletePhoneNumbers(singlePhoneNumberList);
    }


    public ConnectorWithPhoneNumbers deletePhoneNumbers(List<PhoneNumber> phoneNumbers) throws VivialConnectException{
        JsonBodyBuilder builder = JsonBodyBuilder.forClass(Connector.class)
                                                 .addParamPair("phone_numbers", phoneNumbers);

        return request(RequestMethod.DELETE, classURLWithSuffix(Connector.class, String.format("%d/phone_numbers", getId())),
                                                                                    builder.build(), null, Connector.class);
    }


    private void mergePhoneNumberFields(ConnectorWithPhoneNumbers connectorWithPhoneNumbers){
        this.dateModified = connectorWithPhoneNumbers.getDateModified();
        this.phoneNumbers = connectorWithPhoneNumbers.getPhoneNumbers();
    }


    public boolean isMorePhoneNumbers(){
        return morePhoneNumbers;
    }


    public void setMorePhoneNumbers(boolean morePhoneNumbers){
        this.morePhoneNumbers = morePhoneNumbers;
    }
}