package net.vivialconnect.tests.data;

import java.util.List;
import java.util.Map;

import net.vivialconnect.model.account.Account;
import net.vivialconnect.model.account.Contact;
import net.vivialconnect.model.error.VivialConnectException;
import net.vivialconnect.model.message.Message;
import net.vivialconnect.model.number.AssociatedNumber;
import net.vivialconnect.model.number.AvailableNumber;
import net.vivialconnect.model.number.NumberInfo;

public interface DataSource {

    // Account

    Account getAccount() throws VivialConnectException;

    void updateAccount(Account account) throws VivialConnectException;

    // Number

    AssociatedNumber getNumberById(int numberId) throws VivialConnectException;

    AssociatedNumber getLocalNumberById(int numberId) throws VivialConnectException;

    List<AssociatedNumber> getAssociatedNumbers() throws VivialConnectException;

    List<AssociatedNumber> getLocalAssociatedNumbers() throws VivialConnectException;

    List<AvailableNumber> findAvailableNumbersInRegion(String region, Map<String, String> filters) throws VivialConnectException;

    List<AvailableNumber> findAvailableNumbersByAreaCode(String areaCode, Map<String, String> filters) throws VivialConnectException;

    List<AvailableNumber> findAvailableNumbersByPostalCode(String postalCode, Map<String, String> filters) throws VivialConnectException;

    int numberCount() throws VivialConnectException;

    int numberCountLocal() throws VivialConnectException;

    AssociatedNumber buy(String phoneNumber, String areaCode, String phoneNumberType, Map<String, Object> optionalParams) throws VivialConnectException;

    AssociatedNumber buyLocalNumber(String phoneNumber, String areaCode, Map<String, Object> optionalParams) throws VivialConnectException;

    void deleteLocalNumber(AssociatedNumber localNumber) throws VivialConnectException;

    void updateNumber(AssociatedNumber number) throws VivialConnectException;

    void updateLocalNumber(AssociatedNumber number) throws VivialConnectException;

    NumberInfo numberLookup(AssociatedNumber number) throws VivialConnectException;

    // Message

    List<Message> getMessages(Map<String, String> filters) throws VivialConnectException;

    int messageCount() throws VivialConnectException;

    Message getMessageById(int messageId) throws VivialConnectException;

    void sendMessage(Message message) throws VivialConnectException;

    void redactMessage(Message message) throws VivialConnectException;

    //Contact

    Contact createContact(Contact contact) throws VivialConnectException;

    List<Contact> getContacts() throws VivialConnectException;
}