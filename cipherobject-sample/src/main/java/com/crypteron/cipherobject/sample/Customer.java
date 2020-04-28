package com.crypteron.cipherobject.sample;

import java.io.IOException;
import java.math.BigInteger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.crypteron.Opt;
import com.crypteron.Secure;

public class Customer {

  private int        id;

  @Secure(opts = Opt.TOKEN)
  private BigInteger ssn;

  private String     name;

  @Secure(opts = Opt.TOKEN)
  private String     cardNumber;

  @Secure(mask = "6*")
  private String     accountNotes;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public BigInteger getSsn() {
    return ssn;
  }

  public void setSsn(BigInteger ssn) {
    this.ssn = ssn;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getAccountNotes() {
    return accountNotes;
  }

  public void setAccountNotes(String accountNotes) {
    this.accountNotes = accountNotes;
  }

  @Override
  public String toString() {
    ObjectMapper om = new ObjectMapper();
    try {
      return om.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonGenerationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "error";
  }
}